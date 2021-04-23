package com.CENAA.mydegreehelper;

import android.app.Application;

public class BPstate extends Application {
    private StateManager stateManager = new StateManager();

    public StateManager getStateManager() {
        return stateManager;
    }
}

class StateManager {
    Blueprint blueprint;

    StateManager() { this.blueprint = new Blueprint(); }

    Blueprint getState() { return blueprint; }

    public void setState(Blueprint input) { this.blueprint = input; }
}
