package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	public List<VisualStudioProject> getUnitTestProjects() {
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
	public void addUnitTestVisualStudioProject(VisualStudioProject project) {


	}

	@Override
	public List<File> getUnitTestSourceFiles() {
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

}
