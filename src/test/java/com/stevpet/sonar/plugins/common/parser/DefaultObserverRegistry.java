package com.stevpet.sonar.plugins.common.parser;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

public class DefaultObserverRegistry implements ObserverRegistry {

    Dictionary<String,ValueObservers> pathObservers = new Hashtable<String,ValueObservers>(1024);
    @Override
    public ObserverRegistry onElement(Consumer<String> elementObserver, String string) {
        return this;
    }

    @Override
    public ObserverRegistry onPath(ValueObserver pathObserver, String path) {
        ValueObservers observers = pathObservers.get(path);
        if(observers==null) {
            observers = new DefaultValueObsers();
            pathObservers.put(path, observers);
        }
        observers.put(pathObserver);
        return this;
    }

    @Override
    public ObserverRegistry onAttribute(Consumer<String> pathObserver, String string) {

        return this;
    }

    @Override
    public ObserverRegistry onEntry(Consumer<Void> pathObserver, String string) {

        return this;
    }

    @Override
    public ObserverRegistry onExit(Consumer<Void> pathObserver, String string) {
        return this;
    }

    @Override
    public ObserverRegistry invokePathObserver(String path,String value) {
       ValueObservers observers = pathObservers.get(path);
       if(observers!=null) {
           observers.observe(value);
       }
       return this;
    }

}
