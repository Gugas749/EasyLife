package com.alexandreconrado.easylife.fragments.tutorial;

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

import com.alexandreconrado.easylife.activitys.MainActivity;
import com.alexandreconrado.easylife.databinding.FragmentTutorialShowBinding;

public class TutorialShowFragment extends Fragment {

    private final MainActivity parent;
    private FragmentTutorialShowBinding binding;
    private final boolean fromNextFragment;

    public TutorialShowFragment(MainActivity parent, boolean fromNextFragment) {
        this.parent = parent;
        this.fromNextFragment = fromNextFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTutorialShowBinding.inflate(inflater);

        CardView oldCardView;
        if(fromNextFragment){
            oldCardView = binding.cardViewTutorialFragIndicator4FragTutorialShow;
        }else{
            oldCardView = binding.cardViewTutorialFragIndicator2FragTutorialShow;
        }
        fragIndicatorScaleDownAnimation(oldCardView);
        colorChangeFadeOutAnimation(oldCardView);
        fragIndicatorScaleUpAnimation(binding.cardViewTutorialFragIndicator3FragTutorialShow);
        colorChangeFadeInAnimation(binding.cardViewTutorialFragIndicator3FragTutorialShow);

        setupButtonNext();
        setupButtonPrevious();
        setupSkipButton();

        return binding.getRoot();
    }

    private void setupButtonNext(){
        binding.textViewNextFragTutorialShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(3, false, false,0);
            }
        });
    }
    private void setupButtonPrevious(){
        binding.textViewPreviousFragTutorialShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(1, true, false,0);
            }
        });
    }
    //-------------------------ANIMATION------------------
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
    private void setupSkipButton(){
        binding.textViewSkipFragTutorialShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(4, false, true, 2);
            }
        });
    }
}