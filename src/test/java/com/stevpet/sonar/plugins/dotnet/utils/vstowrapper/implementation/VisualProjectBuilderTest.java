package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import org.sonar.api.config.Settings;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioProjectBuilder;


public class VisualProjectBuilderTest {

    private @Mock Settings  settings ;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment = new SimpleMicrosoftWindowsEnvironment();
    private VisualStudioProjectBuilder visualProjectBuilder = new VisualStudioProjectBuilder(settings,microsoftWindowsEnvironment);
    private @Mock SensorContext sensorContext ;
    private @Mock AssemblyLocator  assemblyLocator;
    private File baseDir;
    @Before
    public void setup() {

        ProjectDefinition projectDefinition = ProjectDefinition.create();
        ProjectReactor projectReactor = new ProjectReactor(projectDefinition);
        when(sensorContext).thenReturn(projectReactor);
        
        when()
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY)).thenReturn("CodeCoverage.sln");
        baseDir = TestUtils.getResource("VstoWrapper");
        projectDefinition.setBaseDir(baseDir);
        when(assemblyLocator.locateAssembly("CodeCoverage",new File(baseDir),"CodeCoverage/bin/codecoverage.dll");
        
        assemblyLocatorMock.givenLocate("CodeCoverage.UnitTests",new File(baseDir,"CodeCoverage.UnitTests/bin/codecoverage.unittests.dll"));
    }
    
    @Test
    public void NoSolutionSpecified_ShouldFindSolution() {
        //when
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY,null);
        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
        
        //expect solution to be found
        File solutionDir=microsoftWindowsEnvironment.getCurrentSolution().getSolutionDir();
        assertEquals("solution directory",solutionDir.getAbsolutePath(),baseDir.getAbsolutePath());
        //check artifactNames
        thenExpectBothCSharpProjectsToBeFound();
    }


    @Test
    public void ReadSolution_ShouldHAveOneProjectAndOneTestProject() {
        //when
        when(settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY).thenReturn("CodeCoverage.sln");
        
        visualProjectBuilder.build(sensorContext, assemblyLocator);
        
        thenExpectBothCSharpProjectsToBeFound();
    }
    
    @Test
    public void WrongSolution_ShouldHaveNoSolution() {
        //when
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY,"Bogus.sln");
        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
        
        //check artifactNames
        VisualStudioSolution solution= microsoftWindowsEnvironment.getCurrentSolution();
        assertNull("no solution expected",solution);
    }
    @Test
    public void NosolutionSpecified_ShouldHAveOneProjectAndOneTestProject() {
        //when

        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
        
        thenExpectBothCSharpProjectsToBeFound();
    }
    @Test
    public void ReadSolution_SkipOneProject() {
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY,"CodeCoverage.sln");
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS,"CodeCoverage");
        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
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
