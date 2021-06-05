package com.itsthetom.moodmusicplayer.listeners;

import com.itsthetom.moodmusicplayer.model.Music;

public interface MusicUpdateListener {
    void updateView(Music music,int pos);
    void iconStop();
    void iconPlay();
}
