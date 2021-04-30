package com.CENAA.mydegreehelper;

public class StateManager {
    Blueprint blueprint;
    User user;

    public StateManager() {
        this.user = new User();
        this.blueprint = new Blueprint();
    }

    public Blueprint getState() {
        return blueprint;
    }

    public User getUserState() {
        return user;
    }

    public void setState(Blueprint input) {
        this.blueprint = input;
    }

    public void setUserState(User input) {
        this.user = input;
    }
}
