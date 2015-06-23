package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper;

import java.io.File;

public interface AssemblyLocator {

    File locateAssembly(String projectName, File projectFile,
            VisualStudioProject project);

}