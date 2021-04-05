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
    RecyclerView majorCourses, generalCourses;
    RecyclerAdapter majorAdapter, generalAdapter;

    List<CourseUI> majorCourseList, generalCourseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        majorCourseList = new ArrayList<CourseUI>();
        generalCourseList = new ArrayList<CourseUI>();

        majorCourses = view.findViewById(R.id.majorCourses);
        generalCourses = view.findViewById(R.id.generalCourses);

        initMajorCourseData();
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        majorAdapter = new RecyclerAdapter(majorCourseList);
        generalAdapter = new RecyclerAdapter(generalCourseList);

        majorCourses.setAdapter(majorAdapter);
        generalCourses.setAdapter(generalAdapter);
    }

    private void initMajorCourseData() {
        majorCourseList = new ArrayList<>();
        majorCourseList.add(new CourseUI("CS", 1001,"Intro to Programming", "None"));
        majorCourseList.add(new CourseUI("CS",1002,"Intro to Computer Science", "None"));
        majorCourseList.add(new CourseUI("CS",3233,"Intro to Programming", "MA 3233"));
        majorCourseList.add(new CourseUI("CS",3623,"Intro to Programming", "None"));

        generalCourseList = new ArrayList<>();
        generalCourseList.add(new CourseUI("CS", 1001,"Intro to Programming", "None"));
        generalCourseList.add(new CourseUI("CS",1002,"Intro to Computer Science", "None"));
        generalCourseList.add(new CourseUI("CS",3233,"Intro to Programming", "MA 3233"));
        generalCourseList.add(new CourseUI("CS",3623,"Intro to Programming", "None"));
    }
}
