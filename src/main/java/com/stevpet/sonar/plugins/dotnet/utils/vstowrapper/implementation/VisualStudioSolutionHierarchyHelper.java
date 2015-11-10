package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolutionProject;

public class VisualStudioSolutionHierarchyHelper {

	private Logger LOG  =LoggerFactory.getLogger(VisualStudioSolutionHierarchyHelper.class);
	private List<SimpleVisualStudioProject> projects;
	private Settings settings;
	private AssemblyLocator assemblyLocator;
	
	public VisualStudioSolutionHierarchyHelper(Settings settings,AssemblyLocator assemblyLocator) {
		this.settings=settings;
		this.assemblyLocator=assemblyLocator;
	}
	private SimpleVisualStudioSolution currentSolution;
	private File solutionFile;

	public void build(File solutionFile) {
		this.solutionFile=solutionFile;
		currentSolution = new VisualStudioSolutionParser().parse(solutionFile);

		Set<String> skippedProjects = skippedProjectsByNames();
		projects = new ArrayList<>();
		boolean hasModules = false;
		for (VisualStudioSolutionProject solutionProject : currentSolution
				.projects()) {
			if (shouldBuildProject(skippedProjects, solutionProject)) {
				hasModules |= buildProject(solutionProject);
			}
		}
		Preconditions.checkState(hasModules,
				"No Visual Studio projects were found.");
	}
	
	private boolean buildProject(VisualStudioSolutionProject solutionProject) {
		File projectFile = relativePathFile(solutionFile.getParentFile(),
				solutionProject.path());

		String projectName = solutionProject.name();
		VisualStudioProjectParser projectParser = getProjectParser(projectFile);
		projectParser.setName(projectName);
		SimpleVisualStudioProject project = projectParser.parse(projectFile);

		File assembly = assemblyLocator.locateAssembly(projectName,
				projectFile, project);
		if (skipNotBuildProjects() && assembly == null) {
			logSkippedProject(solutionProject, "because it is not built and \""
					+ VisualStudioPlugin.VISUAL_STUDIO_SKIP_IF_NOT_BUILT
					+ "\" is set.");
			return false;
		} else if (assembly == null) {
			String msg = "Project not built " + projectFile.getAbsolutePath();
			LOG.error(msg);
			throw new VsToWrapperException(msg);

		}
		project.setAssembly(assembly);
		project.setProjectName(solutionProject.name());
		projects.add(project);
		return true;
	}
	private Set<String> skippedProjectsByNames() {
		String skippedProjects = settings
				.getString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS);
		if (skippedProjects == null) {
			return ImmutableSet.of();
		}

		LOG.warn("Replace the deprecated property \""
				+ VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS
				+ "\" by the new \""
				+ VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN
				+ "\".");

		return ImmutableSet
				.<String> builder()
				.addAll(Splitter.on(',').omitEmptyStrings()
						.split(skippedProjects)).build();
	}

	private boolean shouldBuildProject(Set<String> skippedProjects,
			VisualStudioSolutionProject solutionProject) {
		if (skippedProjects.contains(solutionProject.name())) {
			logSkippedProject(
					solutionProject,
					"because it is listed in the property \""
							+ VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS
							+ "\".");
			return false;
		}
		if (isSkippedProjectByPattern(solutionProject.name())) {
			logSkippedProject(
					solutionProject,
					"because it matches the property \""
							+ VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN
							+ "\".");
			return false;
		}
		File projectFile2 = relativePathFile(solutionFile.getParentFile(),
				solutionProject.path());
		if (!projectFile2.isFile()) {
			LOG.warn("Unable to find the Visual Studio project file "
					+ projectFile2.getAbsolutePath());
			return false;
		}
		return true;
	}
	
	private VisualStudioProjectParser getProjectParser(File project) {
		VisualStudioProjectParser projectParser;
		String name = project.getName();
		if (name.endsWith(".csproj")) {
			projectParser = new VisualStudioCsProjectParser();
		} else if (name.endsWith(".vcxproj")) {
			projectParser = new CppProjectParser(settings);
		} else {
			throw new VsToWrapperException("unknown project type "
					+ project.getAbsolutePath());
		}
		return projectParser;
	}
	private void logSkippedProject(
			VisualStudioSolutionProject solutionProject, String reason) {
		LOG.info("Skipping the project \"" + solutionProject.name() + "\" "
				+ reason);
	}
	
	public boolean isTestProject(String projectName) {
		return matchesPropertyRegex(
				VisualStudioPlugin.VISUAL_STUDIO_TEST_PROJECT_PATTERN,
				projectName);
	}

	private boolean isSkippedProjectByPattern(String projectName) {
		return matchesPropertyRegex(
				VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN,
				projectName);
	}

	private boolean skipNotBuildProjects() {
		return settings
				.getBoolean(VisualStudioPlugin.VISUAL_STUDIO_SKIP_IF_NOT_BUILT);
	}
	
	private boolean matchesPropertyRegex(String propertyKey, String value) {
		String pattern = settings.getString(propertyKey);
		try {
			return pattern != null && value.matches(pattern);
		} catch (PatternSyntaxException e) {
			LOG.error("The syntax of the regular expression of the \""
					+ propertyKey + "\" property is invalid: " + pattern);
			throw Throwables.propagate(e);
		}
	}
	
	private static File relativePathFile(File file, String relativePath) {
		return new File(file, relativePath.replace('\\', '/'));
	}

	public List<SimpleVisualStudioProject> getProjects() {
		return projects;
	}

	public SimpleVisualStudioSolution getSolution() {
		Preconditions.checkState(currentSolution!=null,"no currentSolution");
		return currentSolution;
	}
}
