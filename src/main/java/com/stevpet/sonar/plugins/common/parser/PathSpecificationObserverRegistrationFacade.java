package com.stevpet.sonar.plugins.common.parser;

import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;

public class PathSpecificationObserverRegistrationFacade implements ObserverRegistrar {

    
    private final ValueObservers pathObservers;
    private final ValueObservers attributeObservers;
    private ValueObservers elementObservers;
    private EventObservers entryObservers;
    private EventObservers exitObservers;
    private String ancestry;
    public PathSpecificationObserverRegistrationFacade(String ancestry,ValueObservers elementObservers, ValueObservers pathObservers, ValueObservers attributeObservers, EventObservers entryObservers, EventObservers exitObservers) {
        this.ancestry=ancestry;
        this.elementObservers = elementObservers;
        this.pathObservers = pathObservers;
        this.attributeObservers=attributeObservers;
        this.entryObservers=entryObservers;
        this.exitObservers=exitObservers;
    }

    private ObserverRegistrar create(String parent) {
        return new PathSpecificationObserverRegistrationFacade(parent,elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
    }
    @Override
    public ObserverRegistrar onElement(String element, ValueObserver observer) {
        elementObservers.register(createPath(element), observer);
        return this;
    }

    @Override
    public ObserverRegistrar onPath(ValueObserver pathObserver, String path) {
        pathObservers.register(createPath(path), pathObserver);
        return this;
    }


    @Override
    public ObserverRegistrar onAttribute(String path, ValueObserver pathObserver) {
        attributeObservers.register(createPath(path),pathObserver);
        return this;
    }

    @Override
    public ObserverRegistrar onEntry(String path, EventObserver eventObserver) {
        entryObservers.register(createPath(path),eventObserver);
        return this;
    }

    @Override
    public ObserverRegistrar onExit(String element, EventObserver eventObserver) {
        exitObservers.register(createPath(element),eventObserver);
        return this;
    }
    
	@Override
	public ObserverRegistrar onEntry(EventObserver entryObserver) {
		entryObservers.register(ancestry, entryObserver);
		return this;
	}

	@Override
	public ObserverRegistrar onExit(EventObserver exitObserver) {
		exitObservers.register(ancestry, exitObserver);
		return this;
	}

    @Override
    public ObserverRegistrar inElement(String name, Consumer<AttributeRegistrar> attributeRegistration) {
        String parent = createPath(name);
        AttributeRegistrar t = new AttributeRegistrar(parent,attributeObservers);
        attributeRegistration.accept(t);
        return this;
    }
    
    @Override
    public ObserverRegistrar inPath(String path,Consumer<ObserverRegistrar> registrar) {
        String parent = createPath(path);
        ObserverRegistrar t = this.create(parent) ;
        registrar.accept(t);
        return this;
    }
    
	@Override
	public ObserverRegistrar inPath(String path) {
        String parent = createPath(path);
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
	public AttributeRegistrar inElement(String name) {
        String parent = createPath(name);
        AttributeRegistrar t = new  AttributeRegistrar(parent, attributeObservers);
        return t;
	}




}
