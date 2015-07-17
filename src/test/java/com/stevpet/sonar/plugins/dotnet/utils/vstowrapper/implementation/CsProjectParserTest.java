package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

public class CsProjectParserTest {
    private SimpleVisualStudioProject project;
    private VisualStudioProjectParser parser;

    @Before
    public void before() {
        File csProjFile = TestUtils.getResource("VsToWrapper/MissingIncludeInEmbeddedResource/Reactive.csproj");
        assertNotNull("could not find test project ",csProjFile);
        parser = new VisualStudioCsProjectParser();     
        project=parser.parse(csProjFile);
    }
    
    @Test
    public void shouldparseFile() {
        assertNotNull("should return project",project);
    }
    
    @Test
    public void shouldHaveAssemblyName() {
        String assemblyName=project.getAssemblyName();
        assertEquals("assemblyName should be as in AssemblyName","Joa.JewelEarth.Web.Reactive",assemblyName);
    }
    
    @Test
    public void ShouldHaveFiles() {
        List<File> files = project.getSourceFiles();
        assertNotNull("should have list",files);
        assertEquals("should have 1 file",1,files.size());
        assertEquals("AssemblyInfo.cs",files.get(0).getName());
    }
}
