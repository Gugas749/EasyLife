package com.example.easylife.scripts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.easylife.R;
import com.example.easylife.database.entities.DraggableCardViewEntity;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogColorPickerFragment;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogLongPressMainViewObjectsFragment;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogNotifyFragment;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;

public class CustomAlertDialogFragment extends DialogFragment implements AlertDialogNotifyFragment.ContinueButtonClick,
        AlertDialogQuestionFragment.CancelButtonClick,
        AlertDialogQuestionFragment.ConfirmButtonClick,
        AlertDialogColorPickerFragment.ConfirmButtonClickColorPicker,
        AlertDialogColorPickerFragment.CancelButtonClickColorPicker,
        AlertDialogLongPressMainViewObjectsFragment.CancelButtonClickAlertDialogLongPressMainViewObjectsFrag,
        AlertDialogLongPressMainViewObjectsFragment.ConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag {
    private Fragment customFragment;
    private ConfirmButtonClickAlertDialogQuestionFrag confirmButtonClickAlertDialogQuestionFrag;
    private CancelButtonClickAlertDialogQuestionFrag cancelButtonClickAlertDialogQuestionFrag;
    private ConfirmButtonClickAlertDialogColorPickerFrag confirmButtonClickAlertDialogColorPickerFrag;
    private ConfirmButtonClickAlertDialogLongPressMainViewObjects confirmButtonClickAlertDialogLongPressMainViewObjectsListenner;
    private String Tag;
    private int backgroundColor = 0;

    public interface ConfirmButtonClickAlertDialogQuestionFrag{
        void onConfirmButtonClicked(String Tag);
    }

    public interface CancelButtonClickAlertDialogColorPickerFrag{
        void onCancelButtonClicked();
    }

    public interface CancelButtonClickAlertDialogQuestionFrag{
        void onCancelButtonClicked(String Tag);
    }

    public interface ConfirmButtonClickAlertDialogColorPickerFrag{
        void onConfirmButtonClicked(int color, int position, String name);
    }

    public interface ConfirmButtonClickAlertDialogLongPressMainViewObjects{
        void onConfirmButtonClickAlertDialogLongPressMainViewObjects(DraggableCardViewEntity object);
    }
    public CustomAlertDialogFragment() {

    }

    public void setTag(String Tag){
        this.Tag = Tag;
    }

    public void setBackgroundColor(int color){
        backgroundColor = color;
    }

    public void setConfirmListenner(ConfirmButtonClickAlertDialogQuestionFrag listenner){
        this.confirmButtonClickAlertDialogQuestionFrag = listenner;
    }

    public void setCancelListenner(CancelButtonClickAlertDialogQuestionFrag listenner){
        this.cancelButtonClickAlertDialogQuestionFrag = listenner;
    }

    public void setConfirmColorPickerListenner(ConfirmButtonClickAlertDialogColorPickerFrag listenner){
        this.confirmButtonClickAlertDialogColorPickerFrag = listenner;
    }

    public void setConfirmButtonClickAlertDialogLongPressMainViewObjects(ConfirmButtonClickAlertDialogLongPressMainViewObjects listenner){
        this.confirmButtonClickAlertDialogLongPressMainViewObjectsListenner = listenner;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_layout_alertd_dialog, container, false);
        CardView parent = view.findViewById(R.id.linearLayout_parent_custom_alertDialog);

        if(backgroundColor != 0){
            parent.setCardBackgroundColor(backgroundColor);
        }
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
        cancelButtonClickAlertDialogQuestionFrag.onCancelButtonClicked(Tag);
        this.dismiss();
    }

    @Override
    public void onConfirmButtonClicked() {
        confirmButtonClickAlertDialogQuestionFrag.onConfirmButtonClicked(Tag);
        this.dismiss();
    }

    @Override
    public void onConfirmButtonClickedColorPicker(int color, int position, String name) {
        confirmButtonClickAlertDialogColorPickerFrag.onConfirmButtonClicked(color, position, name);
        this.dismiss();
    }

    @Override
    public void onCancelButtonClickedColorPicker() {
        this.dismiss();
    }
    @Override
    public void onCancelButtonClickAlertDialogLongPressMainViewObjectsFrag() {
        this.dismiss();
    }

    @Override
    public void onConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag(DraggableCardViewEntity object) {
        confirmButtonClickAlertDialogLongPressMainViewObjectsListenner.onConfirmButtonClickAlertDialogLongPressMainViewObjects(object);
        this.dismiss();
    }
}
