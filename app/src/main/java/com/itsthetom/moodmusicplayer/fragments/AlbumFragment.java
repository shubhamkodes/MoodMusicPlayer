package com.itsthetom.moodmusicplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.activities.MusicPlayerActivity;
import com.itsthetom.moodmusicplayer.adapter.MusicListAdapter;
import com.itsthetom.moodmusicplayer.databinding.FragmentAlbumBinding;
import com.itsthetom.moodmusicplayer.databinding.FragmentArtistBinding;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.singleton.Repo;

public class AlbumFragment extends Fragment {
    FragmentAlbumBinding binding;
    AdapterListener listener;
    MusicListAdapter albumRvAdapter;
    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentAlbumBinding.inflate(inflater);
        listener=(AdapterListener) getContext();
        albumRvAdapter=new MusicListAdapter(getContext(),listener, MusicPlayerActivity.FROM_LIBRARY_ALBUM);

        binding.rvAlbum.setAdapter(albumRvAdapter);
        binding.rvAlbum.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        Repo.getInstance(getContext()).isMetadataAvailable().observe(getViewLifecycleOwner(),(i)->{
            albumRvAdapter.notifyDataSetChanged();
        });
        return binding.getRoot();
    }
}