package com.itsthetom.moodmusicplayer.activities;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.database.MusicRoomDB;
import com.itsthetom.moodmusicplayer.databinding.ActivityMainBinding;
import com.itsthetom.moodmusicplayer.fragments.SearchFragment;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.listeners.MusicUpdateListener;
import com.itsthetom.moodmusicplayer.model.Music;
import com.itsthetom.moodmusicplayer.singleton.PlayerHandler;
import com.itsthetom.moodmusicplayer.singleton.Repo;

public class MainActivity extends AppCompatActivity implements AdapterListener, MusicUpdateListener, MediaPlayer.OnCompletionListener {

    ActivityMainBinding binding;
    Repo repo;
    PlayerHandler playerHandler;
    Music currentMusic;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        NavController navController=navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.btmNavView,navController);

        MusicRoomDB.getInstance(this);
        playerHandler=PlayerHandler.getInstance();

        repo= Repo.getInstance(this);

        initMiniPlayer();

    }

    @Override
    protected void onResume() {
        super.onResume();
       initMiniPlayer();
    }

    private void initMiniPlayer(){
        if (playerHandler.getMediaPlayer()==null) {
            binding.miniMusicPlayer.setVisibility(View.GONE);
        }else {
            binding.miniMusicPlayer.setVisibility(View.VISIBLE);
            PlayerHandler.getInstance().setUpMusicListAndPlay(PlayerHandler.getInstance().getCurrentList(),
                    PlayerHandler.getInstance().getMusicCurrentPos(),this,this );
            currentMusic=PlayerHandler.getInstance().getCurrentMusic();
            updateView(currentMusic,0);

            binding.miniNextBtn.setOnClickListener((view)->{
               playNext();
            });
            binding.miniPrevBtn.setOnClickListener((view)->{
               playPrev();
            });
            binding.miniPlayPauseBtn.setOnClickListener((view)->{
                PlayerHandler.getInstance().playAndPause(this);
            });

            if(playerHandler.getMediaPlayer().isPlaying()){
                binding.miniPlayPauseBtn.setImageResource(R.drawable.ic_pause);
            } else binding.miniPlayPauseBtn.setImageResource(R.drawable.ic_play);


            binding.miniMusicPlayer.setOnClickListener((view)->{
                Intent intent=new Intent(this, MusicPlayerActivity.class);
                intent.putExtra(MusicPlayerActivity.FROM_TYPE,MusicPlayerActivity.FROM_NOTIFICATION);
                startActivity(intent);
            });
        }
    }

    @Override
    public void startPlayerActivity(int musicPos, String intentType) {
        Intent intent=new Intent(this,MusicPlayerActivity.class);
        intent.putExtra(MusicPlayerActivity.EXTRA_MUSIC_POS,musicPos);
        intent.putExtra(MusicPlayerActivity.FROM_TYPE,intentType);

        if(intentType==MusicPlayerActivity.FROM_SEARCH)
            MusicRoomDB.getInstance(this)
                    .insertMusic(SearchFragment.searchArrayList.get(musicPos));

        startActivity(intent);
    }

    @Override
    public void startArtistAlbumActivity(String artistListTitle, String intentType) {
        Intent intent=new Intent(this,ArtistAlbumActivity.class);
        intent.putExtra(ArtistAlbumActivity.EXTRA_ARTISTALBUM_TITLE,artistListTitle);
        intent.putExtra(MusicPlayerActivity.FROM_TYPE,intentType);
        startActivity(intent);
    }



    @Override
    public void updateView(Music music, int pos) {
        binding.miniMusicPlayer.setVisibility(View.VISIBLE);
        if(music.getImage()!=null)
            Glide.with(this).asBitmap()
            .load(music.getImage())
            .into(binding.MiniMusicImage);
        else binding.MiniMusicImage.setImageResource(R.drawable.ic_music);

        binding.miniPlayPauseBtn.setImageResource(R.drawable.ic_pause);
        binding.MiniMusicName.setText(music.getTitle());
        binding.MiniMusicArtist.setText(music.getArtist());

        binding.miniMusicProgressBar.setMax(PlayerHandler.getInstance().getDuration());

        handler=new Handler();
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (PlayerHandler.getInstance().isPlaying()){
                    binding.miniMusicProgressBar.setProgressCompat(PlayerHandler.getInstance().getMediaPlayer().getCurrentPosition(),true);
                }
                handler.postDelayed(this,1000);
            }
        });
    }



    @Override
    public void iconStop() {
        binding.miniPlayPauseBtn.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void iconPlay() {
        binding.miniPlayPauseBtn.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    private void playNext(){
        PlayerHandler.getInstance().playNextPrevMusic(PlayerHandler.NEXT,this);
        PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
    }
    private void playPrev(){
        PlayerHandler.getInstance().playNextPrevMusic(PlayerHandler.PREV,this);
        PlayerHandler.getInstance().getMediaPlayer().setOnCompletionListener(this);
    }

}