package com.stevpet.sonar.plugins.common.parser;


public interface PathValueObservers {
    void put(ValueObserver observer);
    void observe(String value);
    
}
