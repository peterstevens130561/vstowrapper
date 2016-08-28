package com.stevpet.sonar.plugins.common.parser.observer;

import java.util.ArrayList;
import java.util.List;

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