package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;











import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.anyString;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolutionProject;

public class SimpleMicrosoftWindowsEnvironmentTest {

	@Mock private Settings settings;
	@Mock private FileSystem fileSystem;
	@Mock private Project project;
	private DefaultMicrosoftWindowsEnvironmentBase microsoftWindowsEnvironment;
	private VisualStudioSolution solution;
	@Mock private AssemblyLocator assemblyLocator;
	@Mock private HierarchyBuilder hierarchyHelper;
	private VisualStudioSolution currentSolution;

	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void instantiationTest() {
		instantiate();
	}
	
	@Test
	public void oneChildButNotInSolution() {
		when(project.isRoot()).thenReturn(false);
		when(project.getPath()).thenReturn("App/Viewer/SubViewer");
		File solutionDir=new File("C:\\Build\\MySolution");
		File baseDir = new File(solutionDir,"App\\Viewer\\SubViewer");
		when(fileSystem.baseDir()).thenReturn(baseDir);
		
		solution=new SimpleVisualStudioSolution(solutionDir, null);
		when(hierarchyHelper.getSolution()).thenReturn(solution);
		instantiate();;

		currentSolution=microsoftWindowsEnvironment.getCurrentSolution();
		
		verify(hierarchyHelper,times(1)).build(eq(solutionDir));
		verify(hierarchyHelper,times(0)).isTestProject(anyString());
		
		assertNotNull(microsoftWindowsEnvironment.getAssemblies());
		assertEquals(microsoftWindowsEnvironment.getAssemblies().size(),0);
		
		assertEquals(solution,currentSolution);
	}
	
	@Test
	public void oneChildInSolutionIsTestProject() {
		File solutionDir = createSolutionWithOneProject("SubViewer");
		when(hierarchyHelper.isTestProject(eq("SubViewerProject"))).thenReturn(true);
		instantiate();
		
		currentSolution=microsoftWindowsEnvironment.getCurrentSolution();
		
		verify(hierarchyHelper,times(1)).build(eq(solutionDir));
		verify(hierarchyHelper,times(1)).isTestProject(anyString());
		
		assertNotNull(microsoftWindowsEnvironment.getAssemblies());
		assertEquals(1,microsoftWindowsEnvironment.getAssemblies().size());

		assertEquals(solution,currentSolution);
		
		List<VisualStudioProject> unitTestProjects=microsoftWindowsEnvironment.getCurrentSolution().getUnitTestProjects();
		assertEquals(1,unitTestProjects.size());
	}
	
	@Test
	public void oneChildInSolutionIsNormalProject() {
		File solutionDir = createSolutionWithOneProject("SubViewer");
		when(hierarchyHelper.isTestProject(eq("SubViewerProject"))).thenReturn(false);
		instantiate();
		currentSolution=microsoftWindowsEnvironment.getCurrentSolution();
		
		verify(hierarchyHelper,times(1)).build(eq(solutionDir));
		verify(hierarchyHelper,times(1)).isTestProject(anyString());
		
		assertNotNull(microsoftWindowsEnvironment.getAssemblies());
		assertEquals(1,microsoftWindowsEnvironment.getAssemblies().size());

		assertEquals(solution,currentSolution);
		
		List<VisualStudioProject> unitTestProjects=microsoftWindowsEnvironment.getCurrentSolution().getUnitTestProjects();
		assertEquals(0,unitTestProjects.size());
	}
	
	@Test
	public void oneTestProjectInSolutionCheckIsTestProjectProject() {
		createSolutionWithOneProject("SubViewer");
		when(hierarchyHelper.isTestProject(eq("SubViewerProject"))).thenReturn(true);
		instantiate();

		when(project.getName()).thenReturn("SubViewerAssembly");
		boolean isTestProject=microsoftWindowsEnvironment.isUnitTestProject(project);
		assertTrue("The name of this sonar project should be equal to the name of the assembly",isTestProject);
	}
	
	@Test
	public void oneNormalProjectInSolutionCheckIsTestProjectProject() {
		createSolutionWithOneProject("SubViewer");
		when(hierarchyHelper.isTestProject(eq("SubViewerProject"))).thenReturn(false);
		instantiate();

		when(project.getName()).thenReturn("SubViewerAssembly");
		boolean isTestProject=microsoftWindowsEnvironment.isUnitTestProject(project);
		assertFalse("It should not be a testprojecty",isTestProject);
	}
	
	private File createSolutionWithOneProject(String projectName) {

		when(project.isRoot()).thenReturn(false);
		when(project.getPath()).thenReturn("App/Viewer/" + projectName);
		File solutionDir=new File("C:\\Build\\MySolution");
		File baseDir = new File(solutionDir,"App\\Viewer\\SubViewer");
		SimpleVisualStudioProject project = new SimpleVisualStudioProject();
		
		File projectFile=new File(baseDir,"App/Viewer/SubViewer/SubViewer.csproj");
		project.setProjectFile(projectFile);
		
		List<VisualStudioSolutionProject> solutionProjects = new ArrayList<>();
		
		String outputType= "assembly";
		project.setOutputType(outputType);
		
		String assemblyName = "SubViewerAssembly";
		project.setAssemblyName(assemblyName);
		
		project.setProjectName("SubViewerProject");
		
		VisualStudioSolutionProject solutionProject = new VisualStudioSolutionProject("SubViewerProject","App/Viewer/SubViewer/SubViewer.csproj");
		solutionProjects.add(solutionProject);
		
		when(fileSystem.baseDir()).thenReturn(baseDir);
		
		solution=new SimpleVisualStudioSolution(solutionDir, solutionProjects);
		when(hierarchyHelper.getSolution()).thenReturn(solution);
		List<VisualStudioProject> projects = new ArrayList<>();
		

		projects.add(project);
		when(hierarchyHelper.getProjects()).thenReturn(projects);
		return solutionDir;
	}

	private void instantiate() {
		microsoftWindowsEnvironment = new DefaultMicrosoftWindowsEnvironmentBase(hierarchyHelper,fileSystem,project);
	}
}
