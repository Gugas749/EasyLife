package com.example.easylife.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.database.daos.DraggableCardViewDao;
import com.example.easylife.database.daos.SpendingsAccountsDao;
import com.example.easylife.database.daos.UserInfosDao;
import com.example.easylife.database.entities.DraggableCardViewEntity;
import com.example.easylife.database.LocalDataBase;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.database.entities.UserInfosEntity;
import com.example.easylife.databinding.ActivityMainBinding;
import com.example.easylife.fragments.mainactivityfragments.MainACAddViewFragment;
import com.example.easylife.fragments.mainactivityfragments.mainview.MainACMainViewEditLayoutFragment;
import com.example.easylife.fragments.mainactivityfragments.mainview.MainACMainViewFragment;
import com.example.easylife.fragments.mainactivityfragments.overview_view.MainACOverviewViewAddSpendingAccountFormFragment;
import com.example.easylife.fragments.mainactivityfragments.overview_view.MainACOverviewViewFragment;
import com.example.easylife.fragments.tutorial.TutorialAddFragment;
import com.example.easylife.fragments.tutorial.TutorialEditFragment;
import com.example.easylife.fragments.tutorial.TutorialEndFragment;
import com.example.easylife.fragments.tutorial.TutorialShowFragment;
import com.example.easylife.fragments.tutorial.TutorialWelcomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MainACMainViewEditLayoutFragment.OnFragMainACMainViewEditLayoutExitClick {
    //-------------------OTHERS---------------
    private ActivityMainBinding binding;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private long sessionTime;
    private boolean seenTutorial;
    private UserInfosEntity UserInfosEntity;
    //-------------------SIDE MENU---------------
    private DrawerLayout drawerLayoutSideMenu;
    private NavigationView navigationViewSideMenu;
    private ActionBarDrawerToggle drawerToggleSideMenu;
    //-------------------ACTIVITIES AND FRAGMENTS---------------
    private MainActivity THIS;
    private MainACMainViewFragment mainACMainViewFragment;
    private MainACOverviewViewFragment mainACOverviewViewFragment;
    private MainACAddViewFragment mainACAddViewFragment;
    //-------------------LISTS---------------
    private List<DraggableCardViewEntity> draggableCardViewObjectsList, exampleDraggableCardViewObjectsList;
    private List<SpendingAccountsEntity> spendingAccountsEntitiesList;
    //-------------------LOCAL DATABASE---------------
    private LocalDataBase localDataBase;
    private DraggableCardViewDao draggableCardViewDao;
    private SpendingsAccountsDao spendingsAccountsDao;
    private UserInfosDao userInfosDao;

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

            binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
            tutorialChangeFragments(0, false, false, 0);
        }
    }

    //-----------------SETUPS--------------------
    private void init(){
        //TODO: adicionar session prompt total
        THIS = this;

        draggableCardViewObjectsList = new ArrayList<>();
        spendingAccountsEntitiesList = new ArrayList<>();
        exampleDraggableCardViewObjectsList = new ArrayList<>();

        mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
        mainACOverviewViewFragment = new MainACOverviewViewFragment();
        mainACAddViewFragment = new MainACAddViewFragment();

        setupBottomNavigation();
        changeFragmentFromMainFragmentContainer(1);
        setupSideMenu();
        setupMultiFunctionButtonButton();
        setupLocalDataBase();

        new LocalDatabaseGetAllDraggableCardViewsTask().execute();
        new LocalDatabaseGetAllSpendingsAccountsTask().execute();
        new LocalDatabaseGetUserInfosTask().execute();
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
    private void setupMultiFunctionButtonButton(){
        binding.imageViewButtonMultiFunctionMainViewMainAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleUpAnimtion();
                new CountDownTimer(1200, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        inFragment();

                        binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
                        binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.VISIBLE);
                        binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(true);

                        Object tag = binding.imageViewButtonMultiFunctionMainViewMainAC.getTag();
                        if (tag.equals("0")) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new MainACOverviewViewAddSpendingAccountFormFragment(UserInfosEntity))
                                    .addToBackStack(null)
                                    .commit();
                        } else if (tag.equals("1")) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new MainACMainViewEditLayoutFragment(THIS, draggableCardViewObjectsList))
                                    .addToBackStack(null)
                                    .commit();
                        }
                        scaleDownAnimtion();
                    }
                }.start();
            }
        });
    }
    private void setupLocalDataBase(){
        localDataBase = Room.databaseBuilder(getApplicationContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        draggableCardViewDao = localDataBase.draggableCardViewDao();
        spendingsAccountsDao = localDataBase.spendingsAccountsDao();
        userInfosDao = localDataBase.userInfosDao();
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

        binding.frameLayoutFragmentContainerMainAC.setVisibility(View.INVISIBLE);

        binding.cardViewTopNavigationMainAc.setVisibility(View.INVISIBLE);
        binding.imageViewButtonMultiFunctionMainViewMainAC.setVisibility(View.INVISIBLE);
        binding.imageViewButtonSideMenuMainAC.setVisibility(View.INVISIBLE);

        binding.imageViewAux1MainActivity.setEnabled(false);

        binding.bottomNavigationViewMainAC.setEnabled(false);

        binding.frameLayoutFragmentContainerMainAC.setEnabled(false);

        binding.cardViewTopNavigationMainAc.setEnabled(false);
        binding.imageViewButtonMultiFunctionMainViewMainAC.setEnabled(false);
        binding.imageViewButtonSideMenuMainAC.setEnabled(false);
    }
    private void outFragment(){
        binding.imageViewAux1MainActivity.setVisibility(View.VISIBLE);

        binding.bottomNavigationViewMainAC.setVisibility(View.VISIBLE);

        binding.frameLayoutFragmentContainerMainAC.setVisibility(View.VISIBLE);

        binding.cardViewTopNavigationMainAc.setVisibility(View.VISIBLE);
        binding.imageViewButtonMultiFunctionMainViewMainAC.setVisibility(View.VISIBLE);
        binding.imageViewButtonSideMenuMainAC.setVisibility(View.VISIBLE);

        binding.imageViewAux1MainActivity.setEnabled(true);

        binding.bottomNavigationViewMainAC.setEnabled(true);

        binding.frameLayoutFragmentContainerMainAC.setEnabled(true);

        binding.cardViewTopNavigationMainAc.setEnabled(true);
        binding.imageViewButtonMultiFunctionMainViewMainAC.setEnabled(true);
        binding.imageViewButtonSideMenuMainAC.setEnabled(true);
    }
    public void tutorialChangeFragments(int selected, boolean fromNextFragment, boolean skipped, int skippedFromWhere){
        switch (selected){
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new TutorialWelcomeFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new TutorialAddFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new TutorialShowFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new TutorialEditFragment(this, fromNextFragment))
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new TutorialEndFragment(this, fromNextFragment, skipped, skippedFromWhere))
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                scaleUpAnimtion();
                new CountDownTimer(1500, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.GONE);
                        binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);

                        SharedPreferences sharedPreferences = getSharedPreferences("Perf_User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("seenTutorial", true);
                        editor.apply();

                        outFragment();
                    }
                }.start();
                break;
        }
    }
    private void changeFragmentFromMainFragmentContainer(int selected){
        changeMultiFunctionButtonFunction(selected);
        switch (selected){
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACOverviewViewFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACMainViewFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACAddViewFragment)
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
    private void changeMultiFunctionButtonFunction(int selected){
        fadeOutAnimation(binding.imageViewButtonMultiFunctionMainViewMainAC);
        Boolean dontFadeIn = false;
        switch (selected){
            case 0:
                binding.imageViewButtonMultiFunctionMainViewMainAC.setImageDrawable(getDrawable(R.drawable.add));
                binding.imageViewButtonMultiFunctionMainViewMainAC.setTag("0");
                break;
            case 1:
                binding.imageViewButtonMultiFunctionMainViewMainAC.setImageDrawable(getDrawable(R.drawable.grid_icon));
                binding.imageViewButtonMultiFunctionMainViewMainAC.setTag("1");
                break;
            case 2:
                dontFadeIn = true;
                break;
        }
        if(dontFadeIn){
            binding.imageViewButtonMultiFunctionMainViewMainAC.setVisibility(View.GONE);
        }else{
            binding.imageViewButtonMultiFunctionMainViewMainAC.setVisibility(View.INVISIBLE);
            fadeInAnimation(binding.imageViewButtonMultiFunctionMainViewMainAC);
        }
    }
    //----------------------------------------------

    //----------EXAMPLE LIST----------------
    private List<DraggableCardViewEntity> loadExampleDraggableCardViewList(){
        List<DraggableCardViewEntity> list = new ArrayList<>();
        int width2 = (int) (binding.frameLayoutFragmentContainerMainAC.getWidth() * 0.5f);
        int height2 = (int) (binding.frameLayoutFragmentContainerMainAC.getHeight() * 0.2f);
        List<Point> predefinedPositions = setPredefinedPositions(width2, height2);
        int id = 0;
        for (int j = 0; j <= 5; j++) {
            Point point = new Point();
            String type = "";
            String style = "1";

            switch (j){
                case 1:
                    point = predefinedPositions.get(0);
                    type = "2";
                    break;
                case 2:
                    point = predefinedPositions.get(1);
                    type = "1";
                    break;
                case 3:
                    point = predefinedPositions.get(6);
                    type = "1";
                    break;
                case 4:
                    point = predefinedPositions.get(2);
                    type = "2";
                    style = "2";
                    break;
                case 5:
                    point = predefinedPositions.get(3);
                    type = "3";
                    break;
            }

            for (int i = 0; i < predefinedPositions.size(); i++) {
                if(predefinedPositions.get(i).equals(point)){
                    DraggableCardViewEntity object = new DraggableCardViewEntity(i, type, getString(R.string.mainAc_FragMainView_Example_ChartName_Text), style);
                    object.setInfos(25, 25, 25, 25,
                            getString(R.string.mainAc_FragMainView_Example_Percentage1_Text), getString(R.string.mainAc_FragMainView_Example_Percentage2_Text),
                            getString(R.string.mainAc_FragMainView_Example_Percentage3_Text), getString(R.string.mainAc_FragMainView_Example_Percentage4_Text),
                            getResources().getColor(R.color.highlightedTextDark), getResources().getColor(R.color.textDark),
                            getResources().getColor(R.color.highlightedTextLight), getResources().getColor(R.color.textLight));
                    object.setId(id);
                    list.add(object);
                    break;
                }
            }
        }
        return list;
    }
    private List<Point> setPredefinedPositions(int width, int height){
        List<Point> predefinedPositions = new ArrayList<>();

        int column1X = 0;
        int column2X = width;

        int RowY = 0;
        int RowY2 = 0;

        for(int i = 0; i < 10; i++){
            Point point = new Point();
            if(i < 5){
                point.x = column1X;
                point.y = RowY;

                RowY +=height;
            }else{
                point.x = column2X;
                point.y = RowY2;

                RowY2+=height;
            }

            predefinedPositions.add(point);
        }

        return predefinedPositions;
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
    private void startFragAnimations(){
        binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
        binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
        scaleDownAnimtion();
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

        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                binding.imageViewAux1MainActivity.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
    private void scaleUpAnimtion(){
        float startScale = 1.0f;
        float endScale = 200.0f;

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                startScale, endScale,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        scaleAnimation.setDuration(1500);

        binding.imageViewAux1MainActivity.startAnimation(scaleAnimation);
    }
    private void fadeInAnimation(View view){
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_in);
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
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
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
    //----------------------------------------------

    //----------------LISTENNERS--------------------
    @Override
    public void OnFragMainACMainViewEditLayoutExitClick(Boolean Changed, List<DraggableCardViewEntity> listReturned) {
        scaleUpAnimtion();
        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                mainACMainViewFragment = new MainACMainViewFragment(listReturned);
                changeFragmentFromMainFragmentContainer(1);
                outFragment();
                scaleDownAnimtion();
                binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
            }
        }.start();
    }
    //----------------------------------------------

    //----------------DATABASE OPERATIONS--------------------
    private class LocalDatabaseGetAllDraggableCardViewsTask extends AsyncTask<Void, Void, List<DraggableCardViewEntity>> {
        @Override
        protected List<DraggableCardViewEntity> doInBackground(Void... voids) {
            draggableCardViewObjectsList = draggableCardViewDao.getObjects();
            Collections.sort(draggableCardViewObjectsList, new Comparator<DraggableCardViewEntity>() {
                @Override
                public int compare(DraggableCardViewEntity obj1, DraggableCardViewEntity obj2) {
                    // Compare based on the intValue field
                    return Integer.compare(obj1.getId(), obj2.getId());
                }
            });
            if(draggableCardViewObjectsList.size() > 0){
                return draggableCardViewObjectsList;
            }else{
                exampleDraggableCardViewObjectsList = loadExampleDraggableCardViewList();
                return exampleDraggableCardViewObjectsList;
            }
        }

        @Override
        protected void onPostExecute(List<DraggableCardViewEntity> draggableCardViewEntityList) {
            mainACMainViewFragment.updateData(draggableCardViewEntityList);
        }
    }
    private class LocalDatabaseGetAllSpendingsAccountsTask extends AsyncTask<Void, Void, List<SpendingAccountsEntity>> {
        @Override
        protected List<SpendingAccountsEntity> doInBackground(Void... voids) {
            spendingAccountsEntitiesList = spendingsAccountsDao.getSpendingsAccounts();
            return spendingAccountsEntitiesList;
        }

        @Override
        protected void onPostExecute(List<SpendingAccountsEntity> spendingAccountsEntitiesList) {
            mainACOverviewViewFragment.updateData(spendingAccountsEntitiesList);
        }
    }
    private class LocalDatabaseGetUserInfosTask extends AsyncTask<Void, Void, UserInfosEntity> {
        @Override
        protected UserInfosEntity doInBackground(Void... voids) {
            List<UserInfosEntity> userInfosEntityList = userInfosDao.getUserInfos();
            UserInfosEntity userInfosEntity = userInfosEntityList.get(0);;

            return userInfosEntity;
        }

        @Override
        protected void onPostExecute(UserInfosEntity userInfos) {
            UserInfosEntity = userInfos;
        }
    }
    //----------------------------------------------
}