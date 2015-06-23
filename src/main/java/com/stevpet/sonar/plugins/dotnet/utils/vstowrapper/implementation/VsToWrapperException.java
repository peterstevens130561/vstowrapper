package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import org.sonar.api.utils.SonarException;

public class VsToWrapperException extends SonarException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public VsToWrapperException(String msg) {
        super(msg);
    }
}
