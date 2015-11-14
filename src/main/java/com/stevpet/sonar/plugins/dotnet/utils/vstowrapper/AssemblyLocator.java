package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper;

import java.io.File;

import org.sonar.api.BatchExtension;

public interface AssemblyLocator extends BatchExtension {

    File locateAssembly(String projectName, File projectFile,
            VisualStudioProject project);

}