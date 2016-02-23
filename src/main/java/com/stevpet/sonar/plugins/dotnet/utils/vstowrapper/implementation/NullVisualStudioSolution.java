package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolutionProject;

public class NullVisualStudioSolution implements VisualStudioSolution {

	@Override
	public File getSolutionDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VisualStudioProject> getProjects() {
		return new ArrayList<VisualStudioProject>();
	}

	@Override
	public List<VisualStudioProject> getTestProjects() {
		return new ArrayList<VisualStudioProject>();
	}

	@Override
	public List<VisualStudioSolutionProject> projects() {
		// TODO Auto-generated method stub
		return new ArrayList<VisualStudioSolutionProject>();
	}

	@Override
	public void addVisualStudioProject(VisualStudioProject project) {
	}

	@Override
	public void addTestVisualStudioProject(VisualStudioProject project) {


	}

	@Override
	public List<File> getTestSourceFiles() {
		return new ArrayList<File>();
	}

	@Override
	public List<String> getArtifactNames() {
		return new ArrayList<String>();
	}

	@Override
	public File getSolutionFile() {
		return null;
	}

    @Override
    public List<VisualStudioProject> getTestProjects(Pattern pattern) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasTestProjects(Pattern pattern) {
        // TODO Auto-generated method stub
        return false;
    }

}
