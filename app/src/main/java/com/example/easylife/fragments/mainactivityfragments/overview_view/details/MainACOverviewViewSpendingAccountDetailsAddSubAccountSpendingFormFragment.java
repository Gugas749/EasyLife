package com.example.easylife.fragments.mainactivityfragments.overview_view.details;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.database.LocalDataBase;
import com.example.easylife.database.daos.SpendingsAccountsDao;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.SpendsEntity;
import com.example.easylife.database.entities.SubSpendingAccountsEntity;
import com.example.easylife.database.entities.UserInfosEntity;
import com.example.easylife.databinding.FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogColorPickerFragment;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.example.easylife.fragments.mainactivityfragments.overview_view.add.MainACOverviewViewAddSpendingAccountFormFragment;
import com.example.easylife.fragments.mainactivityfragments.overview_view.add.RVAdapterPercentagesNamesColors;
import com.example.easylife.scripts.CustomAlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment extends Fragment implements
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag,
        RVAdapterPercentagesNamesColors.ItemClickedRVAdapterPercentagesNamesAndColors,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogColorPickerFrag {

    private FragmentMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormBinding binding;
    private MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment THIS;
    private UserInfosEntity userInfos;
    private String SubAccountName = "";
    private List<String> percentagesNamesFromParent = new ArrayList<>();
    private int positionOnList;
    private boolean allDisable = false;
    private RVAdapterPercentagesNamesColors adapter;
    private List<String> percentagesNamesList, percentagesColorsList;
    private SpendingAccountsEntity account;
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;
    private ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm exitListenner;
    public interface ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm{
        void onExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm(boolean save, SpendingAccountsEntity account);
    }
    public void setExitListenner(ExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm exitListenner){
        this.exitListenner = exitListenner;
    }

    public MainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingFormFragment(String SubAccountName, int positionOnList, SpendingAccountsEntity account, List<String> percentagesNamesFromParent) {
        this.SubAccountName = SubAccountName;
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

        return binding.getRoot();
    }
    private void init(){
        THIS = this;
        binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setText(SubAccountName);
        binding.cardViewChangeParentShowingColorFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.setCardBackgroundColor(Integer.parseInt(account.getPercentagesColorList().get(positionOnList)));
        percentagesNamesList = new ArrayList<>();
        percentagesColorsList = new ArrayList<>();
        adapter = new RVAdapterPercentagesNamesColors(getContext(), percentagesNamesList, percentagesColorsList, this);
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
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Save_Title), getString(R.string.general_AlertDialog_Question_save_text), customAlertDialogFragment, customAlertDialogFragment, "1");
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
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Leave_Title), getString(R.string.general_AlertDialog_Question_saveBeforeLeaving_text), customAlertDialogFragment, customAlertDialogFragment, "2");
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
        if(Tag.equals("FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_SaveButton") || Tag.equals("FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_ExitButton")){
            String accountName = binding.editTextAccountNameFragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm.getText().toString().trim();
            if(!accountName.equals("") && percentagesNamesList.size() >= 2 && percentagesColorsList.size() >= 2){
                boolean repeated = false;
                for (int i = 0; i < percentagesNamesFromParent.size(); i++) {
                    if(percentagesNamesFromParent.get(i).equals(accountName)){
                        repeated = true;
                        break;
                    }
                }
                if(!repeated){
                    new LocalDatabaseInsertTask().execute();
                }else{
                    Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_RepeatedName_Text), Toast.LENGTH_SHORT).show();
                    enableDisableEverything(true);
                }
            }else{
                Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_MissingInputs_Text), Toast.LENGTH_SHORT).show();
                enableDisableEverything(true);
            }
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {
        enableDisableEverything(false);
        if (Tag.equals("FragMainACOverviewViewSpendingAccountDetailsFormAddSubAccountForm_ExitButton")){
            exitListenner.onExitFragMainACOverviewViewSpendingAccountDetailsAddSubAccountSpendingForm(false, account);
        }
        enableDisableEverything(true);
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
                if(selected.getWhere().equals(SubAccountName)){
                    spendsEntities.add(selected);
                    spendsEntitiesParentAccount.remove(selected);
                    i--;
                }
            }
            account.setSpendsList(spendsEntitiesParentAccount);

            subAccount.setInfos(account.getId(), (positionOnList+1), accountName, spendsEntities, percentagesNamesList, percentagesColorsList);

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