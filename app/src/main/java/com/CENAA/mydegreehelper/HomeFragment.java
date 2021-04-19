package com.CENAA.mydegreehelper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    RecyclerView majorCourses, generalCourses;
    RecyclerAdapter majorAdapter, generalAdapter;

    List<Course> majorCourseList, generalCourseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        stateManager = ((BPstate)getActivity().getApplicationContext()).getStateManager();

        majorCourseList = new ArrayList<Course>();
        generalCourseList = new ArrayList<Course>();

        majorCourses = view.findViewById(R.id.majorCourses);
        generalCourses = view.findViewById(R.id.generalCourses);

        initData();
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        majorAdapter = new RecyclerAdapter(majorCourseList);
        generalAdapter = new RecyclerAdapter(generalCourseList);

        majorCourses.setAdapter(majorAdapter);
        generalCourses.setAdapter(generalAdapter);
    }

    private void initData() { // Initialize data for RecyclerViews from blueprint
        state = stateManager.getState();
        majorCourseList = state.requirements.get(0).requiredCourses; // Pull major courses from blueprint
        majorCourseList.add(new Course("Test", 9999, "TS", 3, 92.3, new ArrayList<Course>(), new ArrayList<Course>(), true)); // For testing completed courses

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
