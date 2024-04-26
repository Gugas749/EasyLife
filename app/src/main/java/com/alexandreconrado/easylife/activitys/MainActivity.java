package com.alexandreconrado.easylife.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.search.MainACMainViewSearchFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu.BackupsFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu.settings.CreditsFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.sidemenu.settings.SettingsFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.MainACMainViewEditLayoutFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.MainACMainViewFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.howto.MainACMainViewHowToBindAccountToCardHomeFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.main_view.howto.MainACMainViewHowToBindAccountToCardPopUpFormFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.add.MainACOverviewViewAddSpendingAccountFormFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.MainACOverviewViewFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.details.MainACOverviewViewSpendingAccountDetailsFormFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.howto.MainACOverviewViewHowToAddFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view.howto.MainACOverviewViewHowToDetailsFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.MainACSpendingsViewSpendsDetailsFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.add.MainACSpendingsViewAddSpendingsFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.MainACSpendingsViewFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.howto.MainACSpendingsViewHowToAddFragment;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.howto.MainACSpendingsViewHowToDetailsFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialAddFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialEditFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialEndFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialShowFragment;
import com.alexandreconrado.easylife.fragments.tutorial.TutorialWelcomeFragment;
import com.alexandreconrado.easylife.scripts.BackupsUpLoader;
import com.alexandreconrado.easylife.scripts.UniqueRandomStringGenerator;
import com.alexandreconrado.easylife.scripts.mailsending.SendMailTask;
import com.alexandreconrado.easylife.scripts.mailsending.SendMonthlyResumeEmail;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MainACMainViewEditLayoutFragment.OnFragMainACMainViewEditLayoutExitClick,
        MainACOverviewViewAddSpendingAccountFormFragment.ExitButtonClickFragMainACOverviewViewAddSpendingsForm,
        MainACMainViewFragment.ConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC,
        MainACOverviewViewFragment.SpendingsAccountItemClickFragMainACOverviewView,
        MainACOverviewViewSpendingAccountDetailsFormFragment.ExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm,
        AuthenticationFragment.AuthenticationCompletedFragAuthentication,
        MainACSpendingsViewAddSpendingsFragment.ExitMainACSpendingsViewAddSpendingsFrag,
        SettingsFragment.ExitSettingsFrag,
        MainACSpendingsViewSpendsDetailsFragment.ExitFragMainACSpendingsViewSpendsDetails,
        MainACSpendingsViewFragment.ItemClickRVAdapeterSpendsFragMainACSpendingsView,
        BackupsFragment.ExitBackupsFrag,
        CreditsFragment.ExitCreditsFrag,
        MainACMainViewFragment.ChangeToSearchButtonClick,
        MainACMainViewSearchFragment.ChangeToDefaultButtonClick {
    //-------------------OTHERS---------------
    private ActivityMainBinding binding;
    private long sessionTime;
    public boolean seenTutorial, allDisable = false, updateMainViewInNextLoad = false, showTutorials = false, fastRegister = false, inMainViewSearch = false;
    private UserInfosEntity UserInfosEntity;
    private String TAG = "EasyLife_Logs_MainAc", fastRegisterData = "";
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
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

        initSettings();
        new CountDownTimer(500, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                init();
                startFragAnimations();
                showAuthenticationScreen();

                Intent intent = getIntent();
                String processedDataFormPhoto = intent.getStringExtra("processedDataFormPhoto");
                if(processedDataFormPhoto != null && !processedDataFormPhoto.equals("")){
                    fastRegister = true;
                    fastRegisterData = processedDataFormPhoto;
                }
            }
        }.start();
    }

    //-----------------SETUPS--------------------
    private void initSettings(){
        SharedPreferences prefs = getSharedPreferences("Perf_User", MODE_PRIVATE);
        seenTutorial = prefs.getBoolean("seenTutorial", false);
        showTutorials = prefs.getBoolean("hideTutorials", true);
        boolean protectionMode = prefs.getBoolean("protectionMode", true);
        if(protectionMode){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        if(showTutorials){
            fadeInAnimation(binding.imageViewButtonHowToUseMainAC);
        }else{
            fadeOutAnimation(binding.imageViewButtonHowToUseMainAC);
        }
    }
    private void init(){
        THIS = this;
        disableBackPressed();
        draggableCardViewObjectsList = new ArrayList<>();
        spendingAccountsEntitiesList = new ArrayList<>();
        allSpendsList = new ArrayList<>();

        DatabaseCallback callback = new DatabaseCallback() {
            @Override
            public void onTaskCompleted(List<SpendingAccountsEntity> result) {
                mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                mainACOverviewViewFragment = new MainACOverviewViewFragment();
                mainACSpendingsViewFragment = new MainACSpendingsViewFragment(THIS);

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
                setupHowToButton();
                enableSwipeToOpenSideMenu();

                autoBackupCheck();
            }
        };

        setupLocalDataBase();
        new LocalDatabaseGetAllDraggableCardViewsTask().execute();
        new LocalDatabaseGetUserInfosTask().execute();
        new LocalDatabaseGetAllSpendingsAccountsTask(callback).execute();
    }
    private void setupBottomNavigation() {
        binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Graffics).setChecked(false);
        binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_SpendingsView).setChecked(false);
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
                new CountDownTimer(900, 1000) {
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
                            MainACSpendingsViewAddSpendingsFragment fragment = new MainACSpendingsViewAddSpendingsFragment(spendingAccountsEntitiesList, UserInfosEntity, fastRegister, fastRegisterData);
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
    private void setupHowToButton(){
        binding.imageViewButtonHowToUseMainAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("0")) {
                    runSwipeDownAnimation("OverviewView_HowTo");
                }else if (v.getTag().equals("1")) {
                    runSwipeDownAnimation("MainView_HowTo");
                } else if (v.getTag().equals("2")) {
                    runSwipeDownAnimation("SpendingsView_HowTo");
                }
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
        disableBackPressed();
        monthlyResumeEmailCheck();
        if(getSession()){ // SESSION STILL AVALIABLE

        }else{// SESSION NOT AVALIABLE
            showAuthenticationScreen();
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
    private void setupPieChart(PieChart pieChart, List<Float> percentagesList) {
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < percentagesList.size(); i++) {
            entries.add(new PieEntry(percentagesList.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        int color1 = getResources().getColor(R.color.textDark);
        int color2 = getResources().getColor(R.color.extra1);
        int color3 = getResources().getColor(R.color.extra2);
        int color4 = getResources().getColor(R.color.extra3);
        int color5 = getResources().getColor(R.color.extra4);
        int[] colors = new int[0];

        switch (percentagesList.size()){
            case 1:
                colors = new int[]{color1};
                break;
            case 2:
                colors = new int[]{color1, color2};
                break;
            case 3:
                colors = new int[]{color1, color2, color3};
                break;
            case 4:
                colors = new int[]{color1, color2, color3, color4};
                break;
            case 5:
                colors = new int[]{color1, color2, color3, color4, color5};
                break;
        }
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleRadius(20f);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.invalidate(); // refresh
    }
    private String convertColorToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
    private void monthlyResumeEmailCheck(){
        SharedPreferences sharedPreferences = getSharedPreferences("Perf_User", MODE_PRIVATE);
        String lastMonth = sharedPreferences.getString("lastSendMonthlyResumeEmailMonth", "");
        String lastYear = sharedPreferences.getString("lastSendMonthlyResumeEmailYear", "");
        if(!lastMonth.equals("") && !lastYear.equals("")){
            LocalDate currentDate = LocalDate.now();
            Month month = currentDate.getMonth();
            String year = String.valueOf(currentDate.getYear());
            String monthName = month.toString();

            if(!year.equals(lastYear) && !monthName.equals(lastMonth)){
                sendMonthlyResumeEmailHelper();
            }
        }

    }
    private void sendMonthlyResumeEmailHelper(){
        LocalDate currentDate = LocalDate.now();
        Month month = currentDate.getMonth();
        int year = currentDate.getYear();
        String monthNameEn = month.toString();

        Locale locale = Locale.getDefault();
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String monthName = symbols.getMonths()[month.getValue() - 1];

        SharedPreferences sharedPreferences = getSharedPreferences("Perf_User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastSendMonthlyResumeEmailMonth", monthNameEn);
        editor.putString("lastSendMonthlyResumeEmailYear", String.valueOf(year));
        editor.apply();

        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
        String title = " "+monthName+" "+year+" "+getString(R.string.mainAc_Monthly_ResumeEmail_Text_4);

        String textValue1 = getString(R.string.mainAc_Monthly_ResumeEmail_Text_1);
        String mostSpendText = getString(R.string.mainAc_Monthly_ResumeEmail_Text_2);
        String lessSpendText = getString(R.string.mainAc_Monthly_ResumeEmail_Text_3);
        String other = getString(R.string.general_Others);

        List<SpendsEntity> allSpendThisMonth = new ArrayList<>();
        for (int i = 0; i < allSpendsList.size(); i++) {
            SpendsEntity spendSelected = allSpendsList.get(i);

            Calendar cal = Calendar.getInstance();
            cal.setTime(spendSelected.getDate());
            int monthFor = cal.get(Calendar.MONTH);
            monthFor++;
            if(monthFor == month.getValue()){
                allSpendThisMonth.add(allSpendsList.get(i));
            }
        }

        double totalAmount = 0.0;
        for (int i = 0; i < allSpendThisMonth.size(); i++) {
            totalAmount += allSpendThisMonth.get(i).getAmount();
        }
        String totalAmountString = String.format("%.2f", totalAmount);
        totalAmountString += "€";

        double amountAux1 = 0.0;
        double amountAux2 = 0.0;
        String mostSpendAccountName = "";
        boolean moreThanOneAccount = false;
        if(spendingAccountsEntitiesList.size() > 1){
            moreThanOneAccount = true;
        }
        for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
            SpendingAccountsEntity selected = spendingAccountsEntitiesList.get(i);
            for (int k = 0; k < selected.getPercentagesNamesList().size(); k++) {
                for (int j = 0; j < allSpendThisMonth.size(); j++) {
                    if(allSpendThisMonth.get(j).getIsPartOf().equals(selected.getPercentagesNamesList().get(k))){
                        amountAux1 += allSpendThisMonth.get(j).getAmount();
                    }else if(allSpendThisMonth.get(j).getSubAccountID().equals(selected.getPercentagesNamesList().get(k))){
                        amountAux1 += allSpendThisMonth.get(j).getAmount();
                    }
                }
            }

            if(amountAux1 > amountAux2){
                mostSpendAccountName = selected.getAccountTitle();
                amountAux2 = amountAux1;
                amountAux1 = 0.0;
            }
        }

        List<String> accountsNames = new ArrayList<>();
        List<SpendsEntity> spendsEntityList = new ArrayList<>();
        List<SpendsEntity> spendsEntityListAUX = new ArrayList<>(allSpendThisMonth);

        if(moreThanOneAccount){
            boolean moreThanFour = false;
            for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                List<SpendsEntity> auxList1 = new ArrayList<>();

                auxList1.addAll(spendingAccountsEntitiesList.get(i).getSpendsList());

                if(spendingAccountsEntitiesList.get(i).getSubAccountsList() != null){
                    for (int j = 0; j < spendingAccountsEntitiesList.get(i).getSubAccountsList().size(); j++) {
                        SubSpendingAccountsEntity subSelected = spendingAccountsEntitiesList.get(j).getSubAccountsList().get(j);

                        auxList1.addAll(subSelected.getSpendsList());
                    }
                }


                List<SpendsEntity> auxList2 = new ArrayList<>();
                for (int j = 0; j < auxList1.size(); j++) {
                    SpendsEntity spend = auxList1.get(j);
                    spend.setIsPartOf(spendingAccountsEntitiesList.get(i).getAccountTitle());
                    auxList2.add(spend);
                }

                if(accountsNames.size() >= 4){
                    moreThanFour = true;
                    double aux1Amount = 0.0;
                    double maisPequeno = 0.0;
                    String maisPequenoString = "";
                    for (int j = 0; j < auxList2.size(); j++) {
                        aux1Amount += auxList2.get(j).getAmount();
                    }

                    for (int j = 0; j < accountsNames.size(); j++) {
                        double aux2Amount = 0.0;
                        for (int k = 0; k < spendsEntityList.size(); k++) {
                            if(spendsEntityList.get(k).getIsPartOf().equals(accountsNames.get(j))){
                                aux2Amount += spendsEntityList.get(k).getAmount();
                            }
                        }
                        if(aux2Amount < maisPequeno){
                            maisPequeno = aux2Amount;
                            maisPequenoString = accountsNames.get(j);
                        }
                    }

                    if(aux1Amount > maisPequeno){
                        accountsNames.remove(maisPequenoString);
                        accountsNames.add(spendingAccountsEntitiesList.get(i).getAccountTitle());
                        spendsEntityList.addAll(auxList2);
                    }
                }else{
                    accountsNames.add(spendingAccountsEntitiesList.get(i).getAccountTitle());
                    spendsEntityList.addAll(auxList2);
                }
            }

            if(spendingAccountsEntitiesList.size() > 4){
                accountsNames.add("Others");
            }
        }else{
            for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                accountsNames.addAll(spendingAccountsEntitiesList.get(i).getPercentagesNamesList());
                spendsEntityList.addAll(spendingAccountsEntitiesList.get(i).getSpendsList());
            }
        }
        List<SpendsEntity> spendsEntityListAUXXXXX = new ArrayList<>();
        for (int i = 0; i < spendsEntityList.size(); i++) {
            SpendsEntity spendSelected = spendsEntityList.get(i);

            Calendar cal = Calendar.getInstance();
            cal.setTime(spendSelected.getDate());
            int monthFor = cal.get(Calendar.MONTH);
            monthFor++;
            if(monthFor == month.getValue()){
                spendsEntityListAUXXXXX.add(spendsEntityList.get(i));
            }
        }
        spendsEntityList.clear();
        spendsEntityList.addAll(spendsEntityListAUXXXXX);

        for (int i = 0; i < accountsNames.size(); i++) {
            for (int j = 0; j < spendsEntityList.size(); j++) {
                if(spendsEntityList.get(j).getIsPartOf().equals(accountsNames.get(i))){
                    spendsEntityListAUX.add(spendsEntityList.get(j));
                }
            }
        }
        spendsEntityList.removeAll(spendsEntityListAUX);
        List<SpendsEntity> auxList2 = new ArrayList<>();
        for (int i = 0; i < spendsEntityList.size(); i++) {
            SpendsEntity spend = spendsEntityList.get(i);
            spend.setIsPartOf(other);
            auxList2.add(spend);
        }
        spendsEntityList.clear();
        spendsEntityList.addAll(spendsEntityListAUX);
        spendsEntityList.addAll(auxList2);

        List<Float> percentagesList = calculateSpendPercentages(spendsEntityList, accountsNames);

        String lessSpendAccountName = getString(R.string.mainAc_Monthly_ResumeEmail_Text_1);
        for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
            SpendingAccountsEntity selected = spendingAccountsEntitiesList.get(i);
            for (int k = 0; k < selected.getPercentagesNamesList().size(); k++) {
                for (int j = 0; j < allSpendThisMonth.size(); j++) {
                    if(allSpendThisMonth.get(j).getIsPartOf().equals(selected.getPercentagesNamesList().get(k))){
                        amountAux1 += allSpendThisMonth.get(j).getAmount();
                    }else if(allSpendThisMonth.get(j).getSubAccountID().equals(selected.getPercentagesNamesList().get(k))){
                        amountAux1 += allSpendThisMonth.get(j).getAmount();
                    }
                }
            }

            if(amountAux1 < amountAux2){
                lessSpendAccountName = selected.getAccountTitle();
                amountAux2 = amountAux1;
                amountAux1 = 0.0;
            }
        }

        PieChart pieChart = binding.piechartAuxMainAc;
        setupPieChart(pieChart, percentagesList);

        Bitmap chartBitmap = Bitmap.createBitmap(pieChart.getWidth(), pieChart.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(chartBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        pieChart.draw(canvas);

        List<String> colorsHexList = new ArrayList<>();
        int color1 = getResources().getColor(R.color.textDark);
        int color2 = getResources().getColor(R.color.extra1);
        int color3 = getResources().getColor(R.color.extra2);
        int color4 = getResources().getColor(R.color.extra3);
        int color5 = getResources().getColor(R.color.extra4);
        colorsHexList.add(convertColorToHex(color1));
        colorsHexList.add(convertColorToHex(color2));
        colorsHexList.add(convertColorToHex(color3));
        colorsHexList.add(convertColorToHex(color4));
        colorsHexList.add(convertColorToHex(color5));

        List<String> percentagesListString = new ArrayList<>();
        for (int i = 0; i < percentagesList.size(); i++) {
            String aux = String.format("%.2f", percentagesList.get(i));
            aux += "%";
            percentagesListString.add(aux);
        }

        SendMonthlyResumeEmail sendMonthlyResumeEmail = new SendMonthlyResumeEmail(UserInfosEntity, this);
        sendMonthlyResumeEmail.prepareMonthlyResumeEmailInfo(chartBitmap, title, textValue1, mostSpendText, lessSpendText,
                totalAmountString, mostSpendAccountName, lessSpendAccountName, percentagesListString, accountsNames, colorsHexList);
    }
    //-----------------------------------------------BACKUPS-----------------------------------------------
    private interface FirestoreDBCallback_getAllBackups{
        void onFirestoreDBCallback_getAllBackups(List<Timestamp> listBackupsDates);
    }
    private interface FirestoreDBCallback_UploadBackup{
        void onFirestoreDBCallback_UploadBackup(String docID);
    }
    private interface FirestoreDBCallback_getSelectedBackup{
        void onFirestoreDBCallback_getSelectedBackup(String docID);
    }
    private interface FirestoreDBCallback_deleteOldestBackup{
        void onFirestoreDBCallback_deleteOldestBackup();
    }

    private void autoBackupCheck(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String backups = prefs.getString("autoBackupTime", "monthly");
        String userFBid = prefs.getString("firebaseID", "");
        Timestamp lastBackup = UserInfosEntity.lastBackup;
        if(lastBackup != null){
            Date timestampDate = lastBackup.toDate();
            Date currentDate = new Date();
            long differenceInMillis = currentDate.getTime() - timestampDate.getTime();
            long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
            boolean aux = false;

            switch (backups){
                case "never":
                    aux = false;
                    break;
                case "weekly":
                    if(differenceInDays >= 7){
                        aux = true;
                    }
                    break;
                case "biweekly":
                    if(differenceInDays >= 14){
                        aux = true;
                    }
                    break;
                case "monthly":
                    if(differenceInDays >= 30){
                        aux = true;
                    }
                    break;
            }

            if(aux){
                FirestoreDBCallback_getAllBackups callbackGetAllBackups = new FirestoreDBCallback_getAllBackups() {
                    @Override
                    public void onFirestoreDBCallback_getAllBackups(List<Timestamp> listBackupsDates) {
                        if(listBackupsDates.size() < 8){
                            uploadBackup();
                        }else{
                            FirestoreDBCallback_deleteOldestBackup deleteOldestBackup = new FirestoreDBCallback_deleteOldestBackup() {
                                @Override
                                public void onFirestoreDBCallback_deleteOldestBackup() {
                                    uploadBackup();
                                }
                            };

                            deleteOldestBackup(deleteOldestBackup, listBackupsDates);
                        }
                    }
                };

                getAllBackups(userFBid, callbackGetAllBackups);
            }
        }else{
            UserInfosEntity.lastBackup = Timestamp.now();
            new LocalDatabaseJustUpdateUserInfoTask().execute();
        }
    }
    private void deleteOldestBackup(FirestoreDBCallback_deleteOldestBackup callback, List<Timestamp> listBackupsDates){
        Timestamp oldestTimestamp = listBackupsDates.get(0);
        for (Timestamp timestamp : listBackupsDates) {
            if (timestamp.compareTo(oldestTimestamp) < 0) {
                oldestTimestamp = timestamp;
            }
        }

        Timestamp selectedTimeStamp = oldestTimestamp;

        FirestoreDBCallback_getSelectedBackup callback2 = new FirestoreDBCallback_getSelectedBackup() {
            @Override
            public void onFirestoreDBCallback_getSelectedBackup(String docID) {
                deleteBackup(docID, callback);
            }
        };
        getSelectedBackup(callback2, selectedTimeStamp);
    }
    private void deleteBackup(String docID, FirestoreDBCallback_deleteOldestBackup callback){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        firestoreDB.collection("Users")
                .document(firebaseID)
                .collection("Backups")
                .document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onFirestoreDBCallback_deleteOldestBackup();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failure delete backup firestore operation: "+e);
                    }
                });
    }
    private void uploadBackup(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");

        FirestoreDBCallback_UploadBackup callback = new FirestoreDBCallback_UploadBackup() {
            @Override
            public void onFirestoreDBCallback_UploadBackup(String docID) {
                BackupsUpLoader backupsUpLoader = new BackupsUpLoader(getApplicationContext());
                backupsUpLoader.uploadBackup(firebaseID, docID);
            }
        };

        Timestamp currentTime = Timestamp.now();
        Map<String, Object> object = new HashMap<>();
        object.put("TimeStamp", currentTime);

        firestoreDB.collection("Users")
                .document(firebaseID)
                .collection("Backups")
                .add(object)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String backupDocID = documentReference.getId();
                        callback.onFirestoreDBCallback_UploadBackup(backupDocID);
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    private void getAllBackups(String userID, FirestoreDBCallback_getAllBackups callback){
        List<Timestamp> listBackupsDates = new ArrayList<>();
        firestoreDB.collection("Users")
                .document(userID)
                .collection("Backups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timestamp backupDate = document.getTimestamp("TimeStamp");
                                listBackupsDates.add(backupDate);
                            }
                            callback.onFirestoreDBCallback_getAllBackups(listBackupsDates);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void getSelectedBackup(FirestoreDBCallback_getSelectedBackup callback, Timestamp selectedTimeStamp){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Perf_User", MODE_PRIVATE);
        String firebaseID = sharedPreferences.getString("firebaseID", "");
        firestoreDB.collection("Users")
                .document(firebaseID)
                .collection("Backups")
                .whereEqualTo("TimeStamp", selectedTimeStamp)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            callback.onFirestoreDBCallback_getSelectedBackup(document.getId());
                            break;
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    }
                });
    }
    //------------------------------------------------------------------

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
        changeHowToButtonFunction(selected);
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
                if(inMainViewSearch){
                    MainACMainViewSearchFragment fragment = new MainACMainViewSearchFragment(THIS, spendingAccountsEntitiesList);
                    fragment.setChangeToDefaultButtonClick(THIS);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout_fragmentContainer_MainAC, fragment)
                            .addToBackStack(null)
                            .commit();
                }else{
                    if(updateMainViewInNextLoad){
                        mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                    }
                    mainACMainViewFragment.setAccountsList(spendingAccountsEntitiesList);
                    mainACMainViewFragment.setChangeToSearchButtonClick(THIS);
                    mainACMainViewFragment.setConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainACListenner(THIS);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACMainViewFragment)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 2:
                allSpendsList = getAllSpends();
                mainACSpendingsViewFragment.updateData(allSpendsList);
                mainACSpendingsViewFragment.setItemClickListenner(THIS);
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
    private void changeHowToButtonFunction(int selected){
        binding.imageViewButtonHowToUseMainAC.setTag(String.valueOf(selected));
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
    private void enableDisableAll(boolean disable){
        allDisable = disable;
        if(mainACMainViewFragment != null){
            mainACMainViewFragment.setParentALlDisbale(disable);
        }
        if(disable){
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
        disableSwipeToOpenSideMenu();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        enableDisableAll(true);
        binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
        binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.VISIBLE);
        binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(true);

        binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc.setBackground(null);
        binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc.setVisibility(View.GONE);

        AuthenticationFragment fragment = new AuthenticationFragment(THIS);
        fragment.setAuthenticationCompletedFragAuthenticationListenner(THIS);

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

                    }else if(item.getItemId() == R.id.mainAc_SideBar_Backups){
                        runSwipeRightAnimation("Backups");
                    }else if(item.getItemId() == R.id.mainAc_SideBar_Configs){
                        runSwipeRightAnimation("Settings");
                    }else if(item.getItemId() == R.id.mainAc_SideBar_Credits){
                        runSwipeRightAnimation("Credits");
                    }
                    item.setEnabled(true);
                }
                return true;
            }
        });
        navigationViewSideMenu.bringToFront();
    }
    private void enableSwipeToOpenSideMenu() {
        if (drawerLayoutSideMenu != null) {
            drawerLayoutSideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }
    private void disableSwipeToOpenSideMenu() {
        if (drawerLayoutSideMenu != null) {
            drawerLayoutSideMenu.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
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

        scaleAnimation.setDuration(1000);

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

        scaleAnimation.setDuration(1000);

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

        if(showTutorials){
            binding.imageViewButtonHowToUseMainAC.setVisibility(View.INVISIBLE);
            binding.imageViewButtonHowToUseMainAC.setEnabled(false);
        }
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

        if(showTutorials){
            binding.imageViewButtonHowToUseMainAC.setVisibility(View.VISIBLE);
            binding.imageViewButtonHowToUseMainAC.setEnabled(true);
        }
    }
    //----------------------------------------------

    //----------------LISTENNERS--------------------
    @Override
    public void OnFragMainACMainViewEditLayoutExitClick(boolean Changed, List<DraggableCardViewEntity> listReturned) {
        disableBackPressed();
        scaleUpAnimtion();
        new CountDownTimer(900, 1000) {
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
    public void onExitButtonClickFragMainACOverviewViewAddSpendingsForm(boolean Changed, SpendingAccountsEntity returned) {
        disableBackPressed();
        scaleUpAnimtion();
        new CountDownTimer(900, 1000) {
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
        disableBackPressed();
        new LocalDatabaseUpdateDraggableObjectsTask(object, object, canHoldMainAccount, selectedSubAccountIndex, false).execute();
    }
    @Override
    public void onSpendingsAccountItemClickFragMainACOverviewView(SpendingAccountsEntity account) {
        disableBackPressed();
        if(!allDisable){
            scaleUpAnimtion();
            enableDisableAll(true);
            new CountDownTimer(900, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    inFragment();

                    binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
                    binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.VISIBLE);
                    binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(true);

                    MainACOverviewViewSpendingAccountDetailsFormFragment fragment = new MainACOverviewViewSpendingAccountDetailsFormFragment(account, account.getAccountTitle());
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
    public void onExitButtonClickFragMainACOverviewViewSpendingAccountDetailsForm(SpendingAccountsEntity accountFromExitDetailsAccounts, boolean deleted, boolean changed, String oldAccountName) {
        disableBackPressed();
        scaleUpAnimtion();
        new CountDownTimer(900, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                if(changed){
                    if(deleted){
                        for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                            if(spendingAccountsEntitiesList.get(i).getId() == accountFromExitDetailsAccounts.getId()){
                                spendingAccountsEntitiesList.remove(spendingAccountsEntitiesList.get(i));

                                new LocalDatabaseJustDeleteSpendingAccountTask(accountFromExitDetailsAccounts).execute();
                                break;
                            }
                        }

                        for (int i = 0; i < draggableCardViewObjectsList.size(); i++) {
                            DraggableCardViewEntity selectedDraggable = draggableCardViewObjectsList.get(i);
                            if(selectedDraggable.getAccountID().equals(String.valueOf(accountFromExitDetailsAccounts.getId()))){
                                DraggableCardViewEntity newObject = setObjectToExample(selectedDraggable);

                                draggableCardViewObjectsList.remove(selectedDraggable);
                                draggableCardViewObjectsList.add(i, newObject);

                                new LocalDatabaseJustUpdateDraggableObjectTask(newObject).execute();
                                break;
                            }
                        }
                    }else{
                        for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                            if(spendingAccountsEntitiesList.get(i).getId() == accountFromExitDetailsAccounts.getId()){
                                spendingAccountsEntitiesList.remove(spendingAccountsEntitiesList.get(i));
                                spendingAccountsEntitiesList.add(i, accountFromExitDetailsAccounts);
                                break;
                            }
                        }

                        for (int i = 0; i < draggableCardViewObjectsList.size(); i++) {
                            DraggableCardViewEntity selected = draggableCardViewObjectsList.get(i);
                            if(selected.getChartName() != null && !selected.getChartName().equals("")){
                                DraggableCardViewEntity oldObject = selected;
                                if(oldAccountName.equals(selected.getChartName())){
                                    boolean can = false;
                                    if(selected.getType().equals("3")){
                                        can = true;
                                    }
                                    int subAccountIdIndex = 0;
                                    selected.setAccountID(String.valueOf(accountFromExitDetailsAccounts.getId()));
                                    if(selected.getSubAccountID() != null && !selected.getAccountID().equals("")){
                                        List<SubSpendingAccountsEntity> auxList = accountFromExitDetailsAccounts.getSubAccountsList();
                                        if(auxList != null){
                                            for (int j = 0; j < auxList.size(); j++) {
                                                SubSpendingAccountsEntity selectedSub = auxList.get(j);
                                                if(!selected.getSubAccountID().equals("")){
                                                    if(selectedSub.getId() == Integer.parseInt(selected.getSubAccountID())){
                                                        subAccountIdIndex = j;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    new LocalDatabaseUpdateDraggableObjectsTask(selected, oldObject, can, subAccountIdIndex, true).execute();
                                }
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disableBackPressed();
                enableSwipeToOpenSideMenu();

                sessionTime = System.currentTimeMillis();
                scaleUpAnimtion();

                new CountDownTimer(900, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // Code for each tick if needed
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

                        if(fastRegister){
                            changeFragmentFromMainFragmentContainer(2);
                            changeMultiFunctionButtonFunction(2);
                            binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Graffics).setChecked(false);
                            binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Home).setChecked(false);
                            binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_SpendingsView).setChecked(true);

                            inFragment();
                            scaleDownAnimtion();
                            enableDisableAll(true);

                            fastRegister = false;

                            MainACSpendingsViewAddSpendingsFragment fragment = new MainACSpendingsViewAddSpendingsFragment(spendingAccountsEntitiesList, UserInfosEntity, true, fastRegisterData);
                            fragment.setExitMainACSpendingsViewAddSpendingsFragListenner(THIS);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frameLayout_fullScreenFragmentContainer_MainAc, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }else{
                            changeFragmentFromMainFragmentContainer(1);
                            changeMultiFunctionButtonFunction(1);
                            binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_SpendingsView).setChecked(false);
                            binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Graffics).setChecked(false);
                            binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Home).setChecked(true);

                            outFragment();
                            scaleDownAnimtion();
                            enableDisableAll(false);
                            binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.GONE);
                            binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
                        }
                    }
                }.start();
            }
        });
    }
    @Override
    public void onExitMainACSpendingsViewAddSpendingsFrag(boolean save, SpendingAccountsEntity object) {
        disableBackPressed();
        scaleUpAnimtion();
        new CountDownTimer(900, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mainACSpendingsViewFragment = new MainACSpendingsViewFragment();
                if(save){
                    for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                        SpendingAccountsEntity selected = spendingAccountsEntitiesList.get(i);
                        if(selected.getId() == object.getId()){
                            spendingAccountsEntitiesList.remove(i);
                            spendingAccountsEntitiesList.add(i, object);
                            break;
                        }
                    }
                    for (int i = 0; i < draggableCardViewObjectsList.size(); i++) {
                        DraggableCardViewEntity selected = draggableCardViewObjectsList.get(i);
                        if(selected.getAccountID() != null && !selected.getAccountID().equals("")){
                            DraggableCardViewEntity oldObject = selected;
                            int id = Integer.parseInt(selected.getAccountID());
                            if(id == object.getId()){
                                boolean can = false;
                                if(selected.getType().equals("3")){
                                    can = true;
                                }
                                int subAccountIdIndex = 0;
                                selected.setAccountID(String.valueOf(object.getId()));
                                if(selected.getSubAccountID() != null && !selected.getAccountID().equals("")){
                                    List<SubSpendingAccountsEntity> auxList = object.getSubAccountsList();
                                    if(auxList != null){
                                        for (int j = 0; j < auxList.size(); j++) {
                                            SubSpendingAccountsEntity selectedSub = auxList.get(j);
                                            if(!selected.getSubAccountID().equals("")){
                                                if(selectedSub.getId() == Integer.parseInt(selected.getSubAccountID())){
                                                    subAccountIdIndex = j;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                new LocalDatabaseUpdateDraggableObjectsTask(selected, oldObject, can, subAccountIdIndex, true).execute();
                            }
                        }
                    }
                }
                changeFragmentFromMainFragmentContainer(2);
                outFragment();
                scaleDownAnimtion();
                enableDisableAll(false);
                binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Graffics).setChecked(false);
                binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_Home).setChecked(false);
                binding.bottomNavigationViewMainAC.getMenu().findItem(R.id.menu_bottomNavigation_painel_SpendingsView).setChecked(true);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);
            }
        }.start();
    }
    @Override
    public void onExitSettingsFrag(boolean changed) {
        disableBackPressed();
        runSwipeLeftAnimation(changed, "Settings");
        FragmentManager fragmentManager = getSupportFragmentManager();
        int containerId = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc.getId();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.getId() == containerId) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }
    @Override
    public void onItemClickRVAdapeterSpendsFragMainACSpendingsView(SpendsEntity spends) {
        disableBackPressed();
        if(!allDisable){
            scaleUpAnimtion();
            enableDisableAll(true);
            new CountDownTimer(900, 1000) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    inFragment();

                    binding.frameLayoutFullScreenFragmentContainerMainAc.setBackground(null);
                    binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.VISIBLE);
                    binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(true);

                    MainACSpendingsViewSpendsDetailsFragment fragment = new MainACSpendingsViewSpendsDetailsFragment(THIS, spends, spendingAccountsEntitiesList);
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
    public void onExitFragMainACSpendingsViewSpendsDetails(boolean changed, SpendingAccountsEntity account) {
        disableBackPressed();
        scaleUpAnimtion();
        new CountDownTimer(900, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mainACSpendingsViewFragment = new MainACSpendingsViewFragment();

                if(changed){
                    for (int i = 0; i < spendingAccountsEntitiesList.size(); i++) {
                        SpendingAccountsEntity selected = spendingAccountsEntitiesList.get(i);
                        if(selected.getId() == account.getId()){
                            spendingAccountsEntitiesList.remove(i);
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
                                    List<SubSpendingAccountsEntity> auxList = account.getSubAccountsList();
                                    if(auxList != null){
                                        for (int j = 0; j < auxList.size(); j++) {
                                            SubSpendingAccountsEntity selectedSub = auxList.get(j);
                                            if(!selected.getSubAccountID().equals("")){
                                                if(selectedSub.getId() == Integer.parseInt(selected.getSubAccountID())){
                                                    subAccountIdIndex = j;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                new LocalDatabaseUpdateDraggableObjectsTask(selected, oldObject, can, subAccountIdIndex, true).execute();
                            }
                        }
                    }
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
    @Override
    public void onExitBackupsFrag(boolean changed) {
        disableBackPressed();
        runSwipeLeftAnimation(changed, "Backups");
        FragmentManager fragmentManager = getSupportFragmentManager();
        int containerId = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc.getId();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.getId() == containerId) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }
    @Override
    public void onExitCreditsFrag() {
        disableBackPressed();
        runSwipeLeftAnimation(false, "Credits");
        FragmentManager fragmentManager = getSupportFragmentManager();
        int containerId = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc.getId();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.getId() == containerId) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }
    @Override
    public void ChangeToSearchButtonClick() {
        inMainViewSearch = true;
        runSwipeLeftAnimationMainView();
        //changeFragmentFromMainFragmentContainer(1);
    }
    @Override
    public void ChangeToDefaultButtonClick() {
        inMainViewSearch = false;
        runSwipeRightAnimationMainView();
        //changeFragmentFromMainFragmentContainer(1);
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
                List<SubSpendingAccountsEntity> listAux = selectedAccount.getSubAccountsList();
                if(listAux != null){
                    for (int i = 0; i < listAux.size(); i++) {
                        if(i == selectedSubAccountIndex){
                            didNotDelete = true;
                            break;
                        }
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

                List<SpendsEntity> spendsListFinal = new ArrayList<>();
                List<SpendsEntity> spendsList = getAllSpends();
                for (int i = 0; i < spendsList.size(); i++) {
                    SpendsEntity selectedSpend = spendsList.get(i);
                    if(selectedSpend.getMainAccountID().equals(selectedAccount.getAccountTitle())){
                        spendsListFinal.add(selectedSpend);
                    }
                }
                percentagesList = calculateSpendPercentages(spendsListFinal,namesList);

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
    private class LocalDatabaseJustUpdateUserInfoTask extends AsyncTask<Void, Void, UserInfosEntity> {
        public LocalDatabaseJustUpdateUserInfoTask() {

        }
        @Override
        protected UserInfosEntity doInBackground(Void... voids) {
            userInfosDao.update(UserInfosEntity);

            return UserInfosEntity;
        }

        @Override
        protected void onPostExecute(UserInfosEntity object) {
            Log.i("DataBaseOperationsMainAC", "onPostExecute LocalDatabaseJustUpdateUserInfoTask");
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
                if(spend.getIsPartOf().equals(percentage) || spend.getSubAccountID().equals(percentage)){
                    amount += spend.getAmount();
                }
            }

            float percentageAmount = calculatePercentage(amount, totalAmount);
            percentagesList.add(percentageAmount);
        }

        return percentagesList;
    }
    //----------------------------------------------
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Check for theme change and recreate the activity if needed
        if (newConfig.uiMode != getApplicationContext().getResources().getConfiguration().uiMode) {
            recreate();
        }
    }
    public void changeHowToFragment(String Tag, Boolean fromNext){
        switch (Tag){
            case "MainView_HowTo_goToHome":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACMainViewHowToBindAccountToCardHomeFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "MainView_HowTo_goToPopUpFrom":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACMainViewHowToBindAccountToCardPopUpFormFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "Overview_HowTo_goToAdd":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACOverviewViewHowToAddFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "Overview_HowTo_goToDetails": ;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACOverviewViewHowToDetailsFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "Spendings_HowTo_goToAdd":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACSpendingsViewHowToAddFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "Spendings_HowTo_goToDetails":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACSpendingsViewHowToDetailsFragment(THIS, fromNext))
                        .addToBackStack(null)
                        .commit();
                break;
            case "MainView_HowTo_finish":
                runSwipeUpAnimation();
                FragmentManager fragmentManager = getSupportFragmentManager();
                int containerId = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc.getId();

                for (Fragment fragment : fragmentManager.getFragments()) {
                    if (fragment != null && fragment.getId() == containerId) {
                        fragmentManager.beginTransaction().remove(fragment).commit();
                    }
                }
                break;
        }
    }
    private void runSwipeDownAnimation(String howToID) {
        ViewGroup container1 = binding.constraintLayoutMainAc;
        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc;
        container2.setVisibility(View.VISIBLE);
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(container1, "translationY", 0, container1.getHeight());
        translateYAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inFragment();
                container1.setVisibility(View.GONE);
                fadeInAnimation(container2);

                switch (howToID){
                    case "OverviewView_HowTo":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACOverviewViewHowToAddFragment(THIS, false))
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "MainView_HowTo":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACMainViewHowToBindAccountToCardHomeFragment(THIS, false))
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "SpendingsView_HowTo":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new MainACSpendingsViewHowToAddFragment(THIS, false))
                                .addToBackStack(null)
                                .commit();
                        break;
                }
            }
        });

        translateYAnimator.start();
    }
    private void runSwipeUpAnimation() {
        outFragment();
        ViewGroup container1 = binding.constraintLayoutMainAc;
        container1.setVisibility(View.VISIBLE);
        container1.setTranslationY(0);
        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc;
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(container1, "translationY", container1.getHeight(), 0);
        translateYAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.imageViewAux1MainActivity.setVisibility(View.GONE);
                container2.setVisibility(View.GONE);
                container2.setEnabled(false);
            }
        });

        translateYAnimator.start();
    }
    private void runSwipeRightAnimation(String fragmentID) {
        ViewGroup container1 = binding.constraintLayoutMainAc;
        container1.setVisibility(View.VISIBLE);

        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc;
        container2.setVisibility(View.INVISIBLE);

        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(container1, "translationX", container1.getWidth());
        translateXAnimator.setDuration(500);

        if(fragmentID.equals("Backups") || fragmentID.equals("Settings") || fragmentID.equals("Credits")){
            drawerLayoutSideMenu.closeDrawer(GravityCompat.END);
        }

        translateXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inFragment();
                container1.setVisibility(View.GONE);
                fadeInAnimation(container2);

                switch (fragmentID){
                    case "Backups":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new BackupsFragment(THIS))
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "Settings":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new SettingsFragment(THIS, UserInfosEntity, THIS))
                                .addToBackStack(null)
                                .commit();
                        break;
                    case "Credits":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout_fullScreenFragmentContainer_forHowTos_MainAc, new CreditsFragment(THIS))
                                .addToBackStack(null)
                                .commit();
                        break;
                }
            }
        });

        translateXAnimator.start();
    }
    private void runSwipeLeftAnimation(boolean changed, String Tag) {
        outFragment();

        ViewGroup container1 = binding.constraintLayoutMainAc;
        container1.setVisibility(View.VISIBLE);
        container1.setTranslationX(container1.getWidth()); // Start off-screen to the right

        ViewGroup container2 = binding.frameLayoutFullScreenFragmentContainerForHowTosMainAc;
        container2.setVisibility(View.VISIBLE);

        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(container1, "translationX", 0);
        translateXAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.imageViewAux1MainActivity.setVisibility(View.GONE);
                container2.setVisibility(View.GONE);
                container2.setEnabled(false);
                enableDisableAll(false);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setVisibility(View.INVISIBLE);
                binding.frameLayoutFullScreenFragmentContainerMainAc.setEnabled(false);

                if(changed){
                    switch (Tag){
                        case "Backups":
                            recreate();
                            break;
                        case "Settings":
                            initSettings();

                            mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                            changeFragmentFromMainFragmentContainer(1);
                            break;
                    }
                }
            }
        });

        translateXAnimator.start();
    }

    private void runSwipeRightAnimationMainView() {
        ViewGroup container1 = binding.frameLayoutFragmentContainer2MainAC;
        container1.setVisibility(View.VISIBLE);

        ViewGroup container2 = binding.frameLayoutFragmentContainerMainAC;
        container2.setVisibility(View.VISIBLE);

        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(container1, "translationX", container1.getWidth());
        translateXAnimator.setDuration(500);
        ObjectAnimator translateXAnimator2 = ObjectAnimator.ofFloat(container2, "translationX", 0);
        translateXAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container1.setVisibility(View.GONE);

                inMainViewSearch = false;
                if(updateMainViewInNextLoad){
                    mainACMainViewFragment = new MainACMainViewFragment(draggableCardViewObjectsList);
                }
                mainACMainViewFragment.setAccountsList(spendingAccountsEntitiesList);
                mainACMainViewFragment.setChangeToSearchButtonClick(THIS);
                mainACMainViewFragment.setConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainACListenner(THIS);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer_MainAC, mainACMainViewFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        translateXAnimator.start();
        translateXAnimator2.start();
    }
    private void runSwipeLeftAnimationMainView() {
        ViewGroup container1 = binding.frameLayoutFragmentContainer2MainAC;
        container1.setVisibility(View.VISIBLE);
        container1.setTranslationX(container1.getWidth());

        ViewGroup container2 = binding.frameLayoutFragmentContainerMainAC;
        container2.setVisibility(View.VISIBLE);

        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(container1, "translationX", 0);
        translateXAnimator.setDuration(500); // Set the duration of the animation in milliseconds
        ObjectAnimator translateXAnimator2 = ObjectAnimator.ofFloat(container2, "translationX", -container2.getWidth());
        translateXAnimator.setDuration(500); // Set the duration of the animation in milliseconds

        translateXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                container2.setVisibility(View.GONE);

                inMainViewSearch = true;
                MainACMainViewSearchFragment fragment = new MainACMainViewSearchFragment(THIS, spendingAccountsEntitiesList);
                fragment.setChangeToDefaultButtonClick(THIS);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout_fragmentContainer2_MainAC, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        translateXAnimator.start();
        translateXAnimator2.start();
    }
}