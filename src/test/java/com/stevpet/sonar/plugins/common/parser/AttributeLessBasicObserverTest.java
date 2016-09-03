package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultEventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.StartObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observer.PathSpecificationObserverRegistrationFacade;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;
public class AttributeLessBasicObserverTest extends BaseParserObserver  {

    private int observedPath;

    private int entryObserved;
    private int exitObserved;
	private XmlHierarchyBuilder xmlHierarchyBuilder;
	private DefaultObserversRepository observersRepository;

    @Before
    public void before() {
    	observersRepository = new DefaultObserversRepository();
    }
    
    @Override
    public void registerObservers(StartObserverRegistrar methodRegistry) {
        methodRegistry.inPath("").onElement("public",this::public_method)
        .onElement("private",this::private_method)
        .inPath("a/b").onElement("c",this::path_method)
        .inElement("element", v-> v.onAttribute("a",this::attributeMatcher_a))
        .onEntry("a/b/c", this::elementObserverEntry)
        .onExit("a/b/c",this::elementObserverExit);
    }
    
    @Test
    public void invokeTest() {
        StartObserverRegistrar methodRegistry = newObserverRegistrationFacade();
        registerObservers(methodRegistry);
        observedPath=0;
        observersRepository.observeElement("a/b/c","value");
        assertEquals(1,observedPath);
    }
    
    @Test
    public void invokeRegisterTwice() {
        StartObserverRegistrar methodRegistry = newObserverRegistrationFacade();
        registerObservers(methodRegistry);
        registerObservers(methodRegistry);
        observedPath=0;
        observersRepository.observeElement("a/b/c","value");
        assertEquals(2,observedPath);
    }
    
    @Test
    public void invokeObserverTwice() {
        StartObserverRegistrar methodRegistry = newObserverRegistrationFacade();
        registerObservers(methodRegistry);
        observedPath=0;
        observersRepository.observeElement("a/b/c","value");
        observersRepository.observeElement("a/b/c","value");
        assertEquals(2,observedPath);
    }
    
    
    private StartObserverRegistrar newObserverRegistrationFacade() {
        return new PathSpecificationObserverRegistrationFacade("",observersRepository);
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
