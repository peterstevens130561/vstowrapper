package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultEventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observer.PathSpecificationObserverRegistrationFacade;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;
public class AttributeLessBasicObserverTest extends BaseParserObserver  {

    private int observedPath;
    private ValueObservers elementObservers;
    private ValueObservers pathObservers;
    private ValueObservers attributeObservers;
    private EventObservers entryObservers;
    private EventObservers exitObservers;
    private int entryObserved;
    private int exitObserved;
	private XmlHierarchyBuilder xmlHierarchyBuilder;

    @Before
    public void before() {
        pathObservers = new DefaultValueObservers();
        elementObservers = new DefaultValueObservers();
        attributeObservers = new DefaultValueObservers();
        entryObservers = new DefaultEventObservers();
        exitObservers = new DefaultEventObservers();
        xmlHierarchyBuilder=new DefaultXmlHierarchyBuilder();
    }
    
    @Override
    public void registerObservers(ObserverRegistrar methodRegistry) {
        methodRegistry.onElement("public",this::public_method)
        .onElement("private",this::private_method)
        .onPath(this::path_method,"a/b/c")
        .onAttribute("element/a",this::attributeMatcher_a)
        .onEntry("a/b/c", this::elementObserverEntry)
        .onExit("a/b/c",this::elementObserverExit);
    }
    
    @Test
    public void invokeTest() {
        ObserverRegistrar methodRegistry = newObserverRegistrationFacade();
        registerObservers(methodRegistry);
        observedPath=0;
        pathObservers.observe("a/b/c","value");
        assertEquals(1,observedPath);
    }
    
    @Test
    public void invokeRegisterTwice() {
        ObserverRegistrar methodRegistry = newObserverRegistrationFacade();
        registerObservers(methodRegistry);
        registerObservers(methodRegistry);
        observedPath=0;
        pathObservers.observe("a/b/c","value");
        assertEquals(2,observedPath);
    }
    
    @Test
    public void invokeObserverTwice() {
        ObserverRegistrar methodRegistry = newObserverRegistrationFacade();
        registerObservers(methodRegistry);
        observedPath=0;
        pathObservers.observe("a/b/c","value");
        pathObservers.observe("a/b/c","value");
        assertEquals(2,observedPath);
    }
    
    
    private ObserverRegistrar newObserverRegistrationFacade() {
        return new PathSpecificationObserverRegistrationFacade("",xmlHierarchyBuilder, elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
    }

    //@ElementMatcher(elementName="public") 
    public void public_method(String a) {
        
    }
    
    //@ElementMatcher(elementName="private")
    public void private_method(String a) {
        
    }
    
    //@PathMatcher(path="a/b/c")
    public void path_method(String value) {
        observedPath +=1;
    }
    
    //@AttributeMatcher(attributeName = "a", elementName = "element")
    public void attributeMatcher_a(String value) {
        
    }
    
    //@AttributeMatcher(attributeName = "b", elementName = "element")
    public void attributeMatcher_b() {
        
    }
    
    public void elementObserverEntry() {
        entryObserved++;
    }
    
    public void elementObserverExit() {
        exitObserved++;
    }
}
