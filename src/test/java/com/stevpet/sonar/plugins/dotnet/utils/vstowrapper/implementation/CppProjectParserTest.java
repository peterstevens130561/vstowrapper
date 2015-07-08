package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

/**
 * parsers Cpp project definitions
 * @author stevpet
 *
 */
public class CppProjectParserTest  {

	private VisualStudioProject project;

	@Before
	public void before() {
		VisualStudioProjectParser parser = new CppProjectParser();
		File someFile=new File("bogus.vcxproj");
		project = parser.parse(someFile);
	}
	
	@Test
	public void assemblyNameIsProjectName() {
		assertEquals("expect bogusd.dll to be the name, as it is the project name","bogusd.dll",project.getAssemblyName());
	}

	@Test
	public void langugageIsCpp() {
		assertEquals("language is cpp","cpp",project.getLanguage());
	}
}
