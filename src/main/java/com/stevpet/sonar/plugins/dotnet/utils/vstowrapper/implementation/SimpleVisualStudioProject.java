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

import javax.annotation.Nullable;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * All information related to Visual Studio projects which can be extracted only from a project file.
 * Should not be mixed with information gathered from solution files.
 */
public class SimpleVisualStudioProject implements VisualStudioProject {

    private final File projectFile ;
  private final List<String> files;
  private final String outputType;
  private final String assemblyName;
  private final List<String> outputPaths;
  private String language ;
  public void setLanguage(String language) {
	this.language = language;
}

private boolean isTest;
  private File assemblyFile;

/**
 * 
 * @param projectFile - the csproj file
 * @param files - all sourcefiles relative to the project dir
 * @param outputType 
 * @param assemblyName - name of assembly
 * @param outputPaths
 */
  public SimpleVisualStudioProject(File projectFile,List<String> files,  @Nullable String outputType, @Nullable String assemblyName,
    List<String> outputPaths) {
    this.projectFile = projectFile;
    this.outputType = outputType;
    this.assemblyName = assemblyName;
    this.outputPaths = outputPaths;
    this.files = files;
  }

  public List<File> getSourceFiles() {
    return createFiles(projectFile,files);
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
public File getArtifact(String buildConfiguration, String buildPlatform) {
    return assemblyFile;
}

@Override
public boolean isUnitTest() {
    return isTest;
}

@Override
public String getArtifactName() {
    return assemblyFile.getName();
}

@Override
public File getDirectory() {
    return projectFile.getParentFile();
}

@Override
public String getName() {
    // TODO Auto-generated method stub
    return null;
}

@Override
public boolean isTest() {
    return isTest;
}

public void setIsTest() {
    this.isTest=true; 
}

public void setAssembly(File assembly) {
    this.assemblyFile = assembly;
}

private List<File> createFiles(File projectFile,List<String> pathsList) {
    File projectDir=projectFile.getParentFile();
    List<File> filesList = new ArrayList<File>();
    for(String path:pathsList) {
        if(path.endsWith(".cs")) {
            File file = new File(projectDir,path.replace('\\', '/'));
            filesList.add(file);
        }
    }
    return filesList;
}

@Override
public String getLanguage() {
	return language;
}
}
