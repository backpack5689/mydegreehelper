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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView majorCourses;
    RecyclerAdapter recyclerAdapter;

    List<CourseUI> majorCourseList;

    Blueprint userBP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        majorCourseList = new ArrayList<CourseUI>();

        majorCourses = view.findViewById(R.id.majorCourses);

        try {
            initBP();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initMajorCourseData();
        initRecyclerView();

        return view;
    }

    private void initBP() throws IOException {
        File file = new File(getActivity().getApplicationContext().getFilesDir() + "/local/", "localFile.txt");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis);
        StringBuilder stringBuilder = new StringBuilder();
        String contents;
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            contents = stringBuilder.toString();
        }
        Log.d("line", contents);
        Gson gson = new Gson();
        userBP = gson.fromJson(contents, Blueprint.class);
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
