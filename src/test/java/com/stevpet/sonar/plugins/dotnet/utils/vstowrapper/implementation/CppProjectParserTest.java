package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

/**
 * parsers Cpp project definitions
 * @author stevpet
 *
 */
public class CppProjectParserTest  {

	private VisualStudioProject project;
    private VisualStudioProjectParser parser;

	@Before
	public void before() {
		parser = new CppProjectParser();
	}
	
	@Test
	public void assemblyNameIsDefinedProjectName() {
	       File someFile=TestUtils.getResource("VstoWrapper/CppProject/joaObjects/joaObjects.vcxproj");
	       parser.setName("ProjectName");
	        project = parser.parse(someFile);
		assertEquals("expect to be the defined name + d.dll","ProjectNamed.dll",project.getAssemblyName());
	}

	   @Test
	    public void assemblyNameIsDefaultProjectName() {
	           File someFile=TestUtils.getResource("VstoWrapper/CppProject/joaBasicObjectsUnitTests/joaBasicObjectsUnitTests.vcxproj");
	           parser.setName("ProjectName");
	            project = parser.parse(someFile);
	        assertEquals("expect to be the defined name","ProjectName.dll",project.getAssemblyName());
	    }
}
