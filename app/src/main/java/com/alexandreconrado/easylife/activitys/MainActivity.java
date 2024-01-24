package com.alexandreconrado.easylife.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.daos.DraggableCardViewDao;
import com.alexandreconrado.easylife.database.daos.SpendingsAccountsDao;
import com.alexandreconrado.easylife.database.daos.UserInfosDao;
import com.alexandreconrado.easylife.database.entities.DraggableCardViewEntity;
import com.alexandreconrado.easylife.database.LocalDataBase;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.database.entities.SubSpendingAccountsEntity;
import com.alexandreconrado.easylife.database.entities.UserInfosEntity;
import com.alexandreconrado.easylife.databinding.ActivityMainBinding;
import com.alexandreconrado.easylife.fragments.AuthenticationFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.MainACMainViewEditLayoutFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.MainACMainViewFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.add.MainACOverviewViewAddSpendingAccountFormFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.MainACOverviewViewFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.MainACOverviewViewSpendingAccountDetailsFormFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.MainACSpendingsViewAddSpendingsFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.MainACSpendingsViewFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialAddFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialEditFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialEndFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialShowFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialWelcomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainACMainViewEditLayoutFragment.OnFragMainACMainViewEditLayoutExitClick,
        MainACOverviewViewAddSpendingAccountFormFragment.ExitButtonClickFragMainACOverviewViewAddSpendingsForm,
        MainACMainViewFragment.ConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC,
        MainACOverviewViewFragment.SpendingsAccountItemClickFragMainACOverviewView,
        MainACOverviewViewSpendingAccountDetailsFormFragment.ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm,
        AuthenticationFragment.AuthenticationCompletedFragAuthentication,
        MainACSpendingsViewAddSpendingsFragment.ExitMainACSpendingsViewAddSpendingsFrag {
    //-------------------OTHERS---------------
    private ActivityMainBinding binding;
    private long sessionTime;
    public boolean seenTutorial, allDisable = false, updateMainViewInNextLoad = false;
    private UserInfosEntity UserInfosEntity;
    //-------------------SIDE MENU---------------
    private DrawerLayout drawerLayoutSideMenu;
    private NavigationView navigationViewSideMenu;
    private ActionBarDrawerToggle drawerToggleSideMenu;
    //-------------------ACTIVITIES AND FRAGMENTS---------------
    private MainActivity THIS;
    private MainACMainViewFragment mainACMainViewFragment;
    private MainACOverviewViewFragment mainACOverviewViewFragment;
    private MainACSpendingsViewFragment mainACSpendingsViewFragment;
    //-------------------LISTS---------------
    private List<DraggableCardViewEntity> draggableCardViewObjectsList;
    private List<SpendingAccountsEntity> spendingAccountsEntitiesList;
    private List<SpendsEntity> allSpendsList;
    //-------------------LOCAL DATABASE---------------
    private LocalDataBase localDataBase;
    private DraggableCardViewDao draggableCardViewDao;
    private SpendingsAccountsDao spendingsAccountsDao;
    private UserInfosDao userInfosDao;

    public interface DatabaseCallback {
        void onTaskCompleted(List<SpendingAccountsEntity> result);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("Perf_User", MODE_PRIVATE);
        seenTutorial = prefs.getBoolean("seenTutorial", false);
        boolean debugMode = prefs.getBoolean("debugMode", false);
        if(!debugMode){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        if(seenTutorial){
            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    init();
                    startFragAnimations();
                    showAuthenticationScreen();
                }
            }.start();
        } else{
            inFragment();
            init();
            binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
            tutorialChangeFragments(0, false, false, 0);
        }
    }

    //-----------------SETUPS--------------------
    private void init(){
        THIS = this;
        draggableCardViewObjectsList = new ArrayList<>();
        spendingAccountsEntitiesList = new ArrayList<>();
        allSpendsList = new ArrayList<>();

        DatabaseCallback callback = new DatabaseCallback() {
            @Override
            public void onTaskCompleted(List<SpendingAccountsEntity> result) {
                mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                mainACOverviewViewFragment = new MainACOverviewViewFragment();
                mainACSpendingsViewFragment = new MainACSpendingsViewFragment();

                mainACOverviewViewFragment.updateData(spendingAccountsEntitiesList);
                mainACOverviewViewFragment.setSpendingsAccountItemClickFragMainACOverviewViewListenner(THIS);
                mainACMainViewFragment.setAccountsList(spendingAccountsEntitiesList);
                mainACMainViewFragment.setParent(THIS);

                allSpendsList = getAllSpends();
                mainACSpendingsViewFragment.updateData(allSpendsList);

                setupBottomNavigation();
                changeFragmentFromMainFragmentContainer(1);
                setupSideMenu();
                setupMultiFunctionButtonButton();
            }
        };

        setupLocalDataBase();
        new LocalDatabaseGetAllDraggableCardViewsTask().execute();
        new LocalDatabaseGetUserInfosTask().execute();
        new LocalDatabaseGetAllSpendingsAccountsTask(callback).execute();
    }
    private void setupBottomNavigation() {
        binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Home).setChecked(true);
        binding.bottomNavigationViewMainAC.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == binding.bottomNavigationViewMainAC.getSelectedItemId()) {
                    return true;
                }

                if(!allDisable){
                    if (item.getItemId() == R.id.menu_bottomNavigation_painel_Graffics) {
                        changeFragmentFromMainFragmentContainer(0);
                    } else if (item.getItemId() == R.id.menu_bottomNavigation_painel_Home) {
                        changeFragmentFromMainFragmentContainer(1);
                    } else if (item.getItemId() == R.id.menu_bottomNavigation_painel_SpendingsView) {
                        changeFragmentFromMainFragmentContainer(2);
                    }
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
                enableDisableAll(true);
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
                            MainACOverviewViewAddSpendingAccountFormFragment fragment = new MainACOverviewViewAddSpendingAccountFormFragment(UserInfosEntity);
                            fragment.setExitListenner(THIS);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else if (tag.equals("1")) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, new MainACMainViewEditLayoutFragment(THIS, draggableCardViewObjectsList))
                                    .addToBackStack(null)
                                    .commit();
                        }else if (tag.equals("2")) {
                            MainACSpendingsViewAddSpendingsFragment fragment = new MainACSpendingsViewAddSpendingsFragment(spendingAccountsEntitiesList, UserInfosEntity);
                            fragment.setExitMainACSpendingsViewAddSpendingsFragListenner(THIS);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, fragment)
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
                showAuthenticationScreen();
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

                        SharedPreferences sharedPreferences = getSharedPreferences("Perf_User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("seenTutorial", true);
                        editor.apply();

                        init();

                        outFragment();
                        scaleDownAnimtion();
                        binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                        binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
                    }
                }.start();
                break;
        }
    }
    private void changeFragmentFromMainFragmentContainer(int selected){
        changeMultiFunctionButtonFunction(selected);
        switch (selected){
            case 0:
                mainACOverviewViewFragment.setSpendingsAccountItemClickFragMainACOverviewViewListenner(THIS);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACOverviewViewFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                if(updateMainViewInNextLoad){
                    mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                }
                mainACMainViewFragment.setAccountsList(spendingAccountsEntitiesList);
                mainACMainViewFragment.setConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainACListenner(THIS);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACMainViewFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                allSpendsList = getAllSpends();
                mainACSpendingsViewFragment.updateData(allSpendsList);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACSpendingsViewFragment)
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
                binding.imageViewButtonMultiFunctionMainViewMainAC.setImageDrawable(getDrawable(R.drawable.add));
                binding.imageViewButtonMultiFunctionMainViewMainAC.setTag("2");
                break;
        }
        fadeInAnimation(binding.imageViewButtonMultiFunctionMainViewMainAC);
    }
    private void enableDisableAll(boolean bool){
        allDisable = bool;
        if(mainACMainViewFragment != null){
            mainACMainViewFragment.setParentALlDisbale(bool);
        }
        if(bool){
            binding.imageViewButtonSideMenuMainAC.setEnabled(false);
            binding.imageViewButtonMultiFunctionMainViewMainAC.setEnabled(false);

            binding.frameLayoutFragmentContainerMainAC.setEnabled(false);

            binding.bottomNavigationViewMainAC.setEnabled(false);
        }else{
            binding.imageViewButtonSideMenuMainAC.setEnabled(true);
            binding.imageViewButtonMultiFunctionMainViewMainAC.setEnabled(true);

            binding.frameLayoutFragmentContainerMainAC.setEnabled(true);

            binding.bottomNavigationViewMainAC.setEnabled(true);
        }
    }
    private void showAuthenticationScreen(){
        inFragment();
        enableDisableAll(true);
        binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
        binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.VISIBLE);
        binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(true);
        AuthenticationFragment fragment = new AuthenticationFragment();
        fragment.setAuthenticationCompletedFragAuthenticationListenner(this);
        fragment.setParent(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, fragment)
                .addToBackStack(null)
                .commit();
    }
    private List<SpendsEntity> getAllSpends(){
        List<SpendsEntity> spendsEntityList = new ArrayList<>();

        for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
            SpendingAccountsEntity spendingAccountsSelected = spendingAccountsEntitiesList.get(i);

            spendsEntityList.addAll(spendingAccountsSelected.getSpendsList());
            List<SubSpendingAccountsEntity> subAccounts = spendingAccountsSelected.getSubAccountsList();
            if(subAccounts != null){
                for (int j = 0; j < subAccounts.size(); j++) {
                    SubSpendingAccountsEntity subSpendingAccountsSelected = spendingAccountsSelected.getSubAccountsList().get(j);
                    spendsEntityList.addAll(subSpendingAccountsSelected.getSpendsList());
                }
            }
        }

        return spendsEntityList;
    }
    //----------------------------------------------

    //----------EXAMPLE LIST----------------
    private List<DraggableCardViewEntity> loadExampleDraggableCardViewList(){
        List<DraggableCardViewEntity> list = new ArrayList<>();
        int width2 = (int) (binding.frameLayoutFragmentContainerMainAC.getWidth() * 0.5f);
        int height2 = (int) (binding.frameLayoutFragmentContainerMainAC.getHeight() * 0.2f);
        List<Point> predefinedPositions = setPredefinedPositions(width2, height2);
        int id = 0;
        for (int j = 0; j < 5; j++) {
            Point point = new Point();
            String type = "";
            String style = "1";
            int position = 0;

            switch (j){
                case 0:
                    point = predefinedPositions.get(0);
                    type = "2";
                    break;
                case 1:
                    point = predefinedPositions.get(1);
                    type = "1";
                    position = 1;
                    break;
                case 2:
                    point = predefinedPositions.get(6);
                    type = "1";
                    position = 6;
                    break;
                case 3:
                    point = predefinedPositions.get(2);
                    type = "2";
                    style = "2";
                    position = 2;
                    break;
                case 4:
                    point = predefinedPositions.get(3);
                    type = "3";
                    position = 3;
                    break;
            }

            for (int i = 0; i < predefinedPositions.size(); i++) {
                if(predefinedPositions.get(i).equals(point)){
                    float percentage = 25;
                    if(type.equals("3")){
                        percentage = 12.5f;
                    }
                    DraggableCardViewEntity object = new DraggableCardViewEntity(i, type, getString(R.string.mainAc_FragMainView_Example_ChartName_Text), style);
                    object.setPosition(position);
                    object.setInfos("", "", percentage, percentage, percentage, percentage,
                            getString(R.string.mainAc_FragMainView_Example_Percentage1_Text), getString(R.string.mainAc_FragMainView_Example_Percentage2_Text),
                            getString(R.string.mainAc_FragMainView_Example_Percentage3_Text), getString(R.string.mainAc_FragMainView_Example_Percentage4_Text),
                            getResources().getColor(R.color.highlightedTextDark), getResources().getColor(R.color.textDark),
                            getResources().getColor(R.color.highlightedTextLight), getResources().getColor(R.color.textLight));
                    if(type.equals("3")){
                        object.setInfosType3(percentage, percentage, percentage, percentage,
                                getString(R.string.mainAc_FragMainView_Example_Percentage5_Text), getString(R.string.mainAc_FragMainView_Example_Percentage6_Text),
                                getString(R.string.mainAc_FragMainView_Example_Percentage7_Text), getString(R.string.mainAc_FragMainView_Example_Percentage8_Text),
                                getResources().getColor(R.color.highlightedTextDark), getResources().getColor(R.color.textDark),
                                getResources().getColor(R.color.highlightedTextLight), getResources().getColor(R.color.textLight));
                    }
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
                if(!allDisable){
                    item.setEnabled(false);
                    if (item.getItemId() == R.id.mainAc_SideBar_Share) {
                        showShareBottomSheet();
                    }else if(item.getItemId() == R.id.mainAc_SideBar_RateUs){
                        SharedPreferences sharedPreferences = getSharedPreferences("Perf_User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        int debugModeHelper = sharedPreferences.getInt("debugModeHelper", 0);
                        if(debugModeHelper >= 5){
                            editor.putBoolean("debugMode", true);
                            editor.putInt("debugModeHelper", 0);
                            editor.apply();

                            finish();
                        }else{
                            debugModeHelper++;
                            editor.putInt("debugModeHelper", debugModeHelper);
                            editor.apply();
                        }
                    }
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

                if(Changed){
                    mainACMainViewFragment = new MainACMainViewFragment(listReturned);
                    draggableCardViewObjectsList.clear();
                    draggableCardViewObjectsList.addAll(listReturned);
                }else{
                    mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                }
                changeFragmentFromMainFragmentContainer(1);
                outFragment();
                scaleDownAnimtion();
                enableDisableAll(false);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
            }
        }.start();
    }
    @Override
    public void onExitButtonClickFragMainACOverviewViewAddSpendingsForm(Boolean Changed, SpendingAccountsEntity returned) {
        scaleUpAnimtion();
        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mainACOverviewViewFragment = new MainACOverviewViewFragment();
                mainACOverviewViewFragment.setSpendingsAccountItemClickFragMainACOverviewViewListenner(THIS);
                if(Changed){
                    spendingAccountsEntitiesList.add(returned);
                }
                mainACOverviewViewFragment.updateData(spendingAccountsEntitiesList);
                changeFragmentFromMainFragmentContainer(0);
                outFragment();
                scaleDownAnimtion();
                enableDisableAll(false);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
            }
        }.start();
    }
    @Override
    public void onConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC(DraggableCardViewEntity object, boolean canHoldMainAccount, int selectedSubAccountIndex) {
        new LocalDatabaseUpdateDraggableObjectsTask(object, object, canHoldMainAccount, selectedSubAccountIndex, false).execute();
    }
    @Override
    public void onSpendingsAccountItemClickFragMainACOverviewView(SpendingAccountsEntity account) {
        if(!allDisable){
            scaleUpAnimtion();
            enableDisableAll(true);
            new CountDownTimer(1200, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    inFragment();

                    binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
                    binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.VISIBLE);
                    binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(true);

                    MainACOverviewViewSpendingAccountDetailsFormFragment fragment = new MainACOverviewViewSpendingAccountDetailsFormFragment(account);
                    fragment.setExitButtonClickFragMainACOverviewViewSpendingAccountDetailsFormListenner(THIS);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, fragment)
                            .addToBackStack(null)
                            .commit();

                    scaleDownAnimtion();
                }
            }.start();
        }
    }
    @Override
    public void onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(SpendingAccountsEntity account, boolean deleted) {
        scaleUpAnimtion();
        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                if(deleted){
                    for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                        if(spendingAccountsEntitiesList.get(i).getId() == account.getId()){
                            spendingAccountsEntitiesList.remove(spendingAccountsEntitiesList.get(i));

                            new LocalDatabaseJustDeleteSpendingAccountTask(account).execute();
                            break;
                        }
                    }

                    for (int i = 0; i < draggableCardViewObjectsList.size(); i++) {
                        DraggableCardViewEntity selectedDraggable = draggableCardViewObjectsList.get(i);
                        if(selectedDraggable.getAccountID().equals(String.valueOf(account.getId()))){
                            DraggableCardViewEntity newObject = setObjectToExample(selectedDraggable);

                            draggableCardViewObjectsList.remove(selectedDraggable);
                            draggableCardViewObjectsList.add(i, newObject);

                            new LocalDatabaseJustUpdateDraggableObjectTask(newObject).execute();
                            break;
                        }
                    }
                }else{
                    for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                        if(spendingAccountsEntitiesList.get(i).getId() == account.getId()){
                            spendingAccountsEntitiesList.remove(spendingAccountsEntitiesList.get(i));
                            spendingAccountsEntitiesList.add(i, account);
                            break;
                        }
                    }

                    for (int i = 0; i < draggableCardViewObjectsList.size(); i++) {
                        DraggableCardViewEntity selected = draggableCardViewObjectsList.get(i);
                        if(selected.getAccountID() != null && !selected.getAccountID().equals("")){
                            DraggableCardViewEntity oldObject = selected;
                            int id = Integer.parseInt(selected.getAccountID());
                            if(id == account.getId()){
                                boolean can = false;
                                if(selected.getType().equals("3")){
                                    can = true;
                                }
                                int subAccountIdIndex = 0;
                                selected.setAccountID(String.valueOf(account.getId()));
                                if(selected.getSubAccountID() != null && !selected.getAccountID().equals("")){
                                    for (int j = 0; j < account.getSubAccountsList().size(); j++) {
                                        SubSpendingAccountsEntity selectedSub = account.getSubAccountsList().get(j);
                                        if(!selected.getSubAccountID().equals("")){
                                            if(selectedSub.getId() == Integer.parseInt(selected.getSubAccountID())){
                                                subAccountIdIndex = j;
                                                break;
                                            }
                                        }
                                    }
                                }
                                new LocalDatabaseUpdateDraggableObjectsTask(selected, oldObject, can, subAccountIdIndex, true).execute();
                            }
                        }
                    }
                }

                mainACOverviewViewFragment = new MainACOverviewViewFragment();
                mainACOverviewViewFragment.updateData(spendingAccountsEntitiesList);
                changeFragmentFromMainFragmentContainer(0);

                outFragment();
                scaleDownAnimtion();
                enableDisableAll(false);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
            }
        }.start();
    }
    @Override
    public void onAuthenticationCompletedFragAuthentication() {
        sessionTime = System.currentTimeMillis();
        scaleUpAnimtion();
        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                mainACOverviewViewFragment = new MainACOverviewViewFragment();
                mainACSpendingsViewFragment = new MainACSpendingsViewFragment();

                mainACOverviewViewFragment.updateData(spendingAccountsEntitiesList);
                mainACOverviewViewFragment.setSpendingsAccountItemClickFragMainACOverviewViewListenner(THIS);
                mainACMainViewFragment.setAccountsList(spendingAccountsEntitiesList);
                mainACMainViewFragment.setParent(THIS);

                changeFragmentFromMainFragmentContainer(1);
                changeMultiFunctionButtonFunction(1);
                binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Home).setChecked(true);
                binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_SpendingsView).setChecked(false);
                binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Graffics).setChecked(false);

                outFragment();
                scaleDownAnimtion();
                enableDisableAll(false);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
            }
        }.start();
    }
    @Override
    public void onExitMainACSpendingsViewAddSpendingsFrag(boolean save) {
        scaleUpAnimtion();
        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mainACSpendingsViewFragment = new MainACSpendingsViewFragment();
                if(save){

                }
                changeFragmentFromMainFragmentContainer(2);
                outFragment();
                scaleDownAnimtion();
                enableDisableAll(false);
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
            try {
                draggableCardViewObjectsList = draggableCardViewDao.getObjects();
                if(draggableCardViewObjectsList.size() < 1){
                    draggableCardViewObjectsList = loadExampleDraggableCardViewList();
                    draggableCardViewDao.insertList(draggableCardViewObjectsList);
                }else{
                    Collections.sort(draggableCardViewObjectsList, new Comparator<DraggableCardViewEntity>() {
                        @Override
                        public int compare(DraggableCardViewEntity obj1, DraggableCardViewEntity obj2) {
                            return Integer.compare(obj1.getId(), obj2.getId());
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("DatabaseError", "Error fetching draggable card views", e);
            }
            return draggableCardViewObjectsList;
        }

        @Override
        protected void onPostExecute(List<DraggableCardViewEntity> draggableCardViewEntityList) {
            Log.i("DataBaseOperationsMainAC", "onPostExecute LocalDatabaseGetAllDraggableCardViewsTask");
        }
    }
    private class LocalDatabaseGetAllSpendingsAccountsTask extends AsyncTask<Void, Void, List<SpendingAccountsEntity>> {
        private DatabaseCallback callback;

        public LocalDatabaseGetAllSpendingsAccountsTask(DatabaseCallback callback) {
            this.callback = callback;
        }
        @Override
        protected List<SpendingAccountsEntity> doInBackground(Void... voids) {
            Log.i("DataBaseOperationsMainAC", "onPostExecute LocalDatabaseGetAllSpendingsAccountsTask");
            spendingAccountsEntitiesList = spendingsAccountsDao.getSpendingsAccounts();
            return spendingAccountsEntitiesList;
        }

        @Override
        protected void onPostExecute(List<SpendingAccountsEntity> spendingAccountsEntitiesList) {
            // Notify the callback that the task is completed
            if (callback != null) {
                callback.onTaskCompleted(spendingAccountsEntitiesList);
            }
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
            Log.i("DataBaseOperationsMainAC", "onPostExecute LocalDatabaseGetUserInfosTask");
            UserInfosEntity = userInfos;
        }
    }
    private class LocalDatabaseUpdateDraggableObjectsTask extends AsyncTask<Void, Void, DraggableCardViewEntity> {
        private DraggableCardViewEntity object;
        private DraggableCardViewEntity oldObject;
        private boolean canHoldMainAccount;
        private int selectedSubAccountIndex;
        private boolean justUpdate = false;

        public LocalDatabaseUpdateDraggableObjectsTask(DraggableCardViewEntity object,  DraggableCardViewEntity oldObject, boolean canHoldMainAccount, int selectedSubAccountIndex, boolean justUpdate) {
            this.object = object;
            this.oldObject = oldObject;
            this.canHoldMainAccount = canHoldMainAccount;
            this.selectedSubAccountIndex = selectedSubAccountIndex;
            this.justUpdate = justUpdate;
        }
        @Override
        protected DraggableCardViewEntity doInBackground(Void... voids) {
            SpendingAccountsEntity selectedAccount = null;
            for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                if(spendingAccountsEntitiesList.get(i).getId() == Integer.parseInt(object.getAccountID())){
                    selectedAccount = spendingAccountsEntitiesList.get(i);
                    break;
                }
            }

            List<String> percentagesNameList = new ArrayList<>();
            List<String> percentagesColorList = new ArrayList<>();
            List<Float> percentagesList = new ArrayList<>();
            String AccountName = getString(R.string.mainAc_FragMainView_Example_ChartName_Text);

            if(!canHoldMainAccount){
                boolean didNotDelete = false;
                for (int i = 0; i < selectedAccount.getSubAccountsList().size(); i++) {
                    if(i == selectedSubAccountIndex){
                        didNotDelete = true;
                        break;
                    }
                }
                if(didNotDelete){
                    SubSpendingAccountsEntity subAccount = selectedAccount.getSubAccountsList().get(selectedSubAccountIndex);
                    if(subAccount != null){
                        AccountName = selectedAccount.getAccountTitle()+" - "+subAccount.getAccountTitle();
                        percentagesNameList = subAccount.getPercentagesNamesList();

                        while (percentagesNameList.size() < 4){
                            percentagesNameList.add("+");
                        }

                        percentagesColorList = subAccount.getPercentagesColorList();

                        while (percentagesColorList.size() < 4){
                            percentagesColorList.add("-1");
                        }

                        List<String> namesList = subAccount.getPercentagesNamesList();
                        for (int i = 0; i < namesList.size(); i++) {
                            if(namesList.get(i).equals("+")){
                                namesList.remove(i);
                                i--;
                            }
                        }
                        percentagesList = calculateSpendPercentages(subAccount.getSpendsList(),namesList);
                        object = clearObject(object);
                        object.setSubAccountID(String.valueOf(subAccount.getId()));
                    }
                }else{
                    object = setObjectToExample(object);
                }
            }else{
                AccountName = selectedAccount.getAccountTitle();
                percentagesNameList = selectedAccount.getPercentagesNamesList();

                while (percentagesNameList.size() < 8){
                    percentagesNameList.add("+");
                }

                percentagesColorList = selectedAccount.getPercentagesColorList();

                while (percentagesColorList.size() < 8){
                    percentagesColorList.add("-1");
                }

                List<String> namesList = selectedAccount.getPercentagesNamesList();
                for (int i = 0; i < namesList.size(); i++) {
                    if(namesList.get(i).equals("+")){
                        namesList.remove(i);
                        i--;
                    }
                }
                percentagesList = calculateSpendPercentages(selectedAccount.getSpendsList(),namesList);

                object = clearObject(object);
            }

            boolean everythingIs0 = true;

            for (int i = 0; i < percentagesList.size(); i++) {
                if(percentagesList.get(i) > 0.0){
                    everythingIs0 = false;
                    break;
                }
            }

            if(everythingIs0){
                float size = percentagesList.size();
                float percentagesAmount = 1.0f / size;
                for (int i = 0; i < size; i++) {
                    percentagesList.add(i, percentagesAmount);
                }
            }

            object.setChartName(AccountName);

            for (int i = 0; i < percentagesNameList.size(); i++) {
                int color = Integer.parseInt(percentagesColorList.get(i));
                String text = percentagesNameList.get(i);
                float percentage = percentagesList.get(i);
                switch (i){
                    case 0:
                        object.setValue1Text(text);
                        object.setValue1Color(color);
                        object.setValue1Percentage(percentage);
                        break;
                    case 1:
                        object.setValue2Text(text);
                        object.setValue2Color(color);
                        object.setValue2Percentage(percentage);
                        break;
                    case 2:
                        object.setValue3Text(text);
                        object.setValue3Color(color);
                        object.setValue3Percentage(percentage);
                        break;
                    case 3:
                        object.setValue4Text(text);
                        object.setValue4Color(color);
                        object.setValue4Percentage(percentage);
                        break;
                    case 4:
                        object.setValue5Text(text);
                        object.setValue5Color(color);
                        object.setValue5Percentage(percentage);
                        break;
                    case 5:
                        object.setValue6Text(text);
                        object.setValue6Color(color);
                        object.setValue6Percentage(percentage);
                        break;
                    case 6:
                        object.setValue7Text(text);
                        object.setValue7Color(color);
                        object.setValue7Percentage(percentage);
                        break;
                    case 7:
                        object.setValue8Text(text);
                        object.setValue8Color(color);
                        object.setValue8Percentage(percentage);
                        break;
                }
            }

            if(justUpdate){
                draggableCardViewObjectsList.remove(oldObject);
                draggableCardViewObjectsList.add(object);
            }

            draggableCardViewDao.update(object);

            return object;
        }

        @Override
        protected void onPostExecute(DraggableCardViewEntity object) {
            Log.i("DataBaseOperationsMainAC", "onPostExecute LocalDatabaseUpdateDraggableObjectsTask");
            if(!justUpdate){
                mainACMainViewFragment.updateData(draggableCardViewObjectsList);
            }else{
                updateMainViewInNextLoad = true;
            }
        }
    }
    private class LocalDatabaseJustUpdateDraggableObjectTask extends AsyncTask<Void, Void, DraggableCardViewEntity> {
        private DraggableCardViewEntity newObject;

        public LocalDatabaseJustUpdateDraggableObjectTask(DraggableCardViewEntity newObject) {
            this.newObject = newObject;
        }
        @Override
        protected DraggableCardViewEntity doInBackground(Void... voids) {
            draggableCardViewDao.update(newObject);

            return newObject;
        }

        @Override
        protected void onPostExecute(DraggableCardViewEntity object) {
            Log.i("DataBaseOperationsMainAC", "onPostExecute LocalDatabaseJustUpdateDraggableObjectTask");
            updateMainViewInNextLoad = true;
        }
    }
    private class LocalDatabaseJustDeleteSpendingAccountTask extends AsyncTask<Void, Void, SpendingAccountsEntity> {
        private SpendingAccountsEntity newObject;

        public LocalDatabaseJustDeleteSpendingAccountTask(SpendingAccountsEntity newObject) {
            this.newObject = newObject;
        }
        @Override
        protected SpendingAccountsEntity doInBackground(Void... voids) {
            spendingsAccountsDao.delete(newObject);

            return newObject;
        }

        @Override
        protected void onPostExecute(SpendingAccountsEntity object) {
            Log.i("DataBaseOperationsMainAC", "onPostExecute LocalDatabaseJustDeleteSpendingAccountTask");
            updateMainViewInNextLoad = true;
        }
    }
    private DraggableCardViewEntity clearObject(DraggableCardViewEntity object){
        object.setValue1Text("");
        object.setValue1Color(0);
        object.setValue1Percentage(0.0f);

        object.setValue2Text("");
        object.setValue2Color(0);
        object.setValue2Percentage(0.0f);

        object.setValue3Text("");
        object.setValue3Color(0);
        object.setValue3Percentage(0.0f);

        object.setValue4Text("");
        object.setValue4Color(0);
        object.setValue4Percentage(0.0f);

        object.setValue5Text("");
        object.setValue5Color(0);
        object.setValue5Percentage(0.0f);

        object.setValue6Text("");
        object.setValue6Color(0);
        object.setValue6Percentage(0.0f);

        object.setValue7Text("");
        object.setValue7Color(0);
        object.setValue7Percentage(0.0f);

        object.setValue8Text("");
        object.setValue8Color(0);
        object.setValue8Percentage(0.0f);

        return object;
    }
    private DraggableCardViewEntity setObjectToExample(DraggableCardViewEntity object){
        float percentage = 25;
        if(object.getType().equals("3")){
            percentage = 12.5f;
        }

        object.setChartName(getString(R.string.mainAc_FragMainView_Example_ChartName_Text));
        object.setInfos("", "", percentage, percentage, percentage, percentage,
                getString(R.string.mainAc_FragMainView_Example_Percentage1_Text), getString(R.string.mainAc_FragMainView_Example_Percentage2_Text),
                getString(R.string.mainAc_FragMainView_Example_Percentage3_Text), getString(R.string.mainAc_FragMainView_Example_Percentage4_Text),
                getResources().getColor(R.color.highlightedTextDark), getResources().getColor(R.color.textDark),
                getResources().getColor(R.color.highlightedTextLight), getResources().getColor(R.color.textLight));
        if(object.getType().equals("3")){
            object.setInfosType3(percentage, percentage, percentage, percentage,
                    getString(R.string.mainAc_FragMainView_Example_Percentage5_Text), getString(R.string.mainAc_FragMainView_Example_Percentage6_Text),
                    getString(R.string.mainAc_FragMainView_Example_Percentage7_Text), getString(R.string.mainAc_FragMainView_Example_Percentage8_Text),
                    getResources().getColor(R.color.highlightedTextDark), getResources().getColor(R.color.textDark),
                    getResources().getColor(R.color.highlightedTextLight), getResources().getColor(R.color.textLight));
        }

        return object;
    }
    private static float calculatePercentage(float part, float whole) {
        if (whole != 0) {
            return (part / whole) * 100.0f;
        } else {
            return 0.0f;
        }
    }
    private List<Float> calculateSpendPercentages(List<SpendsEntity> objects, List<String> percentagesNameList){
        List<Float> percentagesList = new ArrayList<>();
        float totalAmount = 0;

        for (int i = 0; i < objects.size(); i++) {
            totalAmount += objects.get(i).getAmount();
        }

        for (int i = 0; i < percentagesNameList.size(); i++) {
            String percentage = percentagesNameList.get(i);
            float amount = 0;

            for (int j = 0; j < objects.size(); j++) {
                SpendsEntity spend = objects.get(j);
                if(spend.getIsPartOf().equals(percentage)){
                    amount += spend.getAmount();
                }
            }

            float percentageAmount = calculatePercentage(amount, totalAmount);
            percentagesList.add(percentageAmount);
        }

        return percentagesList;
    }
    //----------------------------------------------
}