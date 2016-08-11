package com.stevpet.sonar.plugins.common.parser;

public interface EventObservers {
    void observe(String path);

    void register(String path, EventObserver observer);
}
