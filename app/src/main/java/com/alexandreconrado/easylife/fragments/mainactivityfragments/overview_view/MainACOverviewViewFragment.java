package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.activitys.MainActivity;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import com.alexandreconrado.easylife.databinding.FragmentMainACOverviewViewBinding;

import java.util.List;

public class MainACOverviewViewFragment extends Fragment implements RVAdapterSpendingsAccounts.SpendingsAccountItemClick {
    private FragmentMainACOverviewViewBinding binding;
    private List<SpendingAccountsEntity> accountsEntityList;
    private RVAdapterSpendingsAccounts adapter;
    private MainActivity parent;

    private SpendingsAccountItemClickFragMainACOverviewView listenner;
    public interface SpendingsAccountItemClickFragMainACOverviewView{
        void onSpendingsAccountItemClickFragMainACOverviewView(SpendingAccountsEntity account);
    }
    public void setSpendingsAccountItemClickFragMainACOverviewViewListenner(SpendingsAccountItemClickFragMainACOverviewView listenner){
        this.listenner = listenner;
    }

    public MainACOverviewViewFragment() {

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
        binding = FragmentMainACOverviewViewBinding.inflate(inflater);

        if(accountsEntityList != null){
            init();
            loadRecyclerView();
        }
        disableBackPressed();

        return binding.getRoot();
    }
    public void updateData(List<SpendingAccountsEntity> accountsEntityList){
        this.accountsEntityList = accountsEntityList;
    }
    private void init(){
        adapter = new RVAdapterSpendingsAccounts(getContext(), accountsEntityList);
        adapter.setSpendingsAccountItemListenner(this);
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
    @Override
    public void onSpendingsAccountItemClick(SpendingAccountsEntity account) {
        listenner.onSpendingsAccountItemClickFragMainACOverviewView(account);
    }
}