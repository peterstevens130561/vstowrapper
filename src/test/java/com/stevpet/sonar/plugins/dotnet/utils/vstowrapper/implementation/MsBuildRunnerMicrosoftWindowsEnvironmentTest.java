package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class MsBuildRunnerMicrosoftWindowsEnvironmentTest {
    
    @Mock private Settings settings;
    @Mock private FileSystem fs;
    @Mock private Project project;
    private MicrosoftWindowsEnvironment mwe;

    
    @Before()
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        mwe = new MsBuildRunnerMicrosoftWindowsEnvironment(settings, fs, project);    
    }
    
    @Test
    public void inHierarchy() {
        String solution = "E:\\Development\\Radiant\\Main\\JewelEarth\\Core\\JewelExplorer";
        String path=solution + "\\.sonarqube\\out\\.sonar\\JewelExplorer_JewelExplorer_";
        when(fs.workDir()).thenReturn(new File(path));
        File actualDir = mwe.getSolutionDirectory();
        assertEquals(solution,actualDir.getAbsolutePath());
    }
}
