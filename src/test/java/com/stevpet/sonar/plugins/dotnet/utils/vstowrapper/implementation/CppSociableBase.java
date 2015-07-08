package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.mockito.Mock;
import org.sonar.api.batch.bootstrap.ProjectBuilder.Context;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import org.sonar.api.config.Settings;
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
    protected VisualStudioProjectBuilder builder;

    public void setup() {
        org.mockito.MockitoAnnotations.initMocks(this);
        File pureCppSolutionFile = TestUtils.getResource("VsToWrapper/CppProject/joaBasicObjects.sln");
        File objectsAssembly = new File("joaObjectsd.dll");
        File unitTestAssembly = new File("joaBasicObjects.UnitTest.dll");
        microsoftWindowsEnvironment = new SimpleMicrosoftWindowsEnvironment();

        when(context.projectReactor()).thenReturn(projectReactor);
        when(projectReactor.getRoot()).thenReturn(projectDefinition);
        when(projectDefinition.getBaseDir()).thenReturn(pureCppSolutionFile.getParentFile());

        when(assemblyLocator.locateAssembly(eq("joaObjects"), any(File.class), any(VisualStudioProject.class))).thenReturn(
            objectsAssembly);
        when(assemblyLocator.locateAssembly(eq("joaBasicObjects.UnitTest"), any(File.class), any(VisualStudioProject.class)))
            .thenReturn(unitTestAssembly);

        builder = new VisualStudioProjectBuilder(settings, microsoftWindowsEnvironment);
    }

}