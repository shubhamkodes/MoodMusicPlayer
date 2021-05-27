package com.itsthetom.moodmusicplayer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.itsthetom.moodmusicplayer.fragments.AlbumFragment;
import com.itsthetom.moodmusicplayer.fragments.ArtistFragment;

import org.jetbrains.annotations.NotNull;

public class LibraryPagerAdapter extends FragmentStateAdapter {

    public LibraryPagerAdapter(Fragment fragment){
        super(fragment);
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new ArtistFragment();
            case 1: return new AlbumFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
