package com.CENAA.mydegreehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {

    RecyclerView achievementsView;
    AchievementAdapter achievementAdapter;

    StateManager stateManager;
    Blueprint state;

    List<Achievement> achievementsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_achievements, container, false);
        stateManager = ((BPstate)requireActivity().getApplicationContext()).getStateManager();

        achievementsList = new ArrayList<Achievement>();

        achievementsView = view.findViewById(R.id.achievementsView);

        initData();
        initRecyclerView();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        achievementsView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private void initRecyclerView() {
        achievementAdapter = new AchievementAdapter(achievementsList);
        achievementsView.setAdapter(achievementAdapter);
    }

    private void initData() {

        state = stateManager.getState();

        boolean firstClass, sophomore, junior, senior, completeMajor, completeGeneral;

        double progressDbl = Math.floor((double) state.creditsCompleted / (double) state.totalCredits * 100);
        int progress = (int) progressDbl;

        firstClass = progress > 0;
        sophomore = progress >= 25;
        junior = progress >= 50;
        senior = progress >= 75;
        completeMajor = checkMajorComplete();
        completeGeneral = checkGeneralComplete();

        achievementsList = new ArrayList<>();
        achievementsList.add(new Achievement("First steps", "Complete your first course", firstClass));
        achievementsList.add(new Achievement("Freshie", "Begin your college career", true));
        achievementsList.add(new Achievement("Year 2", "Finish your freshman year and become a sophomore", sophomore));
        achievementsList.add(new Achievement("Halfway there!", "Finish half of your degree, becoming a junior", junior));
        achievementsList.add(new Achievement("Approaching the finish line!", "Reach your senior year", senior));
        achievementsList.add(new Achievement("Focused", "Complete all your major courses", completeMajor));
        achievementsList.add(new Achievement("Associate", "Complete all your general education courses", completeGeneral));
    }

    private boolean checkMajorComplete() {
        List<Course> majorCourses = state.requirements.get(0).requiredCourses;
        int incompleteCount = 0;
        for (int i = 0; i < majorCourses.size(); i++) {
            if (!majorCourses.get(i).isCompleted()) {
                incompleteCount++;
            }
        }

        return incompleteCount == 0;
    }

    private boolean checkGeneralComplete() {
        List<Course> generalCourses;
        int incompleteCount = 0;
        for (int j = 0; j < state.requirements.size(); j++) {
            generalCourses = state.requirements.get(j).requiredCourses;
            for (int i = 0; i < generalCourses.size(); i++) {
                if (!generalCourses.get(i).isCompleted()) {
                    incompleteCount++;
                }
            }
        }
        
        return incompleteCount == 0;
    }
}
