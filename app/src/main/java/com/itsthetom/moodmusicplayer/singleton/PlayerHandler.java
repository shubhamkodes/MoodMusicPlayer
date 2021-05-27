package com.itsthetom.moodmusicplayer.singleton;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.itsthetom.moodmusicplayer.model.Music;


public class PlayerHandler {
    private static PlayerHandler instance;
    private MediaPlayer mediaPlayer;
    Music music;

    private PlayerHandler(){
        music=null;
    }

    public static synchronized PlayerHandler getInstance(){
        if(instance==null)
        {
            instance=new PlayerHandler();
        }
        return instance;
    }

    public void startMusic(Music music, Context context ) {
           if (mediaPlayer!=null){
              if (mediaPlayer.isPlaying())
                   mediaPlayer.stop();
               mediaPlayer.reset();
               mediaPlayer.release();
               mediaPlayer=null;
           }
            this.music=music;
            mediaPlayer = MediaPlayer.create(context, Uri.parse(music.getData()));
            mediaPlayer.start();

    }

    public void playAndPause(){
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();

            }else{
                mediaPlayer.start();
            }
        }
        else
            Log.d("PLAY","media player is null");
    }

    public void playNext(Music music,Context context ){
            startMusic(music, context );
    }


    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
