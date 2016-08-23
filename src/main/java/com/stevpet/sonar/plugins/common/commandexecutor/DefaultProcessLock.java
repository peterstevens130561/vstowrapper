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
package com.stevpet.sonar.plugins.common.commandexecutor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;


/**
 * Very simple apprach to an interprocesslock
 * 
 * @author stevpet
 * 
 */
public class DefaultProcessLock implements BatchExtension, ProcessLock {
    private static Logger LOG = LoggerFactory.getLogger(DefaultProcessLock.class);
    private File lockFile;
    private FileChannel channel;
    private FileLock lock;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ProcessLock#
     * lock(java.lang.String)
     */
    @Override
    public void lock(String name) {
        String lockPath = System.getenv("TMP");
        lockFile = new File(lockPath, name + ".lock");
        try {

            channel = new RandomAccessFile(lockFile, "rw").getChannel();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not find lockFile " + lockFile.getAbsolutePath(), e);
        }
        try {
            LOG.debug("Acquiring processlock on " + lockFile.getAbsolutePath());
            lock = channel.lock();
            LOG.debug("Acquired processlock on " + lockFile.getAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("Could not lock " + lockFile.getAbsolutePath(), e);
        }
    }

    public void release() {
        try {
            lock.release();
        } catch (IOException e) {
            throw new IllegalStateException("Could not release " + lockFile.getAbsolutePath(), e);
        }
        try {
            channel.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not release " + lockFile.getAbsolutePath(), e);
        }
        LOG.debug("Released processlock on " + lockFile.getAbsolutePath());
    }
}
