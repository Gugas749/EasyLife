package com.example.easylife.fragments.mainactivityfragments.overview_view.add;

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
import com.example.easylife.database.entities.UserInfosEntity;
import com.example.easylife.databinding.FragmentMainACOverviewViewAddSpendingAccountFormBinding;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogColorPickerFragment;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.example.easylife.scripts.CustomAlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainACOverviewViewAddSpendingAccountFormFragment extends Fragment implements
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogColorPickerFrag,
        RVAdapterPercentagesNamesColors.ItemClickedRVAdapterPercentagesNamesAndColors,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag {

    private FragmentMainACOverviewViewAddSpendingAccountFormBinding binding;
    private UserInfosEntity userInfos;
    private RVAdapterPercentagesNamesColors adapter;
    private List<String> percentagesNamesList, percentagesColorsList;
    private ExitButtonClickFragMainACOverviewViewAddSpendingsForm exitListenner;
    private MainACOverviewViewAddSpendingAccountFormFragment THIS;
    private LocalDataBase localDataBase;
    private SpendingsAccountsDao spendingsAccountsDao;

    public interface ExitButtonClickFragMainACOverviewViewAddSpendingsForm{
        void onExitButtonClickFragMainACOverviewViewAddSpendingsForm(Boolean Changed, SpendingAccountsEntity returned);
    }

    public MainACOverviewViewAddSpendingAccountFormFragment(UserInfosEntity userInfos) {
        this.userInfos = userInfos;
    }

    public void setExitListenner(ExitButtonClickFragMainACOverviewViewAddSpendingsForm exitListenner){
        this.exitListenner = exitListenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewAddSpendingAccountFormBinding.inflate(inflater);

        init();
        setupLocalDataBase();
        setupButtonAddPercentagesNames();
        setupSaveButton();
        setupExitButton();

        return binding.getRoot();
    }
    private void init(){
        THIS = this;
        percentagesNamesList = new ArrayList<>();
        percentagesColorsList = new ArrayList<>();
        adapter = new RVAdapterPercentagesNamesColors(getContext(), percentagesNamesList, percentagesColorsList, this);
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
    }
    private void setupSaveButton(){
        binding.imageViewButtonConfirmFragMainACOverviewViewAddSpendingAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setConfirmListenner(THIS);
                customAlertDialogFragment.setCancelListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Save_Title), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_AlertDialog_Question_Save_Text), customAlertDialogFragment, customAlertDialogFragment, "1");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragMainACOverviewViewAddSpendingsAccountForm_Save");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACOverviewViewAddSpendingAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                customAlertDialogFragment.setConfirmListenner(THIS);
                customAlertDialogFragment.setCancelListenner(THIS);
                AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.mainAc_FragMainViewEditLayout_AlertDialog_Leave_Title), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_AlertDialog_Question_SaveBeforeLeaving_Text), customAlertDialogFragment, customAlertDialogFragment, "2");
                customAlertDialogFragment.setCustomFragment(fragment);
                customAlertDialogFragment.setTag("FragMainACOverviewViewAddSpendingsAccountForm_Exit");
                customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
            }
        });
    }
    private void setupButtonAddPercentagesNames(){
        binding.buttonSubmitNameFragMainACOverviewViewAddSpendingAccountForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.buttonSubmitNameFragMainACOverviewViewAddSpendingAccountForm.setEnabled(false);
                if(percentagesNamesList.size() >= 8){
                    Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_PecentageMaxCap_Text), Toast.LENGTH_LONG).show();
                }else{
                    if(binding.editTextNamesPercentagesFragMainACOverviewViewAddSpendingAccountForm.length() > 0){
                        Boolean repeated = false;
                        for (int i = 0; i < percentagesNamesList.size(); i++) {
                            if(percentagesNamesList.get(i).equals(binding.editTextNamesPercentagesFragMainACOverviewViewAddSpendingAccountForm.getText().toString().trim())){
                                repeated = true;
                                break;
                            }
                        }
                        if(!repeated){
                            percentagesNamesList.add(binding.editTextNamesPercentagesFragMainACOverviewViewAddSpendingAccountForm.getText().toString().trim());
                            percentagesColorsList.add(String.valueOf(getResources().getColor(R.color.extra1)));

                            loadRecyclerView();
                        }else{
                            Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_RepeatedName_Text), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                binding.buttonSubmitNameFragMainACOverviewViewAddSpendingAccountForm.setEnabled(true);
            }
        });
    }
    private void loadRecyclerView(){
        adapter.updateData(percentagesNamesList, percentagesColorsList);
        binding.rvPercentagesNamesFragMainACOverviewViewAddSpendingAccountForm.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPercentagesNamesFragMainACOverviewViewAddSpendingAccountForm.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onConfirmButtonClicked(int color, int position, String name, boolean justGetColor) {
        percentagesNamesList.remove(position);
        percentagesNamesList.add(position, name);
        percentagesColorsList.remove(position);
        percentagesColorsList.add(position, String.valueOf(color));
        adapter.updateData(percentagesNamesList, percentagesColorsList);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClickedRVAdapterPercentagesNamesAndColors(int position) {
        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
        AlertDialogColorPickerFragment fragment = new AlertDialogColorPickerFragment();
        fragment.setInfos(customAlertDialogFragment, customAlertDialogFragment, position, percentagesNamesList.get(position));
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;
        customAlertDialogFragment.setBackgroundColor(color);
        customAlertDialogFragment.setCustomFragment(fragment);
        customAlertDialogFragment.setConfirmColorPickerListenner(MainACOverviewViewAddSpendingAccountFormFragment.this);
        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
    }

    //-----------------QUESTION ALERT DIALOG---------------------
    @Override
    public void onConfirmButtonClicked(String Tag) {
        if(Tag.equals("FragMainACOverviewViewAddSpendingsAccountForm_Exit") || Tag.equals("FragMainACOverviewViewAddSpendingsAccountForm_Save")){
            String accountName = binding.editTextAccountNameFragMainACOverviewViewAddSpendingAccountForm.getText().toString().trim();
            if(!accountName.equals("") && percentagesNamesList.size() >= 2 && percentagesColorsList.size() >= 2){
                new LocalDatabaseInsertTask().execute();
            }else{
                Toast.makeText(getContext(), getString(R.string.mainAc_FragOverviewViewAddSpendingsAccount_Toast_MissingInputs_Text), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {
        if(Tag.equals("FragMainACOverviewViewAddSpendingsAccountForm_Exit")){
            SpendingAccountsEntity newSpendingsAccount = new SpendingAccountsEntity();
            exitListenner.onExitButtonClickFragMainACOverviewViewAddSpendingsForm(false, newSpendingsAccount);
        }
    }
    //----------------------------------------------------------------

    private class LocalDatabaseInsertTask extends AsyncTask<Void, Void, SpendingAccountsEntity> {
        @Override
        protected SpendingAccountsEntity doInBackground(Void... voids) {
            SpendingAccountsEntity spendingAccountsEntity = new SpendingAccountsEntity();
            String accountName = binding.editTextAccountNameFragMainACOverviewViewAddSpendingAccountForm.getText().toString().trim();
            List<SpendsEntity> spendsEntities = new ArrayList<>();
            spendingAccountsEntity.setInfos(userInfos.firebaseID, userInfos.email, accountName, spendsEntities, percentagesNamesList, percentagesColorsList);

            spendingsAccountsDao.insert(spendingAccountsEntity);

            return spendingAccountsEntity;
        }

        @Override
        protected void onPostExecute(SpendingAccountsEntity object) {
            exitListenner.onExitButtonClickFragMainACOverviewViewAddSpendingsForm(true, object);
        }
    }
}