package com.example.dnd_manager.updater;

/**
 * Terminator implementation based on System.exit(0).
 */
public class SystemExitTerminator implements ApplicationTerminator {

    @Override
    public void terminate() {
        System.exit(0);
    }
}

