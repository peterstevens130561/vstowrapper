package com.stevpet.sonar.plugins.common.parser;

public class AttributeRegistrar {

    private final ValueObservers attributeObservers;
    private final String name;

    public AttributeRegistrar(String name, ValueObservers attributeObservers) {
        this.attributeObservers=attributeObservers;
        this.name=name;
    }

    public AttributeRegistrar onAttribute(String attribute, ValueObserver observer) {
        attributeObservers.register(name + "/" + attribute, observer);
        return this;
    }
    
}
