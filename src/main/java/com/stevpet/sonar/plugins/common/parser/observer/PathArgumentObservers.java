package com.stevpet.sonar.plugins.common.parser.observer;

import java.util.function.Consumer;

public interface PathArgumentObservers<T> {
    void put(Consumer<T> observer);
    void observe(T arg);
}
