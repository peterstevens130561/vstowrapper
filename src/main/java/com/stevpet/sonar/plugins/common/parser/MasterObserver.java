package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class MasterObserver {
    Map<String,Method> elementMatchers = new HashMap<>();
    Map<String,Method> pathMatchers = new HashMap<>();
    Map<String,Method> attributeMatchers = new HashMap<>();
    
    public void addObserver(ParserObserver observer) {
        for( Method method:observer.getClass().getDeclaredMethods()) {
            PathMatcher pathMatcher= method.getAnnotation(PathMatcher.class);
            if(pathMatcher !=null) {
                pathMatchers.put(pathMatcher.path(),method);
            }
            ElementMatcher elementMatcher = method.getAnnotation(ElementMatcher.class);
            if(elementMatcher!=null) {
                elementMatchers.put(elementMatcher.elementName(),method);
            }
            AttributeMatcher attributeMatcher = method.getAnnotation(AttributeMatcher.class);
            if(attributeMatcher!=null) {
                attributeMatchers.put(attributeMatcher.elementName() + "!" + attributeMatcher.attributeName(),method );
            }
        }
    }
    
    /**
     * return the method that matches either the path or the name. If none matches, null is returned
     * @param path
     * @param name
     * @return
     */
    public Method matchingElement(String path, String name) {
        Method method = pathMatchers.get(path);
        if(method!=null) {
            return method;
        }
        method = elementMatchers.get(name);
        return method;
    }

    public void addAll(List<ParserObserver> parserObservers) {
        for(ParserObserver parserObserver:parserObservers){
            addObserver(parserObserver);
        }
    }

    public Method matchingAttribute(String elementName, String attributeName) {
        // TODO Auto-generated method stub
        return null;
    }
}
