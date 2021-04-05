package com.CENAA.mydegreehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView majorCourses;
    RecyclerAdapter recyclerAdapter;

    List<CourseUI> majorCourseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        majorCourseList = new ArrayList<CourseUI>();

        majorCourses = view.findViewById(R.id.majorCourses);

        initMajorCourseData();
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        recyclerAdapter = new RecyclerAdapter(majorCourseList);
        majorCourses.setAdapter(recyclerAdapter);
    }

    private void initMajorCourseData() {
        majorCourseList = new ArrayList<>();
        majorCourseList.add(new CourseUI("CS", 1001,"Intro to Programming", "None"));
        majorCourseList.add(new CourseUI("CS",1002,"Intro to Computer Science", "None"));
        majorCourseList.add(new CourseUI("CS",3233,"Intro to Programming", "MA 3233"));
        majorCourseList.add(new CourseUI("CS",3623,"Intro to Programming", "None"));
    }
}
