package com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACSpendingsViewBinding;

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

        init();
        loadRecyclerView();

        return binding.getRoot();
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
            adapter.updateData(spendsEntityList);
            binding.rvSpendingsAccountsFragMainACSpendingsView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSpendingsAccountsFragMainACSpendingsView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            binding.textViewNoItemsToDisplayFragMainACSpendingsView.setVisibility(View.VISIBLE);
            binding.rvSpendingsAccountsFragMainACSpendingsView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSpendingsItemClick(SpendsEntity spend) {

    }
}