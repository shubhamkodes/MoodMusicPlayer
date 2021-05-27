package com.itsthetom.moodmusicplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itsthetom.moodmusicplayer.activities.MusicPlayerActivity;
import com.itsthetom.moodmusicplayer.adapter.MusicListAdapter;
import com.itsthetom.moodmusicplayer.databinding.FragmentHomeBinding;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.singleton.Repo;

public class HomeFragment extends Fragment {


    AdapterListener listener;
    FragmentHomeBinding binding;
    public static MusicListAdapter adapter;
    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater);

        listener=(AdapterListener)getContext();

        adapter=new MusicListAdapter(getContext(),listener, MusicPlayerActivity.FROM_HOME);
        binding.rvMusicList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMusicList.setAdapter(adapter);

        Repo.getInstance(getContext()).isMetadataAvailable().observe(getViewLifecycleOwner(),(check)->{
            if(check==1)
                adapter.notifyDataSetChanged();
        });

        Repo.getInstance(getContext()).isListAvailable().observe(getViewLifecycleOwner(),(check)->{
            if (check==1)
                adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }
}