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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
        holder.inWhatTextView.setText(SelectedAccount.getCategory());
        String amountFinal = shortenNumber(SelectedAccount.getAmount());
        holder.amountTextView.setText(amountFinal+"€");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy      HH:mm");
        String formattedDate = dateFormat.format(SelectedAccount.getDate());
        holder.whenTextView.setText(formattedDate);

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
    public static String shortenNumber(double number) {
        String stringFinal = "";
        if (number == (long) number) {
            String originalString = String.format("%.0f", number);
            String string1 = "", string2 = "", character = "";
            int value1 = 0, value2 = 0;

            if (number >= 1_000_000_000) {
                character = "b";
                value1 = 1;
                value2 = 3;
            } else if (number >= 100_000_000) {
                character = "m";
                value1 = 3;
                value2 = 5;
            } else if (number >= 10_000_000) {
                character = "m";
                value1 = 2;
                value2 = 4;
            } else if (number >= 1_000_000) {
                character = "m";
                value1 = 1;
                value2 = 3;
            } else if (number >= 100_000) {
                character = "k";
                value1 = 3;
                value2 = 5;
            } else{
                stringFinal = originalString;
            }
            if (originalString.length() > 5) {
                string1 = originalString.substring(0, value1);
                string2 = originalString.substring(value1, value2);
                stringFinal = string1+","+string2+character;
            }
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String originalString = decimalFormat.format(number);
            String string1 = "", string2 = "", character = "";
            int value1 = 0, value2 = 0;

            if (number >= 1_000_000_000.00) {
                character = "b";
                value1 = 1;
                value2 = 3;
            } else if (number >= 100_000_000.00) {
                character = "m";
                value1 = 3;
                value2 = 5;
            } else if (number >= 10_000_000.00) {
                character = "m";
                value1 = 2;
                value2 = 4;
            } else if (number >= 1_000_000.00) {
                character = "m";
                value1 = 1;
                value2 = 3;
            } else if (number >= 100_000.00) {
                character = "k";
                value1 = 3;
                value2 = 5;
            } else{
                stringFinal = originalString;
            }
            if (originalString.length() > 7) {
                string1 = originalString.substring(0, value1);
                string2 = originalString.substring(value1, value2);
                stringFinal = string1+","+string2+character;
            }
        }
        return stringFinal;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView inWhatTextView, amountTextView, whenTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            inWhatTextView = itemView.findViewById(R.id.textView_inWhat_rvRowSpendings_MainACSpendingsView);
            amountTextView = itemView.findViewById(R.id.textView_howMuch_rvRowSpendings_MainACSpendingsView);
            whenTextView = itemView.findViewById(R.id.textView_When_rvRowSpendings_MainACSpendingsView);
        }
    }
}
