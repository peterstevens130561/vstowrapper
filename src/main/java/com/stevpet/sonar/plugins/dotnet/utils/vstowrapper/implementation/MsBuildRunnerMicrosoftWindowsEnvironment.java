package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.google.common.base.Preconditions;

public class MsBuildRunnerMicrosoftWindowsEnvironment extends DefaultMicrosoftWindowsEnvironment {

    private final FileSystem fileSystem;

    public MsBuildRunnerMicrosoftWindowsEnvironment(Settings settings, FileSystem fs, Project project) {
        super(settings, fs, project);
        this.fileSystem=fs;
    }

    @Override
    public File getSolutionDirectory() {
        File workDir = fileSystem.workDir();
        if(workDir.getName().equals(".sonar")) {
            LOG.info("using old resolution");
            // this is the old way
            return super.getSolutionDirectory();
        }
        // this is the new way
        LOG.info("using new resolution");
        if(!workDir.getAbsolutePath().contains(".sonarqube\\out\\.sonar")) {
            String logMsg = "workdir can't be translated to solutiondir " + workDir.getAbsolutePath();
            LOG.error(logMsg);
            throw new IllegalStateException(logMsg);
        }
        
        while( workDir!=null && !workDir.getName().equals(".sonarqube")) {
            workDir=workDir.getParentFile();
        }
        
        Preconditions.checkNotNull(workDir,"could not find solutionDir for " + fileSystem.workDir());
        return workDir.getParentFile();
    }
}
