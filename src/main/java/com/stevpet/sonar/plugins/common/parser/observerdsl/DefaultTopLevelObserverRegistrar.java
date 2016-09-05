package com.stevpet.sonar.plugins.common.parser.observerdsl;

import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;

public class DefaultTopLevelObserverRegistrar implements TopLevelObserverRegistrar {
	private ObserversRepository observersRepository;
    
    public DefaultTopLevelObserverRegistrar() {

    }
    public DefaultTopLevelObserverRegistrar(ObserversRepository observersRepository) {
        this.observersRepository = observersRepository;
    }

	@Override
	public ObserverRegistrar inPath(String path) {
        observersRepository.addPath(path);
        ObserverRegistrar t = new DefaultObserverRegistrar(path,observersRepository);
        return t;
	}

}
