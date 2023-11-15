package com.example.easylife.fragments.register;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.databinding.FragmentRegisterDialogFaceIDBinding;

public class RegisterDialogFaceIDFragment extends Fragment {

    private FragmentRegisterDialogFaceIDBinding binding;
    private RegisterFragment parent;

    public RegisterDialogFaceIDFragment(RegisterFragment parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterDialogFaceIDBinding.inflate(inflater);

        binding.butConfirmRegisterFaceIDFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonReturn(true);
            }
        });

        binding.butCancelRegisterFaceIDFrag.setOnClickListener(new View.OnClickListener() {
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
        editor.putBoolean("faceIdLoginPermission", option);
        editor.apply();

        parent.changeDialogFragments();
    }
}