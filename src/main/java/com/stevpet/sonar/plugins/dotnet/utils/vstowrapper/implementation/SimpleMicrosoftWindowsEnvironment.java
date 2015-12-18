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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

/**
 * Intended to replace the dotnet fun, so is a minimalistic implementation
 * 
 * Include in the extensionlist of the plugin
 * 
 * @author stevpet
 * 
 */
@InstantiationStrategy(InstantiationStrategy.PER_PROJECT)
public class SimpleMicrosoftWindowsEnvironment extends DefaultMicrosoftWindowsEnvironmentBase implements BatchExtension
		 {

	Logger LOG = LoggerFactory
			.getLogger(SimpleMicrosoftWindowsEnvironment.class);
	VisualStudioSolution solution = new NullVisualStudioSolution();

	@Deprecated
	public SimpleMicrosoftWindowsEnvironment(Settings settings, FileSystem fs,
			Project project) {
		this(settings, new VisualStudioAssemblyLocator(settings), fs, project);
	}

	@Deprecated
	public SimpleMicrosoftWindowsEnvironment(Settings settings,
			AssemblyLocator assemblyLocator, FileSystem fs, Project project) {
		super(new VisualStudioSolutionHierarchyHelper(settings,
				assemblyLocator),fs,project);
	}
	
}
