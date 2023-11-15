package com.example.easylife.activitys;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.easylife.R;
import com.example.easylife.databinding.ActivitySplashBinding;
import com.example.easylife.fragments.register.RegisterFragment;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    Boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("Perf_User", MODE_PRIVATE);
        isLogged = prefs.getBoolean("logged", false);

        if(isLogged){
            binding.butScanSplashAc.setVisibility(View.VISIBLE);
        }else{
            binding.butScanSplashAc.setVisibility(View.INVISIBLE);
        }

        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                iniciateAnimation();
            }
        }.start();
    }

    //------------------SETUPS----------------------
    private void setupScanButton(){

    }
    private void iniciateRegisterFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_splashAc, new RegisterFragment(this)).commit();
    }
    private void iniciateAnimation(){
        snapLogoAnimation();
        fadeOutTextViews();
        if(isLogged){
            fadeOutScanButton();
        }
        new CountDownTimer(1600, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = getApplicationContext().getTheme();
                theme.resolveAttribute(android.R.attr.colorControlNormal, typedValue, true);
                @ColorInt int color = typedValue.data;
                binding.constraintLayoutSplashAc.setBackgroundColor(color);
                fragActive();

                fadeOutSnapLogo();
                new CountDownTimer(600, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        if(isLogged){
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            iniciateRegisterFragment();
                        }
                    }
                }.start();
            }
        }.start();
    }
    private void fragActive(){
        int colorAttr = android.R.attr.colorControlNormal;
        TypedArray ta = obtainStyledAttributes(new TypedValue().data, new int[]{colorAttr});
        int color = ta.getColor(0, 0);
        ta.recycle();
        binding.constraintLayoutSplashAc.setBackgroundColor(color);
        binding.constraintLayoutSplashAc.setBackgroundColor(getColor(R.color.white));

        binding.textView1SplashAc.setVisibility(View.INVISIBLE);
        binding.imageView1SplashAc.setVisibility(View.INVISIBLE);
        binding.textView2SplashAc.setVisibility(View.INVISIBLE);
        binding.cardview1SplashAc.setVisibility(View.INVISIBLE);
        binding.butScanSplashAc.setVisibility(View.INVISIBLE);

        binding.textView1SplashAc.setEnabled(false);
        binding.imageView1SplashAc.setEnabled(false);
        binding.textView2SplashAc.setEnabled(false);
        binding.cardview1SplashAc.setEnabled(false);
        binding.butScanSplashAc.setEnabled(false);
    }
    public void goToMainActivity(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("logged", true);
        editor.apply();

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    //----------------------------------------------

    //----------------ANIMATIONS--------------------
    private void snapLogoAnimation(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        float centerX = screenWidth / 2f - binding.cardview1SplashAc.getWidth() / 2f;
        float centerY = screenHeight / 2f - binding.cardview1SplashAc.getHeight() / 2f;

        binding.cardview1SplashAc.animate()
                .x(centerX)
                .y(centerY)
                .setDuration(1000)
                .start();

        binding.imageView1SplashAc.animate()
                .x(centerX)
                .y(centerY)
                .setDuration(1000)
                .start();
    }
    private void fadeOutTextViews(){
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        binding.textView1SplashAc.startAnimation(fadeOut);
        binding.textView2SplashAc.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textView1SplashAc.setVisibility(View.INVISIBLE);
                binding.textView2SplashAc.setVisibility(View.INVISIBLE);
                extendAnimtion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeOutScanButton(){
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        binding.butScanSplashAc.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.butScanSplashAc.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeOutSnapLogo(){
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out2);
        binding.imageView1SplashAc.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.imageView1SplashAc.setVisibility(View.INVISIBLE);
                extendAnimtion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void extendAnimtion(){
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(binding.cardview1SplashAc, "scaleX", 1f, 9f);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(binding.cardview1SplashAc, "scaleY", 1f, 9f);

        ObjectAnimator scaleAnimatorX2 = ObjectAnimator.ofFloat(binding.imageView1SplashAc, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleAnimatorY2 = ObjectAnimator.ofFloat(binding.imageView1SplashAc, "scaleY", 1f, 1.5f);

        binding.cardview1SplashAc.setPivotX(binding.cardview1SplashAc.getWidth() / 2);
        binding.cardview1SplashAc.setPivotY(binding.cardview1SplashAc.getHeight() / 2);

        binding.imageView1SplashAc.setPivotX(binding.imageView1SplashAc.getWidth() / 2);
        binding.imageView1SplashAc.setPivotY(binding.imageView1SplashAc.getHeight() / 2);

        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(scaleAnimatorX2, scaleAnimatorY2);
        animatorSet2.setDuration(1000);
        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(1000);
        animatorSet2.start();
        animatorSet.start();
    }
    //----------------------------------------------
}