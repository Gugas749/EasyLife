package com.example.easylife.scripts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.example.easylife.R;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogRestartSessionFaceIDFragment;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;




public class CustomAlertDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_layout_alertd_dialog, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_custom_alertDialog, new AlertDialogRestartSessionFaceIDFragment())
                .commit();

        return view;
    }
}
