package com.example.easylife.fragments.alertDialogFragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.database.LocalDataBase;
import com.example.easylife.database.daos.DraggableCardViewDao;
import com.example.easylife.database.entities.DraggableCardViewEntity;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.UserInfosEntity;
import com.example.easylife.databinding.FragmentAlertDialogLongPressMainViewObjectsBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.skydoves.powerspinner.SpinnerSizeSpec;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogLongPressMainViewObjectsFragment extends Fragment {

    private FragmentAlertDialogLongPressMainViewObjectsBinding binding;
    private DraggableCardViewEntity object;
    private List<SpendingAccountsEntity> spendingAccountsEntityList;
    private CancelButtonClickAlertDialogLongPressMainViewObjectsFrag cancelListenner;
    private ConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag confirmListenner;
    private boolean hasStyles = false, cantHoldSubAccounts = false;;
    public interface CancelButtonClickAlertDialogLongPressMainViewObjectsFrag{
        void onCancelButtonClickAlertDialogLongPressMainViewObjectsFrag();
    }
    public interface ConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag{
        void onConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag(DraggableCardViewEntity object);
    }
    public AlertDialogLongPressMainViewObjectsFragment(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList) {
        this.object = object;
        this.spendingAccountsEntityList = spendingAccountsEntityList;
    }
    public void setListenners(CancelButtonClickAlertDialogLongPressMainViewObjectsFrag cancelListenner, ConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag confirmListenner){
        this.cancelListenner = cancelListenner;
        this.confirmListenner = confirmListenner;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogLongPressMainViewObjectsBinding.inflate(inflater);

        setupConfirmButton();
        setupCancelButton();
        setupOnItemSelectedSpinnerAccounts();
        hideThings();
        processData();
        loadSpinnerMainAccounts();

        return binding.getRoot();
    }
    private void setupConfirmButton(){
        binding.buttonConfirmFragAlertDialogLongPressMainViewObjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndexSpinnerAccounts = binding.spinnerSpendigsAccountsFragAlertDialogLongPressMainViewObjects.getSelectedIndex();
                if(selectedIndexSpinnerAccounts >= 0){
                    boolean alright = false;
                    if(hasStyles){
                        if(binding.spinnerWidgetStyleFragAlertDialogLongPressMainViewObjects.getSelectedIndex() >= 0){
                            alright = true;
                        }
                    }else{
                        alright = true;
                    }
                    if(alright){
                        object.setAccountID(String.valueOf(spendingAccountsEntityList.get(selectedIndexSpinnerAccounts).getId()));
                        if(hasStyles){
                            int style = binding.spinnerWidgetStyleFragAlertDialogLongPressMainViewObjects.getSelectedIndex()+1;
                            object.setStyle(String.valueOf(style));
                        }
                        confirmListenner.onConfirmButtonClickAlertDialogLongPressMainViewObjectsFrag(object);
                    }else{
                        Toast.makeText(getContext(), getString(R.string.alertDialog_LongPressMainViewObjects_Toast_SelectStyle), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.alertDialog_LongPressMainViewObjects_Toast_SelectAccount), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setupCancelButton(){
        binding.buttonCancelFragAlertDialogLongPressMainViewObjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelListenner.onCancelButtonClickAlertDialogLongPressMainViewObjectsFrag();
            }
        });
    }
    private void setupOnItemSelectedSpinnerAccounts(){
        binding.spinnerSpendigsAccountsFragAlertDialogLongPressMainViewObjects.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                if(!cantHoldSubAccounts){
                    loadSpinnerSubAccounts(i1);
                }
            }
        });
    }
    private void hideThings(){
        binding.textViewSpinnerWidgetStyleExplainFragAlertDialogLongPressMainViewObjects.setVisibility(View.GONE);
        binding.cardViewSpinnerWidgetStyleHolderFragAlertDialogLongPressMainViewObjects.setVisibility(View.GONE);
    }
    private void processData(){
        switch (object.getType()){
            case "2":
                hasStyles = true;
                break;
            case "3":
                cantHoldSubAccounts = true;
                break;
        }

        if(hasStyles){
            binding.cardViewSpinnerWidgetStyleHolderFragAlertDialogLongPressMainViewObjects.setVisibility(View.VISIBLE);
            binding.textViewSpinnerWidgetStyleExplainFragAlertDialogLongPressMainViewObjects.setVisibility(View.VISIBLE);
            List<String> items = new ArrayList<>();
            for (int i = 1; i <= 2 ; i++) {
                items.add(getString(R.string.alertDialog_LongPressMainViewObjects_spinner_WidgetStyle_ItemText)+" "+i);
            }
            PowerSpinnerView powerSpinner = binding.spinnerWidgetStyleFragAlertDialogLongPressMainViewObjects;
            powerSpinner.setItems(items);
        }

        if(cantHoldSubAccounts){
            binding.textViewSpinnerSpendigsSubAccountsExplainFragAlertDialogLongPressMainViewObjects.setVisibility(View.GONE);
            binding.cardViewSpinnerSpendigsSubAccountsHolderFragAlertDialogLongPressMainViewObjects.setVisibility(View.GONE);
        }
    }
    private void loadSpinnerMainAccounts(){
        List<String> items = new ArrayList<>();
        for (int i = 0; i < spendingAccountsEntityList.size(); i++) {
            SpendingAccountsEntity selectedObject = spendingAccountsEntityList.get(i);
            items.add(selectedObject.getAccountTitle());
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsAccountsFragAlertDialogLongPressMainViewObjects;
        powerSpinner.setItems(items);
    }
    private void loadSpinnerSubAccounts(int SelectedAccount){
        List<String> items = new ArrayList<>();
        List<SpendingAccountsEntity> subAcountsList = spendingAccountsEntityList.get(SelectedAccount).getSubAccountsList();
        if(subAcountsList != null){
            for (int i = 0; i < subAcountsList.size(); i++) {
                SpendingAccountsEntity selectedObject = subAcountsList.get(i);
                items.add(selectedObject.getAccountTitle());
            }
        }
        PowerSpinnerView powerSpinner = binding.spinnerSpendigsSubAccountsFragAlertDialogLongPressMainViewObjects;
        powerSpinner.setItems(items);
    }
}