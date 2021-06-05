package com.itsthetom.moodmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.itsthetom.moodmusicplayer.activities.MusicPlayerActivity;
import com.itsthetom.moodmusicplayer.singleton.PlayerHandler;

public class NoticationActionReceiver extends BroadcastReceiver implements MediaPlayer.OnCompletionListener {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Log.d("NOTIF", " in notification action receiver");
        String action=intent.getAction();
        Log.d("NOTIF",action+" in notification action receiver");
        switch (action) {
            case PlayerHandler.NEXT:
                PlayerHandler.getInstance().playNextPrevMusic(PlayerHandler.NEXT, context);
                PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
                Log.d("NOTIF", "next clicked");
                break;
            case PlayerHandler.PREV:
                PlayerHandler.getInstance().playNextPrevMusic(PlayerHandler.PREV, context);
                PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
                Log.d("NOTIF", "Prev clicked");
                break;
            case PlayerHandler.PLAY_PAUSE:
                PlayerHandler.getInstance().playAndPause(context);
                Log.d("NOTIF", "playAndPause clicked");
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
            PlayerHandler.getInstance().playNextPrevMusic(PlayerHandler.NEXT,context);
            PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
    }



}