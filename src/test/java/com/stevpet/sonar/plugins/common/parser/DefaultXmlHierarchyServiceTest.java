package com.stevpet.sonar.plugins.common.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.DefaultXmlHierarchyBuilder;
import com.stevpet.sonar.plugins.common.parser.hierarchybuilder.XmlHierarchyBuilder;

public class DefaultXmlHierarchyServiceTest {

	XmlHierarchyBuilder service = new DefaultXmlHierarchyBuilder() ;
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
	
	@Test
	public void testTwoLevels() {
		service.add("Modules/Module");
		List <String> hierarchy = service.build();
		assertEquals("two elements" ,2,hierarchy.size());
		assertTrue("should contain Modules",hierarchy.contains("Modules"));
		assertTrue("should contain Module",hierarchy.contains("Module"));		
	}
	
	@Test
	public void testTwoLevelsDuplicated() {
		service.add("Modules/Module");
		service.add("Modules/Module");
		List <String> hierarchy = service.build();
		assertEquals("two elements" ,2,hierarchy.size());
		assertTrue("should contain Modules",hierarchy.contains("Modules"));
		assertTrue("should contain Module",hierarchy.contains("Module"));		
		
	}
}
