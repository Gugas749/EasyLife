package com.example.easylife.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentManager;

import android.animation.AnimatorSet;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.databinding.ActivityMainBinding;
import com.example.easylife.fragments.tutorial.TutorialAddFragment;
import com.example.easylife.fragments.tutorial.TutorialEditFragment;
import com.example.easylife.fragments.tutorial.TutorialEndFragment;
import com.example.easylife.fragments.tutorial.TutorialShowFragment;
import com.example.easylife.fragments.tutorial.TutorialWelcomeFragment;
import com.example.easylife.scripts.CustomAlertDialogFragment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Executor executor = Executors.newSingleThreadExecutor();
    private long sessionTime;
    private boolean seenTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("Perf_User", MODE_PRIVATE);
        seenTutorial = prefs.getBoolean("seenTutorial", false);

        if(seenTutorial){
            sessionTime = System.currentTimeMillis();

            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    startFragAnimations();
                }
            }.start();
        } else{
            inFragment();
            binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setBackground(null);
            tutorialChangeFragments(0, false);
        }
    }

    //-----------------FUNCTIONS---------------------
    @Override
    public void onResume() {
        super.onResume();
        if(seenTutorial){
            if(getSession()){ // SESSION STILL AVALIABLE

            }else{// SESSION NOT AVALIABLE
                showBiometricPrompt();
            }
        }
    }
    private boolean getSession(){
        boolean output = false;
        long currentTimeInMillis = System.currentTimeMillis();
        long fiveMinutesInMillis = 2 * 60 * 1000;
        if (currentTimeInMillis - sessionTime >= fiveMinutesInMillis) {
            output = false;
        } else {
            sessionTime = System.currentTimeMillis();
            output = true;
        }
        return output;
    }
    private void inFragment(){
        binding.imageViewAux1MainActivity.setVisibility(View.INVISIBLE);
        binding.bottomNavigationViewPainel.setVisibility(View.INVISIBLE);
        binding.cardViewTopNavigationMainAc.setVisibility(View.INVISIBLE);

        binding.imageViewAux1MainActivity.setEnabled(false);
        binding.bottomNavigationViewPainel.setEnabled(false);
        binding.cardViewTopNavigationMainAc.setEnabled(false);
    }
    private void outFragment(){
        binding.imageViewAux1MainActivity.setVisibility(View.VISIBLE);
        binding.bottomNavigationViewPainel.setVisibility(View.VISIBLE);
        binding.cardViewTopNavigationMainAc.setVisibility(View.VISIBLE);

        binding.imageViewAux1MainActivity.setEnabled(true);
        binding.bottomNavigationViewPainel.setEnabled(true);
        binding.cardViewTopNavigationMainAc.setEnabled(true);
    }
    public void tutorialChangeFragments(int selected, boolean fromNextFragment){
        switch (selected){
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialWelcomeFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialAddFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialShowFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialEditFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialEndFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setEnabled(false);

                SharedPreferences sharedPreferences = getSharedPreferences("Perf_User", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("seenTutorial", true);
                editor.apply();

                outFragment();
                break;
        }
    }
    //----------------------------------------------

    //----------BIOMETRICS----------------
    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Face ID Login")
                .setSubtitle("Place your face in front of the camera")
                .setNegativeButtonText("Cancel")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Implement your logic for a successful login
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        CustomAlertDialogFragment customDialogFragment = new CustomAlertDialogFragment();
                        customDialogFragment.show(getSupportFragmentManager(), "custom_dialog");
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
    //----------------------------------------------

    //----------------ANIMATIONS--------------------
  /*  private void startFragAnimations(){
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
    }*/
    private void startFragAnimations(){
        binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setVisibility(View.INVISIBLE);
        binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setEnabled(false);
        scaleDownAnimtion();
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                binding.imageViewAux1MainActivity.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
    private void scaleDownAnimtion(){
        float startScale = 200.0f;
        float endScale = 1.0f;

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        scaleAnimation.setDuration(1500);

        binding.imageViewAux1MainActivity.startAnimation(scaleAnimation);
    }
    private void scaleUpAnimtion(){
       // ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(binding.cardviewAuxiliarLoginFrag, "scaleX", 1f, 18f);
       // ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(binding.cardviewAuxiliarLoginFrag, "scaleY", 1f, 18f);

       // binding.cardviewAuxiliarLoginFrag.setPivotX(binding.cardviewAuxiliarLoginFrag.getWidth() / 2);
       // binding.cardviewAuxiliarLoginFrag.setPivotY(binding.cardviewAuxiliarLoginFrag.getHeight() / 2);

        AnimatorSet animatorSet = new AnimatorSet();
      //  animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
    //----------------------------------------------
}