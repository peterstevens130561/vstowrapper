package com.stevpet.sonar.plugins.common.parser.observer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DefaultElementBodyRegistrarTests {

	private ElementBodyObserverRegistrar registrar;
	private String name="root";
	@Mock private ObserversRepository observersRepository;
	
	@Before()
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		registrar = new DefaultElementBodyRegistrar(name,observersRepository);
	}
	
	@Test
	public void onEntryTest() {
		EventObserver observer = this::entry;
		ElementBodyObserverRegistrar result = registrar.onEntry(observer);
		assertEquals("should return same",registrar,result);
		verify(observersRepository,times(1)).registerEntryObserver(name, observer);
	}
	
	@Test
	public void onExitest() {
		EventObserver observer = this::entry;
		ElementBodyObserverRegistrar result = registrar.onExit(observer);
		assertEquals("should return same",registrar,result);
		verify(observersRepository,times(1)).registerExitObserver(name, observer);
	}
	
	@Test
	public void onAttributeTest() {
		ValueObserver observer = this::valueObserver;
		ElementBodyObserverRegistrar result = registrar.onAttribute("a",observer);
		assertEquals("should return same",registrar,result);
		verify(observersRepository,times(1)).registerAttributeObserver(name + "/a", observer);
	}
	
	@Test
	public void onValueTest() {
		ValueObserver observer = this::valueObserver;
		ElementBodyObserverRegistrar result = registrar.onValue(observer);
		assertEquals("should return same",registrar,result);
		verify(observersRepository,times(1)).registerElementObserver(name , observer);
	}
	
	private void entry() {
		
	}
	
	private void valueObserver(String value) {
		
	}
}
