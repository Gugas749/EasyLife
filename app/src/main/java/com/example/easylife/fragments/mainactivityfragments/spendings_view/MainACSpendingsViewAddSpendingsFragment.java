package com.example.easylife.fragments.mainactivityfragments.spendings_view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.SubSpendingAccountsEntity;
import com.example.easylife.database.entities.UserInfosEntity;
import com.example.easylife.databinding.FragmentMainACSpendingsViewAddSpendingsBinding;
import com.example.easylife.databinding.FragmentMainACSpendingsViewBinding;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogDateHourPickerFragment;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.example.easylife.scripts.CustomAlertDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainACSpendingsViewAddSpendingsFragment extends Fragment implements CustomAlertDialogFragment.ExitAlertDialogDateHourPicker_CustomAlertDialogFrag {

    private FragmentMainACSpendingsViewAddSpendingsBinding binding;
    private List<SpendingAccountsEntity> spendingAccountsEntityList;
    private UserInfosEntity userInfos;
    private MainACSpendingsViewAddSpendingsFragment THIS;
    private ExitMainACSpendingsViewAddSpendingsFrag listenner;
    public interface ExitMainACSpendingsViewAddSpendingsFrag{
        void onExitMainACSpendingsViewAddSpendingsFrag(boolean save);
    }
    public void setExitMainACSpendingsViewAddSpendingsFragListenner(ExitMainACSpendingsViewAddSpendingsFrag listenner){
        this.listenner = listenner;
    }

    public MainACSpendingsViewAddSpendingsFragment(List<SpendingAccountsEntity> spendingAccountsEntityList, UserInfosEntity userInfos) {
        this.spendingAccountsEntityList = spendingAccountsEntityList;
        this.userInfos = userInfos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACSpendingsViewAddSpendingsBinding.inflate(inflater);

        init();
        setupButtonSave();
        setupButtonExit();
        setupButtonChangeHours();

        return binding.getRoot();
    }
    private void init(){
        THIS = this;
        loadSpinnerWheres();
        loadSpinnerMainAccounts();
    }
    private void setupButtonSave(){
        binding.imageViewButtonConfirmFragMainACSpendingsViewAddSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void setupButtonExit(){
        binding.imageViewButtonExitFragMainACSpendingsViewAddSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onExitMainACSpendingsViewAddSpendingsFrag(false);
            }
        });
    }
    private void setupButtonChangeHours(){
        binding.buttonChangeHoursAlertDialogDateHourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                AlertDialogDateHourPickerFragment fragment = new AlertDialogDateHourPickerFragment();
                fragment.setExitAlertDialogDateHourPickerListenner(customAlertDialogFragment);
                customAlertDialogFragment.setCustomFragment(fragment);
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
                int color = typedValue.data;
                customAlertDialogFragment.setBackgroundColor(color);
                customAlertDialogFragment.setExitAlertDialogDateHourPickerListenner_CustomAlertDialogFrag(THIS);
                customAlertDialogFragment.setTag("Exit");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void loadSpinnerWheres(){
        List<String> wheres = userInfos.addSpedingsWheres;
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragMainACSpendingsViewAddSpendings;
        if(wheres != null){
            if(wheres.size() > 0){
                powerSpinner.setItems(wheres);
            }
        }
    }
    private void loadSpinnerMainAccounts(){
        List<String> items = new ArrayList<>();
        for (int i = 0; i < spendingAccountsEntityList.size(); i++) {
            SpendingAccountsEntity selectedObject = spendingAccountsEntityList.get(i);
            items.add(selectedObject.getAccountTitle());
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragMainACSpendingsViewAddSpendings;
        powerSpinner.setItems(items);
    }
    private void loadSpinnerSubAccounts(int SelectedAccount){
        List<String> items = new ArrayList<>();
        List<SubSpendingAccountsEntity> subAcountsList = spendingAccountsEntityList.get(SelectedAccount).getSubAccountsList();
        if(subAcountsList != null){
            for (int i = 0; i < subAcountsList.size(); i++) {
                SubSpendingAccountsEntity selectedObject = subAcountsList.get(i);
                items.add(selectedObject.getAccountTitle());
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsFragMainACSpendingsViewAddSpendings;
        powerSpinner.setItems(items);
    }
    @Override
    public void onExitAlertDialogDateHourPicker_CustomAlertDialogFrag(boolean save, Date date) {
        Toast.makeText(getContext(), ""+ save+ "    "+date+ "     ", Toast.LENGTH_SHORT).show();
    }
}