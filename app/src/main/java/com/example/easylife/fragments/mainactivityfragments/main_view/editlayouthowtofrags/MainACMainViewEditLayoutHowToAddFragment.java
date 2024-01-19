package com.example.easylife.fragments.mainactivityfragments.main_view.editlayouthowtofrags;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentMainACMainViewEditLayoutHowToAddBinding;
import com.example.easylife.fragments.mainactivityfragments.main_view.MainACMainViewEditLayoutFragment;

public class MainACMainViewEditLayoutHowToAddFragment extends Fragment {

    private FragmentMainACMainViewEditLayoutHowToAddBinding binding;
    private String nextButtonState = "base";
    private MainACMainViewEditLayoutFragment parent;
    private Boolean fromNext;

    public MainACMainViewEditLayoutHowToAddFragment(MainACMainViewEditLayoutFragment parent, Boolean fromNext) {
        this.parent = parent;
        this.fromNext = fromNext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewEditLayoutHowToAddBinding.inflate(inflater);

        fadeInAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToAdd);

        if(fromNext){
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator3FragMainACMainViewEditLayoutHowToAdd);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator3FragMainACMainViewEditLayoutHowToAdd);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToAdd);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToAdd);
        } else {
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator1FragMainACMainViewEditLayoutHowToAdd);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator1FragMainACMainViewEditLayoutHowToAdd);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToAdd);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToAdd);
        }

        setupNextButton();
        setupPreviousButton();

        return binding.getRoot();
    }
    private void setupPreviousButton(){
        binding.textViewPreviousFragMainACMainViewEditLayoutHowToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewPreviousFragMainACMainViewEditLayoutHowToAdd.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        fadeOutAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToAdd);
                        parent.changeHowToFragment("goToHome", true);
                        break;
                    case "fase1":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd);
                                binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd.setVisibility(View.GONE);
                                fadeOutAnimation(binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd);
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);

                                pointYAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd, 20);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_1));
                                binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd.setVisibility(View.GONE);
                                binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd.setImageDrawable(null);
                            }
                        }.start();
                        nextButtonState = "base";
                        break;
                    case "fase2":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        forwardAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd, 0 , 360);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);

                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_2));
                            }
                        }.start();
                        nextButtonState = "fase1";
                        break;
                    case "fase3":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        forwardAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd, 360, 720);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);

                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_3));
                            }
                        }.start();
                        nextButtonState = "fase2";
                        break;
                    case "fase4":
                        fadeOutAnimation(binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd);
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                                fadeInAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd);
                                fadeInAnimation(binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_4));
                            }
                        }.start();
                        nextButtonState = "fase3";
                        break;
                }
                binding.textViewPreviousFragMainACMainViewEditLayoutHowToAdd.setEnabled(true);
            }
        });
    }
    private void setupNextButton(){
        binding.textViewNextFragMainACMainViewEditLayoutHowToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewNextFragMainACMainViewEditLayoutHowToAdd.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd.setVisibility(View.INVISIBLE);
                                fadeInAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd);
                                binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd.setVisibility(View.INVISIBLE);
                                fadeInAnimation(binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd);
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);

                                pointYAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd, 20);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_2));
                                binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd.setImageDrawable(getResources().getDrawable(R.drawable.bottomnav_howto_layoutedit_lightmode));
                            }
                        }.start();
                        nextButtonState = "fase1";
                        break;
                    case "fase1":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        forwardAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd, 360 , 0);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);

                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_3));
                            }
                        }.start();
                        nextButtonState = "fase2";
                        break;
                    case "fase2":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        forwardAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd, 720, 360);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);

                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_4));
                            }
                        }.start();
                        nextButtonState = "fase3";
                        break;
                    case "fase3":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                        fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToAdd);
                        fadeOutAnimation(binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd);
                                fadeInAnimation(binding.imageViewApresentationFragMainACMainViewEditLayoutHowToAdd);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToAdd.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Add_Text_5));
                            }
                        }.start();
                        nextButtonState = "fase4";
                        break;
                    case "fase4":
                        fadeOutAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToAdd);
                        parent.changeHowToFragment("goToDragNDrop", false);
                        break;
                }
                binding.textViewNextFragMainACMainViewEditLayoutHowToAdd.setEnabled(true);
            }
        });
    }
    private void pointAnimation(View view, float value) {
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, value);
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, value);

        translateXAnimator.setDuration(800);
        translateYAnimator.setDuration(800);

        translateXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        translateYAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        translateXAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        translateYAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        translateXAnimator.setInterpolator(new LinearInterpolator());
        translateYAnimator.setInterpolator(new LinearInterpolator());

        translateXAnimator.start();
        translateYAnimator.start();
    }
    private void pointYAnimation(View view, float value) {
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, value);
        translateYAnimator.setDuration(800);
        translateYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        translateYAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        translateYAnimator.setInterpolator(new LinearInterpolator());
        translateYAnimator.start();
    }
    private void fadeInAnimation(View view){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        view.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeOutAnimation(View view){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        view.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void upwardsAnimation(View view, float value) {
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, value);
        translateYAnimator.setDuration(1000); // Set the duration of the animation in milliseconds

        translateYAnimator.start();
    }
    private void downwardsAnimation(View view, float value) {
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, value);
        translateYAnimator.setDuration(1000); // Set the duration of the animation in milliseconds

        translateYAnimator.start();
    }
    private void forwardAnimation(View view, float value, float initialValue) {
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", initialValue, value);
        translateXAnimator.setDuration(1000); // Set the duration of each phase in milliseconds
        translateXAnimator.setInterpolator(new LinearInterpolator()); // Use a linear interpolator for constant speed

        translateXAnimator.start();
    }
    private void fragIndicatorScaleDownAnimation(CardView cardView){
        int initialWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, getResources().getDisplayMetrics());
        int finalWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 7, getResources().getDisplayMetrics());

        ValueAnimator animator = ValueAnimator.ofInt(initialWidth, finalWidth);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Get the animated value and set it as the new width
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = cardView.getLayoutParams();
                params.width = animatedValue;
                cardView.setLayoutParams(params);
            }
        });

        animator.setDuration(1000);
        animator.start();
    }
    private void fragIndicatorScaleUpAnimation(CardView cardView){
        int initialWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 7, getResources().getDisplayMetrics());
        int finalWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, getResources().getDisplayMetrics());

        ValueAnimator animator = ValueAnimator.ofInt(initialWidth, finalWidth);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Get the animated value and set it as the new width
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = cardView.getLayoutParams();
                params.width = animatedValue;
                cardView.setLayoutParams(params);
            }
        });

        animator.setDuration(1000);
        animator.start();
    }
    private void colorChangeFadeOutAnimation(CardView cardView){
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
        int color = typedValue.data;
        int initialColor = color;

        typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        color = typedValue.data;
        int finalColor = color;

        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(cardView, "cardBackgroundColor", new ArgbEvaluator(), initialColor, finalColor);

        colorAnimator.setDuration(1000);
        colorAnimator.start();

    }
    private void colorChangeFadeInAnimation(CardView cardView){
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        int color = typedValue.data;
        int initialColor = color;

        typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
        color = typedValue.data;
        int finalColor = color;

        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(cardView, "cardBackgroundColor", new ArgbEvaluator(), initialColor, finalColor);

        colorAnimator.setDuration(1000);
        colorAnimator.start();

    }
}