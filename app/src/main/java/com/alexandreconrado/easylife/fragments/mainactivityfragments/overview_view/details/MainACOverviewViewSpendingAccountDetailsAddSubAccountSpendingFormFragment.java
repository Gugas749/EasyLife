package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.SpendingsAccountsDao;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogColorPickerFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.add.RVAdapterPercentagesNamesColors;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment extends Fragment implements
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag,
        RVAdapterPercentagesNamesColors.ItemClickedRVAdapterPercentagesNamesAndColors,
        RVAdapterPercentagesNamesColors.ItemSwipeRightRVAdapterPercentagesNamesAndColors,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogColorPickerFrag {

    private FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding binding;
    private MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment THIS;
    private UserInfosEntity userInfos;
    private String SubAccountName = "", nameFromDB = "";
    private List<String> percentagesNamesFromParent = new ArrayList<>();
    private int positionOnList;
    private boolean allDisable = false;
    private RVAdapterPercentagesNamesColors adapter;
    private List<String> percentagesNamesList, percentagesColorsList;
    private SpendingAccountsEntity account;
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;
    private String itemSwiped = "";
    private ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm exitListenner;

    public interface ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm{
        void onExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm(boolean save, SpendingAccountsEntity account);
    }
    public void setExitListenner(ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm exitListenner){
        this.exitListenner = exitListenner;
    }

    public MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment(String SubAccountName, int positionOnList, SpendingAccountsEntity account, List<String> percentagesNamesFromParent) {
        this.SubAccountName = SubAccountName;
        this.nameFromDB = SubAccountName;
        this.positionOnList = positionOnList;
        this.account = account;
        this.percentagesNamesFromParent = percentagesNamesFromParent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding.inflate(inflater);

        init();
        setupLocalDataBase();
        setupSaveButton();
        setupExitButton();
        setupButtonAddPercentagesNames();
        setupChangeParentColorCardView();
        disableBackPressed();

        return binding.getRoot();
    }
    private void disableBackPressed(){
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });
    }
    private void init(){
        THIS = this;
        if(SubAccountName.equals("+")){
            SubAccountName = "";
        }
        binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setText(SubAccountName);
        binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setCardBackgroundColor(Integer.parseInt(account.getPercentagesColorList().get(positionOnList)));
        percentagesNamesList = new ArrayList<>();
        percentagesColorsList = new ArrayList<>();
        adapter = new RVAdapterPercentagesNamesColors(getContext(), percentagesNamesList, percentagesColorsList, this, this);
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
    }
    private void setupSaveButton(){
        binding.imageViewButtonConfirmFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setCancelListenner(THIS);
                customAlertDialogFragment.setConfirmListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_Save), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm_AlertDialog_Question_Saving_Text), customAlertDialogFragment, customAlertDialogFragment, "1");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_SaveButton");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setCancelListenner(THIS);
                customAlertDialogFragment.setConfirmListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_AlertDialog_Question_ExitWithoutSaving_Title), getString(R.string.general_AlertDialog_Question_ExitWithoutSaving_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_ExitButton");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupChangeParentColorCardView(){
        binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                AlertDialogColorPickerFragment fragment = new AlertDialogColorPickerFragment();
                fragment.setInfos(customAlertDialogFragment, customAlertDialogFragment, 0, "FragMainACOverviewViewSpendingAccountDetailsAddSubAccountFrom_SecretMenssage");
                fragment.setJustGetColor(true);
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
                int color = typedValue.data;
                customAlertDialogFragment.setBackgroundColor(color);
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setConfirmColorPickerListenner(THIS);
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupButtonAddPercentagesNames(){
        binding.buttonSubmitNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.buttonSubmitNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(false);
                if(percentagesNamesList.size() >= 4){
                    Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm_Toast_PercentageCap_Text), Toast.LENGTH_SHORT).show();
                }else{
                    if(binding.editTextNamesPercentagesFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.length() > 0){
                        Boolean repeated = false;
                        for (int i = 0; i < percentagesNamesList.size(); i++) {
                            if(percentagesNamesList.get(i).equals(binding.editTextNamesPercentagesFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.getText().toString().trim())){
                                repeated = true;
                                break;
                            }
                        }
                        if(!repeated){
                            percentagesNamesList.add(binding.editTextNamesPercentagesFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.getText().toString().trim());
                            percentagesColorsList.add(String.valueOf(getResources().getColor(R.color.extra1)));

                            loadRecyclerView();

                            binding.editTextNamesPercentagesFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.requestFocus();
                            binding.editTextNamesPercentagesFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setText("");
                        }else{
                            Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_RepeatedName_Text), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                binding.buttonSubmitNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(true);
            }
        });
    }
    private void loadRecyclerView(){
        adapter.updateData(percentagesNamesList, percentagesColorsList);
        binding.rvPercentagesNamesFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPercentagesNamesFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void enableDisableEverything(boolean enable){
        if(enable){
            allDisable = false;
            binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(true);
            binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(true);
            binding.imageViewButtonConfirmFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(true);

            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(true);
            binding.buttonSubmitNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(true);
        }else{
            allDisable = true;
            binding.imageViewButtonExitFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(false);
            binding.imageViewButtonHowToUseFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(false);
            binding.imageViewButtonConfirmFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(false);

            binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(false);
            binding.buttonSubmitNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setEnabled(false);
        }
    }
    @Override
    public void onConfirmButtonClicked(int color, int position, String name, boolean justGetColor) {
        if(justGetColor){
            binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setCardBackgroundColor(color);
        }else{
            percentagesNamesList.remove(position);
            percentagesNamesList.add(position, name);
            percentagesColorsList.remove(position);
            percentagesColorsList.add(position, String.valueOf(color));
            adapter.updateData(percentagesNamesList, percentagesColorsList);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onItemClickedRVAdapterPercentagesNamesAndColors(int position) {
        if(!allDisable){
            CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
            AlertDialogColorPickerFragment fragment = new AlertDialogColorPickerFragment();
            fragment.setInfos(customAlertDialogFragment, customAlertDialogFragment, position, percentagesNamesList.get(position));
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
            int color = typedValue.data;
            customAlertDialogFragment.setBackgroundColor(color);
            customAlertDialogFragment.setCustomFragment(fragment);
            customAlertDialogFragment.setConfirmColorPickerListenner(this);
            customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
        }
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        enableDisableEverything(false);
        switch (Tag){
            case "FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_SaveButton":
                String accountName = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.getText().toString().trim();
                if(!accountName.equals("") && percentagesNamesList.size() >= 2 && percentagesColorsList.size() >= 2){
                    boolean repeated = false;
                    if(!accountName.equals("+")){
                        if(!accountName.equals(nameFromDB)){
                            for (int i = 0; i < percentagesNamesFromParent.size(); i++) {
                                if(percentagesNamesFromParent.get(i).equals(accountName)){
                                    repeated = true;
                                    break;
                                }
                            }
                        }
                        if(!repeated){
                            new LocalDatabaseInsertTask().execute();
                        }else{
                            Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_RepeatedName_Text), Toast.LENGTH_SHORT).show();
                            enableDisableEverything(true);
                        }
                    }else{
                        Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_NamedPlus_Text), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_MissingInputs_Text), Toast.LENGTH_SHORT).show();
                    enableDisableEverything(true);
                }
                break;
            case "FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_ExitButton":
                exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm(false, account);
                break;
            case "FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_SwipeRight":
                for (int i = 0; i < percentagesNamesList.size(); i++) {
                    if(percentagesNamesList.get(i).equals(itemSwiped)){
                        percentagesNamesList.remove(i);
                        percentagesColorsList.remove(i);
                        break;
                    }
                }

                loadRecyclerView();
                enableDisableEverything(true);
                break;
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {

    }

    @Override
    public void onItemSwipeRightRVAdapterPercentagesNamesAndColors(int pos) {
        if(!allDisable){
            CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
            customAlertDialogFragment.setConfirmListenner(THIS);
            customAlertDialogFragment.setCancelListenner(THIS);
            AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Delete_Title), getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Delete_Description), customAlertDialogFragment, customAlertDialogFragment, "2");
            customAlertDialogFragment.setCustomFragment(fragment);
            customAlertDialogFragment.setTag("FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_SwipeRight");
            customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");

            itemSwiped = percentagesNamesList.get(pos);
        }
    }

    private class LocalDatabaseInsertTask extends AsyncTask<Void, Void, SpendingAccountsEntity> {
        @Override
        protected SpendingAccountsEntity doInBackground(Void... voids) {
            SubSpendingAccountsEntity subAccount = new SubSpendingAccountsEntity();
            String accountName = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.getText().toString().trim();

            List<SpendsEntity> spendsEntitiesParentAccount = account.getSpendsList();
            List<SpendsEntity> spendsEntities = new ArrayList<>();

            account.getPercentagesColorList().remove(positionOnList);
            ColorStateList colorStateList = binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.getCardBackgroundColor();
            int color = colorStateList.getDefaultColor();
            account.getPercentagesColorList().add(positionOnList, String.valueOf(color));

            account.getPercentagesNamesList().remove(positionOnList);
            account.getPercentagesNamesList().add(positionOnList, accountName);

            for (int i = 0; i < spendsEntitiesParentAccount.size(); i++) {
                SpendsEntity selected = spendsEntitiesParentAccount.get(i);
                if(selected.getSubAccountID().equals(SubAccountName)){
                    spendsEntities.add(selected);
                    spendsEntitiesParentAccount.remove(selected);
                    i--;
                }
            }
            account.setSpendsList(spendsEntitiesParentAccount);

            while (percentagesNamesList.size() < 4){
                percentagesNamesList.add("+");
            }

            while (percentagesColorsList.size() < 4){
                percentagesColorsList.add("-1");
            }

            subAccount.setInfos(account.getId(), (positionOnList+1), accountName, spendsEntities, percentagesNamesList, percentagesColorsList, String.valueOf(color));

            List<SubSpendingAccountsEntity> subSpendingAccountsEntityList = account.getSubAccountsList();
            if(subSpendingAccountsEntityList == null){
                subSpendingAccountsEntityList = new ArrayList<>();
            }
            subSpendingAccountsEntityList.add(subAccount);

            account.setSubAccountsList(subSpendingAccountsEntityList);

            spendingsAccountsDao.update(account);
            return account;
        }

        @Override
        protected void onPostExecute(SpendingAccountsEntity object) {
            exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm(true, account);
        }
    }
}