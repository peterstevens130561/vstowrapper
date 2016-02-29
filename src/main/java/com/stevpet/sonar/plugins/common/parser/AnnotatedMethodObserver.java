package com.stevpet.sonar.plugins.common.parser;

import java.lang.reflect.Method;

public interface AnnotatedMethodObserver {

    public boolean shouldObserve(String key,String value);

    public Method getMethod();
}
