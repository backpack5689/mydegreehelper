package com.CENAA.mydegreehelper;

import java.util.ArrayList;

public class Course {
    String courseName;
    String courseSub;
    int courseNum;
    double grade;
    int creditValue;
    boolean completed;
    ArrayList<Course> prereqs;
    ArrayList<Course> equivalents;

    public Course(){
        courseName = "";
        courseSub = "";
        courseNum = 0000;
        creditValue = 0;
        grade = 0.0;
        completed = false;
        prereqs = new ArrayList<Course>();
        equivalents = new ArrayList<Course>();
    }

    public Course(String cName, int cr){
        courseName = cName;
        courseSub = "";
        courseNum = 0000;
        creditValue = cr;
        grade = 0.0;
        completed = false;
        prereqs = new ArrayList<Course>();
        equivalents = new ArrayList<Course>();
    }

}
