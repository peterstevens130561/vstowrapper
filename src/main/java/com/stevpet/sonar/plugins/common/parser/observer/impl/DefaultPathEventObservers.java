package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.PathEventObservers;

public class DefaultPathEventObservers implements PathEventObservers {

    private final List<EventObserver> observers = new ArrayList<>();
    @Override
    public void put(EventObserver observer) {
        observers.add(observer);
    }
    @Override
    public void observe() {
        observers.forEach(action -> action.observe());
    }

}
