package com.CENAA.mydegreehelper;

public class Achievement {

    private String achievementName;
    private String achievementDesc;
    private boolean achievementComplete;

    public Achievement(String achievementName, String achievementDesc, boolean achievementComplete) {
        this.achievementName = achievementName;
        this.achievementDesc = achievementDesc;
        this.achievementComplete = achievementComplete;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public void setAchievementName() {
        this.achievementName = achievementName;
    }

    public String getAchievementDesc() {
        return achievementDesc;
    }

    public void setAchievementDesc() {
        this.achievementDesc = achievementDesc;
    }

    public boolean isComplete() {
        return achievementComplete;
    }

    public void setExpanded(boolean achievementComplete) { this.achievementComplete = achievementComplete; }

    @Override
    public String toString() {
        return "Achievement{" +
                "Name='" + achievementName + "'" +
                ", Desc='" + achievementDesc + "'" +
                ", Complete='" + achievementComplete +  "'";
    }
}
