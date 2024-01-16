package com.example.easylife.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.activitys.MainActivity;
import com.example.easylife.database.LocalDataBase;
import com.example.easylife.database.daos.UserInfosDao;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.UserInfosEntity;
import com.example.easylife.databinding.FragmentAuthenticationBinding;
import com.example.easylife.fragments.mainactivityfragments.MainACAddViewFragment;
import com.example.easylife.fragments.mainactivityfragments.mainview.MainACMainViewFragment;
import com.example.easylife.fragments.mainactivityfragments.overview_view.MainACOverviewViewFragment;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthenticationFragment extends Fragment {
    private FragmentAuthenticationBinding binding;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private AuthenticationCompletedFragAuthentication listenner;
    private String inputedPinCode = "";
    private LocalDataBase localDataBase;
    private UserInfosDao userInfosDao;
    private static UserInfosEntity userInfos;
    private MainActivity parent;
    public interface AuthenticationCompletedFragAuthentication{
        void onAuthenticationCompletedFragAuthentication();
    }
    public void setAuthenticationCompletedFragAuthenticationListenner(AuthenticationCompletedFragAuthentication listenner){
        this.listenner = listenner;
    }
    public void setParent(MainActivity parent){
        this.parent = parent;
    }
    public AuthenticationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAuthenticationBinding.inflate(inflater);

        new CountDownTimer(450, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                binding.frameLayoutAnimationAuxFragAuthentication.setVisibility(View.GONE);
                binding.frameLayoutAnimationAuxFragAuthentication.setEnabled(false);
            }
        }.start();

        init();

        return binding.getRoot();
    }
    private void init(){
        setupLocalDataBase();
        setupClickListennerNumPad();
        setupDeleteAndBiometricPromptButton();
        DatabaseCallbackFragAuthentication callback = new DatabaseCallbackFragAuthentication() {
            @Override
            public void onTaskCompleted(UserInfosEntity result) {
                userInfos = result;
            }
        };
        new LocalDatabaseGetUserInfosTask(callback).execute();
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(parent, LocalDataBase.class, "EasyLifeLocalDB").build();
        userInfosDao = localDataBase.userInfosDao();
    }
    //----------NUM PAD RELATED----------------
    private void setupClickListennerNumPad(){
        View root = binding.getRoot();
        for (int i = 0; i < 10; i++) {
            int textViewId = getResources().getIdentifier("textView_NumPad_Num" + i + "_FragAuthentication", "id", requireActivity().getPackageName());
            TextView textView = root.findViewById(textViewId);
            textView.setOnClickListener(commonOnClickListener);
        }
    }
    private final View.OnClickListener commonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("EL_logs", ""+userInfos);
            if(inputedPinCode.length() < 4){
                inputedPinCode = inputedPinCode+view.getTag();
                pinCodeIndicatorScaleUp(inputedPinCode.length());
                startClickAnimation(view);
                if(inputedPinCode.length() == 4){
                    new CountDownTimer(700, 1000) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            if(inputedPinCode.equals(userInfos.Password)){
                                listenner.onAuthenticationCompletedFragAuthentication();
                            }else{
                                shakeView(binding.relativeLayoutPinCodeIndicatorHolderFragAuthentication);
                                clearPinCodeIndicator();
                                changeImageDeleteAndBiometricPromptButton();
                                inputedPinCode = "";
                                Toast.makeText(getContext(), getString(R.string.authFragment_wrongPinCode_Toast_Text), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.start();
                }
                if(inputedPinCode.length() == 1){
                    changeImageDeleteAndBiometricPromptButton();
                }
            }
        }
    };
    private void setupDeleteAndBiometricPromptButton(){
        binding.imageViewButtonDeleteAndBiometricPromptFragAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().equals("biometric")){
                    showBiometricPrompt();
                }else{
                    if(inputedPinCode.length() > 0){
                        inputedPinCode = inputedPinCode.substring(0, inputedPinCode.length() - 1);;
                        pinCodeIndicatorScaleDown(inputedPinCode.length());
                        startClickAnimation(binding.imageViewButtonDeleteAndBiometricPromptFragAuthentication);
                        if(inputedPinCode.length() == 0){
                            changeImageDeleteAndBiometricPromptButton();
                        }
                    }
                }
            }
        });
    }
    //----------------------------------------------

    //--------------------BIOMETRICS---------------------
    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.alertDialog_RestartSession_BiometricID_Title))
                .setSubtitle(getResources().getString(R.string.alertDialog_RestartSession_BiometricID_Subtitle))
                .setNegativeButtonText(getResources().getString(R.string.general_cancel))
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        // Handle authentication error
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Handle authentication success
                        listenner.onAuthenticationCompletedFragAuthentication();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        // Handle authentication failure (e.g., user's face is not recognized)
                        // This callback may also be triggered for other reasons, such as a pin or password entry
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
    //----------------------------------------------

    //--------------------ANIMATIONS--------------------
    private void changeImageDeleteAndBiometricPromptButton(){
        ImageView view = binding.imageViewButtonDeleteAndBiometricPromptFragAuthentication;
        float initialValue = 0f;
        float endlValue = 0f;
        if(view.getTag().equals("biometric")){
            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    view.setImageDrawable(getResources().getDrawable(R.drawable.arrow_icon));
                }
            }.start();
            view.setTag("back");
            endlValue = 540f;
        } else {
            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    view.setImageDrawable(getResources().getDrawable(R.drawable.biometric_fingerprint));
                }
            }.start();
            view.setTag("biometric");
            initialValue = 540f;
        }
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, initialValue, endlValue);
        rotationAnimator.setDuration(1000);
        rotationAnimator.start();
    }
    private void startClickAnimation(View view) {
        // Create a fade-in animation
        Animation fadeIn = new AlphaAnimation(0.5f, 1.0f);
        fadeIn.setDuration(500); // milliseconds

        // Combine fade-in and fade-out animations
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);

        // Set the animation listener to reset alpha after the animation ends
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Reset alpha to fully visible after the animation ends
                view.setAlpha(1.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeats
            }
        });

        // Start the animation on the TextView
        view.startAnimation(animation);
    }
    private void pinCodeIndicatorScaleUp(int stage){
        CardView animationCardView = binding.cardViewPinCodeLenghtIndicator1FragAuthentication;
        CardView paramsCardView = binding.cardViewPinCodeLenghtIndicator2FragAuthentication;
        if(stage == 1){
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            int color = typedValue.data;
            int initialColor = color;

            typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
            color = typedValue.data;
            int finalColor = color;

            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), initialColor, finalColor);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int animatedColor = (int) animator.getAnimatedValue();
                    animationCardView.setCardBackgroundColor(animatedColor);
                }
            });

            colorAnimator.setDuration(1000);
            colorAnimator.start();
        }else{
            float initialWidth = 0;
            float finalWidth = 0;

            switch (stage){
                case 2:
                    initialWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() / 2 - 5, getResources().getDisplayMetrics());
                    finalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth(), getResources().getDisplayMetrics());
                    break;
                case 3:
                    initialWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth(), getResources().getDisplayMetrics());
                    finalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() * 2 - 10, getResources().getDisplayMetrics());
                    break;
                case 4:
                    initialWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() * 2 - 10, getResources().getDisplayMetrics());
                    finalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() * 3 - 22, getResources().getDisplayMetrics());
                    break;
            }

            ValueAnimator animator = ValueAnimator.ofFloat(initialWidth, finalWidth);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    // Get the animated value and set it as the new width
                    float animatedValue = (float) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams params = animationCardView.getLayoutParams();
                    params.width = (int) animatedValue;
                    animationCardView.setLayoutParams(params);
                }
            });

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    // Animation start
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    // Animation canceled
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    // Animation repeat
                }
            });

            animator.setDuration(650);
            animator.start();
        }
    }
    private void pinCodeIndicatorScaleDown(int stage){
        CardView animationCardView = binding.cardViewPinCodeLenghtIndicator1FragAuthentication;
        CardView paramsCardView = binding.cardViewPinCodeLenghtIndicator2FragAuthentication;
        if(stage == 0){
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
            int color = typedValue.data;
            int initialColor = color;

            typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            color = typedValue.data;
            int finalColor = color;

            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), initialColor, finalColor);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int animatedColor = (int) animator.getAnimatedValue();
                    animationCardView.setCardBackgroundColor(animatedColor);
                }
            });

            colorAnimator.setDuration(1000);
            colorAnimator.start();
        }else{
            float initialWidth = 0;
            float finalWidth = 0;

            switch (stage){
                case 1:
                    initialWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth(), getResources().getDisplayMetrics());
                    finalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() / 2 - 5, getResources().getDisplayMetrics());
                    break;
                case 2:
                    initialWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() * 2 - 10, getResources().getDisplayMetrics());
                    finalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth(), getResources().getDisplayMetrics());
                    break;
                case 3:
                    initialWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() * 3 - 22, getResources().getDisplayMetrics());
                    finalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() * 2 - 10, getResources().getDisplayMetrics());
                    break;
            }

            ValueAnimator animator = ValueAnimator.ofFloat(initialWidth, finalWidth);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    // Get the animated value and set it as the new width
                    float animatedValue = (float) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams params = animationCardView.getLayoutParams();
                    params.width = (int) animatedValue;
                    animationCardView.setLayoutParams(params);
                }
            });

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    // Animation start
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    // Animation canceled
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    // Animation repeat
                }
            });

            animator.setDuration(650);
            animator.start();
        }
    }
    private void clearPinCodeIndicator(){
        CardView animationCardView = binding.cardViewPinCodeLenghtIndicator1FragAuthentication;
        CardView paramsCardView = binding.cardViewPinCodeLenghtIndicator2FragAuthentication;
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
        int color = typedValue.data;
        int initialColor = color;

        typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        color = typedValue.data;
        int finalColor = color;

        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), initialColor, finalColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int animatedColor = (int) animator.getAnimatedValue();
                animationCardView.setCardBackgroundColor(animatedColor);
            }
        });

        colorAnimator.setDuration(1000);
        colorAnimator.start();

        float initialWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() * 3 - 22, getResources().getDisplayMetrics());
        float finalWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, paramsCardView.getWidth() / 2 - 5, getResources().getDisplayMetrics());

        ValueAnimator animator = ValueAnimator.ofFloat(initialWidth, finalWidth);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Get the animated value and set it as the new width
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = animationCardView.getLayoutParams();
                params.width = (int) animatedValue;
                animationCardView.setLayoutParams(params);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // Animation start
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Animation canceled
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Animation repeat
            }
        });

        animator.setDuration(650);
        animator.start();
    }
    private void shakeView(View view) {
        int shakeDistance = 10; // You can adjust the shake distance

        Animation shake = new TranslateAnimation(0, shakeDistance, 0, 0);
        shake.setDuration(100); // Set the duration of each shake
        shake.setInterpolator(new CycleInterpolator(5)); // Set the number of shakes (5 in this case)

        view.startAnimation(shake);
    }
    //----------------------------------------------
    public interface DatabaseCallbackFragAuthentication {
        void onTaskCompleted(UserInfosEntity result);
    }
    private class LocalDatabaseGetUserInfosTask extends AsyncTask<Void, Void, UserInfosEntity> {
        private DatabaseCallbackFragAuthentication callback;

        public LocalDatabaseGetUserInfosTask(DatabaseCallbackFragAuthentication callback) {
            this.callback = callback;
        }
        @Override
        protected UserInfosEntity doInBackground(Void... voids) {
            List<UserInfosEntity> userInfosEntityList = userInfosDao.getUserInfos();
            UserInfosEntity userInfos = userInfosEntityList.get(0);
            return userInfos;
        }

        @Override
        protected void onPostExecute(UserInfosEntity userInfos) {
            callback.onTaskCompleted(userInfos);
        }
    }
}