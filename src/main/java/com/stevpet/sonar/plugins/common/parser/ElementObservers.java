package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;

public class ElementObservers {
    private static Logger LOG = LoggerFactory.getLogger(ElementObservers.class);
    private int line,column;
    private final RegisteredParserObservers observerPathCache;
    
    public ElementObservers(RegisteredParserObservers observerPathCache) {
        this.observerPathCache =observerPathCache;
    }
    public void setPlace(int line, int column) {
        this.line=line;
        this.column=column;
        
    }
    
    void invokeElementObservers(String path, String name, String text) {
        for (ParserObserverMethods observer : observerPathCache.getObserversMatchingPath(path)) {
            observer.observeElement(name, text);
            Method method = observer.getMatchingElementMethod(path, name);
            if (method != null) {
                invokeMethod(observer.getParserObserver(), method, text);
            }
        }
    }
    
    void invokeAttributeObservers(String elementName, String path,
            String attributeValue, String attributeName) {
        for (ParserObserverMethods observer : observerPathCache.getObserversMatchingPath(path)) {
            observer.observeAttribute(elementName, path, attributeValue,
                    attributeName);
            Method method = observer.getMatchingAttributeMethod(elementName, attributeName);
            if (method != null) {
                invokeMethod(observer.getParserObserver(), method, attributeValue);
            }
        }
    }
    
    public void invokeMethod(ParserObserver observer, Method method,
            String... elementValue ) {
        try {
            Object[] varargs = elementValue;
            method.invoke(observer, varargs);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                String msg = "Exception thrown when invoking method"
                        + observer.getClass().getName() + ":"
                        + method.getName() + lineMsg();
                LOG.error(msg, e.getTargetException());
                throw new SonarException(msg, e);
            }
            String msg = "Invocation Target Exception thrown when invoking method "
                    + observer.getClass().getName()
                    + ":"
                    + method.getName()
                    + lineMsg();
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = "Illegal Access Exception thrown when invoking method "
                    + observer.getClass().getName()
                    + ":"
                    + method.getName()
                    + lineMsg();
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        } catch (IllegalArgumentException e) {
            String msg = "Illegal Argument Exception thrown when invoking method "
                    + observer.getClass().getName()
                    + ":"
                    + method.getName()
                    + lineMsg();
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        }
    }
    
    private String lineMsg() {
        return " line/column = " + line + "/" + column;
    }

}
