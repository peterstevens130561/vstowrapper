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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

/**
 * Intended to replace the dotnet fun, so is a minimalistic implementation
 * @author stevpet
 *
 */

public class SimpleMicrosoftWindowsEnvironment implements BatchExtension,MicrosoftWindowsEnvironment {

    Logger LOG = LoggerFactory.getLogger(SimpleMicrosoftWindowsEnvironment.class);
    private VisualStudioSolution solution=null;

    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.IMicrosoftWindowsEnvironment#getCurrentSolution()
     */
    @Override
    public VisualStudioSolution getCurrentSolution() {
        return solution;
    }


    @Override
    public void setCurrentSolution(VisualStudioSolution currentSolution) {
        this.solution=currentSolution;
    }


    @Override
    public List<File> getUnitTestSourceFiles() {
        return getCurrentSolution().getUnitTestSourceFiles();
    }

    @Override
    public List<String> getAssemblies() {
        List<String> coveredAssemblyNames = new ArrayList<String>();
        for (VisualStudioProject visualProject : getCurrentSolution().getProjects()) {
            coveredAssemblyNames.add(visualProject.getAssemblyName());
        }
        return coveredAssemblyNames;
      }


    @Override
    public List<String> getArtifactNames() {
        return getCurrentSolution().getArtifactNames();
    }


    @Override
    public boolean hasUnitTestSourceFiles() {
        return getUnitTestSourceFiles().size()>0;
    }
}
