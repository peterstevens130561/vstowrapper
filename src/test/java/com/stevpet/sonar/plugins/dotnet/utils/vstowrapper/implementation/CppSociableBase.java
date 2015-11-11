package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.mockito.Mock;
import org.sonar.api.batch.bootstrap.ProjectBuilder.Context;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class CppSociableBase {

    @Mock
    protected Context context;
    @Mock
    protected AssemblyLocator assemblyLocator;
    @Mock
    protected Settings settings;
    protected MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock
    protected ProjectReactor projectReactor;
    @Mock
    protected ProjectDefinition projectDefinition;
    @Mock FileSystem fileSystem;
    @Mock Project project;
    protected VisualStudioProjectBuilder builder;
    private Properties properties = new Properties();

    public void setup() {
        org.mockito.MockitoAnnotations.initMocks(this);
        File pureCppSolutionFile = TestUtils.getResource("VsToWrapper/CppProject/joaBasicObjects.sln");
        File objectsAssembly = new File("joaObjectsd.dll");
        File unitTestAssembly = new File("joaBasicObjects.UnitTest.dll");
       
        when(context.projectReactor()).thenReturn(projectReactor);
        when(projectReactor.getRoot()).thenReturn(projectDefinition);
        when(projectDefinition.getBaseDir()).thenReturn(pureCppSolutionFile.getParentFile());
        when(fileSystem.baseDir()).thenReturn(pureCppSolutionFile.getParentFile()); //TODO: Remove at cleanup
        when(projectDefinition.getProperties()).thenReturn(properties);

        when(assemblyLocator.locateAssembly(eq("joaObjects"), any(File.class), any(VisualStudioProject.class))).thenReturn(
            objectsAssembly);
        when(assemblyLocator.locateAssembly(eq("joaBasicObjects.UnitTest"), any(File.class), any(VisualStudioProject.class)))
            .thenReturn(unitTestAssembly);
        when(settings.getString("sonar.dotnet.visualstudio.solution.file")).thenReturn(pureCppSolutionFile.getName());
        when(project.isRoot()).thenReturn(true);
        microsoftWindowsEnvironment = new SimpleMicrosoftWindowsEnvironment(settings,assemblyLocator,fileSystem,project);
        builder = new VisualStudioProjectBuilder(settings, microsoftWindowsEnvironment,assemblyLocator);


    }

}