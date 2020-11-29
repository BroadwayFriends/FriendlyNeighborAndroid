package me.twodee.friendlyneighbor.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import me.twodee.friendlyneighbor.App;
import me.twodee.friendlyneighbor.BaseActivity;
import me.twodee.friendlyneighbor.R;
import me.twodee.friendlyneighbor.TransactionOnGoingAcivity;

import static me.twodee.friendlyneighbor.App.CHANNEL_2_ID;

public class TransactionService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // catch the input and create the notification
        String input = intent.getStringExtra("name");

        // Intent to start our activity, when notification is clicked
        Intent notificationIntent = new Intent(this, BaseActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // To set this intent in the notification, e have to create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        // Create notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setContentText("Example Service")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground((int) System.currentTimeMillis(), notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
