package com.example.inimfonakpabio.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText unameEditText, upswdEditText;
    FirebaseDatabase firebase_db;
    DatabaseReference db_ref;
    FirebaseAuth firebase_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase_db = FirebaseDatabase.getInstance();
        db_ref = firebase_db.getReference();

        unameEditText = (EditText) findViewById(R.id.unameEdit);
        upswdEditText = (EditText) findViewById(R.id.pswdEdit);

        firebase_auth = FirebaseAuth.getInstance();
    }

    public void callLogin(View view) {
        //Log in user and switch to pull
        final String uname = unameEditText.getText().toString().trim();
        final String pswd = upswdEditText.getText().toString();

        try {
            firebase_auth.signInWithEmailAndPassword(uname, pswd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login sucessful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
                                intent.putExtra("email", uname);
                                intent.putExtra("pswd", pswd);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Not logged in", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Log.d("NINI", "Signin error");
        }
    }

    public void callCreateAccount(View view) {
        //Add user to firebase
        final String uname = unameEditText.getText().toString().trim();
        final String pswd = upswdEditText.getText().toString();

        if (uname.equals("") || pswd.equals("")) {
            Toast.makeText(getApplicationContext(),"Enter credentials", Toast.LENGTH_SHORT).show();
        } else {
            try {
                firebase_auth.createUserWithEmailAndPassword(uname, pswd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "New user added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
                                    intent.putExtra("email", uname);
                                    intent.putExtra("pswd", pswd);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Unable to add user", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (Exception e) {
                Log.d("NINI", "Create user error");
            }
        }

    }
}
