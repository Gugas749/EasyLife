package com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentCreditsBinding;

public class CreditsFragment extends Fragment {

    private FragmentCreditsBinding binding;
    private ExitCreditsFrag exitListenner;
    public interface ExitCreditsFrag{
        void onExitCreditsFrag();
    }

    public CreditsFragment() {
        // Required empty public constructor
    }
    public CreditsFragment(ExitCreditsFrag exitListenner) {
        this.exitListenner = exitListenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreditsBinding.inflate(inflater);

        disableBackPressed();
        setupExitButton();

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
    private void setupExitButton(){
        binding.imageViewButtonExitFragCreditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitListenner.onExitCreditsFrag();
            }
        });
    }
}