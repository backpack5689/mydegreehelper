package com.CENAA.mydegreehelper;

import java.util.ArrayList;

public class Course {
    String courseName;
    double grade;
    int creditValue;
    boolean completed;
    ArrayList<Course> prerequsites;

    public Course(String cName, int cr){
        courseName = cName;
        creditValue = cr;
        grade = 0.0;
        completed = false;
        prerequsites = new ArrayList<Course>();
    }

}
