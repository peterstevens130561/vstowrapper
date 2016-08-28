package com.stevpet.sonar.plugins.common.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observer.PathSpecificationObserverRegistrationFacade;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PathSpecificationObserverRegistrationFacadeTest {
    private ObserverRegistrar registrar ;
    @Mock private ValueObservers elementObservers;
    @Mock private ValueObservers pathObservers;
    @Mock private ValueObservers attributeObservers;
    @Mock private EventObservers entryObservers;
    @Mock private EventObservers exitObservers;
	@Mock private XmlHierarchyBuilder xmlHierarchyBuilder;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        registrar = new PathSpecificationObserverRegistrationFacade("",xmlHierarchyBuilder, elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
    }
    
    @Test
    public void testElement() {
        ValueObserver observer = this::valueObserver;
        registrar.onElement("booh",observer);
        verify(elementObservers,times(1)).register("booh",observer);
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
        registrar.onEntry("booh",observer);
        verify(entryObservers,times(1)).register("booh",observer);
    }
    
    @Test
    public void testExit() {
        EventObserver observer = this::eventObserver;
        registrar.onExit("booh",observer);
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
