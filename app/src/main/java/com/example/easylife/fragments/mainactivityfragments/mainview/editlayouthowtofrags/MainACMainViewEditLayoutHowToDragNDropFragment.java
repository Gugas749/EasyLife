package com.example.easylife.fragments.mainactivityfragments.mainview.editlayouthowtofrags;

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
import com.example.easylife.databinding.FragmentMainACMainViewEditLayoutHowToDragNDropBinding;
import com.example.easylife.fragments.mainactivityfragments.mainview.MainACMainViewEditLayoutFragment;

public class MainACMainViewEditLayoutHowToDragNDropFragment extends Fragment {

    private FragmentMainACMainViewEditLayoutHowToDragNDropBinding binding;
    private MainACMainViewEditLayoutFragment parent;
    private String nextButtonState = "base";
    private Boolean fromNext;

    public MainACMainViewEditLayoutHowToDragNDropFragment(MainACMainViewEditLayoutFragment parent, Boolean fromNext) {
        this.parent = parent;
        this.fromNext = fromNext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewEditLayoutHowToDragNDropBinding.inflate(inflater);

        fadeInAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToDragNDrop);

        if(fromNext){
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator4FragMainACMainViewEditLayoutHowToDragNDrop);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator4FragMainACMainViewEditLayoutHowToDragNDrop);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator3FragMainACMainViewEditLayoutHowToDragNDrop);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator3FragMainACMainViewEditLayoutHowToDragNDrop);
        } else {
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToDragNDrop);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToDragNDrop);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator3FragMainACMainViewEditLayoutHowToDragNDrop);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator3FragMainACMainViewEditLayoutHowToDragNDrop);
        }

        new CountDownTimer(1200, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                initialDragAnimation();
            }
        }.start();

        setupNextButton();
        setupPreviousButton();

        return binding.getRoot();
    }
    private void setupPreviousButton(){
        binding.textViewPreviousFragMainACMainViewEditLayoutHowToDragNDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewPreviousFragMainACMainViewEditLayoutHowToDragNDrop.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        fadeOutAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToDragNDrop);
                        parent.changeHowToFragment("goToAdd", true);
                        break;
                    case "fase1":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToDragNDrop);
                        fadeOutAnimation(binding.cardViewDnDExplanationFragMainACMainViewEditLayoutHowToDragNDrop);
                        fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToDragNDrop);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToDragNDrop);
                                fadeInAnimation(binding.cardViewDnDExplanationFragMainACMainViewEditLayoutHowToDragNDrop);
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToDragNDrop);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToDragNDrop.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_DnD_Text_2));
                            }
                        }.start();
                        nextButtonState = "base";
                        break;
                }
                binding.textViewPreviousFragMainACMainViewEditLayoutHowToDragNDrop.setEnabled(true);
            }
        });
    }
    private void setupNextButton(){
        binding.textViewNextFragMainACMainViewEditLayoutHowToDragNDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewNextFragMainACMainViewEditLayoutHowToDragNDrop.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToDragNDrop);
                        fadeOutAnimation(binding.cardViewDnDExplanationFragMainACMainViewEditLayoutHowToDragNDrop);
                        fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToDragNDrop);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToDragNDrop);
                                fadeInAnimation(binding.cardViewDnDExplanationFragMainACMainViewEditLayoutHowToDragNDrop);
                                fadeInAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToDragNDrop);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToDragNDrop.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_DnD_Text_2));
                            }
                        }.start();
                        nextButtonState = "fase1";
                        break;
                    case "fase1":
                        fadeOutAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToDragNDrop);
                        parent.changeHowToFragment("goToDelete", false);
                        break;
                }
                binding.textViewNextFragMainACMainViewEditLayoutHowToDragNDrop.setEnabled(true);
            }
        });
    }
    private void initialDragAnimation(){
        pointYAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToDragNDrop, (-50));
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToDragNDrop.setImageDrawable(getResources().getDrawable(R.drawable.hand_tapping));
                forwardBackwardsAnimation(binding.cardViewDnDExplanationFragMainACMainViewEditLayoutHowToDragNDrop, 200, 0);
                forwardBackwardsAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToDragNDrop, 200, 0);
            }
        }.start();
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
    private void forwardBackwardsAnimation(View view, float value, float initialValue) {
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", initialValue, value);
        translateXAnimator.setDuration(1000); // Set the duration of each phase in milliseconds
        translateXAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Repeat indefinitely
        translateXAnimator.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation on each iteration
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