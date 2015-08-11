/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolutionProject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.bootstrap.ProjectBuilder;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.component.SourceFile;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

/**
 * ProjectBuilder for dotnet projects
 * 
 * The build method will be invoked by sonar in the ProjectBuild phase, and
 * populates the MicrosoftWindowsEnvironment
 * 
 * @author stevpet
 * 
 */
public class VisualStudioProjectBuilder extends ProjectBuilder {

    private static final String SONAR_MODULES_PROPERTY_KEY = "sonar.modules";
    private static final Logger LOG = LoggerFactory.getLogger(VisualStudioProjectBuilder.class);

    private final Settings settings;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private File solutionFile;
    private AssemblyLocator assemblyLocator;
    private ProjectDefinition sonarRootProject;
    private boolean hasCS, hasCPP;
    private List<ProjectDefinition> subProjects = new ArrayList<>();

    /**
     * @param settings
     *            - standard settings
     * @param microsoftWindowsEnvironment
     *            - populated, include SimpleMicrosoftWindowsEnvironment, or
     *            another implementation of MicrosoftWindowsEnvironment in the
     *            plugin extensions.
     * @param assemblyLocator
     */
    public VisualStudioProjectBuilder(Settings settings, MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
        AssemblyLocator assemblyLocator) {
        this.settings = settings;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.assemblyLocator = assemblyLocator;
    }

    public VisualStudioProjectBuilder(Settings settings, MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this(settings, microsoftWindowsEnvironment, new VisualStudioAssemblyLocator(settings));
    }

    @Override
    public void build(Context context) {
        sonarRootProject = context.projectReactor().getRoot();

        solutionFile = getSolutionFile(sonarRootProject.getBaseDir());
        if (solutionFile == null) {
            LOG.info("No Visual Studio solution file found.");
            return;
        }
        if (!solutionFile.exists()) {
            LOG.error("Visual Studio solution file does not exist ", solutionFile.getAbsolutePath());
            return;
        }
        LOG.info("Using the following Visual Studio solution: " + solutionFile.getAbsolutePath());

        if (settings.hasKey(SONAR_MODULES_PROPERTY_KEY)) {
            LOG.warn("The  \"" + SONAR_MODULES_PROPERTY_KEY + "\" is deprecated");
        }

        Set<String> skippedProjects = skippedProjectsByNames();
        boolean hasModules = false;

        SimpleVisualStudioSolution currentSolution = new VisualStudioSolutionParser().parse(solutionFile);

        for (VisualStudioSolutionProject solutionProject : currentSolution.projects()) {
            if (shouldBuildProject(skippedProjects, solutionProject)) {
                hasModules = buildProject(hasModules, currentSolution, solutionProject);

            }
        }
        Preconditions.checkState(hasModules, "No Visual Studio projects were found.");
        microsoftWindowsEnvironment.setCurrentSolution(currentSolution);
        if (hasCS && !hasCPP) {
            LOG.info("- project is recognized as a CS project, adding structure");
            for (ProjectDefinition childProject : subProjects) {
                sonarRootProject.addSubProject(childProject);
            }
        }
    }

    private boolean buildProject(boolean hasModules, SimpleVisualStudioSolution currentSolution,
        VisualStudioSolutionProject solutionProject) {
        File projectFile = relativePathFile(solutionFile.getParentFile(), solutionProject.path());
        String solutionName = solutionProject.name();
        VisualStudioProjectParser projectParser = getProjectParser(projectFile);
        projectParser.setName(solutionName);
        SimpleVisualStudioProject project = projectParser.parse(projectFile);
        File assembly = assemblyLocator.locateAssembly(solutionName, projectFile, project);
        if (skipNotBuildProjects() && assembly == null) {
            logSkippedProject(solutionProject, "because it is not built and \""
                + VisualStudioPlugin.VISUAL_STUDIO_SKIP_IF_NOT_BUILT + "\" is set.");
            return hasModules;
        } else if (assembly == null) {
            String msg = "Project not built " + projectFile.getAbsolutePath();
            LOG.error(msg);
            throw new VsToWrapperException(msg);

        }
        hasModules = true;
        currentSolution.addVisualStudioProject(project);
        if (isTestProject(solutionProject.name())) {
            currentSolution.addUnitTestVisualStudioProject(project);
            project.setIsTest();
        }
        project.setAssembly(assembly);

        createSubProjectDefinition(project);
        return hasModules;
    }

