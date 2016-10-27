package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import org.sonar.api.resources.Project;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;

public class MsBuildRunnerMicrosoftWindowsEnvironment extends DefaultMicrosoftWindowsEnvironment {

    private final FileSystem fileSystem;

    public MsBuildRunnerMicrosoftWindowsEnvironment(Settings settings, FileSystem fs, Project project) {
        super(settings, fs, project);
        this.fileSystem=fs;
    }

    @Override
    public
    File getSolutionDirectory() {
        File workDir = fileSystem.workDir();
        while( workDir!=null && !workDir.getName().equals(".sonarqube")) {
            workDir=workDir.getParentFile();
        }
        
        Preconditions.checkNotNull(workDir,"could not find solutionDir for " + fileSystem.workDir());
        return workDir.getParentFile();
    }
}
