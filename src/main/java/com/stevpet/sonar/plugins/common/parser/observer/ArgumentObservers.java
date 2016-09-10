package com.stevpet.sonar.plugins.common.parser.observer;

public interface ArgumentObservers<T> {

	    void observe(String path, T value);

	    void register(String path, ArgumentObserver<T> observer);

		boolean hasMatch(String elementPath);
}
