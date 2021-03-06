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

import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.resources.Project;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public interface MicrosoftWindowsEnvironment {

    VisualStudioSolution getCurrentSolution();


    @Deprecated
    void setCurrentSolution(VisualStudioSolution currentSolution);
    
    /**
     * 
     * @return (empty) list of unittest sourcefiles.
     */
    List<File> getUnitTestSourceFiles();
    
    List<String> getAssemblies();

    /**
     * get the list of assemblies
     * @return
     */
    List<String> getArtifactNames();

    /**
     * 
     * @return true if there is one or more unittest sourcefiles
     */
    boolean hasUnitTestSourceFiles();

    /**
     * @return true if the project is a (unit) test project
     * @param project
     * @return
     */
    boolean isUnitTestProject(Project project);

    /**
     * return true if the solution has one or more test projects matching the pattern.
     * @param pattern
     * @return
     */
    boolean hasTestProjects(Pattern pattern);


    boolean isUnitTestProject(Project project, Pattern pattern);


    List<VisualStudioProject> getTestProjects(Pattern pattern);
    
}