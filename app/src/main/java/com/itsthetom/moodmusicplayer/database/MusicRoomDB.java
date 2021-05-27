package com.itsthetom.moodmusicplayer.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;

import com.itsthetom.moodmusicplayer.model.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = {Music.class},version = 1,exportSchema = false)
public abstract class MusicRoomDB extends RoomDatabase {
    public abstract MusicDao musicDao();
    private static volatile MusicRoomDB instance;
    public static Executor executor= Executors.newFixedThreadPool(4);

    public MusicRoomDB(){

    }
    public static  MusicRoomDB getInstance(Context context) {
        if(instance==null){
            synchronized (MusicRoomDB.class){
                if(instance==null){
                    instance= Room.databaseBuilder(context,MusicRoomDB.class,"MusicRoomDatabase").build();
                }
            }
        }
        return instance;
    }

    public LiveData<List<Music>> getRecentMusics(){
         return musicDao().getRecentsMusic();
    }

    public void insertMusic(Music music){
        executor.execute(()->{
            musicDao().insertMusic(music);
        });
    }


}
