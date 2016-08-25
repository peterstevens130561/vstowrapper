package com.stevpet.sonar.plugins.common.parser;

import java.util.function.Consumer;

import org.apache.commons.lang.NotImplementedException;


public class DefaultObserverRegistrationFacade implements ObserverRegistrar {

    
    private final ValueObservers pathObservers;
    private final ValueObservers attributeObservers;
    private ValueObservers elementObservers;
    private EventObservers entryObservers;
    private EventObservers exitObservers;
    private String ancestry;
    private ObserverRegistrar newFacade;
    public DefaultObserverRegistrationFacade(String ancestry,ValueObservers elementObservers, ValueObservers pathObservers, ValueObservers attributeObservers, EventObservers entryObservers, EventObservers exitObservers) {
        this.ancestry=ancestry;
        this.elementObservers = elementObservers;
        this.pathObservers = pathObservers;
        this.attributeObservers=attributeObservers;
        this.entryObservers=entryObservers;
        this.exitObservers=exitObservers;
    }


    private ObserverRegistrar create(String parent) {
        return new DefaultObserverRegistrationFacade(parent,elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
    }
    @Deprecated
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

    public void setNewFacade(ObserverRegistrar newFacade) {
        this.newFacade =newFacade;
    }
    
    @Override
    public ObserverRegistrar inElement(String name, Consumer<AttributeRegistrar> attributeRegistration) {
        AttributeRegistrar t = new  AttributeRegistrar(name, attributeObservers);
        attributeRegistration.accept(t);
        return this;
    }
    
	@Override
	public AttributeRegistrar inElement(String name) {
        AttributeRegistrar t = new  AttributeRegistrar(name, attributeObservers);
        return t;
	}

    
    @Override
    public ObserverRegistrar inPath(String path,Consumer<ObserverRegistrar> registrar) {
        String parent = createPath(path);
        ObserverRegistrar t = newFacade;
        t.setName(parent);
        registrar.accept(t);
        return this;
    }
    
    @Override
    public ObserverRegistrar inPath(String path) {
        String parent = createPath(path);
        ObserverRegistrar t = newFacade;
        t.setName(parent);
        return t ;
    }
    private String createPath(String path) {
        return path;
    }


    @Override
    public void setName(String name) {
    }


	@Override
	public ObserverRegistrar onEntry(EventObserver entryObserver) {
		throw new NotImplementedException();
	}


	@Override
	public ObserverRegistrar onExit(EventObserver exitObserver) {
		throw new NotImplementedException();
	}






}
