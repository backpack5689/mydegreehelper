package com.CENAA.mydegreehelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blueprint {
    int bpID;
    String degree;
    int totalCredits;
    int level;
    boolean bpComplete;

    ArrayList<Course> masterList;
    ArrayList<Requirement> requirements;

    public Blueprint(String bpInput){
    //parse file to create BP
    Scanner sc = new Scanner(bpInput);
    String line;
    masterList = new ArrayList<Course>();
    requirements = new ArrayList<Requirement>();
    //Update level for Fresh/Soph/etc. (Possibly move this data elsewhere)
    level = 1;
    bpComplete = false;

    //TO-DO replace 0 with auto-inc from DB
    bpID = 0;
    degree = sc.nextLine();
    totalCredits = Integer.parseInt(sc.nextLine());
    while(sc.hasNextLine()){
        line = sc.nextLine();
        Course course = new Course();
        String[] splitLine = line.split("-");
        course.courseName = splitLine[0];
        course.courseSub = splitLine[1].split(" ")[0];
        course.courseNum = Integer.parseInt(splitLine[1].split(" ")[1]);
        for (int i = 2; i < splitLine.length; i++ ) {
            if (splitLine[i].charAt(0) == '|') {

            } else if (splitLine[i].charAt(0) == '(') {

            } else if (splitLine[i].charAt(0) == '*') {

            }
        }
    }

    }

    public boolean checkBPComplete(){
        for(int i = 0; i < requirements.size(); i++){
            if(!requirements.get(i).getComplete()){
                return false;
            }
        }
        return true;
    }

    public Course findCourse(String query){
        Course course = new Course();
        for(int i = 0; i < masterList.size(); i++)
        {
            if(masterList.get(i).courseName == query){
                 course = masterList.get(i);
            }
        }
        return course;
    }

}
