package com.example.easylife.fragments.mainactivityfragments.spendings_view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.databinding.FragmentMainACAddViewBinding;

public class MainACSpendingsViewAddSpendingsFragment extends Fragment {

    private FragmentMainACAddViewBinding binding;

    public MainACSpendingsViewAddSpendingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACAddViewBinding.inflate(inflater);



        return binding.getRoot();
    }
}