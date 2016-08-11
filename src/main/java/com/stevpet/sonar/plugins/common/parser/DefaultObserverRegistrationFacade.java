package com.stevpet.sonar.plugins.common.parser;

import java.util.function.Consumer;

public class DefaultObserverRegistrationFacade implements ObserverRegistrar {

    
    private final ValueObservers pathObservers;
    private final ValueObservers attributeObservers;
    private ValueObservers elementObservers;
    private EventObservers entryObservers;
    private EventObservers exitObservers;
    public DefaultObserverRegistrationFacade(ValueObservers elementObservers, ValueObservers pathObservers, ValueObservers attributeObservers, EventObservers entryObservers, EventObservers exitObservers) {
        this.elementObservers = elementObservers;
        this.pathObservers = pathObservers;
        this.attributeObservers=attributeObservers;
        this.entryObservers=entryObservers;
        this.exitObservers=exitObservers;
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
    public ObserverRegistrar onEntry(EventObserver eventObserver, String path) {
        entryObservers.register(path,eventObserver);
        return this;
    }

    @Override
    public ObserverRegistrar onExit(EventObserver eventObserver, String path) {
        entryObservers.register(path,eventObserver);
        return this;
    }


}
