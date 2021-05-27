package com.itsthetom.moodmusicplayer.listeners;

import com.itsthetom.moodmusicplayer.model.Music;

public interface AdapterListener {
    public void startPlayerActivity(int musicPos,String intentType);
    public void startArtistAlbumActivity(String artistListPos,String intentType);
}
