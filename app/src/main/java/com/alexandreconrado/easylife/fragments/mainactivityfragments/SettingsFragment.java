package com.alexandreconrado.easylife.fragments.mainactivityfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.activitys.MainActivity;
import com.alexandreconrado.easylife.database.daos.UserInfosDao;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.databinding.FragmentSettingsBinding;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private MainActivity parent;
    private UserInfosEntity userInfos;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater);

        disableBackPressed();
        loadSpinners();
        loadUserSettings();

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
    private void loadSpinners(){
        List<String> entries = Arrays.asList(getResources().getStringArray(R.array.lang_options));
        binding.spinnerLanguageFragSettings.setItems(entries);

        entries = Arrays.asList(getResources().getStringArray(R.array.theme_options));
        binding.spinnerThemeFragSettings.setItems(entries);
    }
    private void loadUserSettings(){
        int themeIndex = 0;
        switch (userInfos.Theme){
            case "Light":
                themeIndex = 0;
                break;
            case "Dark":
                themeIndex = 1;
                break;
            case "System-Sync":
                themeIndex = 2;
                break;
        }

        binding.spinnerThemeFragSettings.selectItemByIndex(themeIndex);

        int langIndex = 0;
        switch (userInfos.Language.toLanguageTag()){
            case "en-US":
                langIndex = 0;
                break;
            case "en-GB":
                // English (United Kingdom)
                break;
            case "pt-PT":
                langIndex = 1;
                break;
            case "pt-BR":
                // Portuguese (Brazil)
                break;
        }

        binding.spinnerLanguageFragSettings.selectItemByIndex(langIndex);
    }
}