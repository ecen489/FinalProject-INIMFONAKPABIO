package com.example.inimfonakpabio.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String FITNESSBUDDY_CHANNEL_ID = "com.example.inimfonakpabio.finalproject.FITNESSBUDDY";
    public static final String FITNESSBUDDY_CHANNEL_NAME = "FITNESSBUDDY CHANNEL";

    public NotificationUtils(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create FITNESSBUDDY channel
            NotificationChannel androidChannel = new NotificationChannel(FITNESSBUDDY_CHANNEL_ID,
                    FITNESSBUDDY_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(androidChannel);
        }
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder CreateNotification(int recommended, int curDaily) {
        String text = "";
        if (recommended < curDaily) {
            text = "You have consumed " + curDaily + "calories. You can stop now.";
        } else {
            text = "You have consumed " + curDaily + "calories. Keep going!!";
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, FITNESSBUDDY_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Fitness Buddy")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder;
    }


}
