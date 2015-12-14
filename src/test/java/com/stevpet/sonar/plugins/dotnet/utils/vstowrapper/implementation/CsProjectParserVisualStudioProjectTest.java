package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;
public class CsProjectParserVisualStudioProjectTest {

	private SimpleVisualStudioProject project;
	
	@Before
	public void before() {
		initMocks(this);
		VisualStudioCsProjectParser parser = new VisualStudioCsProjectParser();
		File csProjectFile = TestUtils.getResource("CsProject/BasicControls.TestApp/BasicControls.testApp.csProj");
		assertNotNull("resource should be present",csProjectFile);
		project=parser.parse(csProjectFile);
		assertNotNull("should have valid project",project);
	}
	
	@Test
	public void shouldHaveProject() {
		assertNotNull("should have valid project",project);
	}
	
	@Test
	public void shouldHaveNoProjectName() {
		String projectName=project.getProjectName();
		assertNull("projectName set from solution",projectName);
	}
	
	@Test
	public void shouldNotHaveArtifact() {
		try {
			project.getArtifactName();
		} catch(NullPointerException e) {
			return;
		}
		fail("Expected NullPointerException");
	}
	
	@Test
	public void shouldHaveAssemblyName() {

		String name=project.getAssemblyName();
		assertEquals("BasicControls.TestApp",name);
	}
	
	@Test
	public void shouldHaveSourceFiles() {
		List<File> sourceFiles = project.getSourceFiles();
		assertNotNull(sourceFiles);
		assertEquals(10,sourceFiles.size());
	}
	
	@Test
	public void checkFirstFile() {
		List<File> sourceFiles = project.getSourceFiles();
		File firstFile=sourceFiles.get(0);
		assertEndsWith("Form1.cs",firstFile.getAbsolutePath());
	}
	
	@Test
	public void checkMiddleFile() {
		List<File> sourceFiles = project.getSourceFiles();
		File firstFile=sourceFiles.get(5);
		assertEndsWith("PixelPerfexctDesignForm.Designer.cs",firstFile.getAbsolutePath());		
	}
	@Test
	public void checkLastFile() {
		List<File> sourceFiles = project.getSourceFiles();
		File firstFile=sourceFiles.get(9);
		assertEndsWith("Properties\\Settings.Designer.cs",firstFile.getAbsolutePath());
	}
	
	private void assertEndsWith(String expected,String actual) {
		assertTrue(actual + " should end with " + expected,actual.endsWith(expected));
	}
}
