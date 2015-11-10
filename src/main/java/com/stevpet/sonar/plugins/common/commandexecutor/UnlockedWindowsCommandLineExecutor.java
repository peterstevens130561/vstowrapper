package com.stevpet.sonar.plugins.common.commandexecutor;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.api.ShellCommand;

public class UnlockedWindowsCommandLineExecutor implements CommandLineExecutor{

    private WindowsCommandLineExecutor commandLineExecutor;

    public UnlockedWindowsCommandLineExecutor(WindowsCommandLineExecutor commandLineExecutor) {
        this.commandLineExecutor=commandLineExecutor;
    }
    @Override
    public int execute(ShellCommand command) {
        // TODO Auto-generated method stub
        return commandLineExecutor.execute(command);
    }

    @Override
    public int execute(ShellCommand command, int timeOutMinutes) {
        return commandLineExecutor.execute(command, timeOutMinutes);
    }

    @Override
    public String getStdOut() {
        return commandLineExecutor.getStdOut();
    }

    @Override
    public String getStdErr() {
        return commandLineExecutor.getStdErr();
    }

}
