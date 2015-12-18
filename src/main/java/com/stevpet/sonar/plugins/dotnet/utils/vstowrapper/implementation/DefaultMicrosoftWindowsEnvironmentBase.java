package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class DefaultMicrosoftWindowsEnvironmentBase implements MicrosoftWindowsEnvironment{

	private HierarchyBuilder hierarchyBuilder;
	private Project project;
	private FileSystem fileSystem;
	private boolean didBuild = false;
	private VisualStudioSolution solution;

	public DefaultMicrosoftWindowsEnvironmentBase(HierarchyBuilder hierarchyBuilder,FileSystem fileSystem, Project project) {
		this.hierarchyBuilder=hierarchyBuilder;
		this.fileSystem = fileSystem;
		this.project=project;
	}
	

	@Override
	public VisualStudioSolution getCurrentSolution() {
		if (!didBuild) {
			File solutionDir;
			if (!project.isRoot()) {
				int  relativePathLength = project.getPath().length();
				String absolutePath=fileSystem.baseDir().getAbsolutePath();
				String parentPath=StringUtils.left(absolutePath,absolutePath.length()-relativePathLength);
				solutionDir = new File(parentPath);
			} else {
				solutionDir = fileSystem.baseDir();
			}
			hierarchyBuilder.build(solutionDir);
			solution = hierarchyBuilder.getSolution();
			List<VisualStudioProject> projects = hierarchyBuilder.getProjects();
			addProjectsToEnvironment(projects);
			didBuild = true;
		}
		return solution;
	}

	@Override
	public void setCurrentSolution(VisualStudioSolution currentSolution) {
		Preconditions
				.checkArgument(currentSolution != null, "invalid solution");
		this.solution = currentSolution;
	}

	@Override
	public List<File> getUnitTestSourceFiles() {
		return getCurrentSolution().getUnitTestSourceFiles();
	}

	@Override
	public List<String> getAssemblies() {
		List<String> coveredAssemblyNames = new ArrayList<String>();
		for (VisualStudioProject visualProject : getCurrentSolution()
				.getProjects()) {
			coveredAssemblyNames.add(visualProject.getAssemblyName());
		}
		return coveredAssemblyNames;
	}

	@Override
	public List<String> getArtifactNames() {
		return getCurrentSolution().getArtifactNames();
	}

	@Override
	public boolean hasUnitTestSourceFiles() {
		return getUnitTestSourceFiles().size() > 0;
	}

	@Override
	public boolean isUnitTestProject(Project project) {
		String name = project.getName();
		List<VisualStudioProject> projects = getCurrentSolution()
				.getUnitTestProjects();
		for (VisualStudioProject unitTestProject : projects) {
			if (unitTestProject.getAssemblyName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private void addProjectsToEnvironment(List<VisualStudioProject> projects) {
		for (VisualStudioProject project : projects) {
			solution.addVisualStudioProject(project);
			String projectName = project.getProjectName();
			if (hierarchyBuilder.isTestProject(projectName)) {
				solution.addUnitTestVisualStudioProject(project);
				project.setIsTest();
			}
		}
	}

}
