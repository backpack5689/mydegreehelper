package com.CENAA.mydegreehelper;

public class CourseUI {

    private String courseSub;
    private int courseNum;
    private String courseName;
    private String requirements;
    private boolean expanded;

    public CourseUI(String courseSub, int courseNum, String courseName, String requirements) {
        this.courseSub = courseSub;
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.requirements = requirements;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getCourseSub() {
        return courseSub;
    }

    public void setCourseSub() {
        this.courseSub = courseSub;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setCourseNum() {
        this.courseNum = courseNum;
    }

    public void setCourseName() {
        this.courseName = courseName;
    }

    public void setRequirements() {
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseSub='" + courseSub + "'" +
                ", courseNum='" + courseNum + "'" +
                ", courseName='" + courseName + "'" +
                ", requirements='" + requirements + "'";
    }
}
