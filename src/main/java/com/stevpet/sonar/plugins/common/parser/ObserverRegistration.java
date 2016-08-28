package com.stevpet.sonar.plugins.common.parser;

import com.stevpet.sonar.plugins.common.parser.observer.ObserverRegistrar;

public interface ObserverRegistration {

    void registerObservers(ObserverRegistrar registrar);

}
