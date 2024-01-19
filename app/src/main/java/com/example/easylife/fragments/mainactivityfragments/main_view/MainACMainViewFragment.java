package com.example.easylife.fragments.mainactivityfragments.main_view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.easylife.activitys.MainActivity;
import com.example.easylife.database.entities.DraggableCardViewEntity;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.databinding.FragmentMainACMainViewBinding;
import com.example.easylife.fragments.alertDialogFragments.AlertDialogLongPressMainViewObjectsFragment;
import com.example.easylife.fragments.mainactivityfragments.main_view.mainviewpiecharts.BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment;
import com.example.easylife.fragments.mainactivityfragments.main_view.mainviewpiecharts.RectangleWithPieChartFragment;
import com.example.easylife.fragments.mainactivityfragments.main_view.mainviewpiecharts.RectangleWithPieChartInTheLeftAndTextInTheRightFragment;
import com.example.easylife.fragments.mainactivityfragments.main_view.mainviewpiecharts.RectangleWithPieChartInTheRightAndTextInTheLeftFragment;
import com.example.easylife.scripts.CustomAlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainACMainViewFragment extends Fragment implements CustomAlertDialogFragment.ConfirmButtonClickAlertDialogLongPressMainViewObjects
        , BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment.LongPressFragBigRectangleWithPieChartInTheLeftAndTextInTheRight
        , RectangleWithPieChartFragment.LongPressFragRectangleWithPieChart
        , RectangleWithPieChartInTheLeftAndTextInTheRightFragment.LongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight
        , RectangleWithPieChartInTheRightAndTextInTheLeftFragment.LongPressFragRectangleWithPieChartInTheRightAndTextInTheLeft{

    private FragmentMainACMainViewBinding binding;
    private List<DraggableCardViewEntity> draggableCardViewObjectsList;
    private MainACMainViewFragment THIS;
    private MainActivity parent;
    private boolean parentALlDisbale = false;
    public void setParentALlDisbale(boolean parentALlDisbale){
        this.parentALlDisbale = parentALlDisbale;
    }
    private List<SpendingAccountsEntity> spendingAccountsEntityList;
    private ConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC confirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC;
    public interface ConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC {
        void onConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC(DraggableCardViewEntity object, boolean canHoldMainAccount, int selectedSubAccountIndex);
    }
    public void setConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainACListenner(ConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC confirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC){
        this.confirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC = confirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC;
    }
    public MainACMainViewFragment(List<DraggableCardViewEntity> draggableCardViewObjectsList) {
        this.draggableCardViewObjectsList = draggableCardViewObjectsList;
    }

    public void setAccountsList(List<SpendingAccountsEntity> spendingAccountsEntityList){
        this.spendingAccountsEntityList = spendingAccountsEntityList;
    }

    public void setDraggableCardViewObjectsList(List<DraggableCardViewEntity> draggableCardViewObjectsList){
        this.draggableCardViewObjectsList = draggableCardViewObjectsList;
    }

    public void setParent(MainActivity parent){
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewBinding.inflate(inflater);

        THIS = this;
        processData();

        return binding.getRoot();
    }
    private void processData(){
        List<String> positionsUsed = new ArrayList<>();
        for (int i = 0; i < draggableCardViewObjectsList.size(); i++) {
            DraggableCardViewEntity selectedObject = draggableCardViewObjectsList.get(i);
            String parentTag = "FragMainACMainView";
            switch (selectedObject.getType()){
                case "3":
                    FrameLayout frameLayout1 = null, frameLayout2 = null;
                    Fragment fragment = new Fragment();
                    switch (selectedObject.getPosition()){
                        case 0:
                            frameLayout1 = binding.frameLayoutLine1FragMainACMainView;
                            frameLayout2 = binding.frameLayoutLine2FragMainACMainView;
                            break;
                        case 1:
                            frameLayout1 = binding.frameLayoutLine2FragMainACMainView;
                            frameLayout2 = binding.frameLayoutLine3FragMainACMainView;
                            break;
                        case 2:
                            frameLayout1 = binding.frameLayoutLine3FragMainACMainView;
                            frameLayout2 = binding.frameLayoutLine4FragMainACMainView;
                            break;
                        case 3:
                            frameLayout1 = binding.frameLayoutLine4FragMainACMainView;
                            frameLayout2 = binding.frameLayoutLine5FragMainACMainView;
                            break;
                    }

                    switch (selectedObject.getStyle()){
                        case "1":
                            BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment fragStyle1 = new BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment();
                            fragStyle1.setInfos(selectedObject.getValue1Color(), selectedObject.getValue2Color(),
                                    selectedObject.getValue3Color(), selectedObject.getValue4Color(),
                                    selectedObject.getValue5Color(), selectedObject.getValue6Color(),
                                    selectedObject.getValue7Color(), selectedObject.getValue8Color(),
                                    selectedObject.getChartName(),
                                    selectedObject.getValue1Percentage(), selectedObject.getValue2Percentage(),
                                    selectedObject.getValue3Percentage(), selectedObject.getValue4Percentage(),
                                    selectedObject.getValue5Percentage(), selectedObject.getValue6Percentage(),
                                    selectedObject.getValue7Percentage(), selectedObject.getValue8Percentage(),
                                    selectedObject.getValue1Text(), selectedObject.getValue2Text(),
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text(),
                                    selectedObject.getValue5Text(), selectedObject.getValue6Text(),
                                    selectedObject.getValue7Text(), selectedObject.getValue8Text());
                            fragStyle1.setListenner(THIS);
                            fragStyle1.setObject(selectedObject);
                            fragStyle1.setAccountsList(spendingAccountsEntityList);
                            fragment = fragStyle1;
                            break;
                        case "2":
                            BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment fragStyle2 = new BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment();
                            fragStyle2.setInfos(selectedObject.getValue1Color(), selectedObject.getValue2Color(),
                                    selectedObject.getValue3Color(), selectedObject.getValue4Color(),
                                    selectedObject.getValue5Color(), selectedObject.getValue6Color(),
                                    selectedObject.getValue7Color(), selectedObject.getValue8Color(),
                                    selectedObject.getChartName(),
                                    selectedObject.getValue1Percentage(), selectedObject.getValue2Percentage(),
                                    selectedObject.getValue3Percentage(), selectedObject.getValue4Percentage(),
                                    selectedObject.getValue5Percentage(), selectedObject.getValue6Percentage(),
                                    selectedObject.getValue7Percentage(), selectedObject.getValue8Percentage(),
                                    selectedObject.getValue1Text(), selectedObject.getValue2Text(),
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text(),
                                    selectedObject.getValue5Text(), selectedObject.getValue6Text(),
                                    selectedObject.getValue7Text(), selectedObject.getValue8Text());
                            fragStyle2.setListenner(THIS);
                            fragStyle2.setObject(selectedObject);
                            fragStyle2.setAccountsList(spendingAccountsEntityList);
                            fragment = fragStyle2;
                            break;
                    }

                    if(frameLayout1 != null && frameLayout2 != null){
                        combineFrameLayouts(frameLayout1, frameLayout2, fragment);
                    }
                    break;
                case "2":
                    frameLayout1 = null;
                    fragment = new Fragment();
                    switch (selectedObject.getPosition()){
                        case 0:
                            frameLayout1 = binding.frameLayoutLine1FragMainACMainView;
                            break;
                        case 1:
                            frameLayout1 = binding.frameLayoutLine2FragMainACMainView;
                            break;
                        case 2:
                            frameLayout1 = binding.frameLayoutLine3FragMainACMainView;
                            break;
                        case 3:
                            frameLayout1 = binding.frameLayoutLine4FragMainACMainView;
                            break;
                        case 4:
                            frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                            break;
                    }

                    switch (selectedObject.getStyle()){
                        case "1":
                            RectangleWithPieChartInTheLeftAndTextInTheRightFragment fragStyle1 = new RectangleWithPieChartInTheLeftAndTextInTheRightFragment();
                            fragStyle1.setInfos(selectedObject.getValue1Color(), selectedObject.getValue2Color(),
                                    selectedObject.getValue3Color(), selectedObject.getValue4Color(),
                                    selectedObject.getChartName(),
                                    selectedObject.getValue1Percentage(), selectedObject.getValue2Percentage(),
                                    selectedObject.getValue3Percentage(), selectedObject.getValue4Percentage(),
                                    selectedObject.getValue1Text(), selectedObject.getValue2Text(),
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text(), parentTag);
                            fragStyle1.setListenner(THIS);
                            fragStyle1.setObject(selectedObject);
                            fragStyle1.setAccountsList(spendingAccountsEntityList);
                            fragment = fragStyle1;
                            break;
                        case "2":
                            RectangleWithPieChartInTheRightAndTextInTheLeftFragment fragStyle2 = new RectangleWithPieChartInTheRightAndTextInTheLeftFragment();
                            fragStyle2.setInfos(selectedObject.getValue1Color(), selectedObject.getValue2Color(),
                                    selectedObject.getValue3Color(), selectedObject.getValue4Color(),
                                    selectedObject.getChartName(),
                                    selectedObject.getValue1Percentage(), selectedObject.getValue2Percentage(),
                                    selectedObject.getValue3Percentage(), selectedObject.getValue4Percentage(),
                                    selectedObject.getValue1Text(), selectedObject.getValue2Text(),
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text(), parentTag);
                            fragStyle2.setListenner(THIS);
                            fragStyle2.setObject(selectedObject);
                            fragStyle2.setAccountsList(spendingAccountsEntityList);
                            fragment = fragStyle2;
                            break;
                    }

                    if(frameLayout1 != null){
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(frameLayout1.getId(), fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                    break;
                case "1":
                    frameLayout1 = null;
                    fragment = new Fragment();
                    Fragment brotherFragment = new Fragment();
                    boolean repeated = false;
                    DraggableCardViewEntity brother = null;

                    if(positionsUsed.size() > 0){
                        for (int j = 0; j < positionsUsed.size(); j++) {
                            if(positionsUsed.get(j).equals(String.valueOf(selectedObject.getPosition()))){
                                repeated = true;
                                break;
                            }
                        }
                    }

                    if(!repeated){
                        int possibleBrother = 0;

                        switch (selectedObject.getPosition()){
                            case 0:
                                frameLayout1 = binding.frameLayoutLine1FragMainACMainView;
                                possibleBrother = 5;
                                positionsUsed.add("0");
                                break;
                            case 1:
                                frameLayout1 = binding.frameLayoutLine2FragMainACMainView;
                                possibleBrother = 6;
                                positionsUsed.add("1");
                                break;
                            case 2:
                                frameLayout1 = binding.frameLayoutLine3FragMainACMainView;
                                possibleBrother = 7;
                                positionsUsed.add("2");
                                break;
                            case 3:
                                frameLayout1 = binding.frameLayoutLine4FragMainACMainView;
                                possibleBrother = 8;
                                positionsUsed.add("3");
                                break;
                            case 4:
                                frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                                possibleBrother = 9;
                                positionsUsed.add("4");
                                break;
                            case 5:
                                frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                                possibleBrother = 0;
                                positionsUsed.add("5");
                                break;
                            case 6:
                                frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                                possibleBrother = 1;
                                positionsUsed.add("6");
                                break;
                            case 7:
                                frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                                possibleBrother = 2;
                                positionsUsed.add("7");
                                break;
                            case 8:
                                frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                                possibleBrother = 3;
                                positionsUsed.add("8");
                                break;
                            case 9:
                                frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                                possibleBrother = 4;
                                positionsUsed.add("9");
                                break;
                        }

                        positionsUsed.add(String.valueOf(possibleBrother));

                        for (int k = 0; k < draggableCardViewObjectsList.size(); k++) {
                            if(draggableCardViewObjectsList.get(k).getPosition() == possibleBrother){
                                brother = draggableCardViewObjectsList.get(k);
                                break;
                            }
                        }

                        RectangleWithPieChartFragment frag1 = new RectangleWithPieChartFragment();
                        frag1.setInfos(selectedObject.getValue1Color(), selectedObject.getValue2Color(),
                                selectedObject.getValue3Color(), selectedObject.getValue4Color(),
                                selectedObject.getChartName(),
                                selectedObject.getValue1Percentage(), selectedObject.getValue2Percentage(),
                                selectedObject.getValue3Percentage(), selectedObject.getValue4Percentage());
                        frag1.setListenner(THIS);
                        frag1.setObject(selectedObject);
                        frag1.setAccountsList(spendingAccountsEntityList);
                        fragment = frag1;

                        if(brother != null){
                            RectangleWithPieChartFragment frag2 = new RectangleWithPieChartFragment();
                            frag2.setInfos(brother.getValue1Color(), brother.getValue2Color(),
                                    brother.getValue3Color(), brother.getValue4Color(),
                                    brother.getChartName(),
                                    brother.getValue1Percentage(), brother.getValue2Percentage(),
                                    brother.getValue3Percentage(), brother.getValue4Percentage());
                            frag2.setListenner(THIS);
                            frag2.setObject(brother);
                            frag2.setAccountsList(spendingAccountsEntityList);
                            brotherFragment = frag2;
                        }

                        divideFrameLayout(frameLayout1, fragment, brotherFragment);
                    }
                    break;
            }
        }
    }
    public void updateData(List<DraggableCardViewEntity> draggableCardViewObjectsList){
        this.draggableCardViewObjectsList = draggableCardViewObjectsList;
        processData();
    }
    private void onObjectLongPress(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList){
        if(!parentALlDisbale){
            CustomAlertDialogFragment customAlertDialogFragment = new CustomAlertDialogFragment();
            customAlertDialogFragment.setConfirmButtonClickAlertDialogLongPressMainViewObjects(THIS);
            AlertDialogLongPressMainViewObjectsFragment fragment = new AlertDialogLongPressMainViewObjectsFragment(object, spendingAccountsEntityList);
            customAlertDialogFragment.setCustomFragment(fragment);
            customAlertDialogFragment.setTag("FragMainACMainView");
            fragment.setListenners(customAlertDialogFragment, customAlertDialogFragment);
            customAlertDialogFragment.show(getParentFragmentManager(), "CustomAlertDialogFragment");
        }
    }

    //------------------------------FRAME LAYOUT RELATED-------------------------
    private void divideFrameLayout(FrameLayout parentFrameLayout, Fragment frag1, Fragment frag2) {
        //Cria um linear layout que e o adult que vai ser inserido no parent frame layout esse linear layout vai ser reponsavel por dividir esse frame layout em 50/50
        LinearLayout adult1 = new LinearLayout(parentFrameLayout.getContext());
        adult1.setId(View.generateViewId());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.START;
        adult1.setLayoutParams(params);
        adult1.setOrientation(LinearLayout.HORIZONTAL);
        parentFrameLayout.addView(adult1);

        // Adiciona o primerio novo frame layout
        FrameLayout child1 = new FrameLayout(parentFrameLayout.getContext());
        child1.setId(View.generateViewId());
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params1.gravity = Gravity.START;
        child1.setLayoutParams(params1);
        adult1.addView(child1);

        // Adiciona o segundo novo frame layout
        FrameLayout child2 = new FrameLayout(parentFrameLayout.getContext());
        child2.setId(View.generateViewId());
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params2.gravity = Gravity.END;
        child2.setLayoutParams(params2);
        adult1.addView(child2);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(child1.getId(), frag1)
                .addToBackStack(null)
                .commit();

        if(frag2 != null){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(child2.getId(), frag2)
                    .addToBackStack(null)
                    .commit();
        }
    }
    private void combineFrameLayouts(FrameLayout frameLayout1, FrameLayout frameLayout2, Fragment fragment) {
        // Define o novo peso para frameLayout1 (40%)
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                2f
        );
        frameLayout1.setLayoutParams(layoutParams1);

        // Define o novo peso para frameLayout2 (1%)
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                0.01f
        );
        frameLayout2.setLayoutParams(layoutParams2);

        getChildFragmentManager()
                .beginTransaction()
                .replace(frameLayout1.getId(), fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLongPressFragBigRectangleWithPieChartInTheLeftAndTextInTheRight(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList) {
        onObjectLongPress(object, spendingAccountsEntityList);
    }
    @Override
    public void onLongPressFragRectangleWithPieChart(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList) {
        onObjectLongPress(object, spendingAccountsEntityList);
    }
    @Override
    public void onLongPressFragRectangleWithPieChartInTheLeftAndTextInTheRight(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList, String parentTag) {
        if(parentTag.equals("FragMainACMainView")){
            onObjectLongPress(object, spendingAccountsEntityList);
        }
    }
    @Override
    public void onLongPressFragRectangleWithPieChartInTheRightAndTextInTheLeft(DraggableCardViewEntity object, List<SpendingAccountsEntity> spendingAccountsEntityList, String parentTag) {
        if(parentTag.equals("FragMainACMainView")){
            onObjectLongPress(object, spendingAccountsEntityList);
        }
    }
    @Override
    public void onConfirmButtonClickAlertDialogLongPressMainViewObjects(DraggableCardViewEntity object, boolean canHoldMainAccount, int selectedSubAccountIndex) {
        confirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC.onConfirmButtonClickAlertDialogLongPressMainViewObjectsToMainAC(object, canHoldMainAccount, selectedSubAccountIndex);
    }
    //---------------------------------------------------------------------------
}