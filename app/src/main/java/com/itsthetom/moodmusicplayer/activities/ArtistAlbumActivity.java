package com.itsthetom.moodmusicplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.itsthetom.moodmusicplayer.adapter.MusicListAdapter;
import com.itsthetom.moodmusicplayer.database.MusicRoomDB;
import com.itsthetom.moodmusicplayer.databinding.ActivityArtistAlbumBinding;
import com.itsthetom.moodmusicplayer.fragments.SearchFragment;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.model.Music;
import com.itsthetom.moodmusicplayer.singleton.Repo;

import java.util.ArrayList;

public class ArtistAlbumActivity extends AppCompatActivity implements AdapterListener{
    ActivityArtistAlbumBinding binding;
    public static final String EXTRA_ARTISTALBUM_TITLE="artistAlbumPos";
    String title,fromType;
    public static ArrayList<Music> artistAlbumList;
    MusicListAdapter artistAlbumAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityArtistAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title=getIntent().getExtras().getString(EXTRA_ARTISTALBUM_TITLE);
        fromType=getIntent().getExtras().getString(MusicPlayerActivity.FROM_TYPE);

        byte[] img=null;
        if(fromType.equals(MusicPlayerActivity.FROM_LIBRARY_ARTIST)) {
            artistAlbumList = Repo.artistList.getOrDefault(title, new ArrayList<>());
            if(artistAlbumList.size()!=0)
                img=artistAlbumList.get(0).getImage();
            artistAlbumAdapter=new MusicListAdapter(this,this,MusicPlayerActivity.FROM_ARTIST);
        }
        else if(fromType.equals(MusicPlayerActivity.FROM_LIBRARY_ALBUM)) {
            artistAlbumList = Repo.albumList.getOrDefault(title, new ArrayList<>());
            Log.d("ALBUM",artistAlbumList.size()+" In ALBUM");
            if(artistAlbumList.size()!=0)
                img=artistAlbumList.get(0).getImage();
            artistAlbumAdapter=new MusicListAdapter(this,this,MusicPlayerActivity.FROM_ALBUM);
        }else{
            Log.d("TAG", title+" in artistalbum activity");
        }

        if(img!=null)
            Glide.with(this).asBitmap()
                    .load(img)
                    .into(binding.coverArt);
        binding.artistAlbumTitle.setText(title);
        binding.rvArtistAlbum.setAdapter(artistAlbumAdapter);
        binding.rvArtistAlbum.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void startPlayerActivity(int musicPos, String intentType) {
        Intent intent=new Intent(this,MusicPlayerActivity.class);
        intent.putExtra(MusicPlayerActivity.EXTRA_MUSIC_POS,musicPos);
        intent.putExtra(MusicPlayerActivity.FROM_TYPE,intentType);

        startActivity(intent);
    }

    @Override
    public void startArtistAlbumActivity(String artistListPos, String intentType) {

    }
}