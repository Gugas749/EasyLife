package com.example.easylife.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.databinding.ActivityMainBinding;
import com.example.easylife.fragments.mainactivityfragments.MainACAddViewFragment;
import com.example.easylife.fragments.mainactivityfragments.MainACMainViewFragment;
import com.example.easylife.fragments.mainactivityfragments.MainACOverviewViewFragment;
import com.example.easylife.fragments.tutorial.TutorialAddFragment;
import com.example.easylife.fragments.tutorial.TutorialEditFragment;
import com.example.easylife.fragments.tutorial.TutorialEndFragment;
import com.example.easylife.fragments.tutorial.TutorialShowFragment;
import com.example.easylife.fragments.tutorial.TutorialWelcomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DrawerLayout drawerLayoutSideMenu;
    private NavigationView navigationViewSideMenu;
    private ActionBarDrawerToggle drawerToggleSideMenu;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private long sessionTime;
    private boolean seenTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("Perf_User", MODE_PRIVATE);
        seenTutorial = prefs.getBoolean("seenTutorial", false);

        if(seenTutorial){
            sessionTime = System.currentTimeMillis();

            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    init();
                    startFragAnimations();
                }
            }.start();
        } else{
            inFragment();

            binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setBackground(null);
            tutorialChangeFragments(0, false, false, 0);
        }
    }

    //-----------------SETUPS--------------------
    private void init(){
        //TODO: nao esquecer de tirar o bioprompt do inicio ou trocar para o total
        showBiometricPrompt();
        setupBottomNavigation();
        changeFragmentFromMainFragmentContainer(1);
        setupSideMenu();
    }
    private void setupBottomNavigation() {
        binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Home).setChecked(true);

        binding.bottomNavigationViewMainAC.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == binding.bottomNavigationViewMainAC.getSelectedItemId()) {
                    return true;
                }

                if (item.getItemId() == R.id.menu_bottomNavigation_painel_Graffics) {
                    changeFragmentFromMainFragmentContainer(0);
                } else if (item.getItemId() == R.id.menu_bottomNavigation_painel_Home) {
                    changeFragmentFromMainFragmentContainer(1);
                } else if (item.getItemId() == R.id.menu_bottomNavigation_painel_Add) {
                    changeFragmentFromMainFragmentContainer(2);
                }

                return true;
            }
        });
    }
    //----------------------------------------------

    //-----------------FUNCTIONS---------------------
    @Override
    public void onResume() {
        super.onResume();
        if(seenTutorial){
            if(getSession()){ // SESSION STILL AVALIABLE

            }else{// SESSION NOT AVALIABLE
                showBiometricPrompt();
            }
        }
    }
    private boolean getSession(){
        boolean output = false;
        long currentTimeInMillis = System.currentTimeMillis();
        long fiveMinutesInMillis = 2 * 60 * 1000;
        if (currentTimeInMillis - sessionTime >= fiveMinutesInMillis) {
            output = false;
        } else {
            sessionTime = System.currentTimeMillis();
            output = true;
        }
        return output;
    }
    private void inFragment(){
        binding.imageViewAux1MainActivity.setVisibility(View.INVISIBLE);
        binding.bottomNavigationViewMainAC.setVisibility(View.INVISIBLE);
        binding.cardViewTopNavigationMainAc.setVisibility(View.INVISIBLE);

        binding.imageViewAux1MainActivity.setEnabled(false);
        binding.bottomNavigationViewMainAC.setEnabled(false);
        binding.cardViewTopNavigationMainAc.setEnabled(false);
    }
    private void outFragment(){
        binding.imageViewAux1MainActivity.setVisibility(View.VISIBLE);
        binding.bottomNavigationViewMainAC.setVisibility(View.VISIBLE);
        binding.cardViewTopNavigationMainAc.setVisibility(View.VISIBLE);

        binding.imageViewAux1MainActivity.setEnabled(true);
        binding.bottomNavigationViewMainAC.setEnabled(true);
        binding.cardViewTopNavigationMainAc.setEnabled(true);
    }
    public void tutorialChangeFragments(int selected, boolean fromNextFragment, boolean skipped, int skippedFromWhere){
        switch (selected){
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialWelcomeFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialAddFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialShowFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialEditFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_SplashAuxAndFragmentsTutorial_MainAc, new TutorialEndFragment(this, fromNextFragment, skipped, skippedFromWhere))
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setEnabled(false);

                SharedPreferences sharedPreferences = getSharedPreferences("Perf_User", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("seenTutorial", true);
                editor.apply();

                outFragment();
                break;
        }
    }
    private void changeFragmentFromMainFragmentContainer(int selected){
        switch (selected){
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, new MainACOverviewViewFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, new MainACMainViewFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, new MainACAddViewFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
    private void showShareBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheetview_share_menu, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        String mensage = getApplicationContext().getString(R.string.bottomSheetView_shareMenu_sharingText);

        ImageView shareFacebookButton = bottomSheetView.findViewById(R.id.bottomSheetView_button_Facebook_MainAC);
        ImageView shareTwitterButton = bottomSheetView.findViewById(R.id.bottomSheetView_button_Twitter_MainAC);
        ImageView shareWhatsappButton = bottomSheetView.findViewById(R.id.bottomSheetView_button_WhatsappIcon_MainAC);

        shareFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.facebook.katana");

                intent.putExtra(Intent.EXTRA_TEXT, mensage);

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Facebook is not installed.", Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
            }
        });
        shareTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.twitter.android");
                intent.putExtra(Intent.EXTRA_TEXT, mensage);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Twitter is not installed.", Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
            }
        });
        shareWhatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sharing via Email
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, mensage);

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
    //----------------------------------------------

    //----------BIOMETRICS----------------
    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.alertDialog_RestartSession_BiometricID_Title))
                .setSubtitle(getResources().getString(R.string.alertDialog_RestartSession_BiometricID_Subtitle))
                .setNegativeButtonText(getResources().getString(R.string.general_cancel))
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        //tituy
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        //validou
                        sessionTime = System.currentTimeMillis();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        //face ou pin
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
    //----------------------------------------------

    //----------SIDE MENU----------------
    private void setupSideMenu(){
        //-------------menu---------------------
        drawerLayoutSideMenu = findViewById(R.id.drawerLayout_MainAc_SideMenu);
        navigationViewSideMenu = findViewById(R.id.navigationView_MainAc_SideMenu);
        drawerToggleSideMenu = new ActionBarDrawerToggle(this,drawerLayoutSideMenu,R.string.general_continue,R.string.general_cancel);
        drawerLayoutSideMenu.addDrawerListener(drawerToggleSideMenu);
        drawerToggleSideMenu.syncState();
        //-----------------------------------------

        binding.imageViewButtonSideMenuMainAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerLayoutSideMenu.isDrawerOpen(GravityCompat.END)){
                    drawerLayoutSideMenu.openDrawer(GravityCompat.END);
                }
            }
        });

        navigationViewSideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.mainAc_SideBar_Share) {
                    item.setEnabled(false);
                    showShareBottomSheet();
                    item.setEnabled(true);
                }
                return true;
            }
        });
        navigationViewSideMenu.bringToFront();
    }
    //----------------------------------------------

    //----------------ANIMATIONS--------------------
  /*  private void startFragAnimations(){
        binding.cardviewDialogLoginFrag.setVisibility(View.INVISIBLE);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(android.R.attr.colorControlNormal, typedValue, true);
        @ColorInt int color = typedValue.data;
        binding.frameLayoutFragRegister.setBackgroundColor(color);
        scaleDownAnimtion();
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                fadeInRegisterDialog();
            }
        }.start();
    }
    private void fadeInRegisterDialog(){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.cardviewDialogLoginFrag.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.cardviewDialogLoginFrag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeOutRegisterDialog(){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        binding.cardviewDialogLoginFrag.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.cardviewDialogLoginFrag.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }*/
    private void startFragAnimations(){
        binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setVisibility(View.INVISIBLE);
        binding.frameLayoutSplashAuxAndFragmentsTutorialMainAc.setEnabled(false);
        scaleDownAnimtion();
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                binding.imageViewAux1MainActivity.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
    private void scaleDownAnimtion(){
        float startScale = 200.0f;
        float endScale = 1.0f;

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        scaleAnimation.setDuration(1500);

        binding.imageViewAux1MainActivity.startAnimation(scaleAnimation);
    }
    private void scaleUpAnimtion(){
       // ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(binding.cardviewAuxiliarLoginFrag, "scaleX", 1f, 18f);
       // ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(binding.cardviewAuxiliarLoginFrag, "scaleY", 1f, 18f);

       // binding.cardviewAuxiliarLoginFrag.setPivotX(binding.cardviewAuxiliarLoginFrag.getWidth() / 2);
       // binding.cardviewAuxiliarLoginFrag.setPivotY(binding.cardviewAuxiliarLoginFrag.getHeight() / 2);

        AnimatorSet animatorSet = new AnimatorSet();
      //  animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }
    //----------------------------------------------
}