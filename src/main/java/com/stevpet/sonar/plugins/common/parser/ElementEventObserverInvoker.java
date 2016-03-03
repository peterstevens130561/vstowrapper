package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;

class ElementEventObserverInvoker {
    private Logger LOG = LoggerFactory.getLogger(ElementEventObserverInvoker.class);
    private RegisteredParserObservers observerPathCache;

    public ElementEventObserverInvoker(RegisteredParserObservers observerPathCache) {
       this.observerPathCache=observerPathCache;
    }

    /**
     * Will invoke the observer methods that match path & event
     * @param path to match
     * @param event to match
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    public void invokeObservers(String path,ElementObserver.Event event)  {
        for (ParserObserverMethods observer : observerPathCache.getObserversMatchingPath(path)) {
            if (observer.isMatch(path)) {
                Method method=observer.getMatchingElementEventObserverMethod(path,event);
                if(method!=null) {
                    invokeEventMethod(observer.getParserObserver(),method);
                }
            }
        }
    }
    
    private void invokeEventMethod(ParserObserver observer, Method method ) {
        try {
            method.invoke(observer);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() != null) {
                String msg = "Exception thrown when invoking method" + createMsg(observer,method);
                throw new SonarException(msg, e);
            }
            String msg = "Invocation Target Exception thrown when invoking method " + createMsg(observer,method);

            LOG.error(msg, e);
            throw new SonarException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = "Illegal Access Exception thrown when invoking method " +  createMsg(observer,method);
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        } catch (IllegalArgumentException e) {
            String msg = "Illegal Argument Exception thrown when invoking method, does the method have one or more arguments? " + createMsg(observer,method);
            LOG.error(msg, e);
            throw new SonarException(msg, e);
        }
    }
    
    private String createMsg(ParserObserver observer, Method method) {
        return  observer.getClass().getName()
        + ":"
        + method.getName();
    }
 
}
