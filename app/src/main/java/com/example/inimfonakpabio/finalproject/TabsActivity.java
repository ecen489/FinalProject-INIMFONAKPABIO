package com.example.inimfonakpabio.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
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

public class TabsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    FirebaseDatabase firebase_db;
    DatabaseReference db_ref;
    FirebaseAuth firebase_auth;
    FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        firebase_db = FirebaseDatabase.getInstance();
        db_ref = firebase_db.getReference();
        firebase_auth = FirebaseAuth.getInstance();

        user = firebase_auth.getCurrentUser();
        if (user == null) {
            Intent intent = getIntent();
            final String uname = intent.getStringExtra("email");
            final String pswd = intent.getStringExtra("pswd");

            try {
                firebase_auth.signInWithEmailAndPassword(uname, pswd)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //we good
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error. Unable to maintain user", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            } catch (Exception e) {
                Log.d("NINI", "Signin error");
            }
        }

        DatabaseReference exRef = db_ref.child("fitnessbuddy/users/");
        Query query = exRef.orderByChild("email").equalTo(user.getEmail());
        exRef.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                Toast.makeText(getApplicationContext(),"listening",Toast.LENGTH_SHORT).show();

                if(dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(getApplicationContext(),"No previous",Toast.LENGTH_SHORT).show();
                    DatabaseReference ddbb = db_ref.child("fitnessbuddy/users/").push();
                    users dummy = new users(0, 0, user.getEmail(), "New User");
                    ddbb.setValue(dummy);
                }

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("NINI", "Database error");
        }
    };

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    TabExercises tb_ex = new TabExercises();
                    return tb_ex;

                case 1:
                    TabWorkouts tb_wk = new TabWorkouts();
                    return tb_wk;

                case 2:
                    TabNutrition tb_nt = new TabNutrition();
                    return tb_nt;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
