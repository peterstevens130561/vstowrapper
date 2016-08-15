package com.stevpet.sonar.plugins.common.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DefaultObserverRegistrationFacadeTest {
    private ObserverRegistrar registrar ;
    @Mock private ValueObservers elementObservers;
    @Mock private ValueObservers pathObservers;
    @Mock private ValueObservers attributeObservers;
    @Mock private EventObservers entryObservers;
    @Mock private EventObservers exitObservers;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        registrar = new DefaultObserverRegistrationFacade(elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
    }
    
    @Test
    public void testElement() {
        ValueObserver observer = this::valueObserver;
        registrar.onElement(observer,"booh");
        verify(elementObservers,times(1)).register("booh",observer);
    }
    
    @Test
    public void testPath() {
        ValueObserver observer = this::valueObserver;
        registrar.onPath(observer,"booh");
        verify(pathObservers,times(1)).register("booh",observer);
    }
    
    @Test
    public void testAttribute() {
        ValueObserver observer = this::valueObserver;
        registrar.onAttribute("booh",observer);
        verify(attributeObservers,times(1)).register("booh",observer);
    }
    
    @Test
    public void testEntry() {
        EventObserver observer = this::eventObserver;
        registrar.onEntry(observer,"booh");
        verify(entryObservers,times(1)).register("booh",observer);
    }
    
    @Test
    public void testExit() {
        EventObserver observer = this::eventObserver;
        registrar.onExit(observer,"booh");
        verify(exitObservers,times(1)).register("booh",observer);
    }
    
    @Test
    public void testAttributeInElement() {
        ValueObserver observer = this::valueObserver;      
        registrar.inElement("element",(registrar -> registrar
                        .onAttribute("a",observer)
                        .onAttribute("b",observer)));
        verify(attributeObservers,times(1)).register("element/a",observer);        
        verify(attributeObservers,times(1)).register("element/b",observer);         
    }
    
    private void valueObserver(String value) {
        
    }
    
    private void eventObserver() {
        
    }
}
