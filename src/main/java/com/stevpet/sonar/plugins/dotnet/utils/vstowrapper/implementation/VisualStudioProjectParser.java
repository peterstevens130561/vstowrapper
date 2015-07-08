package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import java.io.File;

public interface VisualStudioProjectParser {

	public abstract SimpleVisualStudioProject parse(File file);

    public abstract void setName(String projectName);

}