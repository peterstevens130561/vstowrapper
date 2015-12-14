package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolutionProject;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class VisualStudioSolutionParserTest {

	private SimpleVisualStudioSolution projects;
	@Mock
	private Logger Log;

	@Before
	public void before() {
		initMocks(this);
		File solution = TestUtils
				.getResource("VisualStudioSolutionParser/SolutionWithReferenceOutsideHierarchy.sln");
		VisualStudioSolutionParser parser = new VisualStudioSolutionParser();
		parser.setLogger(Log);
		projects = parser.parse(solution);
	}

	@Test
	public void shouldHaveTwoProjects() {
		assertEquals("should have two projects", 2, projects.projects().size());
	}

	@Test
	public void firstProjectIs() {
		VisualStudioSolutionProject project = projects.projects().get(0);
		assertEquals("Joa.JewelEarth.UI.Controls.UnitTest", project.name());
	}

	@Test
	public void secondProjectIs() {
		VisualStudioSolutionProject project = projects.projects().get(1);
		assertEquals("Joa.JewelEarth.Geodetic", project.name());
	}

	@Test
	public void shouldLogWarningForProjectOutsideSolution() {
		verify(Log, times(1))
				.warn("solution references project outside solution hierarchy (anti-pattern), will not be included in analysis:{}",
						"..\\Tests\\SpecflowTests\\Api\\Math.Geometry\\Math.Geometry.SpecFlowTest.csproj");
	}
}
