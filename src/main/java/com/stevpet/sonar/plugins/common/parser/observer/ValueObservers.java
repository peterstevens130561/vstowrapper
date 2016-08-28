package com.stevpet.sonar.plugins.common.parser.observer;

public interface ValueObservers {

    void observe(String path, String value);

    void register(String path, ValueObserver observer);

}
