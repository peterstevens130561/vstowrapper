package com.stevpet.sonar.plugins.common.parser.hierarchybuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;
import com.stevpet.sonar.plugins.common.parser.observer.impl.DefaultObserversRepository;
import com.stevpet.sonar.plugins.common.parser.observerdsl.DefaultObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observerdsl.TopLevelObserverRegistrar;

public class DefaultXmlHierarchyBuilderSociableTest {
	private String ancestry;

	private TopLevelObserverRegistrar facade;
	private List<String> hierarchy;
	private DefaultObserversRepository observersRepository = new DefaultObserversRepository();
	
	@Before
	public void  before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		facade = new DefaultObserverRegistrar("",observersRepository);
	}
	
	@Test
	public void test() {
		facade.inPath("a/b/c");
		hierarchy=observersRepository.buildHierarchy();
		assertEquals("three elements expected",3,hierarchy.size());
		assertTrue("a",hierarchy.contains("a"));
		assertTrue("b",hierarchy.contains("b"));
		assertTrue("c",hierarchy.contains("a"));
	}
	

}
