package com.stevpet.sonar.plugins.common.parser.observer;

public interface PathValueObservers {
    void put(ValueObserver observer);
    void observe(String value);
    
}
