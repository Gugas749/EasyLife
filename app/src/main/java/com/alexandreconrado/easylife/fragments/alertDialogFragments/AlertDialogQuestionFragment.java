package com.alexandreconrado.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentAlertDialogQuestionBinding;

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
    private String title, description, Style;


    public AlertDialogQuestionFragment() {

    }
    public AlertDialogQuestionFragment(String title, String description, ConfirmButtonClick listenner, CancelButtonClick cancelListenner, String Style) {
        this.title = title;
        this.description = description;
        this.listenner = listenner;
        this.cancelListenner = cancelListenner;
        this.Style = Style;
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

        switch (Style){
            case "2":
                binding.buttonCancelAlertDialogFragmentQuestion.setText(getString(R.string.general_cancel));
                binding.buttonConfirmAlertDialogFragmentQuestion.setText(getString(R.string.general_confirm));
                break;
            case "3":
                binding.buttonCancelAlertDialogFragmentQuestion.setText(getString(R.string.general_DontSave));
                binding.buttonConfirmAlertDialogFragmentQuestion.setText(getString(R.string.general_Save));
                break;
            case "4":
                binding.buttonCancelAlertDialogFragmentQuestion.setText(getString(R.string.general_no));
                binding.buttonConfirmAlertDialogFragmentQuestion.setText(getString(R.string.general_yes));
                break;
        }

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