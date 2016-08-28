package com.stevpet.sonar.plugins.common.parser.hierarchybuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.common.parser.observer.EventObservers;
import com.stevpet.sonar.plugins.common.parser.observer.ObserverRegistrar;
import com.stevpet.sonar.plugins.common.parser.observer.PathSpecificationObserverRegistrationFacade;
import com.stevpet.sonar.plugins.common.parser.observer.ValueObservers;

public class DefaultXmlHierarchyBuilderSociableTest {
	private String ancestry;
	@Mock private ValueObservers elementObservers;
	@Mock private ValueObservers pathObservers;
	@Mock private ValueObservers attributeObservers;
	@Mock private EventObservers entryObservers;
	@Mock private EventObservers exitObservers;
	private XmlHierarchyBuilder hierarchyBuilder = new DefaultXmlHierarchyBuilder();
	private ObserverRegistrar facade;
	private List<String> hierarchy;
	
	@Before
	public void  before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		facade = new PathSpecificationObserverRegistrationFacade(ancestry, hierarchyBuilder,elementObservers, pathObservers, attributeObservers, entryObservers, exitObservers);
	}
	
	@Test
	public void test() {
		facade.inPath("a/b/c");
		hierarchy=hierarchyBuilder.build();
		assertEquals("three elements expected",3,hierarchy.size());
		assertTrue("a",hierarchy.contains("a"));
		assertTrue("b",hierarchy.contains("b"));
		assertTrue("c",hierarchy.contains("a"));
	}
	

}
