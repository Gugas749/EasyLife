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
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.MainACOverviewViewSpendingAccountDetailsFormFragment;

public class MainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountFragment extends Fragment {
    
    private FragmentMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountBinding binding;
    private MainACOverviewViewSpendingAccountDetailsFormFragment parent;
    private Boolean fromNext;
    private String nextButtonState = "base";
    private Boolean stopAnims = false, initialSaveAnimationIsFinished = false;
    
    public MainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountFragment() {
        // Required empty public constructor
    }
    public MainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountFragment(MainACOverviewViewSpendingAccountDetailsFormFragment parent, Boolean fromNext) {
        this.parent = parent;
        this.fromNext = fromNext;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccountBinding.inflate(inflater);

        fadeInAnimation(binding.frameLayoutParentFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);

        fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator4FragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);
        colorChangeFadeOutAnimation(binding.cardViewFragIndicator4FragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);
        fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator5FragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);
        colorChangeFadeInAnimation(binding.cardViewFragIndicator5FragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);

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
                    binding.textViewPreviousFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupPreviousButton(){
        binding.textViewPreviousFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewPreviousFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        fadeOutAnimation(binding.frameLayoutParentFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);
                        parent.changeHowToFragment("OverviewViewSpendingAccountDetails_HowTo_goToAddSubAccount", true);
                        break;
                }
                binding.textViewPreviousFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setEnabled(true);
            }
        });
    }
    private void setupNextButton(){
        binding.textViewNextFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewNextFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        stopAnims = true;
                        fadeOutEveryTimeAnimation(binding.frameLayoutParentFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);
                        parent.changeHowToFragment("HowTo_finish", false);
                        break;
                }
                binding.textViewNextFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setEnabled(true);
            }
        });
    }
    private void longPressAnimation(){
        if(!stopAnims){
            fadeInAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);
            fadeInAnimation(binding.viewContainerAnimFragMainACOverviewViewHowToDetails.getRoot());
            float value = getResources().getDimension(com.intuit.sdp.R.dimen._70sdp);
            pointYAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount, (-value));
            new CountDownTimer(1300, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    if(!stopAnims){
                        binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setImageDrawable(getResources().getDrawable(R.drawable.hand_tapping));
                        new CountDownTimer(800, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                if(!stopAnims){
                                    binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setImageDrawable(getResources().getDrawable(R.drawable.hand_pointing));
                                    new CountDownTimer(250, 1000) {
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            if(!stopAnims){
                                                binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setImageDrawable(getResources().getDrawable(R.drawable.hand_pointing));
                                                TypedValue typedValue = new TypedValue();
                                                getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
                                                int color = typedValue.data;
                                                int initialColor = color;

                                                typedValue = new TypedValue();
                                                getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.textAppearanceBodySmall, typedValue, true);
                                                color = typedValue.data;
                                                int finalColor = color;

                                                colorChangePointingAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount, initialColor, finalColor);

                                                fadeInAnimation(binding.cardViewPopupExplanationFragMainACMainViewEditLayoutHowToSave);
                                                forwardAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount, getResources().getDimension(com.intuit.sdp.R.dimen._75sdp), 0);
                                                new CountDownTimer(1000, 1000) {
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    public void onFinish() {
                                                        if(!stopAnims){
                                                            downwardsAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount, getResources().getDimension(com.intuit.sdp.R.dimen._minus35sdp), (-value));
                                                            new CountDownTimer(1000, 1000) {
                                                                public void onTick(long millisUntilFinished) {

                                                                }

                                                                public void onFinish() {
                                                                    if(!stopAnims){
                                                                        binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setImageDrawable(getResources().getDrawable(R.drawable.hand_tapping));
                                                                        new CountDownTimer(250, 1000) {
                                                                            public void onTick(long millisUntilFinished) {

                                                                            }

                                                                            public void onFinish() {
                                                                                if(!stopAnims){
                                                                                    binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount.setImageDrawable(getResources().getDrawable(R.drawable.hand_pointing));
                                                                                    new CountDownTimer(1500, 1000) {
                                                                                        public void onTick(long millisUntilFinished) {

                                                                                        }

                                                                                        public void onFinish() {
                                                                                            if(!stopAnims){
                                                                                                fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount);
                                                                                                fadeOutAnimation(binding.viewContainerAnimFragMainACOverviewViewHowToDetails.getRoot());
                                                                                                fadeOutAnimation(binding.cardViewPopupExplanationFragMainACMainViewEditLayoutHowToSave);
                                                                                                new CountDownTimer(500, 1000) {
                                                                                                    public void onTick(long millisUntilFinished) {

                                                                                                    }

                                                                                                    public void onFinish() {
                                                                                                        if(!stopAnims){
                                                                                                            downwardsAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount, 0, 0);
                                                                                                            forwardAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount, 0, 0);
                                                                                                            colorChangePointingAnimation(binding.imageViewPointingToImageViewFragMainACOverviewViewSpendingsAccountDetailsHowToEditSubAccount, initialColor, initialColor);
                                                                                                            new CountDownTimer(500, 1000) {
                                                                                                                public void onTick(long millisUntilFinished) {

                                                                                                                }

                                                                                                                public void onFinish() {
                                                                                                                    if(!stopAnims){
                                                                                                                        longPressAnimation();
                                                                                                                    }
                                                                                                                }
                                                                                                            }.start();
                                                                                                        }
                                                                                                    }
                                                                                                }.start();
                                                                                            }
                                                                                        }
                                                                                    }.start();
                                                                                }
                                                                            }
                                                                        }.start();
                                                                    }
                                                                }
                                                            }.start();
                                                        }
                                                    }
                                                }.start();
                                            }
                                        }
                                    }.start();
                                }
                            }
                        }.start();
                    }
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