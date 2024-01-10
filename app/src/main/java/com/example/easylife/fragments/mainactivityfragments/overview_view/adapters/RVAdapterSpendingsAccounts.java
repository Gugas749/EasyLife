package com.example.easylife.fragments.mainactivityfragments.overview_view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easylife.R;
import com.example.easylife.database.entities.SpendingAccountsEntity;
import java.io.Serializable;
import java.util.List;

public class RVAdapterSpendingsAccounts extends RecyclerView.Adapter<RVAdapterSpendingsAccounts.MyViewHolder> implements Serializable{

    private final Context context;
    private List<SpendingAccountsEntity> accounts;


    public RVAdapterSpendingsAccounts(Context context, List<SpendingAccountsEntity> accounts) {
        this.context = context;
        this.accounts = accounts;
    }
    public void updateData(List<SpendingAccountsEntity> accounts){
        this.accounts = accounts;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_spendingsaccounts_mainac_overview, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final SpendingAccountsEntity SelectedAccount = accounts.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "SHÉ MA NENGUE CLICOU SHÉ", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
