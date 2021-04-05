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

    List<Achievement> achievementsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_achievements, container, false);

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
        achievementsList = new ArrayList<>();
        achievementsList.add(new Achievement("Test Achievement 1", "Achievement Description", false));
        achievementsList.add(new Achievement("Test Achievement 2", "Achievement Description", true));
    }
}
