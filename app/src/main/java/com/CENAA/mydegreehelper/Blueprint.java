package com.CENAA.mydegreehelper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Blueprint {
    int bpID;
    String degree;
    int totalCredits;
    int level;
    boolean bpComplete;
    int creditsCompleted;
    boolean expanded;

    ArrayList<Course> masterList;
    ArrayList<Requirement> requirements;

    public Blueprint() {
        bpID = -1;
        degree = "";
        totalCredits = 0;
        level = 0;
        bpComplete = false;
        creditsCompleted = 0;
        masterList = new ArrayList<Course>();
        requirements = new ArrayList<Requirement>();
        expanded = false;
    }

    public Blueprint(String bpInput) {
        //parse file to create BP
        Scanner sc = new Scanner(bpInput);
        String line;
        masterList = new ArrayList<Course>();
        requirements = new ArrayList<Requirement>();
        creditsCompleted = 0;
        //Update level for Fresh/Soph/etc. (Possibly move this data elsewhere like User class)
        level = 1;
        bpComplete = false;
        expanded = false;

        //TO-DO replace 0 with auto-inc from DB
        Random rand = new Random();
        bpID = rand.nextInt(5000);

        degree = sc.nextLine();
        totalCredits = Integer.parseInt(sc.nextLine());

        templateParse:
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            //Log.d("line", line);
            if (line.equals("") || line.charAt(0) == '/') {
                continue;
            }
            Course course = new Course();
            String[] splitLine = line.split("-");
            course.courseName = splitLine[0];
            course.courseSub = splitLine[1].split(" ")[0];
            course.courseNum = Integer.parseInt(splitLine[1].split(" ")[1]);
            for (int i = 2; i < splitLine.length; i++) {
                if (splitLine[i].charAt(0) == '|') {
                    Course equiv = new Course();
                    equiv.courseName = splitLine[i].substring(1);
                    equiv.courseSub = splitLine[i].split(" ")[0];
                    equiv.courseNum = Integer.parseInt(splitLine[i].split(" ")[1]);
                    course.equivalents.add(equiv);
                    equiv.equivalents.add(course);
                    masterList.add(equiv);
                } else if (splitLine[i].charAt(0) == '(') {
                    String query = splitLine[i].substring(1);
                    boolean queryFound = false;
                    for (Course c : masterList) {
                        if ((c.courseSub + " " + c.courseNum).equals(query)) {
                            course.prereqs.add(c);
                            queryFound = true;
                            break;
                        }
                    }
                    if (!queryFound) {
                        //Replace with error message to UI
                        Log.d("Template", "File template error: pre-reqs should be listed" +
                                "before their follow on courses.");
                        break templateParse;
                    }
                } else if (splitLine[i].charAt(0) == '*') {
                    String req = splitLine[i].substring(1);
                    boolean reqExists = false;
                    for (Requirement r : requirements) {
                        if (r.name.equals(req)) {
                            r.addCourse(course);
                            reqExists = true;
                        }
                    }
                    if (!reqExists) {
                        Requirement requirement = new Requirement(req);
                        requirement.addCourse(course);
                        requirements.add(requirement);
                    }
                } else {
                    course.creditValue = Integer.parseInt(splitLine[i]);
                }
            }
            masterList.add(course);
        }

    }

    public String getName() {
        return String.valueOf(bpID);

    }

    public boolean checkBPComplete() {
        for (int i = 0; i < requirements.size(); i++) {
            if (!requirements.get(i).getComplete()) {
                return false;
            }
        }
        return true;
    }

    public Course findCourse(String query) {
        Course course = new Course();
        for (int i = 0; i < masterList.size(); i++) {
            if (masterList.get(i).courseName.equals(query)) {
                course = masterList.get(i);
                return course;
            }
        }
        return course;
    }

    public void referenceRebuild() {

        for (int m = 0; m < masterList.size(); m++) {
            ArrayList<String> temp = new ArrayList<>();
            for (Iterator<Course> iterator = masterList.get(m).prereqs.iterator(); iterator.hasNext(); ) {
                Course c = iterator.next();
                temp.add(c.courseName);
                iterator.remove();
            }
            for (int n = 0; n < temp.size(); n++) {
                masterList.get(m).prereqs.add(findCourse(temp.get(n)));
            }
        }

        for (int i = 0; i < requirements.size(); i++) {
            ArrayList<String> temp = new ArrayList<>();
            for (Iterator<Course> iterator = requirements.get(i).requiredCourses.iterator(); iterator.hasNext(); ) {
                Course c = iterator.next();
                temp.add(c.courseName);
                requirements.get(i).totalHours -= c.creditValue;
                iterator.remove();
            }
            for (int j = 0; j < temp.size(); j++) {
                requirements.get(i).addCourse(findCourse(temp.get(j)));
            }
        }
    }

    public void completeCourse(String courseName, double score) {

        Course course = findCourse(courseName);
        course.grade = score;

        if (!course.isCompleted()) {
            course.completed = true;
            creditsCompleted += course.creditValue;
        }


    }

    public void displayBP() {
        //MasterList
        Log.d("BP", "Master List: \n");
        for (int i = 0; i < masterList.size(); i++) {
            Log.d("BP", masterList.get(i).courseName + " " +
                    masterList.get(i).courseSub + " " + masterList.get(i).courseNum +
                    "(" + masterList.get(i).creditValue + ")" + "\n");
            for (int j = 0; j < masterList.get(i).equivalents.size(); j++) {
                if (j == 0) Log.d("BP", "Equivalents: \n");
                Log.d("BP", "\t" + masterList.get(i).equivalents.get(j).courseName + "\n");
            }
            for (int k = 0; k < masterList.get(i).prereqs.size(); k++) {
                if (k == 0) Log.d("BP", "Prerequisites: \n");
                Log.d("BP", "\t" + masterList.get(i).prereqs.get(k).courseName + "\n");
            }
        }

        //Requirements
        Log.d("BP", "Requirements: \n");
        for (int i = 0; i < requirements.size(); i++) {
            Log.d("BP", requirements.get(i).name + "\n");
            for (int j = 0; j < requirements.get(i).requiredCourses.size(); j++) {
                Log.d("BP", "\t" + requirements.get(i).requiredCourses.get(j).courseName + "\n");
            }
        }
    }

    public static void saveBPAsLocal(Context c, Blueprint bp) {
        File file = new File(c.getFilesDir(), "local");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            StateManager stateManager = ((BPstate) c.getApplicationContext()).getStateManager();
            String userIDString = Integer.toString(stateManager.getUserState().id);
            File localFile = new File(file, "user_" + userIDString + "_localFile.txt");
            FileWriter writer = new FileWriter(localFile);
            Gson gson = new Gson();
            String bpString = gson.toJson(bp);
            writer.write(bpString);
            Log.d("saved String", bpString);
            writer.flush();
            writer.close();
            Toast.makeText(c.getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

}
