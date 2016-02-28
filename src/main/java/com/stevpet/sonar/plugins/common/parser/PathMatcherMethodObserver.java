package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;

import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class PathMatcherMethodObserver implements AnnotatedMethodObserver {

    private final PathMatcher pathMatcher;

    public PathMatcherMethodObserver(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }
    
    public static AnnotatedMethodObserver create(Method method) {
        PathMatcher pathMatcher= method.getAnnotation(PathMatcher.class);
        return new PathMatcherMethodObserver(pathMatcher);
        
    }
    
    @Override
    public boolean shouldObserve(String key, String value) {

        return pathMatcher!=null && key.equals(pathMatcher.path());
    }

}
