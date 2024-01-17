package com.example.easylife.fragments.mainactivityfragments.overview_view.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsBinding;

public class MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment extends Fragment {

    private FragmentMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsBinding binding;

    public MainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewSpendingAccountDetailsFormSubSpendingAccountDetailsBinding.inflate(inflater);



        return binding.getRoot();
    }
}