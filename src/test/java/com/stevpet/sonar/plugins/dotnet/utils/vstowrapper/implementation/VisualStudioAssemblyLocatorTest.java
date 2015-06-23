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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioAssemblyLocator;

public class VisualStudioAssemblyLocatorTest {
    private @Mock Settings settings;
    private @Mock VisualStudioProject visualStudioProject;
    private AssemblyLocator locator ;
    
    @Before
    public void before() {
        initMocks(this);
        locator = new VisualStudioAssemblyLocator(settings);
    }
    
    @Test
    public void OutputTypeNotDefined_ExpectNull() {
        File projectFile = new File("myproject.csproj");
        String outputType = null;
        String assemblyName="myProject.dll";
               
        File locatedAssembly = whenLocatingAssembly(projectFile, outputType,
                assemblyName);
        assertNull(locatedAssembly);
    }
    
    @Test
    public void AssemblyNotDefined_ExpectNull() {
        File projectFile = new File("myproject.csproj");
        String outputType = "library";
        String assemblyName=null;
               
        File locatedAssembly = whenLocatingAssembly(projectFile, outputType,
                assemblyName);
        assertNull(locatedAssembly);
    }
    
    /**
     * outputpath not defined, expect that it does not find an assembly
     */
    @Test
    public void AssemblyDefined_OutputPathsNotDefined_ExpectNull() {
        File projectFile = new File("myproject.csproj");
        String outputType = "library";
        String assemblyName="myproject";
        String path=null;

        when(settings.getString("sonar.visualstudio.outputPaths")).thenReturn(path);
        File locatedAssembly = whenLocatingAssembly(projectFile, outputType,
                assemblyName);
        assertNull(locatedAssembly);
    }

    /**
     * absolute outputpath defined, and project is setup ok, expect assembly to be in outputpath dir
     */
    @Test
    public void AssembltDefined_ExpectNotNull() {
        File projectFile = new File("myproject.csproj");
        String outputType = "library";
        String assemblyName="myproject";
        String path="C:/Development/Jewel.Release.Oahu/bin/Debug";
        when(settings.getString("sonar.visualstudio.outputPaths")).thenReturn(path);
        File locatedAssembly = whenLocatingAssembly(projectFile, outputType,
                assemblyName);
        assertNotNull(locatedAssembly);
        assertEquals("C:\\Development\\Jewel.Release.Oahu\\bin\\Debug\\myproject.dll",locatedAssembly.getAbsolutePath());
    }
    
    /**
     * Project it setup ok
     * When Relative output path defined
     * Then assembly should be in relative dir below project
     */
    @Test
    public void RelativeAssemblyDefined_ExpectNotNull() {
        File projectFile = new File("C:/Development/Jewel.Release.Oahu/BasicControls/Controls/ControlsProj.csproj");
        String outputType = "library";
        String assemblyName="myproject";
        String path="bin/Debug";
        when(settings.getString("sonar.visualstudio.outputPaths")).thenReturn(path);
        File locatedAssembly = whenLocatingAssembly(projectFile, outputType,
                assemblyName);
        assertNotNull(locatedAssembly);
        assertEquals("C:\\Development\\Jewel.Release.Oahu\\BasicControls\\Controls\\bin\\Debug\\myproject.dll",locatedAssembly.getAbsolutePath());
    }
    private File whenLocatingAssembly(File projectFile, String outputType,
            String assemblyName) {
        when(visualStudioProject.getAssemblyName()).thenReturn(assemblyName);
        when(visualStudioProject.outputType()).thenReturn(outputType);

        File locatedAssembly=locator.locateAssembly("myproject", projectFile, visualStudioProject);
        return locatedAssembly;
    }
}
