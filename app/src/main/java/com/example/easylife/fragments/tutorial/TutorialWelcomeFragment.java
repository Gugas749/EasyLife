package com.example.easylife.fragments.tutorial;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.easylife.R;
import com.example.easylife.activitys.MainActivity;
import com.example.easylife.databinding.FragmentTutorialWelcomeBinding;

public class TutorialWelcomeFragment extends Fragment {

    private FragmentTutorialWelcomeBinding binding;
    private MainActivity parent;
    private boolean fromAddTutorialFrag;

    public TutorialWelcomeFragment(MainActivity parent, boolean fromAddTutorialFrag) {
        this.parent = parent;
        this.fromAddTutorialFrag = fromAddTutorialFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTutorialWelcomeBinding.inflate(inflater);

        if(fromAddTutorialFrag){
            colorChangeFadeOutAnimation(binding.cardViewTutorialFragIndicator2FragTutorialWelcome);
            fragIndicatorScaleDownAnimation(binding.cardViewTutorialFragIndicator2FragTutorialWelcome);
        }
        colorChangeFadeInAnimation(binding.cardViewTutorialFragIndicator1FragTutorialWelcome);
        fragIndicatorScaleUpAnimation(binding.cardViewTutorialFragIndicator1FragTutorialWelcome);

        waveAnimation();
        setupNextFragment();

        return binding.getRoot();
    }

    private void waveAnimation(){
        ObjectAnimator waveAnimation = ObjectAnimator.ofFloat(binding.imageViewWavingFragTutotialWelcome, "rotation", 0f, 20f, 0f, -20f, 0f);
        waveAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        waveAnimation.setDuration(2000);
        waveAnimation.start();
    }
    private void setupNextFragment(){
        binding.textViewNextFragTutotialWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(1, false);
            }
        });
    }
    //-------------------------ANIMATION------------------
    private void fadeOutTextViewPrevious(){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        binding.textViewNextFragTutotialWelcome.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textViewNextFragTutotialWelcome.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
    private void fragIndicatorScaleDownAnimation(CardView cardView){
        int initialWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, getResources().getDisplayMetrics());
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
}