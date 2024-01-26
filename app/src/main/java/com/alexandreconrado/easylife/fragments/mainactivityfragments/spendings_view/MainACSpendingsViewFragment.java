package com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACSpendingsViewBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainACSpendingsViewFragment extends Fragment implements RVAdapterSpendings.SpendingsItemClick {

    private FragmentMainACSpendingsViewBinding binding;
    private List<SpendsEntity> spendsEntityList;
    private RVAdapterSpendings adapter;

    public MainACSpendingsViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACSpendingsViewBinding.inflate(inflater);

        if(spendsEntityList != null){
            init();
            loadRecyclerView();
        }
        disableBackPressed();

        return binding.getRoot();
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
    public void updateData(List<SpendsEntity> spendsEntityList){
        this.spendsEntityList = spendsEntityList;
    }
    private void init(){
        adapter = new RVAdapterSpendings(getContext(), spendsEntityList);
        adapter.setSpendingsItemListenner(this);
    }
    private void loadRecyclerView(){
        if(spendsEntityList.size() > 0){
            spendsEntityList = sortList(spendsEntityList);
            spendsEntityList = sortList(spendsEntityList);
            spendsEntityList = sortList(spendsEntityList);
            Collections.reverse(spendsEntityList);
            adapter.updateData(spendsEntityList);
            binding.rvSpendingsAccountsFragMainACSpendingsView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSpendingsAccountsFragMainACSpendingsView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            binding.textViewNoItemsToDisplayFragMainACSpendingsView.setVisibility(View.VISIBLE);
            binding.rvSpendingsAccountsFragMainACSpendingsView.setVisibility(View.GONE);
        }
    }
    private List<SpendsEntity> sortList(List<SpendsEntity> list){
        List<SpendsEntity> sortedList = new ArrayList<>(list);
        sortedList.sort(new Comparator<SpendsEntity>() {
            @Override
            public int compare(SpendsEntity o1, SpendsEntity o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        return list;
    }

    @Override
    public void onSpendingsItemClick(SpendsEntity spend) {

    }
}