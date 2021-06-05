package com.itsthetom.moodmusicplayer.singleton;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.itsthetom.moodmusicplayer.ApplicationClass;
import com.itsthetom.moodmusicplayer.NoticationActionReceiver;
import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.activities.MusicPlayerActivity;
import com.itsthetom.moodmusicplayer.listeners.MusicUpdateListener;
import com.itsthetom.moodmusicplayer.model.Music;

import java.util.ArrayList;
import java.util.Random;


public class PlayerHandler {
    public static final String NEXT="NEXT_MUSIC",PREV="PREV_MUSIC",PLAY_PAUSE = "PLAY_PAUSE";
    private static PlayerHandler instance;
    private MediaPlayer mediaPlayer;
    private int musicCurrentPos;
    private ArrayList<Music> playerHandlerMusicList;
    Music music;
    MediaSessionCompat mediaSessionCompat;
    MusicUpdateListener listener;
    private  boolean isShuffle=false, isRepeat=false;
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

    public void setUpMusicListAndPlay(ArrayList<Music> list,int pos,Context context,MusicUpdateListener listener){
        playerHandlerMusicList=list;
        musicCurrentPos=pos;
        this.listener=listener;
       if(PlayerHandler.getInstance().isPlaying() && list.equals(playerHandlerMusicList)
               && list.get(pos).getData().equals(music.getData())){
           listener.updateView(music,musicCurrentPos);
       } else {
            music = playerHandlerMusicList.get(musicCurrentPos);
        }


    }

    public void startMusic(Music music, Context context ) {
           if (mediaPlayer!=null){
              if (mediaPlayer.isPlaying())
                   mediaPlayer.stop();
               mediaPlayer.reset();
               mediaPlayer.release();
               mediaPlayer=null;
           }
           if(music !=null && !music.getData().equals("")) {
               this.music = music;
               mediaPlayer = MediaPlayer.create(context, Uri.parse(music.getData()));
               mediaPlayer.setOnPreparedListener(mp -> {
                   mp.start();
                   listener.updateView(music,musicCurrentPos);
               });

           }
        mediaSessionCompat=new MediaSessionCompat(context,"myMusic");
        showNotification(R.drawable.ic_pause,context);


    }
    public void toggleShuffle(boolean toggle){
        isShuffle=toggle;
    }

    public void toggleRepeat(boolean toggle){
        isRepeat=toggle;
    }
    public int playNextPrevMusic(String check,Context context){
        if(isShuffle && !isRepeat){
                 musicCurrentPos=getRandom(playerHandlerMusicList.size());

        }else if(!isRepeat)
            {
                switch (check) {
                    case NEXT:
                        musicCurrentPos = (musicCurrentPos + 1) % playerHandlerMusicList.size();
                        break;
                    case PREV:
                        musicCurrentPos = (musicCurrentPos - 1) % playerHandlerMusicList.size();
                }
            }

       music = playerHandlerMusicList.get(musicCurrentPos);
       startMusic(music,context);

       return musicCurrentPos;
    }


    public void playAndPause(Context context){
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                showNotification(R.drawable.ic_play,context);
                listener.iconStop();


            }else{
                mediaPlayer.start();
                listener.iconPlay();
                showNotification(R.drawable.ic_pause,context);
            }
        }
        else
            Log.d("PLAY","media player is null");
    }

    public boolean isPlaying() {
        if(mediaPlayer==null){
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    public int getRandom(int size){
        return new Random().nextInt(size);
    }

    public ArrayList<Music> getCurrentList(){
        return playerHandlerMusicList;
    }
    public int getMusicCurrentPos(){
        return musicCurrentPos;
    }

    public Music getCurrentMusic(){
        return music;
    }

   void showNotification(int playPauseBtn,Context context){
        Intent intent=new Intent(context, MusicPlayerActivity.class);
        intent.putExtra(MusicPlayerActivity.FROM_TYPE,MusicPlayerActivity.FROM_NOTIFICATION);
        PendingIntent contentPI=PendingIntent.getActivity(context,69,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent prevIntent=new Intent(context, NoticationActionReceiver.class);
        prevIntent.setAction(PlayerHandler.PREV);
        PendingIntent prevPI=PendingIntent.getBroadcast(context,69,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent=new Intent(context,NoticationActionReceiver.class);
        nextIntent.setAction(PlayerHandler.NEXT);
        PendingIntent nextPI=PendingIntent.getBroadcast(context,69,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playPauseIntent=new Intent(context,NoticationActionReceiver.class);
        playPauseIntent.setAction(PlayerHandler.PLAY_PAUSE);
        PendingIntent playPausePI=PendingIntent.getBroadcast(context,69,playPauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap picture= BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_music);
        if(music.getImage()!=null){
            picture=BitmapFactory.decodeByteArray(music.getImage(),0,music.getImage().length);
        }

        Notification notification=new androidx.core.app.NotificationCompat.Builder(context,ApplicationClass.Channel_ID1)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(picture)
                .setContentTitle(music.getTitle())
                .setContentText(music.getArtist())
                .addAction(R.drawable.ic_previous,"Previous",prevPI)
                .addAction(playPauseBtn,"PlayPause",playPausePI)
                .addAction(R.drawable.ic_next,"Next",nextPI)
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                )
                .setContentIntent(contentPI)
                .build();


        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(89,notification);


    }

    public boolean equals(Music tempMusic){
        if (music != null) return false;
        assert music != null;
        return music.getData().equals(tempMusic.getData());
    }

}
