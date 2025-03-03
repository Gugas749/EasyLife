package com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.editlayouthowtofrags;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentMainACMainViewEditLayoutHowToSaveBinding;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.MainACMainViewEditLayoutFragment;

public class MainACMainViewEditLayoutHowToSaveFragment extends Fragment {

    private FragmentMainACMainViewEditLayoutHowToSaveBinding binding;
    private MainACMainViewEditLayoutFragment parent;
    private String nextButtonState = "base";
    private Boolean stopAnims = false, initialSaveAnimationIsFinished = false;

    public MainACMainViewEditLayoutHowToSaveFragment() {

    }
    public MainACMainViewEditLayoutHowToSaveFragment(MainACMainViewEditLayoutFragment parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewEditLayoutHowToSaveBinding.inflate(inflater);

        fadeInAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToSave);

        fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator4FragMainACMainViewEditLayoutHowToSave);
        colorChangeFadeOutAnimation(binding.cardViewFragIndicator4FragMainACMainViewEditLayoutHowToSave);
        fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator5FragMainACMainViewEditLayoutHowToSave);
        colorChangeFadeInAnimation(binding.cardViewFragIndicator5FragMainACMainViewEditLayoutHowToSave);

        setPointerColor();
        new CountDownTimer(1200, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                initialSaveAnimation();
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
                    binding.textViewPreviousFragMainACMainViewEditLayoutHowToSave.performClick();
                    return true;
                }
                return false;
            }
        });
    }
    private void setPointerColor(){
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
        int color = typedValue.data;
        int initialColor = color;

        typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.textAppearanceBodySmall, typedValue, true);
        color = typedValue.data;
        int finalColor = color;

        colorChangePointingAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave, initialColor, finalColor);
    }
    private void setupPreviousButton(){
        binding.textViewPreviousFragMainACMainViewEditLayoutHowToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewPreviousFragMainACMainViewEditLayoutHowToSave.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        fadeOutAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToSave);
                        parent.changeHowToFragment("goToDelete", true);
                        break;
                    case "fase1":
                        stopAnims = false;
                        fadeOutAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToSave);
                        fadeOutAnimation(binding.cardViewTopNavigationExampleFragMainACMainViewEditLayoutHowToSave);
                        fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave);
                        fadeOutEveryTimeAnimation(binding.textViewNextFragMainACMainViewEditLayoutHowToSave);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave.setVisibility(View.INVISIBLE);
                                binding.cardViewPopupExplanationFragMainACMainViewEditLayoutHowToSave.setVisibility(View.INVISIBLE);
                                binding.cardViewTopNavigationExampleFragMainACMainViewEditLayoutHowToSave.setVisibility(View.INVISIBLE);
                                if(initialSaveAnimationIsFinished){
                                    initialSaveAnimation();
                                }
                                fadeInEveryTimeAnimation(binding.textViewNextFragMainACMainViewEditLayoutHowToSave);
                                fadeInAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToSave);
                                fadeInAnimation(binding.textViewTitleFragMainACMainViewEditLayoutHowToSave);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToSave.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Save_Text_1));
                                binding.textViewNextFragMainACMainViewEditLayoutHowToSave.setText(getString(R.string.general_next));
                            }
                        }.start();
                        nextButtonState = "base";
                        break;
                }
                binding.textViewPreviousFragMainACMainViewEditLayoutHowToSave.setEnabled(true);
            }
        });
    }
    private void setupNextButton(){
        binding.textViewNextFragMainACMainViewEditLayoutHowToSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewNextFragMainACMainViewEditLayoutHowToSave.setEnabled(false);
                switch (nextButtonState){
                    case "base":
                        stopAnims = true;
                        fadeOutEveryTimeAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToSave);
                        fadeOutEveryTimeAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave);
                        fadeOutEveryTimeAnimation(binding.cardViewPopupExplanationFragMainACMainViewEditLayoutHowToSave);
                        fadeOutEveryTimeAnimation(binding.cardViewTopNavigationExampleFragMainACMainViewEditLayoutHowToSave);
                        fadeOutEveryTimeAnimation(binding.textViewNextFragMainACMainViewEditLayoutHowToSave);
                        fadeOutEveryTimeAnimation(binding.textViewTitleFragMainACMainViewEditLayoutHowToSave);
                        new CountDownTimer(700, 1000) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave.setVisibility(View.GONE);
                                binding.cardViewPopupExplanationFragMainACMainViewEditLayoutHowToSave.setVisibility(View.GONE);
                                binding.cardViewTopNavigationExampleFragMainACMainViewEditLayoutHowToSave.setVisibility(View.GONE);
                                fadeInEveryTimeAnimation(binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToSave);
                                fadeInEveryTimeAnimation(binding.textViewNextFragMainACMainViewEditLayoutHowToSave);
                                binding.textViewMainTextViewFragMainACMainViewEditLayoutHowToSave.setText(getString(R.string.mainAc_FragMainViewEditLayoutHowTo_Save_Text_2));
                                binding.textViewNextFragMainACMainViewEditLayoutHowToSave.setText(getString(R.string.general_finish));
                            }
                        }.start();
                        nextButtonState = "fase1";
                        break;
                    case "fase1":
                        fadeOutEveryTimeAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToSave);
                        parent.changeHowToFragment("finish", false);
                        break;
                }
                binding.textViewNextFragMainACMainViewEditLayoutHowToSave.setEnabled(true);
            }
        });
    }
    private void initialSaveAnimation(){
        if(!stopAnims){
            initialSaveAnimationIsFinished = false;
            fadeInAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave);
            fadeInAnimation(binding.cardViewTopNavigationExampleFragMainACMainViewEditLayoutHowToSave);
            forwardAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave, getResources().getDimension(com.intuit.sdp.R.dimen._118sdp), 0);
            new CountDownTimer(1300, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    pointYAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave, getResources().getDimension(com.intuit.sdp.R.dimen._minus30sdp));
                    new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave.setImageDrawable(getResources().getDrawable(R.drawable.hand_tapping));
                            new CountDownTimer(250, 1000) {
                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    fadeInAnimation(binding.cardViewPopupExplanationFragMainACMainViewEditLayoutHowToSave);
                                    binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave.setImageDrawable(getResources().getDrawable(R.drawable.hand_pointing));
                                    forwardAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave, getResources().getDimension(com.intuit.sdp.R.dimen._85sdp), getResources().getDimension(com.intuit.sdp.R.dimen._118sdp));
                                    new CountDownTimer(1000, 1000) {
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            downwardsAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave, getResources().getDimension(com.intuit.sdp.R.dimen._25sdp), getResources().getDimension(com.intuit.sdp.R.dimen._minus30sdp));
                                            new CountDownTimer(1000, 1000) {
                                                public void onTick(long millisUntilFinished) {

                                                }

                                                public void onFinish() {
                                                    binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave.setImageDrawable(getResources().getDrawable(R.drawable.hand_tapping));
                                                    new CountDownTimer(250, 1000) {
                                                        public void onTick(long millisUntilFinished) {

                                                        }

                                                        public void onFinish() {
                                                            binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave.setImageDrawable(getResources().getDrawable(R.drawable.hand_pointing));
                                                            new CountDownTimer(1500, 1000) {
                                                                public void onTick(long millisUntilFinished) {

                                                                }

                                                                public void onFinish() {
                                                                    fadeOutAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave);
                                                                    fadeOutAnimation(binding.cardViewTopNavigationExampleFragMainACMainViewEditLayoutHowToSave);
                                                                    fadeOutAnimation(binding.cardViewPopupExplanationFragMainACMainViewEditLayoutHowToSave);
                                                                    downwardsAnimation(binding.imageViewPointingToImageViewFragMainACMainViewEditLayoutHowToSave, 0, getResources().getDimension(com.intuit.sdp.R.dimen._25sdp));
                                                                    new CountDownTimer(500, 1000) {
                                                                        public void onTick(long millisUntilFinished) {

                                                                        }

                                                                        public void onFinish() {
                                                                            initialSaveAnimationIsFinished = true;
                                                                            initialSaveAnimation();
                                                                        }
                                                                    }.start();
                                                                }
                                                            }.start();
                                                        }
                                                    }.start();
                                                }
                                            }.start();
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
    private void fadeInEveryTimeAnimation(View view){
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

        colorAnimator.setDuration(100);
        colorAnimator.start();
    }
}