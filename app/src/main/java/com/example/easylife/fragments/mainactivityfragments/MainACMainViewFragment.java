package com.example.easylife.fragments.mainactivityfragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.R;
import com.example.easylife.databinding.FragmentMainACMainViewBinding;
import com.example.easylife.fragments.mainviewpiecharts.RectangleWithPieChartInTheLeftAndTextInTheRightFragment;
import com.example.easylife.fragments.mainviewpiecharts.RectangleWithPieChartInTheRightAndTextInTheLeftFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainACMainViewFragment extends Fragment {

    private FragmentMainACMainViewBinding binding;

    public MainACMainViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainACMainViewBinding.inflate(inflater);

        RectangleWithPieChartInTheLeftAndTextInTheRightFragment frag = new RectangleWithPieChartInTheLeftAndTextInTheRightFragment(Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, "TEST", 13.5f, 52.5f, 34f, 33f);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_fragmentContainer_FragMainACMainView, frag)
                .addToBackStack(null)
                .commit();

        RectangleWithPieChartInTheRightAndTextInTheLeftFragment frag2 = new RectangleWithPieChartInTheRightAndTextInTheLeftFragment(Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, "TEST", 13.5f, 52.5f, 34f, 33f);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_fragmentContainer_FragMainACMainView, frag2)
                .addToBackStack(null)
                .commit();

        return binding.getRoot();
    }
}