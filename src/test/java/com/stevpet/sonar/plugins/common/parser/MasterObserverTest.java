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

public class MasterObserverTest {

    private MasterObserver masterObserver;

    @Before
    public void before() {
        masterObserver= new MasterObserver();
        List<ParserObserver> observers=new ArrayList<>();
        observers.add(new BasicObserver());
        masterObserver.addAll(observers);

    }
    @Test
    public void hasPublic() {
        Method method=masterObserver.matchingElement("somepath", "public");
        assertNotNull("expect method",method);
        assertEquals("public_method",method.getName());
    }
    
    @Test
    public void hasPrivate() {
        Method method=masterObserver.matchingElement("somepath", "private");
        assertNotNull("expect method",method);
        assertEquals("private_method",method.getName());
    }
    
    @Test
    public void noMatch() {
        Method method=masterObserver.matchingElement("somepath", "somelement");
        assertNull("expect no method",method);
    }
    
    @Test
    public void pathMatch() {
        Method method=masterObserver.matchingElement("a/b/c","c");
        assertNotNull("expect method",method);
        assertEquals("path_method",method.getName());
    }
    
    @Test
    public void attributeMatchA() {
        Method method = masterObserver.matchingAttribute("element","a");
        assertNotNull("expect method",method);
        assertEquals("attributeMatcher_a",method.getName());
    }
    
    @Test
    public void attributeMatchB() {
        Method method = masterObserver.matchingAttribute("element","b");
        assertNotNull("expect method",method);
        assertEquals("attributeMatcher_b",method.getName());
    }
}
