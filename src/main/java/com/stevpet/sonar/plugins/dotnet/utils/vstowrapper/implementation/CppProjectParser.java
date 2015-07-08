package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CppProjectParser implements VisualStudioProjectParser {

	SimpleVisualStudioProject project;
	@Override
	public SimpleVisualStudioProject parse(File file) {
		File projectFile = file;
		List<String> files= new ArrayList<String>();
		String outputType = "assembly";
		String assemblyName = file.getName().replaceFirst("\\.vcxproj", "d.dll");
		List<String> outputPaths = new ArrayList<String>();
		project = new SimpleVisualStudioProject(projectFile, files, outputType, assemblyName, outputPaths);
		project.setLanguage("cpp");
		return project;
	}

}
