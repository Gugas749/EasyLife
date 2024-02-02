package com.alexandreconrado.easylife.fragments.alertDialogFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.alexandreconrado.easylife.databinding.FragmentAlertDialogBackupLoadBinding;
import com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view.RVAdapterSpendings;
import com.alexandreconrado.easylife.fragments.register.account.RVAdapterBackups;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlertDialogBackupLoadFragment extends Fragment implements RVAdapterBackups.BackupsItemClick {

    private FragmentAlertDialogBackupLoadBinding binding;
    private List<Timestamp> listString = new ArrayList<>();
    private RVAdapterBackups adapter;

    private ConfirmButtonClickAlertDialogBackupLoad confirmListenner;
    private CancelButtonClickAlertDialogBackupLoad cancelListenner;

    public interface ConfirmButtonClickAlertDialogBackupLoad{
        void onConfirmButtonClickAlertDialogBackupLoad(String id);
    }
    public interface CancelButtonClickAlertDialogBackupLoad{
        void onCancelButtonClickAlertDialogBackupLoad();
    }

    public AlertDialogBackupLoadFragment() {
        // Required empty public constructor
    }
    public AlertDialogBackupLoadFragment(List<Timestamp> listString) {
        this.listString = listString;
    }
    public AlertDialogBackupLoadFragment(List<Timestamp> listString, ConfirmButtonClickAlertDialogBackupLoad confirmListenner, CancelButtonClickAlertDialogBackupLoad cancelListenner) {
        this.listString = listString;
        this.confirmListenner = confirmListenner;
        this.cancelListenner = cancelListenner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertDialogBackupLoadBinding.inflate(inflater);

        init();
        loadRv();

        return binding.getRoot();
    }
    private void init(){
        adapter = new RVAdapterBackups(getContext(), listString);
        adapter.setSpendingsItemListenner(this);
    }
    private void loadRv(){
        if(listString.size() > 0){
            listString = sortTimestampsDescending(listString);
            adapter.updateData(listString);
            binding.recyclerViewBackupsFragAlertDialogBackupLoad.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerViewBackupsFragAlertDialogBackupLoad.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            binding.textViewNoItemsToDisplayFragAlertDialogBackupLoad.setVisibility(View.VISIBLE);
            binding.recyclerViewBackupsFragAlertDialogBackupLoad.setVisibility(View.GONE);
        }
    }
    public static List<Timestamp> sortTimestampsDescending(List<Timestamp> timestampList) {
        List<Timestamp> sortedList = new ArrayList<>(timestampList);
        Comparator<Timestamp> timestampComparator = (timestamp1, timestamp2) -> timestamp2.compareTo(timestamp1);
        Collections.sort(sortedList, timestampComparator);
        return sortedList;
    }
    @Override
    public void onBackupsItemClick(Timestamp backup) {

    }
}