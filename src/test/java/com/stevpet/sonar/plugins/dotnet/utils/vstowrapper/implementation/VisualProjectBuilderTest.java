package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.sonar.api.batch.bootstrap.ProjectBuilder.Context;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioProjectBuilder;

public class VisualProjectBuilderTest {

    private @Mock Settings  settings ;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private VisualStudioProjectBuilder visualProjectBuilder;
    private @Mock Context context ;
    private @Mock AssemblyLocator  assemblyLocator;
    private @Mock FileSystem fileSystem;
    private @Mock Project project;
    private File baseDir;
    /**
     * Note that these tests use file resources, but for the dlls we use some mocking, so if you're looking for them, see the mocking below!
     */
    @Before
    public void setup() {
        initMocks(this);

        ProjectDefinition projectDefinition = ProjectDefinition.create();
        ProjectReactor projectReactor = new ProjectReactor(projectDefinition);

        when(context.projectReactor()).thenReturn(projectReactor);
        
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY)).thenReturn("CodeCoverage.sln");
        baseDir = TestUtils.getResource("VstoWrapper");
        projectDefinition.setBaseDir(baseDir);
        when(fileSystem.baseDir()).thenReturn(baseDir); //TODO remove at cleanup
        when(assemblyLocator.locateAssembly(eq("CodeCoverage"), any(File.class),any(VisualStudioProject.class))).thenReturn(new File(baseDir,"CodeCoverage/bin/codecoverage.dll"));
        when(assemblyLocator.locateAssembly(eq("CodeCoverage.UnitTests"), any(File.class),any(VisualStudioProject.class))).thenReturn(new File(baseDir,"CodeCoverage.UnitTests/bin/codecoverage.unittests.dll"));
        when(project.isRoot()).thenReturn(true);
        VisualStudioSolutionHierarchyHelper hierarchyHelper = new VisualStudioSolutionHierarchyHelper(settings, assemblyLocator);
        microsoftWindowsEnvironment = new DefaultMicrosoftWindowsEnvironmentBase(hierarchyHelper,fileSystem,project);
        visualProjectBuilder = new VisualStudioProjectBuilder(settings,microsoftWindowsEnvironment,assemblyLocator);
    }
    
    @Test
    public void NoSolutionSpecified_ShouldFindSolution() {
        //when
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY)).thenReturn(null);
        visualProjectBuilder.build(context);
        
        //expect solution to be found
        File solutionDir=microsoftWindowsEnvironment.getCurrentSolution().getSolutionDir();
        assertEquals("solution directory",solutionDir.getAbsolutePath(),baseDir.getAbsolutePath());
        //check artifactNames
        thenExpectBothCSharpProjectsToBeFound();
    }


    @Test
    public void ReadSolution_ShouldHAveOneProjectAndOneTestProject() {
        //when
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY)).thenReturn("CodeCoverage.sln");
        
        visualProjectBuilder.build(context);
        
        thenExpectBothCSharpProjectsToBeFound();
    }
    
    @Test
    public void WrongSolution_ShouldHaveNoSolution() {
        //when
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY)).thenReturn("Bogus.sln");
        try {
        	visualProjectBuilder.build(context);
        } catch (SonarException e) {
        	return;
        }
        fail("when solution does not exist, then a SonarException is expected");
    }
    @Test
    public void NosolutionSpecified_ShouldHAveOneProjectAndOneTestProject() {
        //when

        visualProjectBuilder.build(context);
        
        thenExpectBothCSharpProjectsToBeFound();
    }
    @Test
    public void ReadSolution_SkipOneProject() {
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY)).thenReturn("CodeCoverage.sln");
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS)).thenReturn("CodeCoverage");

        visualProjectBuilder.build(context);
        //check artifactNames
        List<String> artifacts= microsoftWindowsEnvironment.getArtifactNames();
        assertEquals("1 artifacts expected",1,artifacts.size());
        assertTrue("codecoverage.unittests.dll to be in artifacts",artifacts.contains("codecoverage.unittests.dll"));

        //check assemblies
        List<String> assemblies=microsoftWindowsEnvironment.getAssemblies();
        assertEquals("1 assembly expected",1,assemblies.size());
        assertTrue("CodeCoverage.UnitTests to be in assemblies",assemblies.contains("CodeCoverage.UnitTests"));
    }
    
    private void thenExpectBothCSharpProjectsToBeFound() {
        List<String> artifacts= microsoftWindowsEnvironment.getArtifactNames();
        assertEquals("2 artifacts expected",2,artifacts.size());
        assertTrue("codecoverage.dll to be in artifacts",artifacts.contains("codecoverage.dll"));
        assertTrue("codecoverage.unittests.dll to be in artifacts",artifacts.contains("codecoverage.unittests.dll"));

        //check assemblies
        List<String> assemblies=microsoftWindowsEnvironment.getAssemblies();
        assertEquals("2 assembliess expected",2,assemblies.size());
        assertTrue("CodeCoverage to be in assemblies",assemblies.contains("CodeCoverage"));
        assertTrue("CodeCoverage.UnitTests to be in assemblies",assemblies.contains("CodeCoverage.UnitTests"));
    }

}
