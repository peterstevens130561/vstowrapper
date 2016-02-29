package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;

import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;

public class ElementMatcherObserver implements AnnotatedMethodObserver {

    private ElementMatcher matcher;
    private final Method method;
    
    public ElementMatcherObserver(Method method, ElementMatcher matcher) {
        this.method=method;
        this.matcher=matcher;
    }
    public static AnnotatedMethodObserver create(Method method) {
        ElementMatcher matcher= method.getAnnotation(ElementMatcher.class);
        return new ElementMatcherObserver(method,matcher);
    }

    @Override
    public boolean shouldObserve(String key, String value) {

        return matcher!=null && key.equals(matcher.elementName());
    }
    @Override
    public Method getMethod() {
        return method;
    }

}
