/*******************************************************************************
 * MSCover Plugin
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


public class VisualStudioPlugin {

  static final String VISUAL_STUDIO_SOLUTION_PROPERTY_KEY = "sonar.visualstudio.solution";
  static final String VISUAL_STUDIO_ENABLE_PROPERTY_KEY = "sonar.visualstudio.enable";
  static final String VISUAL_STUDIO_OUTPUT_PATHS_PROPERTY_KEY = "sonar.visualstudio.outputPaths";
  static final String VISUAL_STUDIO_TEST_PROJECT_PATTERN = "sonar.visualstudio.testProjectPattern";
  static final String VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN = "sonar.visualstudio.skippedProjectPattern";
  static final String VISUAL_STUDIO_SKIP_IF_NOT_BUILT = "sonar.visualstudio.skipIfNotBuilt";

  static final String VISUAL_STUDIO_OLD_SKIPPED_PROJECTS = "sonar.visualstudio.skippedProjects";
  static final String VISUAL_STUDIO_OLD_SOLUTION_PROPERTY_KEY = "sonar.dotnet.visualstudio.solution.file";
  public static final String BUILD_CONFIGURATION_KEY = "sonar.dotnet.buildConfiguration";
  public static final String BUILD_CONFIGURATIONS_DEFVALUE = "Debug";

  public static final String BUILD_PLATFORM_KEY = "sonar.dotnet.buildPlatform";
  public static final String BUILD_PLATFORM_DEFVALUE = "x64";
}
