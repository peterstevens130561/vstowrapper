package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;

import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class AttributeMatcherMethodObserver implements AnnotatedMethodObserver {

    private final AttributeMatcher matcher;

    public AttributeMatcherMethodObserver(AttributeMatcher pathMatcher) {
        this.matcher = pathMatcher;
    }
    
    public static AnnotatedMethodObserver create(Method method) {
        AttributeMatcher pathMatcher= method.getAnnotation(AttributeMatcher.class);
        return new AttributeMatcherMethodObserver(pathMatcher);
        
    }
    
    @Override
    public boolean shouldObserve(String elementName, String attributeName) {
        return matcher!=null && elementName.equals(matcher.elementName()) && attributeName.equals(matcher.attributeName());
    }
}
