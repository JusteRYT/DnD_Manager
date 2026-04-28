package com.example.dnd_manager.updater.runtime;

import com.example.dnd_manager.updater.port.AsyncRunner;

import java.util.Objects;

/**
 * Async runner backed by plain Java threads.
 */
public class ThreadAsyncRunner implements AsyncRunner {

    @Override
    public void run(String threadName, Runnable task) {
        Objects.requireNonNull(task, "task must not be null");
        new Thread(task, threadName).start();
    }
}















