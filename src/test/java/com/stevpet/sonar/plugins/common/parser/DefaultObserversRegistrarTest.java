package com.stevpet.sonar.plugins.common.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ParserEventArgs;
import com.stevpet.sonar.plugins.common.parser.observer.impl.DefaultObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observerdsl.DefaultObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observerdsl.ObserverRegistrar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

public class DefaultObserversRegistrarTest {
    private ObserverRegistrar registrar ;
	@Mock private DefaultObserversRepository observersRepository;
	@Mock private ParserEventArgs eventArgs ;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        registrar = new DefaultObserverRegistrar("",observersRepository);
    }
    
    private String touched;
    @Test
    public void testLambda() {
    	DefaultObserversRepository localRepository = new DefaultObserversRepository();
    	localRepository.registerElementObserver("booh", r -> touched=r);
    	localRepository.observeElement("booh", "john");
    	assertEquals("john",touched);
    }
    @Test
    public void testElement() {
        Consumer<String> observer = this::valueObserver;
        registrar.onElement("booh",observer);
        verify(observersRepository,times(1)).registerElementObserver("booh",observer);
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
    	Consumer<String> observer = this::valueObserver;      
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
