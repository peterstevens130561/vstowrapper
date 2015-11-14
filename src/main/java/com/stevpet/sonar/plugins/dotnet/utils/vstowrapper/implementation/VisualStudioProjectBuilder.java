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

	private static final Logger LOG = LoggerFactory
			.getLogger(VisualStudioProjectBuilder.class);

	private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	private ProjectDefinition sonarRootProject;
	private ModuleBuilder moduleBuilder;
	private SimpleVisualStudioSolution currentSolution;
	private VisualStudioSolutionHierarchyHelper hierarchyHelper ;

	/**
	 * @param settings
	 *            - standard settings
	 * @param microsoftWindowsEnvironment
	 *            - populated, include SimpleMicrosoftWindowsEnvironment, or
	 *            another implementation of MicrosoftWindowsEnvironment in the
	 *            plugin extensions.
	 * @param assemblyLocator
	 */

	public VisualStudioProjectBuilder(Settings settings,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,AssemblyLocator assemblyLocator) {
		this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
		this.moduleBuilder = new ModuleBuilder();
		this.hierarchyHelper= new VisualStudioSolutionHierarchyHelper(settings,assemblyLocator) ;
	}

	@Override
	public void build(Context context) {
		sonarRootProject = context.projectReactor().getRoot();
		moduleBuilder.setRoot(sonarRootProject);
		hierarchyHelper.build(sonarRootProject.getBaseDir());
		List<SimpleVisualStudioProject> projects=hierarchyHelper.getProjects();
		currentSolution = hierarchyHelper.getSolution();

		addProjectsToEnvironment(projects);
		addProjectsToBuilder(projects);
	}

	private void addProjectsToBuilder(List<SimpleVisualStudioProject> projects) {
		for (SimpleVisualStudioProject project : projects) {
			if (!moduleBuilder.contains(project)) {
				moduleBuilder.add(project);
			}
		}
		moduleBuilder.build();
	}

	private void addProjectsToEnvironment(List<SimpleVisualStudioProject> projects) {
		microsoftWindowsEnvironment.setCurrentSolution(currentSolution);
		for (SimpleVisualStudioProject project : projects) {
			currentSolution.addVisualStudioProject(project);
			String projectName = project.getProjectName();
			if (hierarchyHelper.isTestProject(projectName)) {
				currentSolution.addUnitTestVisualStudioProject(project);
				project.setIsTest();
			}
		}
	}












}
