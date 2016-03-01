package com.stevpet.sonar.plugins.common.parser;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;

public class BasicObserver extends BaseParserObserver {

    @ElementMatcher(elementName="public") 
    public void public_method() {
        
    }
    
    @ElementMatcher(elementName="private")
    public void private_method() {
        
    }
    
    @PathMatcher(path="a/b/c")
    public void path_method() {
        
    }
    
    @AttributeMatcher(attributeName = "a", elementName = "element")
    public void attributeMatcher_a() {
        
    }
    
    @AttributeMatcher(attributeName = "b", elementName = "element")
    public void attributeMatcher_b() {
        
    }
}
