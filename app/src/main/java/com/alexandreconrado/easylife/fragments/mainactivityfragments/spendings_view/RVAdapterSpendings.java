package com.alexandreconrado.easylife.fragments.mainactivityfragments.spendings_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;

import java.io.Serializable;
import java.util.List;

public class RVAdapterSpendings extends RecyclerView.Adapter<RVAdapterSpendings.MyViewHolder> implements Serializable{

    private final Context context;
    private List<SpendsEntity> spends;
    private SpendingsItemClick listenner;
    public interface SpendingsItemClick{
        void onSpendingsItemClick(SpendsEntity spend);
    }


    public RVAdapterSpendings(Context context, List<SpendsEntity> spends) {
        this.context = context;
        this.spends = spends;
    }
    public void updateData(List<SpendsEntity> spends){
        this.spends = spends;
        notifyDataSetChanged();
    }
    public void setSpendingsItemListenner(SpendingsItemClick listenner){
        this.listenner = listenner;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_spendings_mainac_spendings_view, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final SpendsEntity SelectedAccount = spends.get(position);
        holder.inWhatTextView.setText(SelectedAccount.getWhere());
        holder.amountTextView.setText(String.valueOf(SelectedAccount.getAmount()));
        holder.inWhatTextView.setText(SelectedAccount.getWhere());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onSpendingsItemClick(SelectedAccount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return spends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView inWhatTextView, amountTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            inWhatTextView = itemView.findViewById(R.id.textView_inWhat_rvRowSpendings_MainACSpendingsView);
            amountTextView = itemView.findViewById(R.id.textView_howMuch_rvRowSpendings_MainACSpendingsView);
        }
    }
}
