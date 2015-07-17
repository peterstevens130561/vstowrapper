package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;

public interface VisualStudioProjectParser {

    /**
     * parse a project file
     * @param file .net project file 
     * @return populated {@link SimpleVisualStudioProject}
     */
	public abstract SimpleVisualStudioProject parse(File file);

    public abstract void setName(String projectName);

}