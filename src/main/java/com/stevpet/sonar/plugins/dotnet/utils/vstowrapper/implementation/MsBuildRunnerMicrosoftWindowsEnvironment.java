package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
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
    public File getSolutionDirectory(Project project, FileSystem fileSystem) {
        File workDir = fileSystem.workDir();
        File baseDir = fileSystem.baseDir();
        if(workDir.getName().equals(".sonar")) {
            LOG.info("using old resolution");
            // this is the old way
            return super.getSolutionDirectory(project,fileSystem);
        }
        // this is the new way
        LOG.info("using new resolution");
        Collection<File> solutions;
    	File currentDir=baseDir;
        if(project.isModule()) {

        	do {
        		currentDir=currentDir.getParentFile();
        		if(currentDir == null) {
        			String msg = "could not find solution for module " +fileSystem.baseDir().getAbsolutePath();
        			LOG.error(msg);
        			throw new IllegalStateException(msg);
        		}
        		LOG.info("trying {}",currentDir.getAbsolutePath());
        		solutions=FileUtils.listFiles(currentDir, new String[] { "sln" }, false);
        		LOG.info("found {} solutions",solutions.size());
        	} while(solutions.size()==0)  ;

        }

        return currentDir;
    }
}
