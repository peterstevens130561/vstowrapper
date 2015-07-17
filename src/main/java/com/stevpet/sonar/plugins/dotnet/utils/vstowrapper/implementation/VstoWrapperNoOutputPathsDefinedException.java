package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

public class VstoWrapperNoOutputPathsDefinedException extends VsToWrapperException {
    private static final long serialVersionUID = 1L;
    private static String msg = "No outputpath specified through " + VisualStudioPlugin.VISUAL_STUDIO_OUTPUT_PATHS_PROPERTY_KEY;
 
    public VstoWrapperNoOutputPathsDefinedException() {       
        super(msg);
    }

}
