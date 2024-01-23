package com.alexandreconrado.easylife.fragments.mainactivityfragments.overview_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.entities.SpendingAccountsEntity;
import java.io.Serializable;
import java.util.List;

public class RVAdapterSpendingsAccounts extends RecyclerView.Adapter<RVAdapterSpendingsAccounts.MyViewHolder> implements Serializable{

    private final Context context;
    private List<SpendingAccountsEntity> accounts;
    private SpendingsAccountItemClick listenner;
    public interface SpendingsAccountItemClick{
        void onSpendingsAccountItemClick(SpendingAccountsEntity account);
    }


    public RVAdapterSpendingsAccounts(Context context, List<SpendingAccountsEntity> accounts) {
        this.context = context;
        this.accounts = accounts;
    }
    public void updateData(List<SpendingAccountsEntity> accounts){
        this.accounts = accounts;
        notifyDataSetChanged();
    }
    public void setSpendingsAccountItemListenner(SpendingsAccountItemClick listenner){
        this.listenner = listenner;
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
        holder.accountNameTextView.setText(SelectedAccount.getAccountTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onSpendingsAccountItemClick(SelectedAccount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView accountNameTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            accountNameTextView = itemView.findViewById(R.id.textView_accountNameHolder_rvRowSpendingsAccounts_MainACOverview);
        }
    }
}
