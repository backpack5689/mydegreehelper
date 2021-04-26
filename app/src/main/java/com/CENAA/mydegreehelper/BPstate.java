package com.CENAA.mydegreehelper;

import android.app.Application;
import com.CENAA.mydegreehelper.StateManager;

public class BPstate extends Application {
    private StateManager stateManager = new StateManager();

    public StateManager getStateManager() {
        return stateManager;
    }
}

