package com.CENAA.mydegreehelper;

import android.app.Application;

public class BPstate extends Application {
    private final StateManager stateManager = new StateManager();

    public StateManager getStateManager() {
        return stateManager;
    }
}