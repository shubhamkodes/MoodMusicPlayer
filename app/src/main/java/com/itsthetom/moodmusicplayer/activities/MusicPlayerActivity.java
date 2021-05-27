package com.itsthetom.moodmusicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.databinding.ActivityMusicPlayerBinding;
import com.itsthetom.moodmusicplayer.fragments.SearchFragment;
import com.itsthetom.moodmusicplayer.model.Music;
import com.itsthetom.moodmusicplayer.singleton.PlayerHandler;

import java.util.ArrayList;
import java.util.Random;

import static com.itsthetom.moodmusicplayer.singleton.Repo.musicList;

public class MusicPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    public static final String EXTRA_MUSIC_POS="music_pos",FROM_TYPE="fromIntent",
            FROM_HOME="homeMusicList",FROM_SEARCH="searchMusicList",
            FROM_ARTIST="libraryArtistList", FROM_ALBUM="libraryAlbumList",
            FROM_RECENT="recentMusicList",FROM_LIBRARY_ARTIST = "fromLibraryArtist",
            FROM_LIBRARY_ALBUM="fromLibraryAlbum";
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
        setContentView(binding.getRoot());

        music=null;
        handler=new Handler();
        musicListType=getIntent().getExtras().getString(FROM_TYPE);
        musicCurrentPos=getIntent().getExtras().getInt(EXTRA_MUSIC_POS);
        sharedPreferences=getSharedPreferences("preferences",MODE_PRIVATE);
        isRepeatOn=sharedPreferences.getBoolean("isRepeatOn",false);
        isShuffleOn=sharedPreferences.getBoolean("isShuffleOn",false);

        setMusic();
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
            case FROM_ALBUM:
            case FROM_ARTIST:
                musicPlayerList=ArtistAlbumActivity.artistAlbumList;
            break;
        }
        if(musicCurrentPos!=-1  && musicCurrentPos<musicPlayerList.size()){
            music=musicPlayerList.get(musicCurrentPos);
            playMusic();
            initView();
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
        }
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
        });

        updateView();
    }

    private void playNext(){
        if(isShuffleOn && !isRepeatOn){
            musicCurrentPos=getRandom(musicPlayerList.size());
        }else if(!isRepeatOn){
            musicCurrentPos= (musicCurrentPos+1)%musicPlayerList.size();
        }

        if(musicCurrentPos<0 || musicCurrentPos>=musicPlayerList.size())
            musicCurrentPos=0;
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
        playMusic();
    }

    private void playPrev(){
        musicCurrentPos= (musicCurrentPos-1)%musicPlayerList.size();
        if(musicCurrentPos<0)
            musicCurrentPos=musicPlayerList.size()-1;
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause);
        playMusic();
    }

    private void playMusic(){

        music=musicPlayerList.get(musicCurrentPos);
        PlayerHandler.getInstance().playNext(music,this );
        PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
        updateView();
    }

    private void updateView() {
        binding.btnPlayPause.setOnClickListener((view)->{
            if(PlayerHandler.getInstance().isPlaying()) {
                PlayerHandler.getInstance().playAndPause();
                binding.btnPlayPause.setImageResource(R.drawable.ic_play);
            }else{
                PlayerHandler.getInstance().playAndPause();
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