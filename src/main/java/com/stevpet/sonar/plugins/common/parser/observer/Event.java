package com.stevpet.sonar.plugins.common.parser.observer;

public interface Event {
	/**
	 * String value of current element/attribute, if available
	 * @return
	 */
	String getValue() ;
	/**
	 * Path to current element/attribute
	 * @return
	 */
	String getPath();

	String getLine();
	
	String getColumn();
	
	void setError();
	
	void setSkipTillNextElement();
}
