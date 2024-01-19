package com.example.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentAlertDialogColorPickerBinding;

import java.util.ArrayList;
import java.util.List;

import ir.kotlin.kavehcolorpicker.KavehColorAlphaSlider;
import ir.kotlin.kavehcolorpicker.KavehColorPicker;
import ir.kotlin.kavehcolorpicker.KavehHueSlider;

public class AlertDialogColorPickerFragment extends Fragment {

    private FragmentAlertDialogColorPickerBinding binding;
    private ConfirmButtonClickColorPicker listenner;
    private CancelButtonClickColorPicker cancelListenner;
    private List<String> percentagesNames = new ArrayList<>();
    public void setPercentagesNames(List<String> percentagesNames){
        this.percentagesNames = percentagesNames;
    }
    private String name, nameFromDB;
    private int position;
    private boolean justGetColor;
    public interface ConfirmButtonClickColorPicker{
        void onConfirmButtonClickedColorPicker(int color, int position, String name, boolean justGetColor);
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
        this.nameFromDB = name;
    }
    public void setJustGetColor(boolean justGetColor){
        this.justGetColor=justGetColor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogColorPickerBinding.inflate(inflater);
        if(name.equals("+")){
            name = "";
        }
        binding.editTextAccountNameFragAlertDialogColorPicker.setText(name);
        setupColorPicker();

        binding.buttonConfirmAlertDialogFragmentColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.editTextAccountNameFragAlertDialogColorPicker.getText().toString().trim();
                if(!name.equals("")){
                    boolean repeated = false;

                    if(!name.equals("+")){
                        if(!name.equals(nameFromDB)){
                            for (int i = 0; i < percentagesNames.size(); i++) {
                                if(percentagesNames.get(i).equals(name)){
                                    repeated = true;
                                    break;
                                }
                            }
                        }

                        if(repeated){
                            Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_RepeatedName_Text), Toast.LENGTH_SHORT).show();
                        }else{
                            listenner.onConfirmButtonClickedColorPicker(binding.colorPickerFragAlertDialogColorPicker.getColor(), position, name, justGetColor);
                        }
                    }else{
                        Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_NamedPlus_Text), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_MissingInputs_Text), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonCancelAlertDialogFragmentColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelListenner.onCancelButtonClickedColorPicker();
            }
        });

        if(justGetColor){
            binding.editTextAccountNameFragAlertDialogColorPicker.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }
    private void setupColorPicker(){
        KavehHueSlider hueSlider = binding.hueSliderFragAlertDialogColorPicker;
        KavehColorAlphaSlider colorAlphaSlider = binding.colorAlphaSliderFragAlertDialogColorPicker;

        binding.colorPickerFragAlertDialogColorPicker.setAlphaSliderView(colorAlphaSlider);
        binding.colorPickerFragAlertDialogColorPicker.setHueSliderView(hueSlider);
    }
}