package com.example.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentAlertDialogColorPickerBinding;

import ir.kotlin.kavehcolorpicker.KavehColorAlphaSlider;
import ir.kotlin.kavehcolorpicker.KavehColorPicker;
import ir.kotlin.kavehcolorpicker.KavehHueSlider;

public class AlertDialogColorPickerFragment extends Fragment {

    private FragmentAlertDialogColorPickerBinding binding;
    private ConfirmButtonClickColorPicker listenner;
    private CancelButtonClickColorPicker cancelListenner;
    private String name;
    private int position;
    public interface ConfirmButtonClickColorPicker{
        void onConfirmButtonClickedColorPicker(int color, int position, String name);
    }
    public interface CancelButtonClickColorPicker{
        void onCancelButtonClickedColorPicker();
    }

    public AlertDialogColorPickerFragment() {
        // Required empty public constructor
    }

    public void setInfos(ConfirmButtonClickColorPicker listenner, CancelButtonClickColorPicker cancelListenner, int position, String name) {
        this.listenner = listenner;
        this.cancelListenner = cancelListenner;
        this.position = position;
        this.name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogColorPickerBinding.inflate(inflater);
        binding.editTextAccountNameFragAlertDialogColorPicker.setText(name);
        setupColorPicker();

        binding.buttonConfirmAlertDialogFragmentColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.editTextAccountNameFragAlertDialogColorPicker.getText().toString().trim();
                listenner.onConfirmButtonClickedColorPicker(binding.colorPickerFragAlertDialogColorPicker.getColor(), position, name);
            }
        });

        binding.buttonCancelAlertDialogFragmentColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelListenner.onCancelButtonClickedColorPicker();
            }
        });

        return binding.getRoot();
    }
    private void setupColorPicker(){
        KavehHueSlider hueSlider = binding.hueSliderFragAlertDialogColorPicker;
        KavehColorAlphaSlider colorAlphaSlider = binding.colorAlphaSliderFragAlertDialogColorPicker;

        binding.colorPickerFragAlertDialogColorPicker.setAlphaSliderView(colorAlphaSlider);
        binding.colorPickerFragAlertDialogColorPicker.setHueSliderView(hueSlider);
    }
}