package com.example.easylife.fragments.mainactivityfragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.easylife.databinding.FragmentMainACMainViewEditLayoutBinding;
import com.example.easylife.fragments.mainviewpiecharts.RectangleWithPieChartFragment;
import com.example.easylife.scripts.DraggableCardView;

public class MainACMainViewEditLayoutFragment extends Fragment {
    private FragmentMainACMainViewEditLayoutBinding binding;
    private OnFragMainACMainViewEditLayoutExitClick onExitClickListenner;
    public interface OnFragMainACMainViewEditLayoutExitClick {
        void OnFragMainACMainViewEditLayoutExitClick();
    }

    public MainACMainViewEditLayoutFragment(OnFragMainACMainViewEditLayoutExitClick onExitClickListenner) {
        this.onExitClickListenner = onExitClickListenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACMainViewEditLayoutBinding.inflate(inflater);

        setupConfirmButton();
        setupExitButton();

        // Create DraggableCardView
        DraggableCardView draggableCardView = new DraggableCardView(getContext());
        draggableCardView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        draggableCardView.setBackgroundColor(Color.RED);
        draggableCardView.setPadding(200,200,200,200);
        binding.frameTestttttttttt.addView(draggableCardView);

        return binding.getRoot();
    }



    private void setupExitButton(){
        binding.imageViewButtonExitFragMainACMainViewEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExitClickListenner.OnFragMainACMainViewEditLayoutExitClick();
            }
        });
    }
    private void setupConfirmButton(){
        binding.imageViewButtonConfirmFragMainACMainViewEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}