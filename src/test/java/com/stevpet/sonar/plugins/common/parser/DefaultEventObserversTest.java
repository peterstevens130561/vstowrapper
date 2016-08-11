package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class DefaultEventObserversTest {

    private EventObservers observers = new DefaultEventObservers();
    private int observedPath;
    private int observedPathB;
    
    @Before
    public void before() {
        
    }
    
    @Test
    public void requestWithNoObserversRegistered() {
        try {
            observers.observe("somepath");
        } catch (Exception e) {
            fail("should have continued on empty list");
        }
    }
    
    @Test
    public void registerOneAndRequest() {
        observedPath=0;
        observers.register("path", this::observerPath);
        observers.observe("path");
        assertEquals(1,observedPath);
    }
    
    
    @Test
    public void registerTwoAndRequest() {
        observedPath=0;
        observers.register("pathA", this::observerPath);
        observers.register("pathB", this::observerPathB);
        observers.observe("pathA");
        assertEquals(1,observedPath);
        assertEquals(0,observedPathB);
        observers.observe("pathB");
        assertEquals(1,observedPathB);
    }
    public void observerPath() {
        observedPath++;
    }
    
    public void observerPathB() {
        observedPathB++;
    }
}
