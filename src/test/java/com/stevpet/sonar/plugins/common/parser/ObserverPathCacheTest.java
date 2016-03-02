package com.stevpet.sonar.plugins.common.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class ObserverPathCacheTest {
    private final ObserverPathCache cache = new ObserverPathCache();
    
    @Mock private ParserObserverMethods observerA ;
    @Mock private ParserObserverMethods observerB ;

    private List<ParserObserverMethods> observers;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        observers = new ArrayList<ParserObserverMethods>();
        observers.add(observerA);
        observers.add(observerB);
        cache.setParserObservers(observers);
    }
    
    @Test
    public void simple() {
        when(observerA.isMatch("a/b/c")).thenReturn(true);
        when(observerB.isMatch("a/b/d")).thenReturn(false); 
        List<ParserObserverMethods> result = cache.getObserversMatchingPath("a/b");
        assertNotNull(result);
        assertEquals("no elements",0,result.size());
    }

    @Test
    public void oneElement() {
        when(observerA.isMatch("a/b/c")).thenReturn(true);
        when(observerB.isMatch("a/b/d")).thenReturn(true); 
        List<ParserObserverMethods> result = cache.getObserversMatchingPath("a/b/c");
        assertNotNull(result);
        assertEquals("one element",1,result.size());
    }
    
    @Test
    public void twoElement() {
        when(observerA.isMatch("a/b/c")).thenReturn(true);
        when(observerB.isMatch("a/b/c")).thenReturn(true); 
        List<ParserObserverMethods> result = cache.getObserversMatchingPath("a/b/c");
        assertNotNull(result);
        assertEquals("two elements",2,result.size());
    }
    
    @Test
    public void cacheElement() {
        when(observerA.isMatch("a/b/c")).thenReturn(true);
        when(observerB.isMatch("a/b/c")).thenReturn(true); 
        List<ParserObserverMethods> result = cache.getObserversMatchingPath("a/b/c");
        // lt the cache do its work
        result=cache.getObserversMatchingPath("a/b/c");
        assertNotNull(result);
        assertEquals("two elements",2,result.size());
    }
}
