package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public interface HierarchyBuilder {

	void build(File baseDir);

	boolean isTestProject(String projectName);

	List<VisualStudioProject> getProjects();

	VisualStudioSolution getSolution();

}