package com.example.inimfonakpabio.finalproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    //handle images
    ArrayList<Integer> img_handler = new ArrayList<>();

    TextView tvExNm, tvExMs, tvExEq, tvExIs;
    ImageView imEx;
    private VideoView mVideoView;
    MediaController controller;
    Exercise curEx = null;
    int videoRes = 0;

    boolean isVideo = false;

    //state budle tag
    private static final String PLAYBACK_TIME = "play_time";

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populate_images();

        curEx = (Exercise) getIntent().getParcelableExtra("CURRENT_EXERCISE");

        if(curEx != null) {
            setContentView(R.layout.activity_guide);

            tvExNm = (TextView) findViewById(R.id.tvExNm);
            tvExMs = (TextView) findViewById(R.id.tvExMs);
            tvExEq = (TextView) findViewById(R.id.tvExEq);
            tvExIs = (TextView) findViewById(R.id.tvExIs);
            imEx = (ImageView) findViewById(R.id.imEx);

            tvExNm.setText(curEx.exerciseName);
            tvExMs.setText(curEx.muscleGroups);
            tvExEq.setText(curEx.equipment);
            tvExIs.setText(curEx.instructions);
            imEx.setImageResource(img_handler.get(curEx.imageRes));

        } else {
            isVideo = true;
            setContentView(R.layout.activity_guidevideo);

            videoRes = getIntent().getIntExtra("VIDEORES", R.raw.vid3);
            mVideoView = findViewById(R.id.videoview);

            if (savedInstanceState != null) {
                mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
            }

            controller = new MediaController(this);
            controller.setMediaPlayer(mVideoView);
            mVideoView.setMediaController(controller);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

       if (isVideo) {
           // Load the media each time onStart() is called.
           initializePlayer(); }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isVideo) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mVideoView.pause();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(isVideo) {
            // Media playback takes a lot of resources, so everything should be
            // stopped and released at this time.
            releasePlayer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(isVideo) {
            // Save the current playback position (in milliseconds) to the
            // instance state bundle.
            outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
        }
    }

    private void initializePlayer() {

        // Buffer and decode the video sample.
        Uri videoUri = getMedia(videoRes);
        mVideoView.setVideoURI(videoUri);

        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Restore saved position, if available.
                        if (mCurrentPosition > 0) {
                            mVideoView.seekTo(mCurrentPosition);
                        } else {
                            // Skipping to 1 shows the first frame of the video.
                            mVideoView.seekTo(1);
                        }

                        // Start playing!
                        mVideoView.start();
                    }
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        mVideoView.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(getApplicationContext(),
                                "Completed",
                                Toast.LENGTH_SHORT).show();

                        // Return the video position to the start.
                        releasePlayer(); //experiment, not sure if needed
                        finish();
                    }
                });
    }

    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    private Uri getMedia(int mediaName) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + mediaName);
    }

    public void populate_images() {
        img_handler.add(-1);
        img_handler.add(R.drawable.img_4);
        img_handler.add(R.drawable.img_6);
        img_handler.add(R.drawable.img_16);
        img_handler.add(R.drawable.img_7);
    }

}
