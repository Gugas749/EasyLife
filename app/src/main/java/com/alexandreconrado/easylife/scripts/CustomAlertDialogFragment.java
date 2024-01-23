package com.alexandreconrado.easylife.scripts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.entities.DraggableCardViewEntity;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogColorPickerFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogDateHourPickerFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogLongPressMainViewObjectsFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogNotifyFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;

import java.util.Date;

public class CustomAlertDialogFragment extends DialogFragment implements AlertDialogNotifyFragment.ContinueButtonClick,
        AlertDialogQuestionFragment.CancelButtonClick,
        AlertDialogQuestionFragment.ConfirmButtonClick,
        AlertDialogColorPickerFragment.ConfirmButtonClickColorPicker,
        AlertDialogColorPickerFragment.CancelButtonClickColorPicker,
        AlertDialogLongPressMainViewObjectsFragment.CancelButtonClickAlertDialogLongPressMainViewObjectsFrag,
        AlertDialogLongPressMainViewObjectsFragment.ConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag,
        AlertDialogDateHourPickerFragment.ExitAlertDialogDateHourPicker {
    private Fragment customFragment;
    private ConfirmButtonClickAlertDialogQuestionFrag confirmButtonClickAlertDialogQuestionFrag;
    private CancelButtonClickAlertDialogQuestionFrag cancelButtonClickAlertDialogQuestionFrag;
    private ConfirmButtonClickAlertDialogColorPickerFrag confirmButtonClickAlertDialogColorPickerFrag;
    private ConfirmButtonClickAlertDialogLongPressMainViewObjects confirmButtonClickAlertDialogLongPressMainViewObjectsListenner;
    private String Tag;
    private int backgroundColor = 0;

    private ExitAlertDialogDateHourPicker_CustomAlertDialogFrag exitAlertDialogDateHourPicker_CustomAlertDialogFragListenner;
    public interface ExitAlertDialogDateHourPicker_CustomAlertDialogFrag{
        void onExitAlertDialogDateHourPicker_CustomAlertDialogFrag(boolean save, Date date);
    }
    public void setExitAlertDialogDateHourPickerListenner_CustomAlertDialogFrag(ExitAlertDialogDateHourPicker_CustomAlertDialogFrag listenner){
        this.exitAlertDialogDateHourPicker_CustomAlertDialogFragListenner = listenner;
    }

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
        void onConfirmButtonClicked(int color, int position, String name, boolean justGetColor);
    }

    public interface ConfirmButtonClickAlertDialogLongPressMainViewObjects{
        void onConfirmButtonClickAlertDialogLongPressMainViewObjects(DraggableCardViewEntity object, boolean canHoldMainAccount, int selectedSubAccountIndex);
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
    public void onConfirmButtonClickedColorPicker(int color, int position, String name, boolean justGetColor) {
        confirmButtonClickAlertDialogColorPickerFrag.onConfirmButtonClicked(color, position, name, justGetColor);
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
    public void onConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag(DraggableCardViewEntity object, boolean canHoldMainAccount, int selectedSubAccountIndex) {
        confirmButtonClickAlertDialogLongPressMainViewObjectsListenner.onConfirmButtonClickAlertDialogLongPressMainViewObjects(object, canHoldMainAccount, selectedSubAccountIndex);
        this.dismiss();
    }

    @Override
    public void onExitAlertDialogDateHourPicker(boolean save, Date date) {
        exitAlertDialogDateHourPicker_CustomAlertDialogFragListenner.onExitAlertDialogDateHourPicker_CustomAlertDialogFrag(save, date);
        this.dismiss();
    }
}
