package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.howto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentMainACOverviewViewHowToHomeBinding;

public class MainACOverviewViewHowToHomeFragment extends Fragment {

    private FragmentMainACOverviewViewHowToHomeBinding binding;
    public MainACOverviewViewHowToHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewHowToHomeBinding.inflate(inflater);



        return binding.getRoot();
    }
}