package com.itsthetom.moodmusicplayer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.itsthetom.moodmusicplayer.R;
import com.itsthetom.moodmusicplayer.adapter.LibraryPagerAdapter;
import com.itsthetom.moodmusicplayer.databinding.FragmentLibraryBinding;

import org.jetbrains.annotations.NotNull;


public class LibraryFragment extends Fragment {
    FragmentLibraryBinding binding;
    LibraryPagerAdapter pagerAdapter;
    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLibraryBinding.inflate(inflater);
        // Inflate the layout for this fragment
        pagerAdapter=new LibraryPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                        switch (position){
                            case 0:tab.setText("Artist");
                            break;
                            case 1: tab.setText("Album");
                        }
                    }
                }).attach();

        return binding.getRoot();
    }
}