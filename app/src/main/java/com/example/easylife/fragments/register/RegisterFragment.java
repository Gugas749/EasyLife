package com.example.easylife.fragments.register;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.example.easylife.R;
import com.example.easylife.activitys.SplashActivity;
import com.example.easylife.databinding.FragmentRegisterBinding;
import com.example.easylife.scripts.BiometricChecker;

public class RegisterFragment extends Fragment {

    private SplashActivity parent;
    private FragmentRegisterBinding binding;
    private int currentDialogFragment = 0;
    private RegisterPasswordDialogFragment registerPasswordDialogFragment;
    private RegisterDialogBiometricFragment registerDialogBiometricFragment;
    private RegisterDialogFaceIDFragment registerDialogFaceIDFragment;

    public RegisterFragment(SplashActivity parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);

        setupDialogFragments();
        changeDialogFragments();
        startFragAnimations();

        return binding.getRoot();
    }

    //------------------SETUPS----------------------
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle back press here
                // Leave this method empty to prevent the fragment from closing
            }
        });
    }
    private void setupDialogFragments(){
        registerPasswordDialogFragment = new RegisterPasswordDialogFragment(this);
        registerDialogBiometricFragment = new RegisterDialogBiometricFragment(this);
        registerDialogFaceIDFragment = new RegisterDialogFaceIDFragment(this);
    }
    public void changeDialogFragments(){
        switch (currentDialogFragment){
            case 0:
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_Dialog_RegisterFrag, registerPasswordDialogFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentDialogFragment++;
                break;

            case 1:
                BiometricChecker biometricChecker = new BiometricChecker(getContext());

                if(biometricChecker.isBiometricSupported()){
                    if(biometricChecker.isFingerprintSupported()){
                        fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container_Dialog_RegisterFrag, registerDialogBiometricFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        currentDialogFragment++;
                    }
                    else{
                        currentDialogFragment++;
                        changeDialogFragments();
                    }
                }else{
                    finishedRegister();
                }
                break;

            case 2:
                biometricChecker = new BiometricChecker(getContext());

                if(biometricChecker.isFaceIdSupported()){
                    fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_Dialog_RegisterFrag, registerDialogFaceIDFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    currentDialogFragment++;
                }else{
                    finishedRegister();
                }
                break;

            case 3:
                finishedRegister();
                break;
        }
    }
    private void finishedRegister(){
        fadeOutRegisterDialog();
        scaleUpAnimtion();
        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                parent.goToMainActivity();
            }
        }.start();
    }
    //----------------------------------------------
    //----------------ANIMATIONS--------------------
    private void startFragAnimations(){
        binding.cardviewDialogLoginFrag.setVisibility(View.INVISIBLE);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(android.R.attr.colorControlNormal, typedValue, true);
        @ColorInt int color = typedValue.data;
        binding.frameLayoutFragRegister.setBackgroundColor(color);
        scaleDownAnimtion();
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                fadeInRegisterDialog();
            }
        }.start();
    }
    private void fadeInRegisterDialog(){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.cardviewDialogLoginFrag.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.cardviewDialogLoginFrag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeOutRegisterDialog(){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        binding.cardviewDialogLoginFrag.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.cardviewDialogLoginFrag.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void scaleDownAnimtion(){
        float startScale = 18.0f;
        float endScale = 1.0f;

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        scaleAnimation.setDuration(1000);

        binding.cardviewAuxiliarLoginFrag.startAnimation(scaleAnimation);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        @ColorInt int color = typedValue.data;
        binding.frameLayoutFragRegister.setBackgroundColor(color);
    }
    private void scaleUpAnimtion(){
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(binding.cardviewAuxiliarLoginFrag, "scaleX", 1f, 18f);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(binding.cardviewAuxiliarLoginFrag, "scaleY", 1f, 18f);

        binding.cardviewAuxiliarLoginFrag.setPivotX(binding.cardviewAuxiliarLoginFrag.getWidth() / 2);
        binding.cardviewAuxiliarLoginFrag.setPivotY(binding.cardviewAuxiliarLoginFrag.getHeight() / 2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
    //----------------------------------------------
}