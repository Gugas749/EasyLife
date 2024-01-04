package com.example.easylife.fragments.mainactivityfragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.view.Gravity;
import android.widget.LinearLayout;
import com.example.easylife.databinding.FragmentMainACMainViewBinding;

import java.util.List;

public class MainACMainViewFragment extends Fragment {

    private FragmentMainACMainViewBinding binding;
    private Boolean linesSplitted, linesTogether;
    private int quantLinesSplitted, quantLinesTogether;
    private int[] idsLinesSplitted, getIdsLinesTogether;
    private List<Fragment> listFragmentLinesSplitted, listFragmentLinesTogether;

    public MainACMainViewFragment(Boolean linesSplitted, Boolean linesTogether,
                                  int quantLinesSplitted, int quantLinesTogether,
                                  int[] idsLinesSplitted, int[] getIdsLinesTogether,
                                  List<Fragment> listFragmentLinesSplitted, List<Fragment> listFragmentLinesTogether) {
        this.linesSplitted = linesSplitted;
        this.linesTogether = linesTogether;
        this.quantLinesSplitted = quantLinesSplitted;
        this.quantLinesTogether = quantLinesTogether;
        this.idsLinesSplitted = idsLinesSplitted;
        this.getIdsLinesTogether = getIdsLinesTogether;
        this.listFragmentLinesSplitted = listFragmentLinesSplitted;
        this.listFragmentLinesTogether = listFragmentLinesTogether;
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
        if(linesTogether){
            for(int i = 0; i < quantLinesTogether; i++){
                FrameLayout frameLayout1 = null, frameLayout2 = null;

                switch (getIdsLinesTogether[i]){
                    case 12:
                        frameLayout1 = binding.frameLayoutLine1FragMainACMainView;
                        frameLayout2 = binding.frameLayoutLine2FragMainACMainView;
                        break;
                    case 23:
                        frameLayout1 = binding.frameLayoutLine2FragMainACMainView;
                        frameLayout2 = binding.frameLayoutLine3FragMainACMainView;
                        break;
                    case 34:
                        frameLayout1 = binding.frameLayoutLine3FragMainACMainView;
                        frameLayout2 = binding.frameLayoutLine4FragMainACMainView;
                        break;
                    case 45:
                        frameLayout1 = binding.frameLayoutLine4FragMainACMainView;
                        frameLayout2 = binding.frameLayoutLine5FragMainACMainView;
                        break;
                }

                combineFrameLayouts(frameLayout1, frameLayout2, listFragmentLinesTogether.get(i));
            }
        }
        if(linesSplitted){
            //divideFrameLayout();
        }
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

        getChildFragmentManager()
                .beginTransaction()
                .replace(child2.getId(), frag2)
                .addToBackStack(null)
                .commit();
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