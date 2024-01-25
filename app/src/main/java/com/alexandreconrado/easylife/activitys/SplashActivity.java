package com.alexandreconrado.easylife.activitys;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.ActivitySplashBinding;
import com.alexandreconrado.easylife.fragments.register.RegisterFragment;
import com.alexandreconrado.easylife.scripts.ocr.TextRecognitionUtil;

public class SplashActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ActivitySplashBinding binding;
    private Boolean isLogged;
    private Boolean inScan;

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
        inScan = false;
        setupScanButton();

        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(!inScan){
                    initAnimation();
                }
            }
        }.start();
    }

    //------------------SETUPS----------------------
    private void initRegisterFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_splashAc, new RegisterFragment(this)).commit();
    }
    private void initAnimation(){
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
                            if(!inScan){
                                finish();
                            }
                        }else{
                            initRegisterFragment();
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

    //----------------SCAN BUTTON--------------------
    private void setupScanButton(){
        binding.butScanSplashAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    inScan = true;
                    dispatchTakePictureIntent();
                }
            }
        });
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                String detectedText = TextRecognitionUtil.extractTextFromBitmap(imageBitmap, this);

                // Agora, 'detectedText' contém o texto detectado na imagem
                // Faça o que precisar com o texto (por exemplo, exiba em um TextView)
                // ...
                Log.i("LOG_texto", "TEXTO: "+detectedText);
                Toast.makeText(this, detectedText, Toast.LENGTH_LONG).show();
            }
        }
    }
    //----------------------------------------------

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Check for theme change and recreate the activity if needed
        if (newConfig.uiMode != getApplicationContext().getResources().getConfiguration().uiMode) {
            recreate();
        }
    }
}