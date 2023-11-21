package com.example.easylife.scripts;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.easylife.R;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogRestartSessionFaceIDFragment;

public class CustomAlertDialogFragmentUseable {

    public void showDialog(FragmentManager fragmentManager, Context context, int mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog_fragments_accepted, null);
        builder.setView(dialogView);

        switch (mode) {
            case 1:
                // Use getChildFragmentManager() if calling from within a fragment
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                AlertDialogRestartSessionFaceIDFragment fragment = new AlertDialogRestartSessionFaceIDFragment();
                transaction.replace(R.id.customAlertDialogFrag_fragment_container, fragment);
                transaction.commit();
                break;
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
