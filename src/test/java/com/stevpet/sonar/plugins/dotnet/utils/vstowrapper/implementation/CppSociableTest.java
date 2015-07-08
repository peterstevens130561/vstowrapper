package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import org.sonar.api.batch.bootstrap.ProjectBuilder.Context;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import org.sonar.api.config.Settings;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class CppSociableTest extends CppSociableBase{


    @Before
    public void before() {
        super.setup();
        builder.build(context, assemblyLocator);
    }
    
    @Test
    public void shouldHaveTwoProjects() {
        assertEquals("expect 2 projects",2,microsoftWindowsEnvironment.getAssemblies().size());      
    }
    
    @Test
    public void firstAssembly() {
        assertEquals("expect first one to be joaObjectsd.dll should pick debug|x64 config","joaObjectsd.dll",microsoftWindowsEnvironment.getAssemblies().get(0));      
    }
    
    @Test
    public void secondAssembly() {
        shouldHaveTwoProjects();    
        assertEquals("expect second one","joaBasicObjects.UnitTest.dll",microsoftWindowsEnvironment.getAssemblies().get(1));      
    }
}
