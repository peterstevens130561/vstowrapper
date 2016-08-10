package com.stevpet.sonar.plugins.common.parser;

import java.util.function.Consumer;

public class DefaultObserverRegistrationFacade implements ObserverRegistrar {

    
    private final ValueObservers pathObservers;
    private final ValueObservers attributeObservers;
    private ValueObservers elementObservers;
    public DefaultObserverRegistrationFacade(ValueObservers elementObservers, ValueObservers pathObservers, ValueObservers attributeObservers) {
        this.elementObservers = elementObservers;
        this.pathObservers = pathObservers;
        this.attributeObservers=attributeObservers;
    }

    @Override
    public ObserverRegistrar onElement(ValueObserver observer, String element) {
        elementObservers.register(element, observer);
        return this;
    }

    @Override
    public ObserverRegistrar onPath(ValueObserver pathObserver, String path) {
        pathObservers.register(path, pathObserver);
        return this;
    }


    @Override
    public ObserverRegistrar onAttribute(ValueObserver pathObserver, String string) {
        attributeObservers.register(string,pathObserver);
        return this;
    }

    @Override
    public ObserverRegistrar onEntry(Consumer<Void> pathObserver, String string) {
        return this;
    }

    @Override
    public ObserverRegistrar onExit(Consumer<Void> pathObserver, String string) {
        return this;
    }


}
