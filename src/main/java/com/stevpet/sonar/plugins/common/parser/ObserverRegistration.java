package com.stevpet.sonar.plugins.common.parser;

import com.stevpet.sonar.plugins.common.parser.observer.StartObserverRegistrar;

public interface ObserverRegistration {

    void registerObservers(StartObserverRegistrar registrar);

}
