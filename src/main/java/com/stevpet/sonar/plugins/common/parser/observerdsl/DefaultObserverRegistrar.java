package com.stevpet.sonar.plugins.common.parser.observerdsl;

import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;

public class DefaultObserverRegistrar implements ObserverRegistrar{
    
    private String ancestry;
	private ObserversRepository defaultObserversRepository;
    
    public DefaultObserverRegistrar(String ancestry,ObserversRepository defaultObserversRepository) {
        this.ancestry=ancestry;
        this.defaultObserversRepository=defaultObserversRepository;
    }

    private ObserverRegistrar create(String parent) {
        return new DefaultObserverRegistrar(parent,defaultObserversRepository);
    }
    
    @Override
    public ObserverRegistrar onElement(String element, Consumer<String> observer) {
        defaultObserversRepository.registerElementObserver(createPath(element), observer);
        return this;
    }

    @Override
    public ObserverRegistrar onEntry(String path, EventObserver eventObserver) {
        defaultObserversRepository.registerEntryObserver(createPath(path),eventObserver);
        return this;
    }

    @Override
    public ObserverRegistrar onExit(String element, EventObserver eventObserver) {
        defaultObserversRepository.registerExitObserver(createPath(element),eventObserver);
        return this;
    }
    
	@Override
	public ObserverRegistrar onEntry(EventObserver entryObserver) {
		defaultObserversRepository.registerEntryObserver(ancestry, entryObserver);
		return this;
	}

	@Override
	public ObserverRegistrar onExit(EventObserver exitObserver) {
		defaultObserversRepository.registerExitObserver(ancestry, exitObserver);
		return this;
	}

    @Override
    public ObserverRegistrar inElement(String name, Consumer<DefaultElementBodyRegistrar> attributeRegistration) {
        String parent = createPath(name);
        DefaultElementBodyRegistrar t = new DefaultElementBodyRegistrar(parent,defaultObserversRepository);
        attributeRegistration.accept(t);
        return this;
    }
    @Override
    public ObserverRegistrar inPath(String path,Consumer<ObserverRegistrar> registrar) {
        ObserverRegistrar t = inPath(path);
        registrar.accept(t);
        return this;
    }
    
	@Override
	public ObserverRegistrar inPath(String path) {
        String parent = createPath(path);
        defaultObserversRepository.addPath(parent);
        ObserverRegistrar t = this.create(parent);
        return t;
	}

    private String createPath(String path) {
        String parent = StringUtils.isEmpty(ancestry)?path:ancestry + "/" + path;
        return parent;
    }

    @Override
    public void setName(String name) {
        this.ancestry=name;
    }

	@Override
	public ElementBodyObserverRegistrar inElement(String name) {
        String parent = createPath(name);
        ElementBodyObserverRegistrar t = new  DefaultElementBodyRegistrar(parent, defaultObserversRepository);
        return t;
	}

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeEntry(java.lang.String)
	 */



}
