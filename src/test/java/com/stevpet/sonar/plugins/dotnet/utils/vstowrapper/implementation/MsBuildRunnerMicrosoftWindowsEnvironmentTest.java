package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

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
    
    /**
     * Tests that we find the solution for a module
     */
    @Test
    public void moduleInHierarchy() {
    	File baseDir = getModuleDir();
    	File workDir = getWorkDirBelowSolution(baseDir);
        String solution = "MsBuildRunnerMicrosoftWindowsEnvironment\\Addins\\ReferenceArchitecture";
        setupModuleMocks(baseDir, workDir);
        
        File actualDir = mwe.getSolutionDirectory(project,fs);
        assertTrue(actualDir.getAbsolutePath() + " does not end with " + solution,actualDir.getAbsolutePath().endsWith(solution));
    }

    
    /**
     * workdir is outside hierarchy, should still find module
     */
    @Test
    public void moduleInHierarchyWithWorkDirSeperate() {
    	File baseDir = getModuleDir();
    	File workDir = getWorkDirOutsideHierarchy();
        setupModuleMocks(baseDir, workDir);
        
        String solution = "MsBuildRunnerMicrosoftWindowsEnvironment\\Addins\\ReferenceArchitecture";

        
        File actualDir = mwe.getSolutionDirectory(project,fs);
        assertTrue(actualDir.getAbsolutePath() + " does not end with " + solution,actualDir.getAbsolutePath().endsWith(solution));
    }

    @Test
    public void moduleNotInHierarchy() {
    	File baseDir = getCorruptModuleDir();
    	File workDir = getWorkDirBelowSolution(baseDir);
    	
        setupModuleMocks(baseDir, workDir);
        
        try {
        	mwe.getSolutionDirectory(project,fs);
        } catch ( IllegalStateException e ) {
        	return ;
        }
        fail("should not have found solution");
    }

	private void setupModuleMocks(File baseDir, File workDir) {
		when(fs.baseDir()).thenReturn(baseDir);
        when(fs.workDir()).thenReturn(workDir);
        when(project.isModule()).thenReturn(true);
	}
    /**
     * Tests that we find the solution for the root project
     */
    @Test
    public void rootInHierarchy() {
    	File moduleProject = TestUtils.getResource("MsBuildRunnerMicrosoftWindowsEnvironment\\Addins\\ReferenceArchitecture\\ReferenceArchitecture.sln");
    	assertNotNull(moduleProject);
    	File baseDir=moduleProject.getParentFile();
    	File workDir=new File(baseDir,".sonarqube\\out\\.sonar\\ReferenceArchitecture_ReferenceArchitecture_02728809-7D73-4");
        String solution = "MsBuildRunnerMicrosoftWindowsEnvironment\\Addins\\ReferenceArchitecture";
        String path=solution + "\\Reference.Addin";
        when(fs.baseDir()).thenReturn(baseDir);
        when(fs.workDir()).thenReturn(workDir);
        when(project.isModule()).thenReturn(false);
        
        File actualDir = mwe.getSolutionDirectory(project,fs);
        assertTrue(actualDir.getAbsolutePath() + " does not end with " + solution,actualDir.getAbsolutePath().endsWith(solution));
    }
	private File getModuleDir() {
		File moduleProject = TestUtils.getResource("MsBuildRunnerMicrosoftWindowsEnvironment\\Addins\\ReferenceArchitecture\\Reference.Addin\\Reference.Addin.csproj");
    	assertNotNull(moduleProject);
    	File baseDir=moduleProject.getParentFile();
		return baseDir;
	}
	
	private File getCorruptModuleDir() {
		File moduleProject = TestUtils.getResource("MsBuildRunnerMicrosoftWindowsEnvironment\\Addins\\Corrupt\\Corrupt.Addin\\Corrupt.Addin.csproj");
    	assertNotNull(moduleProject);
    	File baseDir=moduleProject.getParentFile();
		return baseDir;
	}
	private File getWorkDirBelowSolution(File baseDir) {
		File workDir=new File(baseDir.getParentFile(),".sonarqube\\out\\.sonar\\ReferenceArchitecture_ReferenceArchitecture_02728809-7D73-4");
		return workDir;
	}

	private File getWorkDirOutsideHierarchy() {
		File rootFile=TestUtils.getResource("MsBuildRunnerMicrosoftWindowsEnvironment\\root.txt");
    	assertNotNull(rootFile); 
    	File rootDir=rootFile.getParentFile();
    	File workDir=new File(rootDir,".sonarqube\\out\\.sonar\\ReferenceArchitecture_ReferenceArchitecture_02728809-7D73-4");
		return workDir;
	}
    
    

}