    private boolean shouldBuildProject(Set<String> skippedProjects, VisualStudioSolutionProject solutionProject) {
        if (skippedProjects.contains(solutionProject.name())) {
            logSkippedProject(solutionProject, "because it is listed in the property \""
                + VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS + "\".");
            return false;
        }
        if (isSkippedProjectByPattern(solutionProject.name())) {
            logSkippedProject(solutionProject, "because it matches the property \""
                + VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN + "\".");
            return false;
        }
        File projectFile2 = relativePathFile(solutionFile.getParentFile(), solutionProject.path());
        if (!projectFile2.isFile()) {
            LOG.warn("Unable to find the Visual Studio project file " + projectFile2.getAbsolutePath());
            return false;
        }
        return true;
    }

    private VisualStudioProjectParser getProjectParser(File project) {
        VisualStudioProjectParser projectParser;
        String name = project.getName();
        if (name.endsWith(".csproj")) {
            projectParser = new VisualStudioCsProjectParser();
            hasCS = true;
        } else if (name.endsWith(".vcxproj")) {
            projectParser = new CppProjectParser(settings);
            hasCPP = true;
        } else {
            throw new VsToWrapperException("unknown project type " + project.getAbsolutePath());
        }
        return projectParser;
    }

    private void createSubProjectDefinition(SimpleVisualStudioProject visualStudioProject) {
        List<ProjectDefinition> subProjects = sonarRootProject.getSubProjects();
        String name = visualStudioProject.getAssemblyName();
        if (!subProjects.contains(name)) {
            Properties properties = (Properties) sonarRootProject.getProperties().clone();
            String assembly = visualStudioProject.getArtifact(null, null).getAbsolutePath();
            properties.setProperty("sonar.cs.fxcop.assembly", assembly);

            ProjectDefinition newProject = ProjectDefinition.create();
            newProject.setProperties(properties);
            newProject.setName(name);

            String key = sonarRootProject.getKey() + ":" + name;
            newProject.setKey(key);

            File projectDirectory = visualStudioProject.getDirectory();
            newProject.setBaseDir(projectDirectory);
            newProject.setWorkDir(new File(projectDirectory, ".sonar"));
            newProject.setVersion(sonarRootProject.getVersion());
            newProject.setName(visualStudioProject.getAssemblyName());
            LOG.debug("  - Adding Sub Project => {}", newProject.getName());

            subProjects.add(newProject);

        }
    }

    private static void logSkippedProject(VisualStudioSolutionProject solutionProject, String reason) {
        LOG.info("Skipping the project \"" + solutionProject.name() + "\" " + reason);
    }

    private boolean skipNotBuildProjects() {
        return settings.getBoolean(VisualStudioPlugin.VISUAL_STUDIO_SKIP_IF_NOT_BUILT);
    }

    @Nullable
    private File getSolutionFile(File projectBaseDir) {
        File result;

        String solutionPath = settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY);
        if (Strings.nullToEmpty(solutionPath).isEmpty()) {
            solutionPath = settings.getString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SOLUTION_PROPERTY_KEY);
        }
        if (!Strings.nullToEmpty(solutionPath).isEmpty()) {
            result = new File(projectBaseDir, solutionPath);
        } else {
            Collection<File> solutionFiles = FileUtils.listFiles(projectBaseDir, new String[] { "sln" }, false);
            if (solutionFiles.isEmpty()) {
                result = null;
            } else if (solutionFiles.size() == 1) {
                result = solutionFiles.iterator().next();
            } else {
                throw new SonarException("Found several .sln files in " + projectBaseDir.getAbsolutePath() + ". Please set \""
                    + VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY + "\" to explicitly tell which one to use.");
            }
        }

        return result;
    }

    private static File relativePathFile(File file, String relativePath) {
        return new File(file, relativePath.replace('\\', '/'));
    }

    private boolean isTestProject(String projectName) {
        return matchesPropertyRegex(VisualStudioPlugin.VISUAL_STUDIO_TEST_PROJECT_PATTERN, projectName);
    }

    private boolean isSkippedProjectByPattern(String projectName) {
        return matchesPropertyRegex(VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN, projectName);
    }

    private boolean matchesPropertyRegex(String propertyKey, String value) {
        String pattern = settings.getString(propertyKey);
        try {
            return pattern != null && value.matches(pattern);
        } catch (PatternSyntaxException e) {
            LOG.error("The syntax of the regular expression of the \"" + propertyKey + "\" property is invalid: " + pattern);
            throw Throwables.propagate(e);
        }
    }

    private Set<String> skippedProjectsByNames() {
        String skippedProjects = settings.getString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS);
        if (skippedProjects == null) {
            return ImmutableSet.of();
        }

        LOG.warn("Replace the deprecated property \"" + VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS + "\" by the new \""
            + VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN + "\".");

        return ImmutableSet.<String> builder().addAll(Splitter.on(',').omitEmptyStrings().split(skippedProjects)).build();
    }

}
