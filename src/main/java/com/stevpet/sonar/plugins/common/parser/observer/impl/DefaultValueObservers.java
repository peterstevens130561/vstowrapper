package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import com.stevpet.sonar.plugins.common.parser.observer.PathValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;

public class DefaultValueObservers implements ValueObservers  {

    Dictionary<String,PathValueObservers> observerDictionary = new Hashtable<String,PathValueObservers>(1024);

    @Override
    public void register(String path, ValueObserver observer) {
            PathValueObservers observers = observerDictionary.get(path);
            if(observers==null) {
                observers = new DefaultPathValueObservers();
                observerDictionary.put(path, observers);
            }
            observers.put(observer);
        }


    @Override
    public void observe(String path,String value) {
        PathValueObservers observers = observerDictionary.get(path);
        if(observers!=null) {
            observers.observe(value);
        }
    }


	@Override
	public boolean hasMatch(String elementPath) {
		return observerDictionary.get(elementPath) !=null;
	}

}