package com.CENAA.mydegreehelper;

import java.util.ArrayList;
import java.util.List;

public class Requirement {

    public String name;
    public ArrayList<Course> requiredCourses;
    int totalHours;
    private boolean isComplete;

    public Requirement(String rName){
        name = rName;
        requiredCourses = new ArrayList<Course>();
        totalHours = 0;
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

    public void addCourse(Course course){
        requiredCourses.add(course);
        totalHours += course.creditValue;
    }

}
