package com.CENAA.mydegreehelper;

import java.util.ArrayList;
import java.util.List;

public class Requirement {

    public String name;
    public ArrayList<Course> requiredCourses;
    private boolean isComplete;

    public Requirement(String rName){
        name = rName;
        requiredCourses = new ArrayList<Course>();
        isComplete = false;
    }

    public boolean getComplete(){
        return isComplete;
    }

    public boolean checkComplete(){
        for(int i = 0; i < requiredCourses.size();i++){
            if(!requiredCourses.get(i).completed) {
                return false;
            }
        }
        isComplete = true;
        return true;
    }

}
