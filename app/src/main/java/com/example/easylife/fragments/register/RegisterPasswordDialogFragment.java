package com.example.easylife.fragments.register;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.database.LocalDataBase;
import com.example.easylife.database.UserInfosDao;
import com.example.easylife.database.UserInfosEntity;
import com.example.easylife.databinding.FragmentRegisterPasswordDialogBinding;

import java.util.Locale;

public class RegisterPasswordDialogFragment extends Fragment {
    private LocalDataBase databaseLocal;
    private UserInfosDao userInfosDao;
    private FragmentRegisterPasswordDialogBinding binding;
    private RegisterFragment parent;

    public RegisterPasswordDialogFragment(RegisterFragment parent) {
        this.parent = parent;
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
                if(passswordText.length() >= 4){

                } else {
                    binding.editTextPasswordRegisterPasswordDialogFrag.setError(String.valueOf(R.string.register_dialog_password_invalid_password_to_short));
                }
            }
        });
        binding.editTextConfirmPasswordRegisterPasswordDialogFrag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String passswordText = binding.editTextPasswordRegisterPasswordDialogFrag.getText().toString();
                String confirmPassswordText = binding.editTextConfirmPasswordRegisterPasswordDialogFrag.getText().toString();

                if(passswordText.equals(confirmPassswordText)){

                } else {
                    binding.editTextConfirmPasswordRegisterPasswordDialogFrag.setError(String.valueOf(R.string.register_dialog_password_invalid_password_no_equal));
                }
            }
        });
    }
    private void setupContinueButton(){
        binding.butContinueRegisterPasswordFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passswordText = binding.editTextPasswordRegisterPasswordDialogFrag.getText().toString();
                String confirmPassswordText = binding.editTextConfirmPasswordRegisterPasswordDialogFrag.getText().toString();

                if(passswordText.length() >= 4){
                    if(passswordText.equals(confirmPassswordText)){
                        new LocalDatabaseInsetTask().execute();
                    } else {
                        binding.editTextConfirmPasswordRegisterPasswordDialogFrag.setError(String.valueOf(R.string.register_dialog_password_invalid_password_no_equal));
                    }
                } else {
                    binding.editTextPasswordRegisterPasswordDialogFrag.setError(String.valueOf(R.string.register_dialog_password_invalid_password_to_short));
                }
            }
        });
    }
    private class LocalDatabaseInsetTask extends AsyncTask<Void, Void, UserInfosEntity> {
        @Override
        protected UserInfosEntity doInBackground(Void... voids) {
            String password = binding.editTextConfirmPasswordRegisterPasswordDialogFrag.getText().toString();
            String theme = "";
            int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentTheme == Configuration.UI_MODE_NIGHT_NO) {
                theme = "Light";
            } else {
                theme = "Dark";
            }
            String deviceLanguage = Locale.getDefault().getDisplayLanguage();

            UserInfosEntity infosEntity = new UserInfosEntity(0, password, theme, deviceLanguage);
            userInfosDao.insert(infosEntity);
            return infosEntity;
        }

        @Override
        protected void onPostExecute(UserInfosEntity infosEntity) {
            parent.changeDialogFragments();
        }
    }
}