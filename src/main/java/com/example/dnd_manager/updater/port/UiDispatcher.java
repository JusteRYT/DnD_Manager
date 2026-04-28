package com.example.dnd_manager.updater.port;

/**
 * Abstraction for dispatching work on UI thread.
 */
public interface UiDispatcher {

    void dispatch(Runnable action);
}














