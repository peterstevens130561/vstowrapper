package com.stevpet.sonar.plugins.common.parser;

import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;

public interface ObserverRegistration {

    void registerObservers(TopLevelObserverRegistrar registrar);

}
