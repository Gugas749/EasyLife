package com.example.easylife.fragments.mainactivityfragments.overview_view.add;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easylife.R;

import java.io.Serializable;
import java.util.List;

public class RVAdapterPercentagesNamesColors extends RecyclerView.Adapter<RVAdapterPercentagesNamesColors.MyViewHolder> implements Serializable{

    private final Context context;
    private List<String> percentagesNamesList, percentagesColorsList;
    private ItemClickedRVAdapterPercentagesNamesAndColors listenner;
    public interface ItemClickedRVAdapterPercentagesNamesAndColors{
        void onItemClickedRVAdapterPercentagesNamesAndColors(int position);
    }

    public RVAdapterPercentagesNamesColors(Context context, List<String> percentagesNamesList, List<String> percentagesColorsList, ItemClickedRVAdapterPercentagesNamesAndColors listenner) {
        this.context = context;
        this.percentagesNamesList = percentagesNamesList;
        this.percentagesColorsList = percentagesColorsList;
        this.listenner = listenner;
    }
    public void updateData(List<String> percentagesNamesList, List<String> percentagesColorsList){
        this.percentagesNamesList = percentagesNamesList;
        this.percentagesColorsList = percentagesColorsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_percentagesnamesandcolors_mainac_overviewview_add_spendingsaccounts, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final String SelectedName = percentagesNamesList.get(position);
        final String SelectedColor = percentagesColorsList.get(position);
        holder.textViewNamePercentage.setText(SelectedName);
        holder.cardViewColorPercentage.setCardBackgroundColor(Integer.parseInt(SelectedColor));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onItemClickedRVAdapterPercentagesNamesAndColors(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return percentagesNamesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNamePercentage;
        CardView cardViewColorPercentage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNamePercentage = itemView.findViewById(R.id.textView_accountNameHolder_rvRowPercentagesNamesAndColors_MainACOverview);
            cardViewColorPercentage = itemView.findViewById(R.id.cardView_percentagesColor_rvRowPercentagesNamesAndColors_MainACOverview);
        }
    }
}
