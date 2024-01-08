package com.example.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentAlertDialogNotifyBinding;
import com.example.easylife.databinding.FragmentAlertDialogQuestionBinding;

public class AlertDialogQuestionFragment extends Fragment {

    private ConfirmButtonClick listenner;
    private CancelButtonClick cancelListenner;
    public interface ConfirmButtonClick{
        void onConfirmButtonClicked();
    }
    public interface CancelButtonClick{
        void onCancelButtonClicked();
    }
    private FragmentAlertDialogQuestionBinding binding;
    private String title, description;

    public AlertDialogQuestionFragment(String title, String description, ConfirmButtonClick listenner, CancelButtonClick cancelListenner) {
        this.title = title;
        this.description = description;
        this.listenner = listenner;
        this.cancelListenner = cancelListenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogQuestionBinding.inflate(inflater);

        binding.textViewTitleAlertDialogFragmentQuestion.setText(title);
        binding.textViewDescriptionAlertDialogFragmentQuestion.setText(description);

        binding.buttonConfirmAlertDialogFragmentQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onConfirmButtonClicked();
            }
        });

        binding.buttonCancelAlertDialogFragmentQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelListenner.onCancelButtonClicked();
            }
        });

        return binding.getRoot();
    }
}