package com.stevpet.sonar.plugins.common.parser.observer;

public class ElementBodyRegistrar {

    private final String name;
	private final ObserversRepository observersRepository;

    public ElementBodyRegistrar(String name, ObserversRepository observersRepository ) {
        this.observersRepository = observersRepository;
        this.name=name;
    }


	public ElementBodyRegistrar onAttribute(String attribute, ValueObserver observer) {
        observersRepository.registerAttributeObserver(name + "/" + attribute, observer);
        return this;
    }
    
}
