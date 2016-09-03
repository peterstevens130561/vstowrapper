package com.stevpet.sonar.plugins.common.parser.observer;

import java.util.List;
import java.util.function.Consumer;

public interface ObserversRepository {

	void observeEntry(String path);

	void observeExit(String path);

	void observeElement(String elementPath, String text);

	void observeAttribute(String path, String attributeValue);

	void addPath(String parent);

	List<String> buildHierarchy();

	void registerElementObserver(String path, ValueObserver observer);

	void registerEntryObserver(String path, EventObserver observer);

	void registerExitObserver(String path, EventObserver observer);

	void registerAttributeObservers(String path, Consumer<ElementBodyRegistrar> attributeRegistration);

	boolean hasElementMatch(String elementPath);

	void registerAttributeObserver(String string, ValueObserver observer);

}
