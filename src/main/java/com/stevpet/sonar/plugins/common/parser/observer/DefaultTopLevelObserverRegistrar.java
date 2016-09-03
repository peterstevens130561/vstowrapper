package com.stevpet.sonar.plugins.common.parser.observer;

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
        ObserverRegistrar t = new PathSpecificationObserverRegistrationFacade(path,observersRepository);
        return t;
	}

}
