package com.example.easylife.scripts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.easylife.R;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogNotifyFragment;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;

public class CustomAlertDialogFragment extends DialogFragment implements AlertDialogNotifyFragment.ContinueButtonClick, AlertDialogQuestionFragment.CancelButtonClick, AlertDialogQuestionFragment.ConfirmButtonClick {
    private Fragment customFragment;
    private ConfirmButtonClickAlertDialogQuestionFrag listenner;

    public interface ConfirmButtonClickAlertDialogQuestionFrag{
        void onConfirmButtonClicked();
    }

    // Constructor to set the custom fragment
    public CustomAlertDialogFragment() {

    }

    public void setConfirmListenner(ConfirmButtonClickAlertDialogQuestionFrag listenner){
        this.listenner = listenner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_layout_alertd_dialog, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        // Replace the fragment with the custom fragment provided
        if (customFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_custom_alertDialog, customFragment)
                    .commit();
        }

        return view;
    }

    @Override
    public void onContinueButtonClicked() {
        this.dismiss();
    }

    public void setCustomFragment(Fragment customFragment){
        this.customFragment = customFragment;
    }

    @Override
    public void onCancelButtonClicked() {
        this.dismiss();
    }

    @Override
    public void onConfirmButtonClicked() {
        listenner.onConfirmButtonClicked();
        this.dismiss();
    }
}
