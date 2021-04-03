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

import java.util.List;

public class AchievementsFragment extends Fragment {

    RecyclerView achievementsView;
    AchievementAdapter achievementAdapter;

    List<Achievement> achievementsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_achievements, container, false);

        achievementsView = view.findViewById(R.id.achievementsView);
        achievementAdapter = new AchievementAdapter();

        achievementsView.setAdapter(achievementAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        achievementsView.addItemDecoration(dividerItemDecoration);

        return view;
    }
}
