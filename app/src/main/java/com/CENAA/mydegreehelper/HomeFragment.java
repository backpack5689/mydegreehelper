package com.CENAA.mydegreehelper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    StateManager stateManager;
    Blueprint state;

    ProgressBar progressBar;
    TextView progressTitle;

    RecyclerView majorCourses, generalCourses;
    RecyclerAdapter majorAdapter, generalAdapter;

    List<Course> majorCourseList, generalCourseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize state from blueprint
        stateManager = ((BPstate)getActivity().getApplicationContext()).getStateManager();
        state = stateManager.getState();

        majorCourseList = new ArrayList<Course>();
        generalCourseList = new ArrayList<Course>();

        // Attach UI elements to variables
        majorCourses = view.findViewById(R.id.majorCourses);
        generalCourses = view.findViewById(R.id.generalCourses);
        progressBar = view.findViewById(R.id.progressBar);
        progressTitle = view.findViewById(R.id.progressTitle);

        setProgressBar(); // Set progress from blueprint

        initData(); // Send data from blueprint to UI
        initRecyclerView(); // Initialize RecyclerViews from blueprint data

        return view;
    }

    private void setProgressBar() {
        state = stateManager.getState();

        // Set current progress from state
        double progressDbl = Math.floor((double) state.creditsCompleted / (double) state.totalCredits * 100);
        int progress = (int) progressDbl;
        progressBar.setProgress(progress);

        // Set progress title
        if (progress >= 75) {
            progressTitle.setText(R.string.progress_senior);
        } else if (progress >= 50) {
            progressTitle.setText(R.string.progress_junior);
        } else if (progress >= 25) {
            progressTitle.setText(R.string.progress_sophomore);
        } else {
            progressTitle.setText(R.string.progress_freshman);
        }
    }

    private void initRecyclerView() {
        majorAdapter = new RecyclerAdapter(majorCourseList, new ProgressCallback() {
            @Override
            public void onProgressCallback() {
                setProgressBar();
            }
        });
        generalAdapter = new RecyclerAdapter(generalCourseList, new ProgressCallback() {
            @Override
            public void onProgressCallback() {
                setProgressBar();
            }
        });

        majorCourses.setAdapter(majorAdapter);
        generalCourses.setAdapter(generalAdapter);
    }

    private void initData() { // Initialize data for RecyclerViews from blueprint
        state = stateManager.getState();
        majorCourseList = state.requirements.get(0).requiredCourses; // Pull major courses from blueprint

        // Iterate through requirements in blueprint and add all non-duplicate courses to general course list
        int i = 1, j = 0, k = 0;
        boolean duplicate;
        ArrayList<Requirement> genCourses = state.getRequirements();
        ArrayList<Course> compiledList = new ArrayList<>();

        for (i = 1; i < genCourses.size(); i++) { // Check for duplicate courses
            for (j = 0; j < genCourses.get(i).requiredCourses.size(); j++) {
                duplicate = false;
                for (k = 0; k < compiledList.size(); k++) {
                   if (genCourses.get(i).requiredCourses.get(j).toString().equals(compiledList.get(k).toString())) {
                       duplicate = true;
                   }
                }
                if (!duplicate) {
                    compiledList.add(genCourses.get(i).requiredCourses.get(j));
                }
            }
        }
        generalCourseList = new ArrayList<>(compiledList); // Add courses to general education RecyclerView

        // Sort
        Collections.sort(majorCourseList);
        Collections.sort(generalCourseList);
    }
}
