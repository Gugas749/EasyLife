package com.alexandreconrado.easylife.fragments.register.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandreconrado.easylife.R;
import com.alexandreconrado.easylife.database.entities.SpendsEntity;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RVAdapterBackups extends RecyclerView.Adapter<RVAdapterBackups.MyViewHolder> implements Serializable{

    private final Context context;
    private List<Timestamp> backups;
    private BackupsItemClick listenner;
    private int selectedItem = RecyclerView.NO_POSITION;
    public interface BackupsItemClick{
        void onBackupsItemClick(Timestamp backup);
    }


    public RVAdapterBackups(Context context, List<Timestamp> backups) {
        this.context = context;
        this.backups = backups;
    }
    public void updateData(List<Timestamp> backups){
        this.backups = backups;
        notifyDataSetChanged();
    }
    public void setSpendingsItemListenner(BackupsItemClick listenner){
        this.listenner = listenner;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_row_backup_alertdialog_backupload, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Timestamp backupSelected = backups.get(position);
        if(backupSelected != null){
            Date date = backupSelected.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = sdf.format(date);
            holder.dateTextView.setText(formattedDate);
        }

        if (position == selectedItem) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorControlNormal, typedValue, true);
            int color = typedValue.data;
            holder.cardView.setCardBackgroundColor(color);
        } else {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            int color = typedValue.data;
            holder.cardView.setCardBackgroundColor(color);
        }

        holder.cardView.setOnClickListener(v -> {
            notifyItemChanged(selectedItem);
            selectedItem = holder.getBindingAdapterPosition();
            notifyItemChanged(selectedItem);

            if (listenner != null) {
                listenner.onBackupsItemClick(backupSelected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return backups.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textView_date_rvRowBackup_AlertDialogBackupLoad);
            cardView = itemView.findViewById(R.id.cardView_Holder_rvRowBackup_AlertDialogBackupLoad);
        }
    }
}
