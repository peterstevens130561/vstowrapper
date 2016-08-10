package com.stevpet.sonar.plugins.common.parser;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

public class DefaultObserverRegistry implements ObserverRegistry {

    Dictionary<String,List<ValueObserver>> pathObservers = new Hashtable<String,List<ValueObserver>>(1024);
    @Override
    public ObserverRegistry onElement(Consumer<String> elementObserver, String string) {
        return this;
    }

    @Override
    public ObserverRegistry onPath(ValueObserver pathObserver, String path) {
        List<ValueObserver> observers = pathObservers.get(path);
        if(observers==null) {
            observers = new ArrayList<ValueObserver>();
            pathObservers.put(path, observers);
        }
        observers.add(pathObserver);
        return this;
    }

    @Override
    public ObserverRegistry onAttribute(Consumer<String> pathObserver, String string) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public ObserverRegistry onEntry(Consumer<Void> pathObserver, String string) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public ObserverRegistry onExit(Consumer<Void> pathObserver, String string) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public void invokePathObserver(String path,String value) {
       List<ValueObserver> observers = pathObservers.get(path);
       if(observers!=null) {
           observers.forEach(observer -> observer.observe(value));
       }
    }

}
