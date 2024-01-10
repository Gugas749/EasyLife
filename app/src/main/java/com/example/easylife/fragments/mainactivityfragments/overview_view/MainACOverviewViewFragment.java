package com.example.easylife.fragments.mainactivityfragments.overview_view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylife.database.entities.SpendingAccountsEntity;
import com.example.easylife.databinding.FragmentMainACOverviewViewBinding;
import com.example.easylife.fragments.mainactivityfragments.overview_view.adapters.RVAdapterSpendingsAccounts;

import java.util.List;

public class MainACOverviewViewFragment extends Fragment {
    private FragmentMainACOverviewViewBinding binding;
    private List<SpendingAccountsEntity> accountsEntityList;
    private RVAdapterSpendingsAccounts adapter;
    public MainACOverviewViewFragment() {
    }
    public void setInfos(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainACOverviewViewBinding.inflate(inflater);

        init();
        loadRecyclerView();

        return binding.getRoot();
    }
    public void updateData(List<SpendingAccountsEntity> accountsEntityList){
        this.accountsEntityList = accountsEntityList;
    }
    private void init(){
        adapter = new RVAdapterSpendingsAccounts(getContext(), accountsEntityList);
    }
    private void loadRecyclerView(){
        if(accountsEntityList.size() > 0){
            adapter.updateData(accountsEntityList);
            binding.rvSpendingsAccountsFragMainACOverviewView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvSpendingsAccountsFragMainACOverviewView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            binding.textViewNoItemsToDisplayFragMainACOverviewView.setVisibility(View.VISIBLE);
            binding.rvSpendingsAccountsFragMainACOverviewView.setVisibility(View.GONE);
        }
    }
}