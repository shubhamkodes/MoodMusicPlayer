package com.itsthetom.moodmusicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.database.MusicRoomDB;
import com.itsthetom.moodmusicplayer.databinding.ActivityMainBinding;
import com.itsthetom.moodmusicplayer.fragments.SearchFragment;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.singleton.PlayerHandler;
import com.itsthetom.moodmusicplayer.singleton.Repo;

public class MainActivity extends AppCompatActivity implements AdapterListener {

    ActivityMainBinding binding;
    Repo repo;
    PlayerHandler playerHandler;
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
}