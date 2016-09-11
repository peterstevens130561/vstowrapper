package com.stevpet.sonar.plugins.common.parser.observer.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import com.stevpet.sonar.plugins.common.parser.observer.ArgumentObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ArgumentObservers;
import com.stevpet.sonar.plugins.common.parser.observer.PathArgumentObservers;

public class DefaultArgumentObservers<T> implements ArgumentObservers<T> {

    Dictionary<String,PathArgumentObservers<T>> observerDictionary = new Hashtable<String,PathArgumentObservers<T>>(1024);

    @Override
    public void register(String path, ArgumentObserver<T> observer) {
            PathArgumentObservers<T> observers = observerDictionary.get(path);
            if(observers==null) {
                observers = new DefaultPathArgumentObservers<T>();
                observerDictionary.put(path, observers);
            }
            observers.put(observer);
        }


    @Override
    public void observe(String path,T value) {
        PathArgumentObservers<T> observers = observerDictionary.get(path);
        if(observers!=null) {
            observers.observe(value);
        }
    }


	@Override
	public boolean hasMatch(String elementPath) {
		return observerDictionary.get(elementPath) !=null;
	}


}
