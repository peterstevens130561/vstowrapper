package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
        assertEquals("expect first one to be joaObjectsd should pick debug|x64 config","joaObjectsd",microsoftWindowsEnvironment.getAssemblies().get(0));      
    }
    
    @Test
    public void secondAssembly() {
        shouldHaveTwoProjects();    
        assertEquals("expect second one","joaBasicObjects.UnitTest",microsoftWindowsEnvironment.getAssemblies().get(1));      
    }
}
