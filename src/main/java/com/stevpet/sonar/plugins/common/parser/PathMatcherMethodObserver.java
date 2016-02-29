package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;

import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class PathMatcherMethodObserver implements AnnotatedMethodObserver {

    private final PathMatcher pathMatcher;
    private final Method method;

    public PathMatcherMethodObserver(Method method,PathMatcher pathMatcher) {
        this.method=method;
        this.pathMatcher = pathMatcher;
    }
    
    public static AnnotatedMethodObserver create(Method method) {
        PathMatcher pathMatcher= method.getAnnotation(PathMatcher.class);
        return new PathMatcherMethodObserver(method,pathMatcher);
        
    }
    
    @Override
    public boolean shouldObserve(String key, String value) {

        return pathMatcher!=null && key.equals(pathMatcher.path());
    }

    @Override
    public Method getMethod() {
        return method;
    }

}
