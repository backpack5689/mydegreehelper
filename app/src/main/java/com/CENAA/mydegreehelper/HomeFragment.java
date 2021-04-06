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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView majorCourses, generalCourses;
    RecyclerAdapter majorAdapter, generalAdapter;

    List<Course> majorCourseList, generalCourseList;

    Blueprint userBP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        majorCourseList = new ArrayList<Course>();
        generalCourseList = new ArrayList<Course>();

        majorCourses = view.findViewById(R.id.majorCourses);
        generalCourses = view.findViewById(R.id.generalCourses);

        try {
            initBP();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initData();
        initRecyclerView();

        return view;
    }

    private void initBP() throws IOException {
        File file = new File(requireActivity().getApplicationContext().getFilesDir() + "/local/", "localFile.txt");
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
        majorAdapter = new RecyclerAdapter(majorCourseList);
        generalAdapter = new RecyclerAdapter(generalCourseList);

        majorCourses.setAdapter(majorAdapter);
        generalCourses.setAdapter(generalAdapter);
    }

    private void initData() { // Initialize data for RecyclerViews from blueprint
        majorCourseList = userBP.requirements.get(0).requiredCourses;
        majorCourseList.add(new Course("Test", 9999, "TS", 3, 0.0, new ArrayList<Course>(), new ArrayList<Course>(), true));

        int i = 1, j = 0, k = 0;
        boolean duplicate;
        ArrayList<Requirement> genCourses = userBP.getRequirements();
        ArrayList<Course> compiledList = new ArrayList<>();
        for (i = 1; i < genCourses.size(); i++) {
            for (j = 0; j < genCourses.get(i).requiredCourses.size(); j++) {
                duplicate = false;
                for (k = 0; k < compiledList.size(); k++) {
                   if (genCourses.get(i).requiredCourses.get(j) == compiledList.get(k)) {
                       duplicate = true;
                   }
                }
                if (!duplicate) {
                    compiledList.add(genCourses.get(i).requiredCourses.get(j));
                }
            }
        }
        generalCourseList = new ArrayList<>(compiledList);
    }
}
