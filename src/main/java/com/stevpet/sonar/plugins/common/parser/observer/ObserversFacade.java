package com.stevpet.sonar.plugins.common.parser.observer;

public interface ObserversFacade {

	void observeEntry(String path);

	void observeExit(String path);

	void observeElement(String elementPath, String text);

	void observeAttribute(String path, String attributeValue);

}