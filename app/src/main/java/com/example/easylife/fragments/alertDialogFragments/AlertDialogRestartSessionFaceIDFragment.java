package com.example.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentAlertDialogRestartSessionFaceIDBinding;

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