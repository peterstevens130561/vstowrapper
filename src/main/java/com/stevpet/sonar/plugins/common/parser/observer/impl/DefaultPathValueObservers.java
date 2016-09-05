package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.common.parser.observer.PathValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObserver;

public class DefaultPathValueObservers implements PathValueObservers {

    private final List<ValueObserver> observers = new ArrayList<ValueObserver>();
    @Override
    public void put(ValueObserver observer) {
        observers.add(observer);
    }
    @Override
    public void observe(String value) {
        observers.forEach(action -> action.observe(value));
    }
}