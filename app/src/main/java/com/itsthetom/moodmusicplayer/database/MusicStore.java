package com.itsthetom.moodmusicplayer.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.itsthetom.moodmusicplayer.model.Music;

@Entity(tableName = "RecentsMusicStore")
public class MusicStore {

    @PrimaryKey
    String id;
    Music music;
    long time;

    public MusicStore(Music music,long time) {
        this.id =  music.getId();
        this.music = music;
        this.time=time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
