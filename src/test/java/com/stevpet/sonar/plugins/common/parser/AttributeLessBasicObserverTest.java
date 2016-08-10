package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class AttributeLessBasicObserverTest extends BaseParserObserver implements ObserverRegistration {

    private int observedPath;

    @Override
    public void registerObservers(ObserverRegistry methodRegistry) {
        methodRegistry.onElement(this::public_method,"public")
        .onElement(this::private_method,"private")
        .onPath(this::path_method,"a/b/c")
        .onAttribute(this::attributeMatcher_a,"element/a");
        
    }
    
    @Test
    public void invokeTest() {
        ObserverRegistry methodRegistry = new DefaultObserverRegistry();
        registerObservers(methodRegistry);
        observedPath=0;
        methodRegistry.invokePathObserver("a/b/c","value");
        assertEquals(1,observedPath);
    }
    
    @Test
    public void invokeRegisterTwice() {
        ObserverRegistry methodRegistry = new DefaultObserverRegistry();
        registerObservers(methodRegistry);
        registerObservers(methodRegistry);
        observedPath=0;
        methodRegistry.invokePathObserver("a/b/c","value");
        assertEquals(2,observedPath);
    }
    @ElementMatcher(elementName="public") 
    public void public_method(String a) {
        
    }
    
    @ElementMatcher(elementName="private")
    public void private_method(String a) {
        
    }
    
    @PathMatcher(path="a/b/c")
    public void path_method(String value) {
        observedPath +=1;
    }
    
    @AttributeMatcher(attributeName = "a", elementName = "element")
    public void attributeMatcher_a(String value) {
        
    }
    
    @AttributeMatcher(attributeName = "b", elementName = "element")
    public void attributeMatcher_b() {
        
    }
    
    @ElementObserver(path = "a/b/c", event = Event.ENTRY )
    public void elementObserverEntry() {
        
    }
    
    @ElementObserver(path = "a/b/c", event = Event.EXIT )
    public void elementObserverExit() {
        
    }
}
