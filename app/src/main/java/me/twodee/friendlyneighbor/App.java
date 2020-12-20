package me.twodee.friendlyneighbor;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    private static App mInstance;

    public static final String CHANNEL_1_ID = "TestChannel";
    public static final String CHANNEL_2_ID = "OnGoingTransaction";
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        createNotificationChannels();
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "TestApp Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "TestApp Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel2.setDescription("This is Channel 2");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
