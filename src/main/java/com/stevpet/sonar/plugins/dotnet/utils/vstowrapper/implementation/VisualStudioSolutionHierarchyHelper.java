package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.internal.google.common.base.Splitter;
import org.sonar.api.internal.google.common.base.Throwables;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolutionProject;

public class VisualStudioSolutionHierarchyHelper implements BatchExtension, HierarchyBuilder {

	private Logger LOG  =LoggerFactory.getLogger(VisualStudioSolutionHierarchyHelper.class);
	private List<VisualStudioProject> projects;
	private Settings settings;
	private AssemblyLocator assemblyLocator;
	
	public VisualStudioSolutionHierarchyHelper(Settings settings,AssemblyLocator assemblyLocator) {
		this.settings=settings;
		this.assemblyLocator=assemblyLocator;
	}
	private SimpleVisualStudioSolution currentSolution;
	private File solutionFile;

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.HierarchyHelper#build(java.io.File)
	 */
	@Override
	public void build(File baseDir) {
		Preconditions.checkArgument(baseDir!=null,"no baseDir");
		this.solutionFile=getSolutionFile(baseDir);
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
	
	@Nullable
	private File getSolutionFile(File projectBaseDir) {
		File result;

		String solutionPath = settings
				.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY);
		if (StringUtils.isEmpty(solutionPath)) {
			solutionPath = settings
					.getString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SOLUTION_PROPERTY_KEY);
		}
		if (!StringUtils.isEmpty(solutionPath)) {
			result = new File(projectBaseDir, solutionPath);
		} else {
			Collection<File> solutionFiles = FileUtils.listFiles(
					projectBaseDir, new String[] { "sln" }, false);
			if (solutionFiles.isEmpty()) {
				result = null;
			} else if (solutionFiles.size() == 1) {
				result = solutionFiles.iterator().next();
			} else {
				throw new IllegalStateException(
						"Found several .sln files in "
								+ projectBaseDir.getAbsolutePath()
								+ ". Please set \""
								+ VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY
								+ "\" to explicitly tell which one to use.");
			}
		}
		if (result == null) {
			String msg="No Visual Studio solution file found.";
			LOG.error(msg);
			throw new IllegalStateException(msg);
		}
		if (!result.exists()) {
			String msg="Visual Studio solution file does not exist " + 
					result.getAbsolutePath();
			throw new IllegalStateException(msg);
		}
		LOG.debug("Using the following Visual Studio solution: "
				+ result.getAbsolutePath());
		return result;
	}
	private boolean buildProject(VisualStudioSolutionProject solutionProject) {
		File projectFile = relativePathFile(solutionFile.getParentFile(),
				solutionProject.path());

		String projectName = solutionProject.name();
		VisualStudioProjectParser projectParser = getProjectParser(projectFile);
		projectParser.setName(projectName);
		SimpleVisualStudioProject project = projectParser.parse(projectFile);

		File assemblyFile = assemblyLocator.locateAssembly(projectName,
				projectFile, project);
		if (skipNotBuildProjects() && assemblyFile == null) {
			logSkippedProject(solutionProject, "because it is not built and \""
					+ VisualStudioPlugin.VISUAL_STUDIO_SKIP_IF_NOT_BUILT
					+ "\" is set.");
			return false;
		} else if (assemblyFile == null) {
			String msg = "Project not built " + projectFile.getAbsolutePath();
			LOG.error(msg);
			throw new VsToWrapperException(msg);

		}
		if(isTestProject(projectName)) {
		    project.setIsTest();
		}
		project.setAssemblyFile(assemblyFile).setProjectName(solutionProject.name());
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
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.HierarchyHelper#isTestProject(java.lang.String)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.HierarchyHelper#getProjects()
	 */
	@Override
	public List<VisualStudioProject> getProjects() {
		return projects;
	}

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.HierarchyHelper#getSolution()
	 */
	@Override
	public SimpleVisualStudioSolution getSolution() {
		Preconditions.checkState(currentSolution!=null,"no currentSolution");
		return currentSolution;
	}
}
