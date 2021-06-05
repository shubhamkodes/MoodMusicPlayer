package com.itsthetom.moodmusicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;

import com.itsthetom.moodmusicplayer.NoticationActionReceiver;
import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.databinding.ActivityMusicPlayerBinding;
import com.itsthetom.moodmusicplayer.fragments.SearchFragment;
import com.itsthetom.moodmusicplayer.listeners.MusicUpdateListener;
import com.itsthetom.moodmusicplayer.model.Music;
import com.itsthetom.moodmusicplayer.singleton.PlayerHandler;

import java.util.ArrayList;
import java.util.Random;

import static com.itsthetom.moodmusicplayer.singleton.Repo.musicList;

public class MusicPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener , MusicUpdateListener {
    public static final String EXTRA_MUSIC_POS="music_pos",FROM_TYPE="fromIntent",
            FROM_HOME="homeMusicList",FROM_SEARCH="searchMusicList",
            FROM_ARTIST="libraryArtistList", FROM_ALBUM="libraryAlbumList",
            FROM_RECENT="recentMusicList",FROM_LIBRARY_ARTIST = "fromLibraryArtist",
            FROM_LIBRARY_ALBUM="fromLibraryAlbum",FROM_NOTIFICATION="fromNotification";


    public static ArrayList<Music> musicPlayerList;
    ActivityMusicPlayerBinding binding;
    Music music;
    Handler handler;
    String musicListType;
    SharedPreferences sharedPreferences;

    private boolean isRepeatOn,isShuffleOn;
    public static int musicCurrentPos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMusicPlayerBinding.inflate(getLayoutInflater());
        setFullScreen();
        setContentView(binding.getRoot());

        music=null;
        handler=new Handler();
        musicListType=getIntent().getExtras().getString(FROM_TYPE);
        musicCurrentPos=getIntent().getExtras().getInt(EXTRA_MUSIC_POS);
        sharedPreferences=getSharedPreferences("preferences",MODE_PRIVATE);
        isRepeatOn=sharedPreferences.getBoolean("isRepeatOn",false);
        isShuffleOn=sharedPreferences.getBoolean("isShuffleOn",false);
        PlayerHandler.getInstance().toggleRepeat(isRepeatOn);
        PlayerHandler.getInstance().toggleShuffle(isShuffleOn);

