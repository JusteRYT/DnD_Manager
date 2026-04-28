package com.example.dnd_manager.updater.install;

import com.example.dnd_manager.updater.port.ApplicationTerminator;

/**
 * Terminator implementation based on System.exit(0).
 */
public class SystemExitTerminator implements ApplicationTerminator {

    @Override
    public void terminate() {
        System.exit(0);
    }
}















