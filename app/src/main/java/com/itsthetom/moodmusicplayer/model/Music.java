package com.itsthetom.moodmusicplayer.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "RecentsMusicStore")
public class Music {
    @NonNull
    @PrimaryKey
    private String id;
    private String title,data,artist,album;
    private byte[] image;

    public Music(){

    }

    public Music(@NonNull String id, String title, String data) {
        this.id = id;
        this.title = title;
        this.data = data;
    }

    public Music(String id, String title, String data, String artist, String album, byte[] image) {
        this.id = id;
        this.title = title;
        this.data = data;
        this.artist = artist;
        this.album = album;
        this.image = image;
    }

    public Music clone(Music music) {
         return new Music(music.getId(),music.getTitle(),music.getData(),music.getArtist(),music.getAlbum(),music.getImage());

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
