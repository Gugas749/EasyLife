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

import com.example.easylife.R;
import com.example.easylife.activitys.MainActivity;
import com.example.easylife.databinding.FragmentTutorialEditBinding;

public class TutorialEditFragment extends Fragment {

    private FragmentTutorialEditBinding binding;
    private MainActivity parent;
    private boolean fromNextFragment;

    public TutorialEditFragment(MainActivity parent, boolean fromNextFragment) {
        this.parent = parent;
        this.fromNextFragment = fromNextFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTutorialEditBinding.inflate(inflater);

        CardView oldCardView;
        if(fromNextFragment){
            oldCardView = binding.cardViewTutorialFragIndicator5FragTutorialEdit;
        }else{
            oldCardView = binding.cardViewTutorialFragIndicator3FragTutorialEdit;
        }
        fragIndicatorScaleDownAnimation(oldCardView);
        colorChangeFadeOutAnimation(oldCardView);
        fragIndicatorScaleUpAnimation(binding.cardViewTutorialFragIndicator4FragTutorialEdit);
        colorChangeFadeInAnimation(binding.cardViewTutorialFragIndicator4FragTutorialEdit);

        setupButtonNext();
        setupButtonPrevious();
        setupSkipButton();

        return binding.getRoot();
    }

    private void setupButtonNext(){
        binding.textViewNextFragTutorialEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(4, false);
            }
        });
    }
    private void setupButtonPrevious(){
        binding.textViewPreviousFragTutorialEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(2, true);
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
        binding.textViewSkipFragTutorialEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(4, false);
            }
        });
    }
}