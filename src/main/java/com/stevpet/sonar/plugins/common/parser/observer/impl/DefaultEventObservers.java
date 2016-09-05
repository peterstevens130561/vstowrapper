package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.PathEventObservers;

public class DefaultEventObservers implements EventObservers {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEventObservers.class);
    Dictionary<String,PathEventObservers> observerDictionary = new Hashtable<>(1024);
    @Override
    public void observe(String path) {
        PathEventObservers observers = observerDictionary.get(path);
        if(observers!=null) {
            observers.observe();
        }
    }

    @Override
    public void register(String path, EventObserver observer) {
        PathEventObservers observers = observerDictionary.get(path);
        if(observers==null) {
            observers = new DefaultPathEventObservers();
            observerDictionary.put(path, observers);
        }
        observers.put(observer);
    }

}
