package com.itsthetom.moodmusicplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {
    public static final String Channel_ID1="channel-1",
                               Channel_ID2="channel-2",
                                Action_Prev="playPreviMusic",
                                Action_Next="playNextMusic",
                                Action_PlayPause="playPauseMusic";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificatinChannel();
    }

    private void createNotificatinChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(Channel_ID1,"Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Description of channel 1");

            NotificationChannel channel2=new NotificationChannel(Channel_ID2,"Channel 2",NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Description of channel 2");

            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }
}
