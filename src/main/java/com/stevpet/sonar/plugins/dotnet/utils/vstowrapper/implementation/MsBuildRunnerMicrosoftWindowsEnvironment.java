package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.internal.apachecommons.lang.StringUtils;
import org.sonar.api.resources.Project;


public class MsBuildRunnerMicrosoftWindowsEnvironment extends DefaultMicrosoftWindowsEnvironment {

	private Settings settings ;
    public MsBuildRunnerMicrosoftWindowsEnvironment(Settings settings, FileSystem fs, Project project) {
        super(settings, fs, project);
        this.settings=settings;
    }

    @Override
    public File getSolutionDirectory(Project project, FileSystem fileSystem) {
        File workDir = fileSystem.workDir();
        if(workDir.getName().equals(".sonar")) {
            LOG.info("using old resolution");
            // this is the old way
            return super.getSolutionDirectory(project,fileSystem);
        }
        // this is the new way
        // msbuild pre/end step will make sure sonar.projectBaseDir is set
        LOG.info("using new resolution");
        String solutionBaseDirSetting=settings.getString("sonar.projectBaseDir");
        if(StringUtils.isEmpty(solutionBaseDirSetting)) {
        	throw new IllegalStateException("sonar.projectBaseDir is not set");
        }
        File solutionBaseDir=new File(solutionBaseDirSetting);
        Collection<File> solutionFiles=FileUtils.listFiles(solutionBaseDir, new String[] { "sln" }, false);
        if(solutionFiles.size()==0) {
        	throw new IllegalStateException("sonar.projectBaseDir=" + solutionBaseDirSetting + " has no solutions");
        }
        return solutionBaseDir;
    }

}
