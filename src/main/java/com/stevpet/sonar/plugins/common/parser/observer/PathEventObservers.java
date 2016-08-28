package com.stevpet.sonar.plugins.common.parser.observer;

public interface PathEventObservers {
    void put(EventObserver observer);
    void observe();
}
