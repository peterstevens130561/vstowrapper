package com.stevpet.sonar.plugins.common.parser.observer;

/**
 * first statement must be the selection
 * @author stevpet
 *
 */
public interface StartObserverRegistrar{
		ObserverRegistrar inPath(String path);
}
