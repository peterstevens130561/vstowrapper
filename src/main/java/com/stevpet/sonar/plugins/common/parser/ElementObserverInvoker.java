package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;

class ElementObserverInvoker {
    private Logger LOG = LoggerFactory.getLogger(ElementObserverInvoker.class);
    private List<ParserObserver> observers;

    void setObservers(List<ParserObserver> observers) {
        this.observers = observers;
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
        for (ParserObserver observer : observers) {
            if (observer.isMatch(path)) {
                for(Method method: observer.getClass().getMethods()) {
                    ElementObserver annos = method.getAnnotation(ElementObserver.class);

                    if (annos == null || annos.event() != event || !path.equals(annos.path())) {
                        continue;
                    }
                    invokeMethod(observer,method);
                }
            }
        }
    }
    
    private void invokeMethod(ParserObserver observer, Method method ) {
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
