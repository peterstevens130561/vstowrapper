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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * All information related to Visual Studio projects which can be extracted only
 * from a project file. Should not be mixed with information gathered from
 * solution files.
 */
public class SimpleVisualStudioProject implements VisualStudioProject {

	private File projectFile;
	private List<String> files = new ArrayList<String>();
	private String outputType;
	private String assemblyName;
	private List<String> outputPaths = new ArrayList<String>();
	private String language;

	public void setLanguage(String language) {
		this.language = language;
	}

	private boolean isTest;
	private File assemblyFile;
	private String projectName;

	public SimpleVisualStudioProject() {

	}

	public SimpleVisualStudioProject setProjectFile(@Nonnull File projectFile) {
		this.projectFile = projectFile;
		return this;
	}

	public SimpleVisualStudioProject setSourceFiles(
			@Nonnull List<String> sourceFileNames) {
		this.files = sourceFileNames;
		return this;
	}

	public SimpleVisualStudioProject setOutputType(@Nonnull String outputType) {
		this.outputType = outputType;
		return this;
	}

	public SimpleVisualStudioProject setAssemblyName(
			@Nonnull String assemblyName) {
		this.assemblyName = assemblyName;
		return this;
	}

	public List<File> getSourceFiles() {
		return createFiles(projectFile, files);
	}

	@Nullable
	public String outputType() {
		return outputType;
	}

	public List<String> outputPaths() {
		return outputPaths;
	}

	/**
	 * {@link VisualStudioProject#getAssemblyName}
	 */
	@Override
	public String getAssemblyName() {
		return assemblyName;
	}

	@Override
	/**
	 * set the name of the project, as it is known in the solution. 
	 * Which may be different than the name of the csproj file, and which may
	 * be different than the name of the artifact.
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public File getArtifactFile() {
		Preconditions.checkNotNull(assemblyFile,"assemblyFile not set");
		return assemblyFile;
	}

	@Override
	public boolean isUnitTest() {
		return isTest;
	}

	@Override
	public String getArtifactName() {
		return getArtifactFile().getName();
	}

	@Override
	public File getDirectory() {
		return projectFile.getParentFile();
	}

	@Override
	public boolean isTest() {
		return isTest;
	}

	@Override
	public void setIsTest() {
		this.isTest = true;
	}

	public SimpleVisualStudioProject setAssemblyFile(File assembly) {
		this.assemblyFile = assembly;
		return this;
	}

	private List<File> createFiles(File projectFile, List<String> pathsList) {
		File projectDir = projectFile.getParentFile();
		List<File> filesList = new ArrayList<File>();
		for (String path : pathsList) {
			if (path.endsWith(".cs")) {
				File file = new File(projectDir, path.replace('\\', '/'));
				try {
					File canonicalFile = file.getCanonicalFile();
					filesList.add(canonicalFile);

				} catch (IOException e) {
					throw new VsToWrapperException(
							"Could not get canonicalFile on"
									+ file.getAbsolutePath());
				}
			}

		}
		return filesList;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	public SimpleVisualStudioProject setOutputPaths(List<String> outputPaths) {
		this.outputPaths = outputPaths;
		return this;
	}
}
