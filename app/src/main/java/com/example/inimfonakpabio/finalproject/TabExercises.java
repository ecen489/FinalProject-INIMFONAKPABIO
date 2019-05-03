package com.example.inimfonakpabio.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TabExercises extends Fragment {

    ListView listExercises;
    CustomListAdapter cListAdapter;
    ArrayList<Exercise> allExercises;

    FirebaseUser user = null;
    FirebaseAuth firebase_auth;
    DatabaseReference db_ref;

    public TabExercises() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);

        listExercises = (ListView) view.findViewById(R.id.listExercises);

        db_ref = FirebaseDatabase.getInstance().getReference();
        firebase_auth = FirebaseAuth.getInstance();
        user = firebase_auth.getCurrentUser();

        allExercises = new ArrayList<>();
        PopulateArray2();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Exercise curEx = allExercises.get(i);
                Intent intent = new Intent(getContext(), GuideActivity.class);
                intent.putExtra("CURRENT_EXERCISE", curEx);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        user = firebase_auth.getCurrentUser();
    }

    public void PopulateArray2() {
        DatabaseReference exRef = db_ref.child("fitnessbuddy/exercises/");
        exRef.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                Toast.makeText(getActivity(),"listening",Toast.LENGTH_SHORT).show();
                allExercises = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    allExercises.add(exercise);
                }

                cListAdapter = new CustomListAdapter(getContext(), R.layout.custom_listview, allExercises);
                listExercises.setAdapter(cListAdapter);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("NINI", "Database error");
        }
    };

    public void PopulateArray() {
        allExercises.add(new Exercise("Bicep Curls", 1, "Biceps", "Dumbbells",
                "Now, keeping the upper arms stationary, curl the weights while contracting your biceps.\n" +
                        "Raise the weights until your biceps are fully contracted and the dumbbells are at shoulder level.\n" +
                        "Hold the contracted position as you squeeze your biceps and return to the start position.\n",
                10001));
        allExercises.add(new Exercise("Overhead press", 2, "(compound) Shoulders, upperbody", "Dumbbells, Barbells",
                "Stand with the bar on your front shoulders, and your hands next to your shoulders\n" +
                        "Press the bar over your head, until it’s balanced over your shoulders and mid-foot\n" +
                        "Lock your elbows at the top, and shrug your shoulders to the ceiling.",
                10002));
        allExercises.add(new Exercise("Benchpress", 3, "(compound) Arms, Chest, Shoulders, Back", "Flat Bench, Barbell",
                "Dismount the barbell using a grip that is a little over shoulder-width apart.\n" +
                        "Inhale as you lower the barbell to your chest, keeping your elbows tucked in at a 45-degree angle.\n" +
                        "Exhale as you press the barbell back up to the starting position.",
                10003));
        allExercises.add(new Exercise("Dips", 4, "Triceps, Chest, Front shoulders", "horizontal Bar",
                "Straighten your armss and lower your body by bending your arms while leaning forward.\n" +
                        "Dip down until your shoulders are below your elbows.\n" +
                        "Lift your body up by straightening your arms.\n" +
                        "Lock your elbows at the top.",
                10004));

        cListAdapter = new CustomListAdapter(getContext(), R.layout.custom_listview, allExercises);
        listExercises.setAdapter(cListAdapter);
    }
}
