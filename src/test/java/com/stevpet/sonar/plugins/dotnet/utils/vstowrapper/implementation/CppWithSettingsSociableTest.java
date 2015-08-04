package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;


public class CppWithSettingsSociableTest extends CppSociableBase {

    @Before
    public void before() {
        setup();
        when(settings.getString(VisualStudioPlugin.BUILD_CONFIGURATION_KEY)).thenReturn("Release");
        build();
    }
    @Test
    public void shouldHaveTwoProjects() {
        assertEquals("expect 2 projects",2,microsoftWindowsEnvironment.getAssemblies().size());      
    }
    
    @Test
    public void firstAssembly() {
        assertEquals("expect first one to be joaObjects.dll as we're on release platform, so use the projectname",
            "joaObjects",microsoftWindowsEnvironment.getAssemblies().get(0));      
    }
    
    @Test
    public void secondAssembly() {
        shouldHaveTwoProjects();    
        assertEquals("expect second one","joaBasicObjects.UnitTest",microsoftWindowsEnvironment.getAssemblies().get(1));      
    }
}

