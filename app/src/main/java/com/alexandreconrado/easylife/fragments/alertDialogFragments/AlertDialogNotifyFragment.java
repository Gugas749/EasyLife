package com.alexandreconrado.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.databinding.FragmentAlertDialogNotifyBinding;

public class AlertDialogNotifyFragment extends Fragment {

    private ContinueButtonClick listenner;
    public interface ContinueButtonClick{
        void onContinueButtonClicked();
    }
    private FragmentAlertDialogNotifyBinding binding;
    private String title, description;

    public AlertDialogNotifyFragment(String title, String description, ContinueButtonClick listenner) {
        this.title = title;
        this.description = description;
        this.listenner = listenner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogNotifyBinding.inflate(inflater);

        binding.textViewTitleAlertDialogFragmentNotify.setText(title);
        binding.textViewDescriptionAlertDialogFragmentNotify.setText(description);

        binding.buttonOkAlertDialogFragmentNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onContinueButtonClicked();
            }
        });

        return binding.getRoot();
    }
}