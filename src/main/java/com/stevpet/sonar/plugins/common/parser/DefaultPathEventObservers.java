package com.stevpet.sonar.plugins.common.parser;

import java.util.ArrayList;
import java.util.List;

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
