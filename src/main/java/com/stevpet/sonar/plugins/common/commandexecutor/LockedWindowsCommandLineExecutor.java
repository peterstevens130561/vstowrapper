package com.stevpet.sonar.plugins.common.commandexecutor;

import com.stevpet.sonar.plugins.common.api.ShellCommand;


/**
 * Simple cross process locking mechanism, prevents that other sonar-runner processes execute
 * the same commands at the same time. 
 * @author stevpet
 *
 */
public class LockedWindowsCommandLineExecutor extends
        WindowsCommandLineExecutor {
    private final ProcessLock processLock;

    @SuppressWarnings("ucd")
    public LockedWindowsCommandLineExecutor(ProcessLock processLock) {
        this.processLock=processLock;
    }

    @Override
    public int execute(ShellCommand command) {
        String lockName=command.getExecutable();
        processLock.lock(lockName);
        try {
            return super.execute(command);
        } finally {
            processLock.release();
        }
    }
}