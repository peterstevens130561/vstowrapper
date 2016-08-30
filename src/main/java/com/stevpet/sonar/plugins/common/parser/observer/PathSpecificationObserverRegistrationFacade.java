package com.stevpet.sonar.plugins.common.parser.observer;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;

public class PathSpecificationObserverRegistrationFacade implements ObserverRegistrar, ObserversFacade {

    
    private ValueObservers elementObservers = new DefaultValueObservers();
    private ValueObservers attributeObservers= new DefaultValueObservers();
    private EventObservers entryObservers = new DefaultEventObservers();
    private EventObservers exitObservers = new DefaultEventObservers();
	private XmlHierarchyBuilder hierarchyBuilder = new DefaultXmlHierarchyBuilder();
    private String ancestry;
	private ValueObservers pathObservers = new DefaultValueObservers();
    
    public PathSpecificationObserverRegistrationFacade() {

    }
    public PathSpecificationObserverRegistrationFacade(String ancestry,XmlHierarchyBuilder hierarchyBuilder, ValueObservers elementObservers, ValueObservers pathObservers, ValueObservers attributeObservers, EventObservers entryObservers, EventObservers exitObservers) {
        this.ancestry=ancestry;
        this.elementObservers = elementObservers;
        this.pathObservers = pathObservers;
        this.attributeObservers=attributeObservers;
        this.entryObservers=entryObservers;
        this.exitObservers=exitObservers;
        this.hierarchyBuilder=hierarchyBuilder;
    }

    private ObserverRegistrar create(String parent) {
        return new PathSpecificationObserverRegistrationFacade(parent,hierarchyBuilder,elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
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
        ObserverRegistrar t = inPath(path);
        registrar.accept(t);
        return this;
    }
    
	@Override
	public ObserverRegistrar inPath(String path) {
        String parent = createPath(path);
        hierarchyBuilder.add(parent);
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
	public List<String> build() {
		return hierarchyBuilder.build();
	}
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeEntry(java.lang.String)
	 */
	@Override
	public void observeEntry(String path) {
        entryObservers.observe(path);
	}
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeExit(java.lang.String)
	 */
	@Override
	public void observeExit(String path) {
		exitObservers.observe(path);
	}
	public boolean hasElementMatch(String elementPath) {
		return elementObservers.hasMatch(elementPath) ;
	}
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeElement(java.lang.String, java.lang.String)
	 */
	@Override
	public void observeElement(String elementPath, String text) {
		elementObservers.observe(elementPath,text);
	}
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.common.parser.observer.ObserversFacade#observeAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public void observeAttribute(String path, String attributeValue) {
		attributeObservers.observe(path, attributeValue);
	}




}
