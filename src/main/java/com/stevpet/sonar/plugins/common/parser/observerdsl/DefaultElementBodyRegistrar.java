package com.stevpet.sonar.plugins.common.parser.observerdsl;

import java.util.function.Consumer;

import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObserver;

public class DefaultElementBodyRegistrar implements ElementBodyObserverRegistrar {

    private final String name;
	private final ObserversRepository observersRepository;

    public DefaultElementBodyRegistrar(String name, ObserversRepository observersRepository ) {
        this.observersRepository = observersRepository;
        this.name=name;
    }


	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ElementBodyObserverRegistrar#onAttribute(java.lang.String, com.stevpet.sonar.plugins.common.parser.observer.ValueObserver)
	 */
	@Override
	public ElementBodyObserverRegistrar onAttribute(String attribute, Consumer<String> observer) {
        observersRepository.registerAttributeObserver(name + "/" + attribute, observer);
        return this;
    }
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ElementBodyObserverRegistrar#onEntry(com.stevpet.sonar.plugins.common.parser.observer.EventObserver)
	 */
	@Override
	public ElementBodyObserverRegistrar onEntry(EventObserver observer) {
		observersRepository.registerEntryObserver(name, observer);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ElementBodyObserverRegistrar#onExit(com.stevpet.sonar.plugins.common.parser.observer.EventObserver)
	 */
	@Override
	public ElementBodyObserverRegistrar onExit(EventObserver observer) {
		observersRepository.registerExitObserver(name, observer);
		return this;
	}
	
	@Override
	public ElementBodyObserverRegistrar onValue(Consumer<String> observer) {
		observersRepository.registerElementObserver(name,observer);
		return this;
	}
	
    
}
