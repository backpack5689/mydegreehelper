package com.CENAA.mydegreehelper;

import java.util.ArrayList;
import java.util.List;

public class Requirement {

    public String name;
    public List<List<Course>> requiredCourses;
    private boolean isComplete;

    public Requirement(String rName){
        name = rName;
        requiredCourses = new ArrayList<List<Course>>();
        isComplete = false;
    }

    public boolean getComplete(){
        return isComplete;
    }

    public boolean checkComplete(){
        for(int i = 0; i < requiredCourses.size();i++){
            for(int j = 0; j < requiredCourses.get(i).size(); j++){
                if(!requiredCourses.get(i).get(j).completed){
                    return false;
                }
            }
        }
        isComplete = true;
        return true;
    }

}
