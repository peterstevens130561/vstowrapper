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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolutionProject;

/**
 * All information which can be extracted solely out of a .sln file.
 */
public class SimpleVisualStudioSolution implements VisualStudioSolution{

  private final List<VisualStudioSolutionProject> projects;
  private File solutionFile;
  private List<VisualStudioProject> unitTestVisualStudioProjects = new ArrayList<VisualStudioProject>();
  private List<VisualStudioProject> visualStudioProjects = new ArrayList<VisualStudioProject>();
  /**
   * 
   * @param solutionfile - the solution file
   * @param projects - list of projects in the solution
   */
  public SimpleVisualStudioSolution(File solutionfile, List<VisualStudioSolutionProject> projects) {
    this.projects = projects;
    this.solutionFile = solutionfile;
  }

  public List<VisualStudioSolutionProject> projects() {
    return projects;
  }

@Override
public File getSolutionDir() {
    return solutionFile.getParentFile();
}

@Override
public File getSolutionFile() {
    return solutionFile;
}
@Override
public List<VisualStudioProject> getProjects() {
    return visualStudioProjects;
}

@Override
public List<VisualStudioProject> getUnitTestProjects() {
    return unitTestVisualStudioProjects;
}


@Override
public void addVisualStudioProject(VisualStudioProject project) {
    visualStudioProjects.add(project); 
}

@Override
public void addUnitTestVisualStudioProject(VisualStudioProject project) {
    unitTestVisualStudioProjects.add(project);    // TODO Auto-generated method stub
    
}

@Override
public List<File> getUnitTestSourceFiles() {
    List<File> unitTestFiles = new ArrayList<File>();
    for(VisualStudioProject project:getUnitTestProjects()) {
        unitTestFiles.addAll(project.getSourceFiles());
    }
    return unitTestFiles;
}

@Override
public List<String> getArtifactNames() {
    List<String> modules = new ArrayList<String>();
    for(VisualStudioProject project:getProjects()) {
        String name=project.getArtifactName();
        modules.add(name);
    }
    return modules;
}
}
