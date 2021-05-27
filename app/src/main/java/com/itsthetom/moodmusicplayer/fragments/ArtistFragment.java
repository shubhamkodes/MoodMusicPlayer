package com.itsthetom.moodmusicplayer.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.activities.MusicPlayerActivity;
import com.itsthetom.moodmusicplayer.adapter.MusicListAdapter;
import com.itsthetom.moodmusicplayer.databinding.FragmentArtistBinding;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.singleton.Repo;

public class ArtistFragment extends Fragment {
    AdapterListener listener;
    FragmentArtistBinding binding;
    MusicListAdapter artistRvAdapter;
    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentArtistBinding.inflate(inflater);
        listener=(AdapterListener) getContext();
        artistRvAdapter=new MusicListAdapter(getContext(),listener, MusicPlayerActivity.FROM_LIBRARY_ARTIST);

        binding.rvArtist.setAdapter(artistRvAdapter);
        binding.rvArtist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        Repo.getInstance(getContext()).isMetadataAvailable().observe(getViewLifecycleOwner(),(i)->{
            artistRvAdapter.notifyDataSetChanged();
        });
        return binding.getRoot();
    }
}