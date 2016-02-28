package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;

public class ObserverInvoker {
    private static Logger LOG = LoggerFactory.getLogger(ObserverInvoker.class);
    private int line,column;
    
    public void setPlace(int line, int column) {
        this.line=line;
        this.column=column;
        
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
