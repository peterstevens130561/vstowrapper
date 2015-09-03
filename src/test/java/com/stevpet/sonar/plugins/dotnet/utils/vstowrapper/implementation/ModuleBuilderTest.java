package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.sonar.api.batch.bootstrap.ProjectDefinition;

public class ModuleBuilderTest {

    private ModuleBuilder moduleBuilder ;
    @Mock private SimpleVisualStudioProject visualStudioProject;
    private List<ProjectDefinition> modules;
    private ProjectDefinition sonarRootProject;
    
    @Before
    public void before() {
        initMocks(this);
        moduleBuilder = new ModuleBuilder();
        when(visualStudioProject.getArtifactName()).thenReturn("artifactName");
        when(visualStudioProject.getAssemblyName()).thenReturn("assemblyName");
        when(visualStudioProject.getArtifact(null, null)).thenReturn(new File("C:/Development/Radiant/Main/bin/artifactName.dll"));
        when(visualStudioProject.getDirectory()).thenReturn(new File("C:/Development/ProjectDir"));
        

        
    }
    private void setupBasicChild() {
        sonarRootProject = ProjectDefinition.create();
        sonarRootProject.setKey("rootkey");
        sonarRootProject.setName("root name");
        sonarRootProject.setVersion("main");
        
        moduleBuilder.setRoot(sonarRootProject);
        
        moduleBuilder.add(visualStudioProject);
        
        moduleBuilder.build();
        modules = sonarRootProject.getSubProjects();
    }
    
    private void setupChild(String name) {
        when(visualStudioProject.getAssemblyName()).thenReturn(name);
        sonarRootProject = ProjectDefinition.create();
        sonarRootProject.setKey("rootkey");
        sonarRootProject.setName("root name");
        sonarRootProject.setVersion("main");
        
        moduleBuilder.setRoot(sonarRootProject);
        
        moduleBuilder.add(visualStudioProject);
        
        moduleBuilder.build();
        modules = sonarRootProject.getSubProjects();
    }
    @Test
    public void oneModule() {
        setupBasicChild();
        
        List<ProjectDefinition> modules=sonarRootProject.getSubProjects();
        assertTrue("expect one module",modules.size()==1);
   }
    
    @Test
    public void nameIsAssemblyName() {
        setupBasicChild();
        ProjectDefinition module=modules.get(0);
        assertEquals("expect name to be equal to the artifactName","assemblyName",module.getName());
    }
    
    @Test
    public void versionIsOfRoot() {
        setupBasicChild();
        ProjectDefinition module=modules.get(0);
        assertEquals("expect version to be main","main",module.getVersion());        
    }
    
    @Test
    public void keyIsConcatenated() {
        setupBasicChild();
        ProjectDefinition module = modules.get(0);
        assertEquals("key of child module is concatenation of root key, : and module name","rootkey:assemblyName",module.getKey());
    }
    
    @Test
    public void spacesInKeyAreReplaced() {
        setupChild("Jewel Suite Viewer");
        ProjectDefinition module = modules.get(0);
        assertEquals("key of child module is concatenation of root key, : and module name","rootkey:Jewel_Suite_Viewer",module.getKey());
    }
    
    @Test
    public void childIsInList() {
        setupBasicChild();
        assertTrue("module should be there",moduleBuilder.contains(visualStudioProject));
    }
}