        setMusic();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }



    private void setMusic(){
        switch (musicListType){
            case FROM_HOME:
                musicPlayerList=musicList;
                break;
            case FROM_SEARCH:
                musicPlayerList= SearchFragment.searchArrayList;
                break;
            case FROM_RECENT:
                musicPlayerList=SearchFragment.recentsList;
                break;
            case FROM_ALBUM:
            case FROM_ARTIST:
                musicPlayerList=ArtistAlbumActivity.artistAlbumList;
            break;
            case FROM_NOTIFICATION:
                musicPlayerList=PlayerHandler.getInstance().getCurrentList();
                musicCurrentPos=PlayerHandler.getInstance().getMusicCurrentPos();
            break;
        }

            if(musicCurrentPos!=-1  && musicCurrentPos<musicPlayerList.size()){
                music=musicPlayerList.get(musicCurrentPos);
                PlayerHandler.getInstance().setUpMusicListAndPlay(musicPlayerList,musicCurrentPos,this,this);
                if (!musicListType.equals(FROM_NOTIFICATION) ) {
                    PlayerHandler.getInstance().startMusic(music, this);
                    PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
                }
                binding.btnPlayPause.setImageResource(R.drawable.ic_pause);

            }
        initView();

    }

    private void initView(){
        binding.btnClose.setOnClickListener((view)->{
            super.onBackPressed();
        });

        binding.btnNext.setOnClickListener((view)->{
            playNext();
        });

        binding.btnPrev.setOnClickListener((view)->{
           playPrev();
        });

        if(isShuffleOn)
            binding.btnShuffle.setImageResource(R.drawable.ic_shuffle_on);
        else binding.btnShuffle.setImageResource(R.drawable.ic_shuffle_off);

        binding.btnShuffle.setOnClickListener((view)->{
            if(isShuffleOn){
                isShuffleOn=false;
                sharedPreferences.edit().putBoolean("isShuffleOn",false).apply();
                binding.btnShuffle.setImageResource(R.drawable.ic_shuffle_off);
            }else{
                isShuffleOn=true;
                sharedPreferences.edit().putBoolean("isShuffleOn",true).apply();
                binding.btnShuffle.setImageResource(R.drawable.ic_shuffle_on);
            }
            PlayerHandler.getInstance().toggleShuffle(isShuffleOn);
        });

        if(isRepeatOn)
            binding.btnRepeat.setImageResource(R.drawable.ic_repeat_on);
        else binding.btnRepeat.setImageResource(R.drawable.ic_repeat_off);

        binding.btnRepeat.setOnClickListener((view)->{
            if(isRepeatOn){
                isRepeatOn=false;
                sharedPreferences.edit().putBoolean("isRepeatOn",false).apply();
                binding.btnRepeat.setImageResource(R.drawable.ic_repeat_off);
            }else{
                isRepeatOn=true;
                sharedPreferences.edit().putBoolean("isRepeatOn",true).apply();
                binding.btnRepeat.setImageResource(R.drawable.ic_repeat_on);
            }
            PlayerHandler.getInstance().toggleRepeat(isRepeatOn);
        });


    }

    private void playNext(){
        musicCurrentPos=PlayerHandler.getInstance().playNextPrevMusic(PlayerHandler.NEXT,this);
        music=musicPlayerList.get(musicCurrentPos);
        PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause);

    }

    private void playPrev(){

        musicCurrentPos=PlayerHandler.getInstance().playNextPrevMusic(PlayerHandler.PREV,this);
        music=musicPlayerList.get(musicCurrentPos);
        PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause);

    }





    public void updateView(Music music,int pos) {
        this.music=music;
        musicCurrentPos=pos;
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause);

        binding.btnPlayPause.setOnClickListener((view)->{
            if(PlayerHandler.getInstance().isPlaying()) {
                PlayerHandler.getInstance().playAndPause(this);
                binding.btnPlayPause.setImageResource(R.drawable.ic_play);
            }else{
                PlayerHandler.getInstance().playAndPause(this);
                binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
            }
        });

        binding.musicSeekBar.setMax(PlayerHandler.getInstance().getDuration());



        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(PlayerHandler.getInstance().getMediaPlayer()!=null && PlayerHandler.getInstance().isPlaying()){
                    int mCurrenntPos=PlayerHandler.getInstance().getMediaPlayer().getCurrentPosition();
                    binding.musicSeekBar.setProgress(mCurrenntPos);
                    binding.tvpStartTime.setText(formattedTime(mCurrenntPos/1000));
                }
                handler.postDelayed(this,1000);
            }
        });

        binding.musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayerHandler.getInstance().getMediaPlayer().seekTo(seekBar.getProgress());
            }
        });



      MusicPlayerActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              if(music!=null){
                  if (music.getImage()!=null){
                      Glide.with(getApplicationContext()).asBitmap()
                              .load(music.getImage())
                              .into(binding.musicImage);
                  }else
                      binding.musicImage.setImageResource(R.drawable.ic_music);

                  binding.tvpMusicTitle.setText(music.getTitle());
                  binding.tvpMusicArtist.setText(music.getArtist());
                  binding.tvpDurationTime.setText(formattedTime(PlayerHandler.getInstance().getDuration()/1000));
              }
          }
      });

    }

    public void iconStop(){
        binding.btnPlayPause.setImageResource(R.drawable.ic_play);
    }
    public void iconPlay(){
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
    }

    public String formattedTime(int currentPos){
        String totatOut="",totalNew="",seconds=String.valueOf(currentPos%60),minutes=String.valueOf(currentPos/60);
        totatOut=minutes+":"+seconds;
        totalNew=minutes+":"+"0"+seconds;
        if(seconds.length()==1){
            return totalNew;
        }
        return totatOut;

    }

    public int getRandom(int size){
        return new Random().nextInt(size);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        binding.btnPlayPause.setImageResource(R.drawable.ic_play);
        playNext();
    }



}