package com.itsthetom.moodmusicplayer.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itsthetom.moodmusicplayer.activities.MusicPlayerActivity;
import com.itsthetom.moodmusicplayer.adapter.MusicListAdapter;
import com.itsthetom.moodmusicplayer.database.MusicRoomDB;
import com.itsthetom.moodmusicplayer.databinding.FragmentSearchBinding;
import com.itsthetom.moodmusicplayer.listeners.AdapterListener;
import com.itsthetom.moodmusicplayer.model.Music;

import java.util.ArrayList;
import java.util.Collections;

import static com.itsthetom.moodmusicplayer.singleton.Repo.musicList;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentSearchBinding binding;
    public static ArrayList<Music> searchArrayList;
    public static ArrayList<Music> recentsList;
    MusicListAdapter adapter;
    AdapterListener listener;
    public SearchFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            binding=FragmentSearchBinding.inflate(inflater);
            initView();


        return binding.getRoot();
    }
    private void initView(){
        binding.searchView.setFocusable(true);
        binding.searchView.requestFocus();
        binding.searchView.setIconified(false);
        listener=(AdapterListener) getContext();
        
        MusicRoomDB.getInstance(getContext()).getRecentMusics().observe(getViewLifecycleOwner(),(list)->{

            Collections.reverse(list);
            while(list.size()>=15){
                list.remove(list.size()-1);
            }
            recentsList= (ArrayList<Music>) list;
            adapter.updateFilteredList(recentsList,MusicPlayerActivity.FROM_RECENT);
        });

        if (searchArrayList==null)
             searchArrayList=new ArrayList<>();


        adapter=new MusicListAdapter(getContext(),listener, MusicPlayerActivity.FROM_SEARCH);
        binding.rvSearch.setAdapter(adapter);
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.searchView.setOnQueryTextListener(this);
        binding.searchView.performClick();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!newText.isEmpty())
          updateFilteredList(newText.toLowerCase());
        else adapter.updateFilteredList(recentsList,MusicPlayerActivity.FROM_RECENT);
        return true;
    }

    private void updateFilteredList(String text){
        ArrayList<Music> filteredMusic=new ArrayList<>();
        for(Music music: musicList)
            if(music.getTitle().toLowerCase().contains(text))
                filteredMusic.add(music);

        searchArrayList.clear();
        searchArrayList.addAll(filteredMusic);
        adapter.updateFilteredList(filteredMusic,MusicPlayerActivity.FROM_SEARCH);
    }
}

