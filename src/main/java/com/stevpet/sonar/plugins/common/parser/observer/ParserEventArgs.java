package com.stevpet.sonar.plugins.common.parser.observer;

public interface ParserEventArgs {

	String getValue();

	String getPath();

	void setError();

	void setSkipTillNextElement();

}