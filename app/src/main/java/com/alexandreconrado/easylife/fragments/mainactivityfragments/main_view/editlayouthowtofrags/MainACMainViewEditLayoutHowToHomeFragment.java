package com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.editlayouthowtofrags;

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
import android.view.animation.LinearInterpolator;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.databinding.FragmentMainACMainViewEditLayoutHowToHomeBinding;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.MainACMainViewEditLayoutFragment;

public class MainACMainViewEditLayoutHowToHomeFragment extends Fragment {

    private FragmentMainACMainViewEditLayoutHowToHomeBinding binding;
    private MainACMainViewEditLayoutFragment parent;
    private Boolean fromNext;

    public MainACMainViewEditLayoutHowToHomeFragment(MainACMainViewEditLayoutFragment parent, Boolean fromNext) {
        this.parent = parent;
        this.fromNext = fromNext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewEditLayoutHowToHomeBinding.inflate(inflater);

        fadeInAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToHome);

        if(fromNext){
            fragIndicatorScaleDownAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToHome);
            colorChangeFadeOutAnimation(binding.cardViewFragIndicator2FragMainACMainViewEditLayoutHowToHome);
            fragIndicatorScaleUpAnimation(binding.cardViewFragIndicator1FragMainACMainViewEditLayoutHowToHome);
            colorChangeFadeInAnimation(binding.cardViewFragIndicator1FragMainACMainViewEditLayoutHowToHome);
        }

        pointAnimation();
        setupNextButton();

        return binding.getRoot();
    }
    private void setupNextButton(){
        binding.textViewNextFragMainACMainViewEditLayoutHowToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOutAnimation(binding.frameLayoutParentFragMainACMainViewEditLayoutHowToHome);
                parent.changeHowToFragment("goToAdd", false);
            }
        });
    }
    private void pointAnimation() {
        // Diagonal animation: move both along X and Y axes
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(binding.imageViewPointingFragMainACMainViewEditLayoutHowToHome, "translationX", 0, 10);
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(binding.imageViewPointingFragMainACMainViewEditLayoutHowToHome, "translationY", 0, 10);

        translateXAnimator.setDuration(800); // Set the duration of each phase in milliseconds
        translateYAnimator.setDuration(800); // Set the duration of each phase in milliseconds

        translateXAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Repeat indefinitely
        translateYAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Repeat indefinitely

        translateXAnimator.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation on each iteration
        translateYAnimator.setRepeatMode(ObjectAnimator.REVERSE); // Reverse the animation on each iteration

        translateXAnimator.setInterpolator(new LinearInterpolator()); // Use a linear interpolator for constant speed
        translateYAnimator.setInterpolator(new LinearInterpolator()); // Use a linear interpolator for constant speed

        translateXAnimator.start();
        translateYAnimator.start();
    }
    private void fadeInAnimation(ViewGroup view){
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
    private void fadeOutAnimation(ViewGroup view){
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