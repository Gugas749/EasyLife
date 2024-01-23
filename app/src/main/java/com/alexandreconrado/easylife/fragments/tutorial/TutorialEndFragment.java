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
import android.view.animation.AccelerateDecelerateInterpolator;

import com.alexandreconrado.easylife.activitys.MainActivity;
import com.alexandreconrado.easylife.databinding.FragmentTutorialEndBinding;

public class TutorialEndFragment extends Fragment {

    private FragmentTutorialEndBinding binding;
    private final MainActivity parent;
    private final boolean fromNextFragment;
    private final boolean skipped;
    private final int skippedFromWhere;

    public TutorialEndFragment(MainActivity parent, boolean fromNextFragment, boolean skipped, int skippedFromWhere) {
        this.parent = parent;
        this.fromNextFragment = fromNextFragment;
        this.skipped = skipped;
        this.skippedFromWhere = skippedFromWhere;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTutorialEndBinding.inflate(inflater);

        if(skipped){
            switch (skippedFromWhere){
                case 0:
                    fragIndicatorScaleDownAnimation(binding.cardViewTutorialFragIndicator1FragTutorialEnd);
                    break;
                case 1:
                    fragIndicatorScaleDownAnimation(binding.cardViewTutorialFragIndicator2FragTutorialEnd);
                    break;
                case 2:
                    fragIndicatorScaleDownAnimation(binding.cardViewTutorialFragIndicator3FragTutorialEnd);
                    break;
                case 3:
                    fragIndicatorScaleDownAnimation(binding.cardViewTutorialFragIndicator4FragTutorialEnd);
                    break;
            }
            colorChangeFadeOutAnimation(binding.cardViewTutorialFragIndicator4FragTutorialEnd);
        }else{
            fragIndicatorScaleDownAnimation(binding.cardViewTutorialFragIndicator4FragTutorialEnd);
            colorChangeFadeOutAnimation(binding.cardViewTutorialFragIndicator4FragTutorialEnd);
        }
        fragIndicatorScaleUpAnimation(binding.cardViewTutorialFragIndicator5FragTutorialEnd);
        colorChangeFadeInAnimation(binding.cardViewTutorialFragIndicator5FragTutorialEnd);

        jumpingAnimation();
        setupButtonFinish();
        setupButtonPrevious();

        return binding.getRoot();
    }

    private void setupButtonFinish(){
        binding.textViewNextFragTutorialEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewNextFragTutorialEnd.setEnabled(false);
                parent.tutorialChangeFragments(5, false, false, 4);
            }
        });
    }
    private void setupButtonPrevious(){
        binding.textViewPreviousFragTutorialEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.tutorialChangeFragments(3, true, false,4);
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
    private void jumpingAnimation() {
        float jumpHeight = 40f;
        long duration = 800;

        ValueAnimator jumpAnimator = ValueAnimator.ofFloat(0f, -jumpHeight, 0f);
        jumpAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        jumpAnimator.setDuration(duration);
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        jumpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                binding.imageViewFinishFlagFragTutotialEnd.setTranslationY(animatedValue);
            }
        });

        jumpAnimator.start();
    }
}