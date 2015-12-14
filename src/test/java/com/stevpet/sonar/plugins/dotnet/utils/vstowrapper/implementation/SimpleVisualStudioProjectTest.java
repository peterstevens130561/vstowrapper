package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class SimpleVisualStudioProjectTest {

    private File projectFile;
    private List<String> files=new ArrayList<>();
    private String assemblyName;
    private String outputType;
    private SimpleVisualStudioProject visualStudioProject;
    @Before
    public void before() {
        projectFile = new File("john/projectFile");
        assemblyName="project.dll";
        outputType="assembly";
        visualStudioProject = new SimpleVisualStudioProject().setProjectFile(projectFile).setAssemblyName(assemblyName).setOutputType(outputType);
    }
    
    @Test
    public  void getSourceFiles_NoFiles_EmptyList() {
        List<File> sourceFiles = visualStudioProject.getSourceFiles();
        assertNotNull("expect valid list",sourceFiles);
        assertEquals("list to be empty, as no files added",0,sourceFiles.size());
    }
    
    @Test
    public  void getSourceFiles_OneFiles_ListWithOne() {
        files.add("file1.cs");
        visualStudioProject.setSourceFiles(files);
        List<File> sourceFiles = visualStudioProject.getSourceFiles();
        assertNotNull("expect valid list",sourceFiles);
        assertEquals("list to be have one item",1,sourceFiles.size());
        assertTrue("file1 to be in list",sourceFiles.get(0).getAbsolutePath().endsWith("john\\file1.cs"));
    }
    
    @Test
    public  void getSourceFiles_TwoFiles_ListWithTwo() {
        files.add("file1.cs");
        files.add("file2.cs");
        visualStudioProject.setSourceFiles(files);
        List<File> sourceFiles = visualStudioProject.getSourceFiles();
        assertNotNull("expect valid list",sourceFiles);
        assertEquals("list to be have two items",2,sourceFiles.size());
    }
    
    @Test
    public void getOutputType_Same() {
        assertEquals("name should be as when instantiated","assembly",visualStudioProject.outputType());
    }
    
    @Test
    public void isTest_False() {
        assertFalse("expect false when nothing done",visualStudioProject.isTest());
    }
    
    @Test
    public void isTest_Set_True() {
        visualStudioProject.setIsTest();
        assertTrue("expect true",visualStudioProject.isTest());
    }
    
    @Test
    public void getDirectory_ValidDir() {
        assertTrue("should end with john",visualStudioProject.getDirectory().getAbsolutePath().endsWith("\\john"));
    }
}
