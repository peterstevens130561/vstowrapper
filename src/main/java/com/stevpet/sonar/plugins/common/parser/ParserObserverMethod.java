package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;

import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class ParserObserverMethod {
    private PathMatcher pathMatcher;
    private ElementMatcher elementMatcher;
    public ParserObserverMethod(Method method) {
            pathMatcher = method.getAnnotation(PathMatcher.class);
            elementMatcher = method.getAnnotation(ElementMatcher.class);
    }
    public PathMatcher getPatchMatcher() {
        return pathMatcher;
    }
    public void setPatchMatcher(PathMatcher patchMatcher) {
        this.pathMatcher = patchMatcher;
    }
    public ElementMatcher getElementMatcher() {
        return elementMatcher;
    }
    public void setElementMatcher(ElementMatcher elementMatcher) {
        this.elementMatcher = elementMatcher;
    }
}
