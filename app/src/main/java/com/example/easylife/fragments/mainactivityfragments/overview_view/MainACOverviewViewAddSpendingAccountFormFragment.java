package com.example.easylife.fragments.mainactivityfragments.overview_view;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.easylife.database.entities.UserInfosEntity;
import com.example.easylife.databinding.FragmentMainACOverviewViewAddSpendingAccountFormBinding;
import com.example.easylife.scripts.colorpicker.ColorPickerDialog;

public class MainACOverviewViewAddSpendingAccountFormFragment extends Fragment {

    private FragmentMainACOverviewViewAddSpendingAccountFormBinding binding;
    private UserInfosEntity userInfos;
    public MainACOverviewViewAddSpendingAccountFormFragment(UserInfosEntity userInfos) {
        this.userInfos = userInfos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewAddSpendingAccountFormBinding.inflate(inflater);

        binding.imageViewButtonExitFragMainACOverviewViewAddSpendingAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPicker = new ColorPickerDialog(
                        getContext(),
                        Color.BLACK, // color init
                        new ColorPickerDialog.OnColorPickerListener() {
                            @Override
                            public void onCancel(ColorPickerDialog dialog) {
                                // handle click button Cancel
                            }

                            @Override
                            public void onOk(ColorPickerDialog dialog, int colorPicker) {
                                // handle click button OK
                            }
                        });
                colorPicker.show();
            }
        });

        return binding.getRoot();
    }
}