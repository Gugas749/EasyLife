package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.howto;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountBinding;
import com.alexandreconrado.easylife.databinding.FragmentMainACOverviewViewSpendingsAccountDetailsHowToEditTitleBinding;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.MainACOverviewViewSpendingAccountDetailsFormFragment;

public class MainACOverviewViewSpendingsAccountDetailsHowToEditTitleFragment extends Fragment {

    private FragmentMainACOverviewViewSpendingsAccountDetailsHowToEditTitleBinding binding;
    private MainACOverviewViewSpendingAccountDetailsFormFragment parent;
    private Boolean fromNext;
    private String nextButtonState = "base";
    private Boolean stopAnims = false, initialSaveAnimationIsFinished = false;

    public MainACOverviewViewSpendingsAccountDetailsHowToEditTitleFragment() {
        // Required empty public constructor
    }
    public MainACOverviewViewSpendingsAccountDetailsHowToEditTitleFragment(MainACOverviewViewSpendingAccountDetailsFormFragment parent, Boolean fromNext) {
        this.parent = parent;
        this.fromNext = fromNext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewSpendingsAccountDetailsHowToEditTitleBinding.inflate(inflater);

        fadeInAnimation(binding.frameLayoutParentFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);

        if(fromNext){
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator3FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator3FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator2FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator2FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
        } else {
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator1FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator1FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator2FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator2FragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
        }

        new CountDownTimer(700, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                longPressAnimation();
            }
        }.start();

        setupNextButton();
        setupPreviousButton();
        disableBackPressed();

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

    private void setupPreviousButton(){
        binding.textViewPreviousFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewPreviousFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        fadeOutAnimation(binding.frameLayoutParentFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
                        parent.changeHowToFragment("OverviewViewSpendingAccountDetails_HowTo_goToEditMode", true);
                        break;
                }
                binding.textViewPreviousFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setEnabled(true);
            }
        });
    }
    private void setupNextButton(){
        binding.textViewNextFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewNextFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        stopAnims = true;
                        fadeOutEveryTimeAnimation(binding.frameLayoutParentFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
                        parent.changeHowToFragment("OverviewViewSpendingAccountDetails_HowTo_goToAddCategory", false);
                        break;
                }
                binding.textViewNextFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setEnabled(true);
            }
        });
    }
    private void longPressAnimation(){
        if(!stopAnims){
            fadeInAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
            fadeInAnimation(binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm);
            pointYAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle, getResources().getDimension(com.intuit.sdp.R.dimen._minus20sdp));
            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setImageDrawable(getResources().getDrawable(R.drawable.hand_tapping));
                    new CountDownTimer(350, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle.setImageDrawable(getResources().getDrawable(R.drawable.hand_pointing));

                            new CountDownTimer(1000, 1000) {
                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle);
                                    fadeOutAnimation(binding.cardViewEditTextAccountNameHolderFragMainACOverviewViewSpendingAccountDetailsForm);

                                    new CountDownTimer(700, 1000) {
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            pointYAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle, 0);
                                            pointBackToStartPosAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditTitle, 0, 0);
                                            longPressAnimation();
                                        }
                                    }.start();
                                }
                            }.start();
                        }
                    }.start();
                }
            }.start();
        }
    }
    private void pointBackToStartPosAnimation(View view, float value, float initialValue) {
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", initialValue, value);
        translateXAnimator.setDuration(50);
        translateXAnimator.setInterpolator(new LinearInterpolator());
        translateXAnimator.start();
    }
    private void pointYAnimation(View view, float value) {
        if(!stopAnims){
            ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, value);
            translateYAnimator.setDuration(800);
            translateYAnimator.setInterpolator(new LinearInterpolator());
            translateYAnimator.start();
        }
    }
    private void fadeInAnimation(View view){
        if(!stopAnims){
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
    }
    private void fadeOutAnimation(View view){
        if(!stopAnims){
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
    }
    private void alphaAnimation(View view, float value, float initialValue){
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, initialValue, value);
        alphaAnimator.setDuration(500);
        alphaAnimator.start();
    }
    private void fadeOutEveryTimeAnimation(View view){
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
    private void downwardsAnimation(View view, float value, float initialValue) {
        if(!stopAnims){
            ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", initialValue, value);
            translateYAnimator.setDuration(1000); // Set the duration of the animation in milliseconds

            translateYAnimator.start();
        }
    }
    private void forwardAnimation(View view, float value, float initialValue) {
        if(!stopAnims){
            ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", initialValue, value);
            translateXAnimator.setDuration(1000);
            translateXAnimator.setInterpolator(new LinearInterpolator());

            translateXAnimator.start();
        }
    }
    private void fragIndicatorScaleDownAnimation(CardView cardView){
        int initialWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(com.intuit.sdp.R.dimen._7sdp), getResources().getDisplayMetrics());
        int finalWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(com.intuit.sdp.R.dimen._2sdp), getResources().getDisplayMetrics());

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
        int initialWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(com.intuit.sdp.R.dimen._2sdp), getResources().getDisplayMetrics());
        int finalWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(com.intuit.sdp.R.dimen._7sdp), getResources().getDisplayMetrics());

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
    private void colorChangePointingAnimation(ImageView imageView, int initialColor, int finalColor){
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), initialColor, finalColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int animatedColor = (int) animator.getAnimatedValue();
                imageView.setColorFilter(animatedColor, PorterDuff.Mode.SRC_ATOP);
            }
        });

        colorAnimator.setDuration(1000);
        colorAnimator.start();
    }
}