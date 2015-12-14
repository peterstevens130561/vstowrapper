package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.bootstrap.ProjectDefinition;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class ModuleBuilder {

    private final Logger LOG = LoggerFactory.getLogger(ModuleBuilder.class);
    private ProjectDefinition sonarRootProject;
    private List<ProjectDefinition> childProjects = new ArrayList<>();

    public void setRoot(ProjectDefinition sonarRootProject) {
        this.sonarRootProject = sonarRootProject;
    }

    boolean contains(VisualStudioProject project) {
        List<ProjectDefinition> subProjects = sonarRootProject.getSubProjects();
        String key = getKey(project);
        for(ProjectDefinition module:subProjects) {
            String moduleKey=module.getKey();
            if(key.equals(moduleKey)) {
                return true;
            }
        }
        
        return false;
    }
    /**
     * Prepare a module for addition
     * @param project
     */
    void add(VisualStudioProject project) {

        ProjectDefinition newModule = ProjectDefinition.create();
        Properties properties = (Properties) sonarRootProject.getProperties().clone();
        newModule.setProperties(properties);
        
        String name = project.getAssemblyName();
        newModule.setName(name);
        
        String key = getKey(project);
        newModule.setKey(key); 

        String assembly = project.getArtifactFile().getAbsolutePath();
        newModule.setProperty("sonar.cs.fxcop.assembly", assembly);

        File projectDirectory = project.getDirectory();
        newModule.setBaseDir(projectDirectory);
        
        newModule.setWorkDir(new File(projectDirectory, ".sonar"));
        if(project.isUnitTest()) {
            newModule.resetTestDirs();
            newModule.resetSourceDirs();
            newModule.setTestDirs(projectDirectory);
        }
        newModule.setVersion(sonarRootProject.getVersion());
        LOG.debug(" - basedir {}",newModule.getBaseDir().getAbsolutePath());
        LOG.debug("  - Adding Sub Project => {}", newModule.getName());

        childProjects.add(newModule);
    }

    private String getKey(VisualStudioProject project) {
        String name=project.getAssemblyName();
        String key = sonarRootProject.getKey() + ":" + name.replaceAll(" ", "_");
        return key;
    }
    
    /**
     * add all modules to the root
     */
    void build() {
        for (ProjectDefinition childProject : childProjects) {
            sonarRootProject.addSubProject(childProject);
        }
    }
}
