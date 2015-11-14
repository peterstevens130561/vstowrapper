/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.AssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;

import javax.annotation.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VisualStudioAssemblyLocator implements AssemblyLocator {

  private static final Logger LOG = LoggerFactory.getLogger(VisualStudioAssemblyLocator.class);
  private static final Comparator<File> FILE_LAST_MODIFIED_COMPARATOR = new FileLastModifiedComparator();

  private final Settings settings;

  public VisualStudioAssemblyLocator(Settings settings) {
    this.settings = settings;
  }

  /* (non-Javadoc)
 * @see com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation.AssemblyLocator#locateAssembly(java.lang.String, java.io.File, com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject)
 */
@Override
public File locateAssembly(String projectName, File projectFile, VisualStudioProject project) {
    LOG.debug("Locating the assembly for the project " + projectName + "... " + projectFile.getAbsolutePath());
    if (project.outputType() == null || project.getAssemblyName() == null) {
      LOG.warn("Unable to locate the assembly as either the output type or the assembly name is missing.");
      return null;
    }

    String extension = extension(projectFile, project.outputType());
    if (extension == null) {
      LOG.warn("Unable to locate the assembly of the unsupported output type \"" + project.outputType() + "\" of project: " + projectFile.getAbsolutePath());
      return null;
    }

    String assemblyFileName = project.getAssemblyName() + "." + extension;
    List<File> candidates = candidates(assemblyFileName, projectFile, project);

    if (candidates.isEmpty()) {
      LOG.warn("Unable to locate the assembly of project " + projectName);
      return null;
    }

    Collections.sort(candidates, FILE_LAST_MODIFIED_COMPARATOR);

    if (candidates.size() == 1) {
      String title = candidates.size() == 1 ? "Using the the following assembly for project " : "Using the most recently generated assembly for project ";
      LOG.info(title + projectName + ": " + candidates.get(0).getAbsolutePath());
    }

    return candidates.get(0);
  }

  private @VisibleForTesting
  @Nullable
  String extension(File projectFile, String outputType) {
    String result;

    String lowerOutputType = outputType.toLowerCase();

    if ("library".equals(lowerOutputType)) {
      result = "dll";
    } else if ("exe".equals(lowerOutputType)) {
      result = "exe";
    } else if ("winexe".equals(lowerOutputType)) {
      result = "exe";
    } else {
      result = null;
    }

    return result;
  }

  private List<File> candidates(String assemblyFileName, File projectFile, VisualStudioProject project) {
      List<File> candidates = Lists.newArrayList();

      String explicitOutputPaths = settings.getString(VisualStudioPlugin.VISUAL_STUDIO_OUTPUT_PATHS_PROPERTY_KEY);
      if(StringUtils.isEmpty(explicitOutputPaths)) {
          throw new VstoWrapperNoOutputPathsDefinedException();
      } else {
          LOG.debug("Using the assembly output paths specified using the property \"" + VisualStudioPlugin.VISUAL_STUDIO_OUTPUT_PATHS_PROPERTY_KEY
                  + "\" set to: " + explicitOutputPaths);

          for (String explicitOutputPath : Splitter.on(',').omitEmptyStrings().split(explicitOutputPaths)) {
              String path=explicitOutputPath.replace('\\', '/') ;
              String basePath = "";
              if( isRelative(path)) {
                  basePath = projectFile.getParentFile().getAbsolutePath() + "/" ;
              }
              String directoryPath = basePath + path ;
              File absoluteCandidate = new File(directoryPath,assemblyFileName);
              candidates.add(absoluteCandidate);
          }
      } 
      return candidates;
  }

private boolean isRelative(String path) {
    return !path.matches("^\\/|([A-Z]:\\/).*");
}

  private static class FileLastModifiedComparator implements Comparator<File>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(File o1, File o2) {
      if (o1.lastModified() == o2.lastModified()) {
        return 0;
      }

      return o1.lastModified() > o2.lastModified() ? -1 : 1;
    }

  }

}
