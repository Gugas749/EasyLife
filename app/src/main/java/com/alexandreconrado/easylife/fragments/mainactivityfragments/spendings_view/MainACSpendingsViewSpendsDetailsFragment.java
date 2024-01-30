package com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentMainACSpendingsViewSpendsDetailsBinding;

public class MainACSpendingsViewSpendsDetailsFragment extends Fragment {

    private FragmentMainACSpendingsViewSpendsDetailsBinding binding;

    public MainACSpendingsViewSpendsDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACSpendingsViewSpendsDetailsBinding.inflate(inflater);



        return binding.getRoot();
    }
}