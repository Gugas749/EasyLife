package com.alexandreconrado.easylife.fragments.register;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.UserInfosDao;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.databinding.FragmentRegisterPasswordDialogBinding;
import com.google.firebase.Timestamp;

import java.util.Locale;

public class RegisterPasswordDialogFragment extends Fragment {
    private LocalDataBase databaseLocal;
    private UserInfosDao userInfosDao;
    private FragmentRegisterPasswordDialogBinding binding;
    private RegisterFragment parent;
    private String email;

    public RegisterPasswordDialogFragment() {

    }
    public RegisterPasswordDialogFragment(RegisterFragment parent) {
        this.parent = parent;
    }
    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterPasswordDialogBinding.inflate(inflater);

        setupLocalDataBase();
        setupReactiveEditTexts();
        setupContinueButton();

        return binding.getRoot();
    }

    private void setupLocalDataBase(){
        databaseLocal = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        userInfosDao = databaseLocal.userInfosDao();
    }
    private void setupReactiveEditTexts(){
        binding.editTextPasswordRegisterPasswordDialogFrag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String passswordText = binding.editTextPasswordRegisterPasswordDialogFrag.getText().toString();
                if(passswordText.length() >= 1){
                    if(passswordText.length() >= 4){

                    } else {
                        String aux = getResources().getString(R.string.register_dialog_password_invalid_password_to_short);
                        binding.editTextPasswordRegisterPasswordDialogFrag.setError(getString(R.string.register_dialog_password_invalid_password_to_short));
                    }
                }
            }
        });
        binding.editTextConfirmPasswordRegisterPasswordDialogFrag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String passswordText = binding.editTextPasswordRegisterPasswordDialogFrag.getText().toString();
                String confirmPassswordText = binding.editTextConfirmPasswordRegisterPasswordDialogFrag.getText().toString();
                if(passswordText.length() >= 4){
                    if(confirmPassswordText.length() >= 1){
                        if(passswordText.equals(confirmPassswordText)){

                        } else {
                            binding.editTextConfirmPasswordRegisterPasswordDialogFrag.setError(getString(R.string.register_dialog_password_invalid_password_no_equal));
                        }
                    }
                }
            }
        });
    }
    private void setupContinueButton(){
        binding.butContinueRegisterPasswordFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.butContinueRegisterPasswordFrag.setEnabled(false);
                String passswordText = binding.editTextPasswordRegisterPasswordDialogFrag.getText().toString();
                String confirmPassswordText = binding.editTextConfirmPasswordRegisterPasswordDialogFrag.getText().toString();

                if(passswordText.length() == 4){
                    if(passswordText.equals(confirmPassswordText)){
                        new LocalDatabaseInsetTask().execute();
                        binding.butContinueRegisterPasswordFrag.setEnabled(true);
                    } else {
                        binding.editTextConfirmPasswordRegisterPasswordDialogFrag.setError(getString(R.string.register_dialog_password_invalid_password_no_equal));
                        binding.butContinueRegisterPasswordFrag.setEnabled(true);
                    }
                } else {
                    binding.editTextPasswordRegisterPasswordDialogFrag.setError(getString(R.string.register_dialog_password_invalid_password_to_short));
                    binding.butContinueRegisterPasswordFrag.setEnabled(true);
                }
            }
        });
    }
    private class LocalDatabaseInsetTask extends AsyncTask<Void, Void, UserInfosEntity> {
        @Override
        protected UserInfosEntity doInBackground(Void... voids) {
            String password = binding.editTextConfirmPasswordRegisterPasswordDialogFrag.getText().toString();
            String theme = "System-Sync";
            Locale deviceLanguage = Locale.getDefault();
            SharedPreferences perfs = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
            String firebaseID = perfs.getString("firebaseID", "");
            UserInfosEntity infosEntity = new UserInfosEntity(password, theme, deviceLanguage);
            infosEntity.setInfos(firebaseID, email);
            infosEntity.lastBackup = Timestamp.now();
            userInfosDao.insert(infosEntity);
            return infosEntity;
        }

        @Override
        protected void onPostExecute(UserInfosEntity infosEntity) {
            parent.changeDialogFragments("");
        }
    }
}