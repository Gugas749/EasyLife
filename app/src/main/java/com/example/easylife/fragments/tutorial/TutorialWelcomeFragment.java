package com.example.easylife.fragments.tutorial;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentTutorialWelcomeBinding;

public class TutorialWelcomeFragment extends Fragment {

    FragmentTutorialWelcomeBinding binding;

    public TutorialWelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTutorialWelcomeBinding.inflate(inflater);

        waveAnimation();

        return binding.getRoot();
    }

    private void waveAnimation(){
        ObjectAnimator waveAnimation = ObjectAnimator.ofFloat(binding.imageViewWavingFragTutotialWelcome, "rotation", 0f, 20f, 0f, -20f, 0f);
        waveAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        waveAnimation.setDuration(2000);
        waveAnimation.start();
    }
}