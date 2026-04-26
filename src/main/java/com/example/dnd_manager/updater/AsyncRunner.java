package com.example.dnd_manager.updater;

/**
 * Abstraction for running background tasks.
 */
public interface AsyncRunner {

    void run(String threadName, Runnable task);
}

