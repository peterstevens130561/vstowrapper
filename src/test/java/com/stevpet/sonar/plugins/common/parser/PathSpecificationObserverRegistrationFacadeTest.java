package com.stevpet.sonar.plugins.common.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.observer.DefaultObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observer.StartObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observer.PathSpecificationObserverRegistrationFacade;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PathSpecificationObserverRegistrationFacadeTest {
    private ObserverRegistrar registrar ;
	@Mock private DefaultObserversRepository observersRepository;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        registrar = new PathSpecificationObserverRegistrationFacade("",observersRepository);
    }
    
    @Test
    public void testElement() {
        ValueObserver observer = this::valueObserver;
        registrar.onElement("booh",observer);
        verify(observersRepository,times(1)).registerElementObserver("booh",observer);
    }
    
    
    @Test
    public void testAttribute() {
        ValueObserver observer = this::valueObserver;
        //registrar.onAttribute("booh",observer);
        //verify(observersRepository,times(1)).registerAttributeObservers("booh",observer);
    }
    
    @Test
    public void testEntry() {
        EventObserver observer = this::eventObserver;
        registrar.onEntry("booh",observer);
        verify(observersRepository,times(1)).registerEntryObserver("booh",observer);
    }
    
    @Test
    public void testExit() {
        EventObserver observer = this::eventObserver;
        registrar.onExit("booh",observer);
        verify(observersRepository,times(1)).registerExitObserver("booh",observer);
    }
    
    @Test
    public void testAttributeInElement() {
        ValueObserver observer = this::valueObserver;      
        registrar.inElement("element",(registrar -> registrar
                        .onAttribute("a",observer)
                        .onAttribute("b",observer)));
        verify(observersRepository,times(1)).registerAttributeObserver("element/a",observer);        
        verify(observersRepository,times(1)).registerAttributeObserver("element/b",observer);         
    }
    
    private void valueObserver(String value) {
        
    }
    
    private void eventObserver() {
        
    }
}
