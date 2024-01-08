package com.example.easylife.fragments.mainactivityfragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.easylife.R;
import com.example.easylife.database.DraggableCardViewDao;
import com.example.easylife.database.DraggableCardViewEntity;
import com.example.easylife.database.LocalDataBase;
import com.example.easylife.databinding.FragmentMainACMainViewEditLayoutBinding;
import com.example.easylife.scripts.mainvieweditlayout_things.DraggableCardView;
import com.example.easylife.scripts.mainvieweditlayout_things.DraggableCardViewObject;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainACMainViewEditLayoutFragment extends Fragment implements DraggableCardView.OnCardViewDropListener {
    private DraggableCardView draggableCardView;
    private List<DraggableCardView> draggableCardViews;
    private List<Point> predefinedPositions;
    private List<Point> usedPositions = new ArrayList<>();
    private FragmentMainACMainViewEditLayoutBinding binding;
    private OnFragMainACMainViewEditLayoutExitClick onExitClickListenner;
    private List<DraggableCardViewEntity> draggableCardViewObjectList = new ArrayList<>();
    private DraggableCardViewDao draggableCardViewDao;
    private LocalDataBase database;
    private int objectsIDs = 0;
    private int mainCardViewColor;

    public interface OnFragMainACMainViewEditLayoutExitClick {
        void OnFragMainACMainViewEditLayoutExitClick(Boolean changed, List<DraggableCardViewEntity> draggableCardViewObjectList);
    }

    public MainACMainViewEditLayoutFragment(OnFragMainACMainViewEditLayoutExitClick onExitClickListenner, List<DraggableCardViewEntity> list) {
        this.onExitClickListenner = onExitClickListenner;
        this.draggableCardViewObjectList = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewEditLayoutBinding.inflate(inflater);
        disableEverything();
        init();

        setupConfirmButton();
        setupExitButton();
        setupBottomNavigation();
        setupHowToUseButton();
        setupLocalDataBase();

        initPostLoad();

        return binding.getRoot();
    }

    //------------------------------------SETUPS-------------------------------
    private void init(){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        mainCardViewColor = typedValue.data;
        draggableCardView = new DraggableCardView(getContext());
        draggableCardViews = new ArrayList<>();
    }
    private void initPostLoad(){
        new CountDownTimer(1300, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                int width2 = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getWidth() * 0.5f);
                int height2 = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getHeight() * 0.2f);
                predefinedPositions = setPredefinedPositions(width2, height2);
                if(draggableCardViewObjectList.size() > 0){
                    loadUsedPoint();
                    loadObjectsFromList();
                }

                enableEverything();
            }
        }.start();
    }
    private void setupLocalDataBase(){
        database = Room.databaseBuilder(getContext(), LocalDataBase.class, "EasyLifeLocalDB").build();
        draggableCardViewDao = database.draggableCardViewDao();
    }
    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACMainViewEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExitClickListenner.OnFragMainACMainViewEditLayoutExitClick(false, draggableCardViewObjectList);
            }
        });
    }
    private void setupConfirmButton(){
        binding.imageViewButtonConfirmFragMainACMainViewEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: adicionar alertDialog
                new LocalDatabaseDeleteAllTask().execute();
                new LocalDatabaseInsetTask().execute();
            }
        });
    }
    private void setupBottomNavigation() {
        binding.bottomNavigationViewFragMainACMainViewEditLayout.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                float widhtPercentage = 0.0f;
                float heightPercentage = 0.0f;
                String Tag = "";

                if (item.getItemId() == R.id.menu_bottomNavigation_addType1_MainACMainViewEditLayout) {
                    widhtPercentage = 0.495f;
                    heightPercentage = 0.195f;
                    Tag = "1";
                } else if (item.getItemId() == R.id.menu_bottomNavigation_addType2_MainACMainViewEditLayout) {
                    widhtPercentage = 0.98f;
                    heightPercentage = 0.195f;
                    Tag = "2";
                }else if (item.getItemId() == R.id.menu_bottomNavigation_addType3_MainACMainViewEditLayout) {
                    widhtPercentage = 0.98f;
                    heightPercentage = 0.395f;
                    Tag = "3";
                }
                addCardView(widhtPercentage, heightPercentage, Tag);
                return true;
            }
        });
    }
    private void setupHowToUseButton(){
        binding.imageViewButtonHowToUseFragMainACMainViewEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: fazer o how to use
            }
        });
    }
    private void enableEverything(){
        binding.imageViewButtonExitFragMainACMainViewEditLayout.setEnabled(true);
        binding.imageViewButtonHowToUseFragMainACMainViewEditLayout.setEnabled(true);
        binding.imageViewButtonConfirmFragMainACMainViewEditLayout.setEnabled(true);

        binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.setEnabled(true);

        binding.bottomNavigationViewFragMainACMainViewEditLayout.setEnabled(true);
    }
    private void disableEverything(){
        binding.imageViewButtonExitFragMainACMainViewEditLayout.setEnabled(false);
        binding.imageViewButtonHowToUseFragMainACMainViewEditLayout.setEnabled(false);
        binding.imageViewButtonConfirmFragMainACMainViewEditLayout.setEnabled(false);

        binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.setEnabled(false);

        binding.bottomNavigationViewFragMainACMainViewEditLayout.setEnabled(false);
    }
    //--------------------------------------------------------------------------

    //----------------------------DRAG N DROP RELATED---------------------------
    private void addCardView(float widhtPercentage, float heightPercentage, String Tag){
        int width2 = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getWidth() * 0.5f);
        int height2 = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getHeight() * 0.2f);
        Point positionDesocupied = new Point();
        Boolean spaceDesocupied = false;

        switch (Tag){
            case "1":
                //row 1 : 0 - 9
                for (int i = 0; i < 9; i++) {
                    Point point = predefinedPositions.get(i);
                    if(!draggableCardView.isPositionOccupied(point)){
                        if(usedPositions.size() > 0){
                            if(!extraOcupationTest(point, "1")){
                                //desocupado
                                spaceDesocupied = true;
                                positionDesocupied = predefinedPositions.get(i);;
                                usedPositions.add(point);
                                break;
                            }
                        } else {
                            //desocupado
                            spaceDesocupied = true;
                            positionDesocupied = point;
                            usedPositions.add(point);
                            break;
                        }
                    }
                }
                break;
            case "2":
                //row 1 : 0 - 4
                for (int i = 0; i < 4; i++) {
                    Point point = predefinedPositions.get(i);
                    if(!draggableCardView.isPositionOccupied(point)){
                        if(usedPositions.size() > 0){
                            if(!extraOcupationTest(point, "2")){
                                //desocupado
                                spaceDesocupied = true;
                                positionDesocupied = predefinedPositions.get(i);;
                                usedPositions.add(point);
                                usedPositions.add(predefinedPositions.get(i+5));
                                break;
                            }
                        } else {
                            //desocupado
                            spaceDesocupied = true;
                            positionDesocupied = point;
                            usedPositions.add(point);
                            usedPositions.add(predefinedPositions.get(i+5));
                            break;
                        }
                    }
                }
                break;
            case "3":
                //p1 : 0 e 1 | p2 : 1 e 2 | p3 : 2 e 3 | p4 : 3 e 4
                for (int i = 0; i < 4; i++) {
                    switch (i){
                        case 0:
                            Boolean extraTest = true;
                            for (int i2 = 0; i2 < 4; i2++) {
                                Point point = new Point();
                                switch (i2){
                                    case 0:
                                        point = predefinedPositions.get(0);
                                        break;
                                    case 1:
                                        point = predefinedPositions.get(1);
                                        break;
                                    case 2:
                                        point = predefinedPositions.get(5);
                                        break;
                                    case 3:
                                        point = predefinedPositions.get(6);
                                        break;
                                }

                                if(extraOcupationTest(point, "1")){
                                    extraTest = false;
                                    break;
                                }
                            }
                            if(extraTest){
                                //desocupado
                                spaceDesocupied = true;
                                positionDesocupied = predefinedPositions.get(0);

                                usedPositions.add(predefinedPositions.get(0));
                                usedPositions.add(predefinedPositions.get(1));
                                usedPositions.add(predefinedPositions.get(5));
                                usedPositions.add(predefinedPositions.get(6));
                            }
                            break;

                        case 1:
                            extraTest = true;
                            for (int i3 = 0; i3 < 4; i3++) {
                                Point point = new Point();
                                switch (i3){
                                    case 0:
                                        point = predefinedPositions.get(1);
                                        break;
                                    case 1:
                                        point = predefinedPositions.get(2);
                                        break;
                                    case 2:
                                        point = predefinedPositions.get(6);
                                        break;
                                    case 3:
                                        point = predefinedPositions.get(7);
                                        break;
                                }

                                if(extraOcupationTest(point, "1")){
                                    extraTest = false;
                                    break;
                                }
                            }
                            if(extraTest){
                                //desocupado
                                spaceDesocupied = true;
                                positionDesocupied = predefinedPositions.get(1);

                                usedPositions.add(predefinedPositions.get(1));
                                usedPositions.add(predefinedPositions.get(2));
                                usedPositions.add(predefinedPositions.get(6));
                                usedPositions.add(predefinedPositions.get(7));
                            }
                            break;

                        case 2:
                            extraTest = true;
                            for (int i4 = 0; i4 < 4; i4++) {
                                Point point = new Point();
                                switch (i4){
                                    case 0:
                                        point = predefinedPositions.get(2);
                                        break;
                                    case 1:
                                        point = predefinedPositions.get(3);
                                        break;
                                    case 2:
                                        point = predefinedPositions.get(7);
                                        break;
                                    case 3:
                                        point = predefinedPositions.get(8);
                                        break;
                                }

                                if(extraOcupationTest(point, "1")){
                                    extraTest = false;
                                    break;
                                }
                            }
                            if(extraTest){
                                //desocupado
                                spaceDesocupied = true;
                                positionDesocupied = predefinedPositions.get(2);

                                usedPositions.add(predefinedPositions.get(3));
                                usedPositions.add(predefinedPositions.get(4));
                                usedPositions.add(predefinedPositions.get(7));
                                usedPositions.add(predefinedPositions.get(8));
                            }
                            break;

                        case 3:
                            extraTest = true;
                            for (int i5 = 0; i5 < 4; i5++) {
                                Point point = new Point();
                                switch (i5){
                                    case 0:
                                        point = predefinedPositions.get(3);
                                        break;
                                    case 1:
                                        point = predefinedPositions.get(4);
                                        break;
                                    case 2:
                                        point = predefinedPositions.get(8);
                                        break;
                                    case 3:
                                        point = predefinedPositions.get(9);
                                        break;
                                }

                                if(extraOcupationTest(point, "1")){
                                    extraTest = false;
                                    break;
                                }
                            }
                            if(extraTest){
                                //desocupado
                                spaceDesocupied = true;
                                positionDesocupied = predefinedPositions.get(3);

                                usedPositions.add(predefinedPositions.get(3));
                                usedPositions.add(predefinedPositions.get(4));
                                usedPositions.add(predefinedPositions.get(8));
                                usedPositions.add(predefinedPositions.get(9));
                            }
                            break;
                    }

                    if(spaceDesocupied)
                        break;
                }
                break;
        }

        if(spaceDesocupied){
            objectsIDs++;
            draggableCardView = new DraggableCardView(getContext());

            int width = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getWidth() * widhtPercentage);
            int height = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getHeight() * heightPercentage);

            CardView cardView = new CardView(getContext());
            cardView.setLayoutParams(new ViewGroup.LayoutParams(width, height));

            float cornerRadius = 35;
            cardView.setRadius(cornerRadius);

            int colorSecondary = MaterialColors.getColor(getView(), com.google.android.material.R.attr.colorSecondary);
            cardView.setCardBackgroundColor(colorSecondary);

            draggableCardView.addView(cardView);
            draggableCardView.setTag(Tag);
            draggableCardView.setPredefinedPositions(width2, height2);
            draggableCardView.setID(objectsIDs);
            draggableCardViews.add(draggableCardView);
            draggableCardView.setDraggableCardViews(draggableCardViews);
            draggableCardView.setOnCardViewDragListener(this);
            draggableCardView.setLastPosition(positionDesocupied);
            draggableCardView.setInitialPosition(positionDesocupied.x, positionDesocupied.y);

            binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.addView(draggableCardView);

            //Refresh Card View
            MotionEvent downEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, positionDesocupied.x, positionDesocupied.y, 0);
            draggableCardView.dispatchTouchEvent(downEvent);

            MotionEvent upEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, positionDesocupied.x, positionDesocupied.y, 0);
            draggableCardView.dispatchTouchEvent(upEvent);

            addToTheList(positionDesocupied, Tag, objectsIDs, "1");
        }else{
            Toast.makeText(getContext(), getString(R.string.mainAc_FragMainViewEditLayout_Toast_noSpace_Text), Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean extraOcupationTest(Point point, String tag){
        Boolean ocupied = false;

        switch (tag){
            case "1":
                for (int i = 0; i < usedPositions.size(); i++) {
                    if(usedPositions.get(i).equals(point)){
                        ocupied = true;
                        break;
                    }
                }
                break;
            case "2":
                Boolean aux1 = false;
                Boolean aux2 = false;
                int positionOnPredefinedList = 0;

                for (int j = 0; j < predefinedPositions.size(); j++) {
                    if(predefinedPositions.get(j).equals(point)){
                        positionOnPredefinedList = j;
                        break;
                    }
                }

                for (int i = 0; i < usedPositions.size(); i++) {
                    if(usedPositions.get(i).equals(point)){
                        aux1 = true;
                        break;
                    }
                }

                for (int i = 0; i < usedPositions.size(); i++) {
                    if(usedPositions.get(i).equals(predefinedPositions.get(positionOnPredefinedList + 5))){
                        aux2 = true;
                        break;
                    }
                }

                if(aux1 || aux2){
                    ocupied = true;
                }
                break;
        }

        return ocupied;
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
    @Override
    public void onCardViewDrop(DraggableCardView cardView, Point currentPosition, Point lastPosition) {
        Object tag = cardView.getTag();
        if (tag.equals("1")) {
            if(!currentPosition.equals(lastPosition)){
                for (int i = 0; i < usedPositions.size(); i++) {
                    if(usedPositions.get(i).equals(lastPosition)){
                        for (int j = 0; j < usedPositions.size(); j++) {
                            if(usedPositions.get(j).equals(lastPosition)){
                                usedPositions.remove(j);
                            }
                        }
                        usedPositions.add(i, currentPosition);
                        draggableCardView.setLastPosition(currentPosition);
                        break;
                    }
                }
            }
        } else if (tag.equals("2")) {
            if(!currentPosition.equals(lastPosition)){
                for (int i = 0; i < usedPositions.size(); i++) {
                    if(usedPositions.get(i).equals(lastPosition)){
                        int auxPredefined = 0;
                        for (int j = 0; j < predefinedPositions.size(); j++) {
                            if(predefinedPositions.get(j).equals(lastPosition)){
                                auxPredefined = j + 5;
                                break;
                            }
                        }

                        for (int j = 0; j < usedPositions.size(); j++) {
                            if(usedPositions.get(j).equals(lastPosition)){
                                usedPositions.remove(lastPosition);
                                usedPositions.remove(predefinedPositions.get(auxPredefined));
                                break;
                            }
                        }

                        for (int j = 0; j < predefinedPositions.size(); j++) {
                            if(predefinedPositions.get(j).equals(currentPosition)){
                                auxPredefined = j + 5;
                                break;
                            }
                        }

                        usedPositions.add(i, currentPosition);
                        usedPositions.add(i +1, predefinedPositions.get(auxPredefined));
                        draggableCardView.setLastPosition(currentPosition);
                        break;
                    }
                }
            }
        } else if (tag.equals("3")) {
            if(!currentPosition.equals(lastPosition)){
                for (int i = 0; i < usedPositions.size(); i++) {
                    if(usedPositions.get(i).equals(lastPosition)){
                        int auxPredefined = 0;
                        for (int j = 0; j < predefinedPositions.size(); j++) {
                            if(predefinedPositions.get(j).equals(lastPosition)){
                                auxPredefined = j + 5;
                                break;
                            }
                        }

                        for (int j = 0; j < usedPositions.size(); j++) {
                            if(usedPositions.get(j).equals(lastPosition)){
                                usedPositions.remove(predefinedPositions.get(auxPredefined-5));
                                usedPositions.remove(predefinedPositions.get(auxPredefined-4));
                                usedPositions.remove(predefinedPositions.get(auxPredefined));
                                usedPositions.remove(predefinedPositions.get(auxPredefined+1));
                                break;
                            }
                        }

                        for (int j = 0; j < predefinedPositions.size(); j++) {
                            if(predefinedPositions.get(j).equals(currentPosition)){
                                auxPredefined = j + 5;
                                break;
                            }
                        }

                        usedPositions.add(predefinedPositions.get(auxPredefined-5));
                        usedPositions.add(predefinedPositions.get(auxPredefined-4));
                        usedPositions.add(predefinedPositions.get(auxPredefined));
                        usedPositions.add(predefinedPositions.get(auxPredefined+1));
                        draggableCardView.setLastPosition(currentPosition);
                        break;
                    }
                }
            }
        }

        //TODO: adicionar opçao do style
        updateFromTheList(currentPosition, cardView.getID(), "1");
    }
    //--------------------------------------------------------------------------
    private void addToTheList(Point point, String type, int ID, String style){
        for (int i = 0; i < predefinedPositions.size(); i++) {
            if(predefinedPositions.get(i).equals(point)){
                DraggableCardViewEntity object = new DraggableCardViewEntity(i, type, getString(R.string.mainAc_FragMainView_Example_ChartName_Text), style);
                object.setInfos(25, 25, 25, 25,
                        getString(R.string.mainAc_FragMainView_Example_Percentage1_Text), getString(R.string.mainAc_FragMainView_Example_Percentage2_Text),
                        getString(R.string.mainAc_FragMainView_Example_Percentage3_Text), getString(R.string.mainAc_FragMainView_Example_Percentage4_Text),
                        getResources().getColor(R.color.highlightedTextDark), getResources().getColor(R.color.textDark),
                        getResources().getColor(R.color.highlightedTextLight), getResources().getColor(R.color.textLight));
                object.setId(ID);
                draggableCardViewObjectList.add(object);
                break;
            }
        }
    }
    private void updateFromTheList(Point point, int ID, String style){
        for (int i = 0; i < draggableCardViewObjectList.size(); i++) {
            if(draggableCardViewObjectList.get(i).getId() == ID){
                DraggableCardViewEntity object = draggableCardViewObjectList.get(i);
                for (int j = 0; j < predefinedPositions.size(); j++) {
                    if(predefinedPositions.get(j).equals(point)){
                        draggableCardViewObjectList.remove(object);
                        object.setPosition(j);
                        object.setStyle(style);
                        draggableCardViewObjectList.add(object);
                        Log.i("Mudou", "Mudou : "+j);
                        break;
                    }
                }
                break;
            }
        }
    }

    //----------------------------LOAD OLD SCHEME---------------------------
    private void loadUsedPoint(){
        if(draggableCardViewObjectList.size() > 0){
            for (int i = 0; i < draggableCardViewObjectList.size(); i++) {
                DraggableCardViewEntity object = draggableCardViewObjectList.get(i);

                switch (object.getType()){
                    case "3":
                        switch (object.getPosition()){
                            case 0:
                                usedPositions.add(predefinedPositions.get(0));
                                usedPositions.add(predefinedPositions.get(1));
                                usedPositions.add(predefinedPositions.get(5));
                                usedPositions.add(predefinedPositions.get(6));
                                break;
                            case 1:
                                usedPositions.add(predefinedPositions.get(1));
                                usedPositions.add(predefinedPositions.get(2));
                                usedPositions.add(predefinedPositions.get(6));
                                usedPositions.add(predefinedPositions.get(7));
                                break;
                            case 2:
                                usedPositions.add(predefinedPositions.get(2));
                                usedPositions.add(predefinedPositions.get(3));
                                usedPositions.add(predefinedPositions.get(7));
                                usedPositions.add(predefinedPositions.get(8));
                                break;
                            case 3:
                                usedPositions.add(predefinedPositions.get(3));
                                usedPositions.add(predefinedPositions.get(4));
                                usedPositions.add(predefinedPositions.get(8));
                                usedPositions.add(predefinedPositions.get(9));
                                break;
                        }
                        break;
                    case "2":
                        switch (object.getPosition()){
                            case 0:
                                usedPositions.add(predefinedPositions.get(0));
                                usedPositions.add(predefinedPositions.get(5));
                                break;
                            case 1:
                                usedPositions.add(predefinedPositions.get(1));
                                usedPositions.add(predefinedPositions.get(6));
                                break;
                            case 2:
                                usedPositions.add(predefinedPositions.get(2));
                                usedPositions.add(predefinedPositions.get(7));
                                break;
                            case 3:
                                usedPositions.add(predefinedPositions.get(3));
                                usedPositions.add(predefinedPositions.get(8));
                                break;
                            case 4:
                                usedPositions.add(predefinedPositions.get(4));
                                usedPositions.add(predefinedPositions.get(9));
                                break;
                        }
                        break;
                    case "1":
                        usedPositions.add(predefinedPositions.get(object.getPosition()));
                        break;
                }
            }
        }
    }
    private void loadObjectsFromList(){
        if(draggableCardViewObjectList.size() > 0){
            int width2 = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getWidth() * 0.5f);
            int height2 = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getHeight() * 0.2f);
            for (int i = 0; i < draggableCardViewObjectList.size(); i++) {
                objectsIDs++;
                DraggableCardViewEntity object = draggableCardViewObjectList.get(i);

                float percentageWidth = 0.0f;
                float percentageHeiht = 0.0f;

                switch (object.getType()){
                    case "3":
                        percentageWidth = 0.98f;
                        percentageHeiht = 0.395f;
                        break;
                    case "2":
                        percentageWidth = 0.98f;
                        percentageHeiht = 0.195f;
                        break;
                    case "1":
                        percentageWidth = 0.495f;
                        percentageHeiht = 0.195f;
                        break;
                }

                Point position = predefinedPositions.get(object.getPosition());

                draggableCardView = new DraggableCardView(getContext());

                int width = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getWidth() * percentageWidth);
                int height = (int) (binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.getHeight() * percentageHeiht);

                CardView cardView = new CardView(getContext());
                cardView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
                float cornerRadius = 35;
                cardView.setRadius(cornerRadius);
                cardView.setCardBackgroundColor(mainCardViewColor);

                draggableCardView.setTag(object.getType());
                draggableCardView.setPredefinedPositions(width2, height2);
                draggableCardView.setID(object.getId());
                draggableCardView.setDraggableCardViews(draggableCardViews);
                draggableCardView.setOnCardViewDragListener(this);
                draggableCardView.setLastPosition(position);
                draggableCardView.setInitialPosition(position.x, position.y);

                draggableCardView.addView(cardView);
                draggableCardViews.add(draggableCardView);
                binding.framelayoutGridDragNDropFragMainACMainViewEditLayout.addView(draggableCardView);

                MotionEvent downEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, position.x, position.y, 0);
                draggableCardView.dispatchTouchEvent(downEvent);

                MotionEvent upEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, position.x, position.y, 0);
                draggableCardView.dispatchTouchEvent(upEvent);
            }
        }
    }
    //--------------------------------------------------------------------------

    //----------------------------DATABASE FUNCTIONS---------------------------
    private class LocalDatabaseInsetTask extends AsyncTask<Void, Void, List<DraggableCardViewEntity>> {
        @Override
        protected List<DraggableCardViewEntity> doInBackground(Void... voids) {
            draggableCardViewDao.insertList(draggableCardViewObjectList);

            return draggableCardViewObjectList;
        }

        @Override
        protected void onPostExecute(List<DraggableCardViewEntity> list) {
            onExitClickListenner.OnFragMainACMainViewEditLayoutExitClick(true, list);
        }
    }
    private class LocalDatabaseDeleteAllTask extends AsyncTask<Void, Void, List<DraggableCardViewEntity>> {
        @Override
        protected List<DraggableCardViewEntity> doInBackground(Void... voids) {
            draggableCardViewDao.clearAllEntries();
            return draggableCardViewObjectList;
        }

        @Override
        protected void onPostExecute(List<DraggableCardViewEntity> Classes) {

        }
    }
    //--------------------------------------------------------------------------
}