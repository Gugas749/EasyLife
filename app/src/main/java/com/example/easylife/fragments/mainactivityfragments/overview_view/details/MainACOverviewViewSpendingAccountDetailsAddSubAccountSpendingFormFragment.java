package com.example.easylife.fragments.mainactivityfragments.overview_view.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding;

public class MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment extends Fragment {

    private FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding binding;

    public MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding.inflate(inflater);



        return binding.getRoot();
    }
}