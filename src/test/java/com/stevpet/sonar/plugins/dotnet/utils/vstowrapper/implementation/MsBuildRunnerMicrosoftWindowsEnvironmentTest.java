package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
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
    private String solutionRoot = "MsBuildRunnerMicrosoftWindowsEnvironment\\Addins\\ReferenceArchitecture";
    private File solutionFile = TestUtils.getResource(solutionRoot + "\\ReferenceArchitecture.sln");
    @Before()
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        mwe = new MsBuildRunnerMicrosoftWindowsEnvironment(settings, fs, project);    
    }
    
    /**
     * Tests that we find the solutionRoot for a module
     */
    @Test
    public void moduleInHierarchy() {
    	File baseDir = getModuleDir();
    	File workDir = getWorkDirBelowSolution(baseDir);
        setupModuleMocks(baseDir, workDir);
        
        File actualDir = mwe.getSolutionDirectory(project,fs);
        assertSolutionFound(actualDir);
    }

	private void assertSolutionFound(File actualDir) {
		assertTrue(actualDir.getAbsolutePath() + " does not end with " + solutionRoot,actualDir.getAbsolutePath().endsWith(solutionRoot));
	}

    
    /**
     * workdir is outside hierarchy, should still find module
     */
    @Test
    public void moduleInHierarchyWithWorkDirSeperate() {
    	File baseDir = getModuleDir();
    	File workDir = getWorkDirOutsideHierarchy();
        setupModuleMocks(baseDir, workDir);
               
        File actualDir = mwe.getSolutionDirectory(project,fs);
        assertSolutionFound(actualDir);
    }

    /**
     * The directory is outside the solution, but we should still have the solution.
     */
    @Test
    public void moduleNotInHierarchy() {
    	File baseDir = getCorruptModuleDir();
    	File workDir = getWorkDirBelowSolution(baseDir);
        setupModuleMocks(baseDir, workDir);
        
        File actualDir=mwe.getSolutionDirectory(project,fs);
        assertSolutionFound(actualDir);
    }


    /**
     * Tests that we find the solutionRoot for the root project
     */
    @Test
    public void rootInHierarchy() {
    	File baseDir=solutionFile.getParentFile();
    	File workDir=new File(baseDir,".sonarqube\\out\\.sonar\\ReferenceArchitecture_ReferenceArchitecture_02728809-7D73-4");
        setupModuleMocks(baseDir, workDir);
        
        File actualDir = mwe.getSolutionDirectory(project,fs);
        assertSolutionFound(actualDir);
    }
    
    @Test
    public void projectBaseDirNotSet_ShouldThrowException() {
    	File baseDir=solutionFile.getParentFile();
    	File workDir=new File(baseDir,".sonarqube\\out\\.sonar\\ReferenceArchitecture_ReferenceArchitecture_02728809-7D73-4");
        setupModuleMocks(baseDir, workDir);
        when(settings.getString(eq("sonar.projectBaseDir"))).thenReturn(null);
    	try {
            File actualDir = mwe.getSolutionDirectory(project,fs);
            
    	} catch (IllegalStateException e) {
    		return ;
    	}
    	fail("expected exception, as the projectBaseDir property is not set");
    }
    
    @Test
    public void NoSolutionInProjectBaseDir_ShouldThrowException() {
    	File baseDir=solutionFile.getParentFile();
    	File workDir=new File(baseDir,".sonarqube\\out\\.sonar\\ReferenceArchitecture_ReferenceArchitecture_02728809-7D73-4");
        setupModuleMocks(baseDir, workDir);
        when(settings.getString(eq("sonar.projectBaseDir"))).thenReturn(solutionFile.getParentFile().getParentFile().getAbsolutePath());
    	try {
    		mwe.getSolutionDirectory(project,fs);
            
    	} catch (IllegalStateException e) {
    		assertTrue("expected other message, but got" + e.getMessage(),e.getMessage().contains("has no solutions"));
    		return ;
    	}
    	fail("expected exception, as the projectBaseDir property points to a directory without solution");
    }
    
	private File getModuleDir() {
		File moduleProject = TestUtils.getResource(solutionRoot+"\\Reference.Addin\\Reference.Addin.csproj");
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

    
	/**
	 * In the current setup then we have a baseDir, a workDir, and a projectBaseDir
	 * @param baseDir
	 * @param workDir
	 */
	private void setupModuleMocks(File baseDir, File workDir) {
		when(fs.baseDir()).thenReturn(baseDir);
        when(fs.workDir()).thenReturn(workDir);
        when(project.isModule()).thenReturn(true);
        when(settings.getString(eq("sonar.projectBaseDir"))).thenReturn(solutionFile.getParentFile().getAbsolutePath());
	}

}
