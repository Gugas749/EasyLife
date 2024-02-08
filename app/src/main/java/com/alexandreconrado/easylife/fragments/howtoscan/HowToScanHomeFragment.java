package com.alexandreconrado.easylife.fragments.howtoscan;

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
import com.alexandreconrado.easylife.activitys.SplashActivity;
import com.alexandreconrado.easylife.databinding.FragmentHowToScanHomeBinding;
import com.alexandreconrado.easylife.databinding.FragmentMainACMainViewEditLayoutHowToDeleteBinding;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.MainACMainViewEditLayoutFragment;

public class HowToScanHomeFragment extends Fragment {

    private FragmentHowToScanHomeBinding binding;
    private SplashActivity parent;
    private String nextButtonState = "base";
    private Boolean fromNext;
    private Boolean stopAnims = false;

    public HowToScanHomeFragment() {
        // Required empty public constructor
    }
    public HowToScanHomeFragment(SplashActivity parent, Boolean fromNext) {
        this.parent = parent;
        this.fromNext = fromNext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHowToScanHomeBinding.inflate(inflater);

        fadeInAnimation(binding.frameLayoutParentFragHowToScanHome);

        if(fromNext){
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator2FragHowToScanHome);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator2FragHowToScanHome);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator1FragHowToScanHome);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator1FragHowToScanHome);
        } else {
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator1FragHowToScanHome);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator1FragHowToScanHome);
        }

        new CountDownTimer(700, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                initalAnimation();
            }
        }.start();

        setupNextButton();
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
    private void setupNextButton(){
        binding.textViewNextFragHowToScanHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnims = true;
                fadeOutAnimation(binding.frameLayoutParentFragHowToScanHome);
                parent.howToScanFunction(false, "HowToScanFinalFragment");
            }
        });
    }
    private void initalAnimation(){
        if(!stopAnims){
            fadeInAnimation(binding.imageViewReceiptFragHowToScanHome);
            fadeInAnimation(binding.imageViewSmartphoneFragHowToScanHome);
            new CountDownTimer(700, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    float value = getResources().getDimension(com.intuit.sdp.R.dimen._75sdp);
                    forwardAnimation(binding.imageViewSmartphoneFragHowToScanHome, (-value), 0);

                    new CountDownTimer(1700, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            fadeOutAnimation(binding.imageViewReceiptFragHowToScanHome);
                            fadeOutAnimation(binding.imageViewSmartphoneFragHowToScanHome);
                            new CountDownTimer(1200, 1000) {
                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    forwardAnimation(binding.imageViewSmartphoneFragHowToScanHome, 0, 0);
                                    initalAnimation();
                                }
                            }.start();
                        }
                    }.start();
                }
            }.start();
        }
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
        int initialWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(com.intuit.sdp.R.dimen._2sdp), getResources().getDisplayMetrics());
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
}