package com.example.easylife.fragments.mainactivityfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentMainACOverviewViewBinding;

public class MainACOverviewViewFragment extends Fragment {

    private FragmentMainACOverviewViewBinding binding;
    public MainACOverviewViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewBinding.inflate(inflater);



        return binding.getRoot();
    }
}