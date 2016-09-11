package com.stevpet.sonar.plugins.common.parser.observer;

public interface PathArgumentObservers<T> {
    void put(ArgumentObserver <T> observer);
    void observe(T arg);
}
