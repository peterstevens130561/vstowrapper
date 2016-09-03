package com.stevpet.sonar.plugins.common.parser.observer;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;

public class StartObserverRegistration implements StartObserverRegistrar {
	private ObserversRepository observersRepository;
    
    public StartObserverRegistration() {

    }
    public StartObserverRegistration(ObserversRepository observersRepository) {
        this.observersRepository = observersRepository;
    }

	@Override
	public ObserverRegistrar inPath(String path) {
        observersRepository.addPath(path);
        ObserverRegistrar t = new PathSpecificationObserverRegistrationFacade(path,observersRepository);
        return t;
	}

}
