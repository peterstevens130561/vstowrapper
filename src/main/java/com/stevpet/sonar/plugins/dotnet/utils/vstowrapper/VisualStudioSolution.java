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
package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public interface VisualStudioSolution {

    /**
     * directory of the solutionfile
     * @return directory of the solutionfile
     */
    File getSolutionDir();

    List<VisualStudioProject> getProjects();

    /**
     * 
     * @return list of test projects. List can be empty, but not null
     */
    List<VisualStudioProject> getTestProjects();

    /**
     * @return list of projects. List can be empty, but not null
     */
    List<VisualStudioSolutionProject> projects();
    

    void addVisualStudioProject(VisualStudioProject project);

    void addTestVisualStudioProject(VisualStudioProject project);

    List<File> getTestSourceFiles();

    List<String> getArtifactNames();

    /**
     * @return the solution file
     */
    File getSolutionFile();

    /**
     * return the testprojects of which the assemblyname matches the pattern
     * @param pattern
     * @return
     */
    List<VisualStudioProject> getTestProjects(Pattern pattern);

    /**
     * true if the solution has testProjects (one or more) matching the pattern
     */
    boolean hasTestProjects(Pattern pattern);
}