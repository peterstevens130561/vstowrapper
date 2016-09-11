package com.stevpet.sonar.plugins.common.parser.observer;

import java.util.function.Consumer;

public interface ArgumentObservers<T> {

	    void observe(String path, T value);

	    void register(String path, Consumer<T> observer);

		boolean hasMatch(String elementPath);
}
