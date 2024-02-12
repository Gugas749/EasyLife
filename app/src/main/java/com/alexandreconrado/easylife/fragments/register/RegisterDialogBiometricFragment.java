package com.alexandreconrado.easylife.fragments.register;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.databinding.FragmentRegisterDialogBiometricBinding;

public class RegisterDialogBiometricFragment extends Fragment {

    private FragmentRegisterDialogBiometricBinding binding;
    private RegisterFragment parent;

    public RegisterDialogBiometricFragment() {

    }
    public RegisterDialogBiometricFragment(RegisterFragment parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterDialogBiometricBinding.inflate(inflater);

        binding.butConfirmRegisterBiometricFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.butConfirmRegisterBiometricFrag.setEnabled(false);
                buttonReturn(true);
            }
        });

        binding.butCancelRegisterBiometricFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonReturn(false);
            }
        });

        return binding.getRoot();
    }

    private void buttonReturn(Boolean option){
        SharedPreferences prefs = getContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("biometricLoginPermission", option);
        editor.apply();

        parent.changeDialogFragments("");
    }
}