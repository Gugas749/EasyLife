package com.example.easylife.fragments.mainactivityfragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.easylife.database.DraggableCardViewEntity;
import com.example.easylife.databinding.FragmentMainACMainViewBinding;
import com.example.easylife.fragments.mainviewpiecharts.BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment;
import com.example.easylife.fragments.mainviewpiecharts.RectangleWithPieChartFragment;
import com.example.easylife.fragments.mainviewpiecharts.RectangleWithPieChartInTheLeftAndTextInTheRightFragment;
import com.example.easylife.fragments.mainviewpiecharts.RectangleWithPieChartInTheRightAndTextInTheLeftFragment;
import com.example.easylife.scripts.mainvieweditlayout_things.DraggableCardViewObject;

import java.util.ArrayList;
import java.util.List;

public class MainACMainViewFragment extends Fragment {

    private FragmentMainACMainViewBinding binding;
    private List<DraggableCardViewEntity> draggableCardViewObjectsList;

    public MainACMainViewFragment(List<DraggableCardViewEntity> draggableCardViewObjectsList) {
        this.draggableCardViewObjectsList = draggableCardViewObjectsList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewBinding.inflate(inflater);

        processData();
        //setFragmentNormal();

        return binding.getRoot();
    }
    private void processData(){
        List<String> linesUsed = new ArrayList<>();
        linesUsed.add("5");
        for (int i = 0; i < draggableCardViewObjectsList.size(); i++) {
            DraggableCardViewEntity selectedObject = draggableCardViewObjectsList.get(i);

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
                                    selectedObject.getChartName(),
                                    selectedObject.getValue1Percentage(), selectedObject.getValue2Percentage(),
                                    selectedObject.getValue3Percentage(), selectedObject.getValue4Percentage(),
                                    selectedObject.getValue1Text(), selectedObject.getValue2Text(),
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text());
                            fragment = fragStyle1;
                            break;
                        case "2":
                            BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment fragStyle2 = new BigRectangleWithPieChartInTheLeftAndTextInTheRightFragment();
                            fragStyle2.setInfos(selectedObject.getValue1Color(), selectedObject.getValue2Color(),
                                    selectedObject.getValue3Color(), selectedObject.getValue4Color(),
                                    selectedObject.getChartName(),
                                    selectedObject.getValue1Percentage(), selectedObject.getValue2Percentage(),
                                    selectedObject.getValue3Percentage(), selectedObject.getValue4Percentage(),
                                    selectedObject.getValue1Text(), selectedObject.getValue2Text(),
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text());
                            fragment = fragStyle2;
                            break;
                    }

                    combineFrameLayouts(frameLayout1, frameLayout2, fragment);
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
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text());
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
                                    selectedObject.getValue3Text(), selectedObject.getValue4Text());
                            fragment = fragStyle2;
                            break;
                    }

                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(frameLayout1.getId(), fragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case "1":
                    frameLayout1 = null;
                    fragment = new Fragment();
                    Fragment brotherFragment = new Fragment();
                    DraggableCardViewEntity brother = null;

                    for (int j = 0; j < linesUsed.size(); j++) {
                        if(!linesUsed.get(j).equals(String.valueOf(selectedObject.getPosition()))){
                            int possibleBrother = 0;

                            switch (selectedObject.getPosition()){
                                case 0:
                                    frameLayout1 = binding.frameLayoutLine1FragMainACMainView;
                                    possibleBrother = 5;
                                    linesUsed.add("0");
                                    break;
                                case 1:
                                    frameLayout1 = binding.frameLayoutLine2FragMainACMainView;
                                    possibleBrother = 6;
                                    linesUsed.add("1");
                                    break;
                                case 2:
                                    frameLayout1 = binding.frameLayoutLine3FragMainACMainView;
                                    possibleBrother = 7;
                                    linesUsed.add("2");
                                    break;
                                case 3:
                                    frameLayout1 = binding.frameLayoutLine4FragMainACMainView;
                                    possibleBrother = 8;
                                    linesUsed.add("3");
                                    break;
                                case 4:
                                    frameLayout1 = binding.frameLayoutLine5FragMainACMainView;
                                    possibleBrother = 9;
                                    linesUsed.add("4");
                                    break;
                            }

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
                            fragment = frag1;

                            if(brother != null){
                                RectangleWithPieChartFragment frag2 = new RectangleWithPieChartFragment();
                                frag2.setInfos(brother.getValue1Color(), brother.getValue2Color(),
                                        brother.getValue3Color(), brother.getValue4Color(),
                                        brother.getChartName(),
                                        brother.getValue1Percentage(), brother.getValue2Percentage(),
                                        brother.getValue3Percentage(), brother.getValue4Percentage());
                                brotherFragment = frag2;
                            }

                            //divideFrameLayout(frameLayout1, fragment, brotherFragment);
                        }
                    }
                    break;
            }
        }
    }
    public void updateData(List<DraggableCardViewEntity> draggableCardViewObjectsList){
        this.draggableCardViewObjectsList = draggableCardViewObjectsList;
        processData();
    }

    //------------------------------FRAME LAYOUT RELATED-------------------------
    private void setFragmentNormal(FrameLayout frameLayout, Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .addToBackStack(null)
                .commit();
    }
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

        getChildFragmentManager()
                .beginTransaction()
                .replace(child1.getId(), frag1)
                .addToBackStack(null)
                .commit();

        if(child2 != null){
            getChildFragmentManager()
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
    //---------------------------------------------------------------------------
}