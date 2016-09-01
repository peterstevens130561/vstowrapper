package com.stevpet.sonar.plugins.common.parser.observer;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;

public class StartObserverRegistration implements ObserverStartRegistrar {

    private ValueObservers elementObservers = new DefaultValueObservers();
    private ValueObservers attributeObservers= new DefaultValueObservers();
    private EventObservers entryObservers = new DefaultEventObservers();
    private EventObservers exitObservers = new DefaultEventObservers();
	private XmlHierarchyBuilder hierarchyBuilder = new DefaultXmlHierarchyBuilder();
    private String ancestry;
	private ValueObservers pathObservers = new DefaultValueObservers();
    
    public StartObserverRegistration() {

    }
    public StartObserverRegistration(String ancestry,XmlHierarchyBuilder hierarchyBuilder, ValueObservers elementObservers, ValueObservers pathObservers, ValueObservers attributeObservers, EventObservers entryObservers, EventObservers exitObservers) {
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

}
