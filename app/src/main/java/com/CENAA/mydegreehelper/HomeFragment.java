package com.CENAA.mydegreehelper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize state from blueprint
        stateManager = ((BPstate)requireActivity().getApplicationContext()).getStateManager();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    void filter(String search) {
        List<Course> tempMajor = new ArrayList<>();
        List<Course> tempGeneral = new ArrayList<>();
        String searchLowerCase = search.toLowerCase();

        for (Course m: majorCourseList) {
            if (m.getCourseName().toLowerCase().contains(searchLowerCase)) {
                tempMajor.add(m);
            }
        }
        for (Course g: generalCourseList) {
            if (g.getCourseName().toLowerCase().contains(searchLowerCase)) {
                tempGeneral.add(g);
            }
        }

        majorAdapter.updateList(tempMajor);
        generalAdapter.updateList(tempGeneral);
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
        User loggedInUser = stateManager.getUserState();
        Log.d("Logged-In", loggedInUser.username + " " + loggedInUser.id);
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
