package com.stevpet.sonar.plugins.common.commandexecutor;

public interface ProcessLock {

    /**
     * Creates a lock in the TMP directory
     * @param lockName
     */
    void lock(String lockName);

    void release();

}