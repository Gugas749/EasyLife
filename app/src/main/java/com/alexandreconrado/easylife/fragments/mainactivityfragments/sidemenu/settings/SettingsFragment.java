package com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu.settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.activitys.MainActivity;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.daos.UserInfosDao;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.databinding.FragmentSettingsBinding;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogNotifyFragment;
import com.alexandreconrado.easylife.fragments.alertDialogFragments.AlertDialogQuestionFragment;
import com.alexandreconrado.easylife.scripts.CustomAlertDialogFragment;
import com.alexandreconrado.easylife.scripts.niceswitch.NiceSwitch;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SettingsFragment extends Fragment implements
        CustomAlertDialogFragment.ConfirmButtonClickAlertDialogQuestionFrag,
        CustomAlertDialogFragment.CancelButtonClickAlertDialogQuestionFrag {

    private FragmentSettingsBinding binding;
    private MainActivity parent;
    private UserInfosEntity userInfos;
    private SettingsFragment THIS;
    private boolean changed = false, needRestart = false, showTutorials = false, vibration = true, protectionMode = true;
    private String lang = "en-US", theme = "system_default", autoBackups = "monthly";
    private LocalDataBase localDataBase;
    private UserInfosDao userInfosDao;

    private ExitSettingsFrag exitListenner;
    public interface ExitSettingsFrag{
        void onExitSettingsFrag(boolean changed);
    }

    public SettingsFragment() {
        // Required empty public constructor
    }
    public SettingsFragment(MainActivity parent) {
        this.parent = parent;
    }
    public SettingsFragment(MainActivity parent, UserInfosEntity userInfos) {
        this.parent = parent;
        this.userInfos = userInfos;
    }
    public SettingsFragment(MainActivity parent, UserInfosEntity userInfos, ExitSettingsFrag exitListenner) {
        this.parent = parent;
        this.userInfos = userInfos;
        this.exitListenner = exitListenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater);

        SharedPreferences prefs = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        showTutorials = prefs.getBoolean("hideTutorials", true);
        protectionMode = prefs.getBoolean("protectionMode", true);
        vibration = prefs.getBoolean("vibration", true);

        init();
        disableBackPressed();
        setupLocalDataBase();
        setupExitButton();
        setupSwitchs();
        setupSpinners();

        return binding.getRoot();
    }
    private void init(){
        THIS = this;

        loadSpinners();
        loadUserSettings();
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        userInfosDao = localDataBase.userInfosDao();
    }
    private void disableBackPressed(){
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    binding.imageViewButtonExitFragSettings.performClick();
                    return true;
                }
                return false;
            }
        });
    }
    private void loadSpinners(){
        List<String> entries = Arrays.asList(getResources().getStringArray(R.array.lang_options));
        binding.spinnerLanguageFragSettings.setItems(entries);

        entries = Arrays.asList(getResources().getStringArray(R.array.theme_options));
        binding.spinnerThemeFragSettings.setItems(entries);

        entries = Arrays.asList(getResources().getStringArray(R.array.autoBackups_options));
        binding.spinnerAutoBackupsFragSettings.setItems(entries);
    }
    private void loadUserSettings(){
        int themeIndex = 0;
        switch (userInfos.Theme){
            case "light":
                themeIndex = 0;
                theme = "light";
                break;
            case "dark":
                themeIndex = 1;
                theme = "dark";
                break;
            case "system_default":
                themeIndex = 2;
                theme = "system_default";
                break;
        }

        binding.spinnerThemeFragSettings.selectItemByIndex(themeIndex);

        int langIndex = 0;
        switch (userInfos.Language.toLanguageTag()){
            case "en-us":
                langIndex = 0;
                lang = "en-us";
                break;
            case "en-gb":
                langIndex = 1;
                lang = "en-gb";
                break;
            case "pt-pt":
                langIndex = 2;
                lang = "pt-pt";
                break;
            case "pt-br":
                // Portuguese (Brazil)
                break;
        }

        binding.spinnerLanguageFragSettings.selectItemByIndex(langIndex);


        SharedPreferences prefs = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        autoBackups = prefs.getString("autoBackupTime", "monthly");
        int backupsIndex = 0;
        switch (autoBackups){
            case "weekly":
                backupsIndex = 0;
                break;
            case "biweekly":
                backupsIndex = 1;
                break;
            case "monthly":
                backupsIndex = 2;
                break;
        }

        binding.spinnerAutoBackupsFragSettings.selectItemByIndex(backupsIndex);

        new CountDownTimer(350, 1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                binding.switchHowToFragSettings.setChecked(showTutorials);
                binding.switchVibrationFragSettings.setChecked(vibration);
                binding.switchProtectionModeFragSettings.setChecked(protectionMode);
            }
        }.start();
    }
    private void saveSettings(){
        SharedPreferences sharedPreferences = parent.getSharedPreferences("Perf_User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vibration", vibration);
        editor.putBoolean("hideTutorials", showTutorials);
        editor.putBoolean("protectionMode", protectionMode);
        editor.putString("theme_preference", theme);
        editor.putString("user_language", lang);
        editor.putString("autoBackupTime", autoBackups);
        editor.apply();
        Locale userLocale = new Locale(lang);
        userInfos.Language = userLocale;
        userInfos.Theme = theme;
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changed){
                    CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
                    customAlertDialogFragment.setCancelListenner(THIS);
                    customAlertDialogFragment.setConfirmListenner(THIS);
                    AlertDialogQuestionFragment fragment = new AlertDialogQuestionFragment(getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Title), getString(R.string.general_AlertDialog_Question_SaveBeforeLeaving_Text), customAlertDialogFragment, customAlertDialogFragment, "3");
                    customAlertDialogFragment.setCustomFragment(fragment);
                    customAlertDialogFragment.setTag("FragSettings_Exit");
                    customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
                }else{
                    exitListenner.onExitSettingsFrag(false);
                }
            }
        });
    }
    private void setupSwitchs(){
        binding.switchHowToFragSettings.setOnCheckedChangedListener(new NiceSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean checked) {
                changed = true;
                showTutorials = checked;
            }
        });
        binding.switchVibrationFragSettings.setOnCheckedChangedListener(new NiceSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean checked) {
                changed = true;
                vibration = checked;
            }
        });
        binding.switchProtectionModeFragSettings.setOnCheckedChangedListener(new NiceSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(boolean checked) {
                changed = true;
                protectionMode = checked;
            }
        });
    }
    private void setupSpinners(){
        binding.spinnerThemeFragSettings.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                changed = true;
                needRestart = true;

                switch (i1){
                    case 0:
                        theme = "light";
                        break;
                    case 1:
                        theme = "dark";
                        break;
                    case 2:
                        theme = "system_default";
                        break;
                }
            }
        });
        binding.spinnerLanguageFragSettings.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                changed = true;
                needRestart = true;

                switch (i1){
                    case 0:
                        lang = "en-us";
                        break;
                    case 1:
                        lang = "en-gb";
                        break;
                    case 2:
                        lang = "pt-pt";
                        break;
                }
            }
        });
        binding.spinnerAutoBackupsFragSettings.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                changed = true;

                switch (i1){
                    case 0:
                        autoBackups = "never";
                        break;
                    case 1:
                        autoBackups = "weekly";
                        break;
                    case 2:
                        autoBackups = "biweekly";
                        break;
                    case 3:
                        autoBackups = "monthly";
                        break;
                }
            }
        });
    }
    @Override
    public void onConfirmButtonClicked(String Tag) {
        switch (Tag){
            case "FragSettings_Exit":
                saveSettings();
                if(needRestart){
                    new LocalDatabaseUpdateInfosTask().execute();
                }else{
                    exitListenner.onExitSettingsFrag(true);
                }
                break;
        }
    }
    @Override
    public void onCancelButtonClicked(String Tag) {
        switch (Tag){
            case "FragSettings_Exit":
                exitListenner.onExitSettingsFrag(false);
                break;
        }
    }
    private void appRestart(){
        CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
        AlertDialogNotifyFragment fragment = new AlertDialogNotifyFragment(getString(R.string.settingsFrag_AlertDialog_Notify_NeedRestart_Text), getString(R.string.settingsFrag_AlertDialog_Notify_NeedRestart_Title), customAlertDialogFragment);
        customAlertDialogFragment.setCustomFragment(fragment);
        customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");

        new CountDownTimer(1500, 1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (parent != null) {
                    parent.finishAffinity();
                }
            }
        }.start();
    }
    private class LocalDatabaseUpdateInfosTask extends AsyncTask<Void, Void, UserInfosEntity> {
        @Override
        protected UserInfosEntity doInBackground(Void... voids) {
            userInfosDao.update(userInfos);

            return userInfos;
        }

        @Override
        protected void onPostExecute(UserInfosEntity object) {
            appRestart();
        }
    }
}