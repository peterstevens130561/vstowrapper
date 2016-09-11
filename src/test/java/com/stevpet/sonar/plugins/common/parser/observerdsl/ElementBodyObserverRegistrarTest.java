package com.stevpet.sonar.plugins.common.parser.observerdsl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.stevpet.sonar.plugins.common.parser.observer.ObserversRepository;

public class ElementBodyObserverRegistrarTest {

	private ElementBodyObserverRegistrar registrar ;
	@Mock private ObserversRepository observersRepository;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		registrar = new DefaultElementBodyRegistrar("parent", observersRepository);
	}
	
	
	@Test
	public void testEventArgs() {
		registrar.withEventArgs(r->r.setError());
		verify(observersRepository,times(1)).registerElementEventArgsObserver(eq("parent"),anyObject());
	}
}
