package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
public class AttributeLessBasicObserverTest extends BaseParserObserver  {

    private int observedPath;
    private ValueObservers elementObservers;
    private ValueObservers pathObservers;
    private ValueObservers attributeObservers;
    private EventObservers entryObservers;
    private EventObservers exitObservers;
    private int entryObserved;
    private int exitObserved;

    @Before
    public void before() {
        pathObservers = new DefaultValueObservers();
        elementObservers = new DefaultValueObservers();
        attributeObservers = new DefaultValueObservers();
        entryObservers = new DefaultEventObservers();
        exitObservers = new DefaultEventObservers();
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
        ObserverRegistrar methodRegistry = NewDefaultObserverRegistrationFacade();
        registerObservers(methodRegistry);
        observedPath=0;
        pathObservers.observe("a/b/c","value");
        assertEquals(1,observedPath);
    }
    
    @Test
    public void invokeRegisterTwice() {
        ObserverRegistrar methodRegistry = NewDefaultObserverRegistrationFacade();
        registerObservers(methodRegistry);
        registerObservers(methodRegistry);
        observedPath=0;
        pathObservers.observe("a/b/c","value");
        assertEquals(2,observedPath);
    }
    
    @Test
    public void invokeObserverTwice() {
        ObserverRegistrar methodRegistry = NewDefaultObserverRegistrationFacade();
        registerObservers(methodRegistry);
        observedPath=0;
        pathObservers.observe("a/b/c","value");
        pathObservers.observe("a/b/c","value");
        assertEquals(2,observedPath);
    }
    
    
    private ObserverRegistrar NewDefaultObserverRegistrationFacade() {
        return new DefaultObserverRegistrationFacade("",elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
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
