package com.stevpet.sonar.plugins.common.parser;

import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class BasicObserver extends BaseParserObserver {

    @ElementMatcher(elementName="public") 
    public void public_method(String value) {
        
    }
    
    @ElementMatcher(elementName="private")
    public void private_method(String value) {
        
    }
    
    @PathMatcher(path="a/b/c")
    public void path_method(String value) {
        
    }
    
    @AttributeMatcher(attributeName = "a", elementName = "element")
    public void attributeMatcher_a(String value) {
        
    }
    
    @AttributeMatcher(attributeName = "b", elementName = "element")
    public void attributeMatcher_b(String value) {
        
    }
    
    @ElementObserver(path = "a/b/c", event = Event.ENTRY )
    public void elementObserverEntry() {
        
    }
    
    @ElementObserver(path = "a/b/c", event = Event.EXIT )
    public void elementObserverExit() {
        
    }
}
