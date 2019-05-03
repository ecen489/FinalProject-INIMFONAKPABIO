package com.example.inimfonakpabio.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TabWorkouts extends Fragment {

    GridLayout mainGrid;
    RecyclerView recView;
    RecyclerView.Adapter adapter;

    List<Exercise> listitems;

    public TabWorkouts() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        recView = (RecyclerView) view.findViewById(R.id.recView);
        listitems = new ArrayList<>();
        Populate();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new RecAdapter(listitems, getActivity());
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recView.setAdapter(adapter);

    }

    public void Populate() {
        listitems.add(new Exercise("Bicep Curls", 1, "Biceps", "Dumbbells",
                "Now, keeping the upper arms stationary, curl the weights while contracting your biceps.\n" +
                        "Raise the weights until your biceps are fully contracted and the dumbbells are at shoulder level.\n" +
                        "Hold the contracted position as you squeeze your biceps and return to the start position.\n",
                10001));
        listitems.add(new Exercise("Overhead press", 2, "(compound) Shoulders, upperbody", "Dumbbells, Barbells",
                "Stand with the bar on your front shoulders, and your hands next to your shoulders\n" +
                        "Press the bar over your head, until it’s balanced over your shoulders and mid-foot\n" +
                        "Lock your elbows at the top, and shrug your shoulders to the ceiling.",
                10002));
        listitems.add(new Exercise("Benchpress", 3, "(compound) Arms, Chest, Shoulders, Back", "Flat Bench, Barbell",
                "Dismount the barbell using a grip that is a little over shoulder-width apart.\n" +
                        "Inhale as you lower the barbell to your chest, keeping your elbows tucked in at a 45-degree angle.\n" +
                        "Exhale as you press the barbell back up to the starting position.",
                10003));
//        listitems.add(new Exercise("Dips", 4, "Triceps, Chest, Front shoulders", "horizontal Bar",
//                "Straighten your armss and lower your body by bending your arms while leaning forward.\n" +
//                        "Dip down until your shoulders are below your elbows.\n" +
//                        "Lift your body up by straightening your arms.\n" +
//                        "Lock your elbows at the top.",
//                10004));
    }
}
