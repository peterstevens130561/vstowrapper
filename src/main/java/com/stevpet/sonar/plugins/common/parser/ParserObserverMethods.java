package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;

/**
 * The methods for one class
 * @author stevpet
 *
 */
public class ParserObserverMethods{
    private ParserObserver observer;
    Map<String,Method> elementMatchers = new HashMap<>();
    Map<String,Method> pathMatchers = new HashMap<>();
    Map<String,Method> attributeMatchers = new HashMap<>();
    Map<String,Method> elemenEventMatchers = new HashMap<>();

    public ParserObserverMethods(ParserObserver observer) {
        this.observer=observer;
        loadCaches();
    }
    public boolean hasError() {
        return observer.hasError();
    }
    public void setParserData(ParserData parserData) {
        observer.setParserData(parserData);
    }
    public boolean isMatch(String path) {
        return observer.isMatch(path);
    }
    public void observeElement(String name, String text) {
        observer.observeElement(name, text);
    }
    public void observeAttribute(String elementName, String path, String attributeValue, String attributeName) {
        observer.observeAttribute(elementName, path, attributeValue, attributeName);
    }
    
    public ParserObserver getParserObserver() {
        return observer;
    }
    
    private void loadCaches() {
        for( Method method:observer.getClass().getDeclaredMethods()) {
            cachePathMatcher(method);
            cacheElementMatcher(method);
            cacheAttributeMatcher(method);
            cacheElementObserver(method);
        }
    }

    private void cacheElementObserver(Method method) {
        ElementObserver elementObserver = method.getAnnotation(ElementObserver.class);
        if(elementObserver!=null){
            String key=getElementEventObserverKey(elementObserver.path(),elementObserver.event());
            elemenEventMatchers.put(key,method);
        }
    }

    private void cacheAttributeMatcher(Method method) {
        AttributeMatcher attributeMatcher = method.getAnnotation(AttributeMatcher.class);
        if(attributeMatcher!=null) {
            attributeMatchers.put(attributeMatcher.elementName() + "!" + attributeMatcher.attributeName(),method );
        }
    }

    private void cacheElementMatcher(Method method) {
        ElementMatcher elementMatcher = method.getAnnotation(ElementMatcher.class);
        if(elementMatcher!=null) {
            elementMatchers.put(elementMatcher.elementName(),method);
        }
    }

    private void cachePathMatcher(Method method) {
        PathMatcher pathMatcher= method.getAnnotation(PathMatcher.class);
        if(pathMatcher !=null) {
            pathMatchers.put(pathMatcher.path(),method);
        }
    }
    


    /**
     * return the method that matches either the path or the name. If none matches, null is returned
     * @param path
     * @param name
     * @return
     */
    public Method getMatchingElementMethod(String path, String name) {
        Method method = pathMatchers.get(path);
        if(method!=null) {
            return method;
        }
        method = elementMatchers.get(name);
        return method;
    }


    public Method getMatchingAttributeMethod(String elementName, String attributeName) {
        String key=getAttributeKey(elementName,attributeName);
        Method method = attributeMatchers.get(key);
        return method;
    }
    
    public Method getMatchingElementEventObserverMethod(String path,Event event) {
        String key=getElementEventObserverKey(path,event);
        Method method = elemenEventMatchers.get(key);
        return method;
        
    }
    private String getAttributeKey(String elementName, String attributeName) {
        return elementName + "!" + attributeName ;
    }

    private String getElementEventObserverKey(String path, Event event) {
        return path + "!" + event.toString();
    }

}
