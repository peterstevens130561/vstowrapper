package com.stevpet.sonar.plugins.common.parser;

import com.stevpet.sonar.plugins.common.parser.observer.TopLevelObserverRegistrar;

public interface ObserverRegistration {

    void registerObservers(TopLevelObserverRegistrar registrar);

}
