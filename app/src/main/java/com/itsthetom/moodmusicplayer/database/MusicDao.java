package com.itsthetom.moodmusicplayer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsthetom.moodmusicplayer.model.Music;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMusic(Music music);

    @Query("Select * from RecentsMusicStore")
    public LiveData<List<Music>> getRecentsMusic();


}
