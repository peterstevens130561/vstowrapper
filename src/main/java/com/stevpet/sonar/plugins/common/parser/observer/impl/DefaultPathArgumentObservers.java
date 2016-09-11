package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.common.parser.observer.ArgumentObserver;
import com.stevpet.sonar.plugins.common.parser.observer.PathArgumentObservers;
import com.stevpet.sonar.plugins.common.parser.observer.PathValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObserver;

public class DefaultPathArgumentObservers<T> implements PathArgumentObservers<T> {

    private final List<ArgumentObserver<T>> observers = new ArrayList<ArgumentObserver<T>>();
    @Override
    public void put(ArgumentObserver<T> observer) {
        observers.add(observer);
    }
    @Override
    public void observe(T value) {
        observers.forEach(action -> action.observe(value));
    }
}