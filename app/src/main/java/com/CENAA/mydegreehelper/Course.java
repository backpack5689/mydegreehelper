package com.CENAA.mydegreehelper;

import java.util.ArrayList;

public class Course implements Comparable<Course> {
    boolean expanded;
    String courseName;
    String courseSub;
    int courseNum;
    double grade;
    int creditValue;
    boolean completed;
    ArrayList<Course> prereqs;
    ArrayList<Course> equivalents;

    public Course(){
        expanded = false;
        courseName = "";
        courseSub = "";
        courseNum = 0000;
        creditValue = 0;
        grade = 0.0;
        completed = false;
        prereqs = new ArrayList<Course>();
        equivalents = new ArrayList<Course>();
    }

    // {"completed":false,"courseName":"Introduction to Computer Science","courseNum":1122,"courseSub":"CS","creditValue":2,"equivalents":[],"grade":0.0,"prereqs":[]}
    public Course(String courseName, int courseNum, String courseSub, int creditValue, double grade, ArrayList<Course> prereqs, ArrayList<Course> equivalents, boolean completed){
        this.expanded = false;
        this.courseName = courseName;
        this.courseSub = courseSub;
        this.courseNum = courseNum;
        this.creditValue = creditValue;
        this.grade = grade;
        this.completed = completed;
        this.prereqs = prereqs;
        this.equivalents = equivalents;
    }

    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
    public boolean isCompleted() { return completed; }
    public String getCourseSub() {
        return courseSub;
    }
    public int getCourseNum() {
        return courseNum;
    }
    public String getCourseName() {
        return courseName;
    }

    @Override
    public int compareTo(Course o) {
        return Integer.compare(this.getCourseNum(), o.getCourseNum());
    }
}
