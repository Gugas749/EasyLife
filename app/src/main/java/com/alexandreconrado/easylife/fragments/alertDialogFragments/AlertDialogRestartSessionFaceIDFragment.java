package com.alexandreconrado.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.databinding.FragmentAlertDialogRestartSessionFaceIDBinding;

public class AlertDialogRestartSessionFaceIDFragment extends Fragment {

    FragmentAlertDialogRestartSessionFaceIDBinding binding;

    public AlertDialogRestartSessionFaceIDFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogRestartSessionFaceIDBinding.inflate(inflater);

        return binding.getRoot();
    }
}