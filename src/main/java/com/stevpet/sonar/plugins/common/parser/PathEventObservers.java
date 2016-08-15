package com.stevpet.sonar.plugins.common.parser;

public interface PathEventObservers {
    void put(EventObserver observer);
    void observe();
}
