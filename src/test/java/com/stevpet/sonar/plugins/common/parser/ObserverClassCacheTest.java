package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.ParserObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;

public class ObserverClassCacheTest {

    private ObserverMethodCache observerClassCache;

    @Before
    public void before() {
        observerClassCache= new ObserverMethodCache();
        List<ParserObserver> observers=new ArrayList<>();
        observers.add(new BasicObserver());
        observerClassCache.addAll(observers);

    }
    @Test
    public void hasPublic() {
        Method method=observerClassCache.getMatchingElementMethod("somepath", "public");
        assertNotNull("expect method",method);
        assertEquals("public_method",method.getName());
    }
    
    @Test
    public void hasPrivate() {
        Method method=observerClassCache.getMatchingElementMethod("somepath", "private");
        assertNotNull("expect method",method);
        assertEquals("private_method",method.getName());
    }
    
    @Test
    public void noMatch() {
        Method method=observerClassCache.getMatchingElementMethod("somepath", "somelement");
        assertNull("expect no method",method);
    }
    
    @Test
    public void pathMatch() {
        Method method=observerClassCache.getMatchingElementMethod("a/b/c","c");
        assertNotNull("expect method",method);
        assertEquals("path_method",method.getName());
    }
    
    @Test
    public void attributeMatchA() {
        Method method = observerClassCache.matchingAttribute("element","a");
        assertNotNull("expect method",method);
        assertEquals("attributeMatcher_a",method.getName());
    }
    
    @Test
    public void attributeMatchB() {
        Method method = observerClassCache.matchingAttribute("element","b");
        assertNotNull("expect method",method);
        assertEquals("attributeMatcher_b",method.getName());
    }
    
    @Test
    public void noAttributeMatch() {
        Method method = observerClassCache.matchingAttribute("element","c");
        assertNull("expect no method",method);      
    }
    
    @Test
    public void elementMatchEntry() {
        Method method = observerClassCache.matchingElementObserver("a/b/c", Event.ENTRY);
        assertNotNull("expect method",method);
        assertEquals("elementObserverEntry",method.getName());
    }
    
    @Test
    public void elementMatchExit() {
        Method method = observerClassCache.matchingElementObserver("a/b/c", Event.EXIT);
        assertNotNull("expect method",method);
        assertEquals("elementObserverExit",method.getName());
    }
}
