package com.stevpet.sonar.plugins.common.parser;

public interface AnnotatedMethodObserver {

    public boolean shouldObserve(String key,String value);
}
