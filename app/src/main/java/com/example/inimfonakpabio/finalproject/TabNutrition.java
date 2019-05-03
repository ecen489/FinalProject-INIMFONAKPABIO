package com.example.inimfonakpabio.finalproject;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TabNutrition extends Fragment {

    static final int REQCODE = 1;

    EditText textCalories, textRecommended, textDailyTotal;
    ImageView imgDisplay;
    Button bSnap;
    CircularImageView Bulk, slim;

    FirebaseDatabase firebase_db;
    DatabaseReference db_ref;
    FirebaseAuth firebase_auth;
    FirebaseUser user = null;

    public int curDaily = 0;
    public int calorie = 0;
    public int recommended = 2000;

    private NotificationUtils mNotificationUtils;

    public TabNutrition() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        textCalories = (EditText) view.findViewById(R.id.textCalories);
        textRecommended = (EditText) view.findViewById(R.id.textRecommended);
        imgDisplay = (ImageView) view.findViewById(R.id.imgDisplay);
        textDailyTotal = (EditText) view.findViewById(R.id.textDailyTotal);
        bSnap = (Button) view.findViewById(R.id.bSnap);
        Bulk = (CircularImageView) view.findViewById(R.id.Bulk);
        slim = (CircularImageView) view.findViewById(R.id.slim);


        firebase_db = FirebaseDatabase.getInstance();
        db_ref = firebase_db.getReference();
        firebase_auth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        user = firebase_auth.getCurrentUser();

        mNotificationUtils = new NotificationUtils(getActivity());

        Bulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommended = 3500;
                textRecommended.setText("" + recommended);
            }
        });

        slim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommended = 2000;
                textRecommended.setText("" + recommended);
            }
        });

        bSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQCODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap img = (Bitmap) data.getExtras().get("data");
        imgDisplay.setImageBitmap(img);
        TextRecognition( img );
    }

    @Override
    public void onStart() {
        super.onStart();

        user = firebase_auth.getCurrentUser();
    }

    void TextRecognition(Bitmap selectImg) {
        FirebaseVisionImage im = FirebaseVisionImage.fromBitmap(selectImg);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        bSnap.setEnabled(false);
        recognizer.processImage(im)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                bSnap.setEnabled(true);
                                ProcessTexts(firebaseVisionText);
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bSnap.setEnabled(true);
                                e.printStackTrace();
                            }
                        }
                );
    }

    public void ProcessTexts(FirebaseVisionText text) {
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(getActivity(), "No text found", Toast.LENGTH_SHORT).show();
            return;
        }

        String result = "";
        for (int i = 0; i < blocks.size(); i++) {
            Log.d("NINI", blocks.get(i).getText());
            result = result + (blocks.get(i).getText());
        }

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(result);
        String cal = "0";
        while(m.find()) {
            String temp = m.group();
            if(temp.length() >= cal.length()) {
                cal = temp;
            }
            Log.d("NINI", "m.group returns: " + m.group());
        }

        textCalories.setText(cal);
        UpdateDaily(cal);
    }

    void UpdateDaily(String cal) {
        calorie = Integer.parseInt(cal);
        DatabaseReference exRef = db_ref.child("fitnessbuddy/users/");
        Query query = exRef.orderByChild("email").equalTo(user.getEmail());
        exRef.addListenerForSingleValueEvent(valueEventListener);

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                Toast.makeText(getActivity(),"listening",Toast.LENGTH_SHORT).show();

                users us1 = new users(10, 101, "nini16@tamu.com", "Inimfon Akpabio");
                String userKey = "";

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        curDaily = snapshot.child("curDaily").getValue(Integer.class);
                        us1 = snapshot.getValue(users.class);
                        userKey = snapshot.getKey();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                curDaily += calorie;
                us1.curDaily = curDaily;
                db_ref.child("fitnessbuddy/users/").child(userKey).child("curDaily").setValue(curDaily);

                textDailyTotal.setText("" + curDaily);
                doNotify();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("NINI", "Database error");
        }
    };

    public void doNotify() {
        NotificationCompat.Builder nb = mNotificationUtils.
                CreateNotification(recommended, curDaily);

        mNotificationUtils.getManager().notify(101, nb.build());
    }

}
