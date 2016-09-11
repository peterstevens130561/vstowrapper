package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.stevpet.sonar.plugins.common.parser.observer.PathArgumentObservers;

public class DefaultPathArgumentObservers<T> implements PathArgumentObservers<T> {

    private final List<Consumer<T>> observers = new ArrayList<Consumer<T>>();
    @Override
    public void put(Consumer<T> observer) {
        observers.add(observer);
    }
    @Override
    public void observe(T value) {
        observers.forEach(action -> action.accept(value));
    }
}