/*******************************************************************************
 *
 * SonarQube MsCsover Plugin
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
package com.stevpet.sonar.plugins.common.api;

import org.sonar.api.BatchExtension;


public interface CommandLineExecutor extends BatchExtension {

    /**
     * Execute a windows commandline command. Sets new stdout / stderr. timeout set to 30 minutes;
     * @param command
     * @return exit code (0=ok). Exception thrown in other cases...
     */
    int execute(ShellCommand command);

    /**
     * Execute a commandline command
     * @param command
     * @param timeoutMinutes 
     * @return exitcode (0=ok) Exception thrown in other cases...
     */
    int execute(ShellCommand command,int timeoutMinutes);
    /**
     * 
     * @return stdout result. after execute.
     */
    String getStdOut();

    /**
     * 
     * @return stderr result after execute
     */
    String getStdErr();


}