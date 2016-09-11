package com.stevpet.sonar.plugins.common.parser.observer;

import java.util.List;
import java.util.function.Consumer;

import com.stevpet.sonar.plugins.common.parser.observerdsl.DefaultElementBodyRegistrar;

public interface ObserversRepository {

	void observeEntry(String path);

	void observeExit(String path);

	void observeElement(String elementPath, String text);

	void observeAttribute(String path, String attributeValue);

	void addPath(String parent);

	List<String> buildHierarchy();

	void registerElementObserver(String path, Consumer<String> observer);

	void registerEntryObserver(String path, EventObserver observer);

	void registerExitObserver(String path, EventObserver observer);

	void registerAttributeObservers(String path, Consumer<DefaultElementBodyRegistrar> attributeRegistration);

	boolean hasElementMatch(String elementPath);

	void registerAttributeObserver(String string, Consumer<String> observer);

	void registerElementEventArgsObserver(String name, Consumer<ParserEventArgs> observer);

	void observeElement(String path, ParserEventArgs eventArgs);

}
