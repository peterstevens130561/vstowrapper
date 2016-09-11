package com.stevpet.sonar.plugins.common.parser.observerdsl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.common.parser.observer.EventObserver;
import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObserver;
import com.stevpet.sonar.plugins.common.parser.observerdsl.DefaultElementBodyRegistrar;
import com.stevpet.sonar.plugins.common.parser.observerdsl.ElementBodyObserverRegistrar;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

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
		Consumer<String> observer = this::valueObserver;
		ElementBodyObserverRegistrar result = registrar.onAttribute("a",observer);
		assertEquals("should return same",registrar,result);
		verify(observersRepository,times(1)).registerAttributeObserver(name + "/a", observer);
	}
	
	@Test
	public void onValueTest() {
		Consumer<String> observer = this::valueObserver;
		ElementBodyObserverRegistrar result = registrar.onValue(observer);
		assertEquals("should return same",registrar,result);
		verify(observersRepository,times(1)).registerElementObserver(name , observer);
	}
	
	private void entry() {
		
	}
	
	private void valueObserver(String value) {
		
	}
}
