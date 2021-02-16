package com.CENAA.mydegreehelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Blueprint {
    int bpID;
    int totalCredits;
    String level;
    boolean bpComplete;

    ArrayList<Course> masterList;
    ArrayList<Requirement> requirements;

    public Blueprint(File bpInput){
        //parse file to create BP
    }

    public boolean checkBPComplete(){
        for(int i = 0; i < requirements.size(); i++){
            if(!requirements.get(i).getComplete()){
                return false;
            }
        }
        return true;
    }

}
