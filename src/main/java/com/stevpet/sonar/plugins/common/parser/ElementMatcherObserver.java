package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;

import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;

public class ElementMatcherObserver implements AnnotatedMethodObserver {

    private ElementMatcher matcher;
    
    public ElementMatcherObserver(ElementMatcher matcher) {
        this.matcher=matcher;
    }
    public static AnnotatedMethodObserver create(Method method) {
        ElementMatcher matcher= method.getAnnotation(ElementMatcher.class);
        return new ElementMatcherObserver(matcher);
    }

    @Override
    public boolean shouldObserve(String key, String value) {

        return matcher!=null && key.equals(matcher.elementName());
    }

}
