package com.stevpet.sonar.plugins.common.parser.observerdsl;

/**
 * select the first level where we'll start observing.
 * 
 * Suppose a nierarchy Modules/Module/Classes/Class/Methods/Method/SequencePoints, and we want to start
 * observing element ClassName at Class, then inpath is "Modules/Module/Classes/Class"
 * @author stevpet
 *
 */
public interface TopLevelObserverRegistrar{
	
	/**
	 * select the top level to start observing
	 * @param path string i.e. "Modules/Module"
	 * @return
	 */
		ObserverRegistrar inPath(String path);
}
