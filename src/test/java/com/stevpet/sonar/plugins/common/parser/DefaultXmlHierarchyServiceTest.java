package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DefaultXmlHierarchyServiceTest {

	DefaultXmlHierarchyService service = new DefaultXmlHierarchyService() ;
	@Before
	public void before() {
		
	}
	
	@Test
	public void testEmpty() {
		List <String> hierarchy = service.build();
		assertNotNull("hierarchy must be list",hierarchy);
		assertEquals("no elements" ,0,hierarchy.size());
	}
	
	@Test
	public void testTopElement() {
		service.add("Module");
		List <String> hierarchy = service.build();
		assertEquals("one element" ,1,hierarchy.size());
		assertTrue(hierarchy.contains("Module"));
	}
	
}
