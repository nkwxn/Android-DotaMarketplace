package com.nicholas.dotamarketplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransactionHistoryListRVAdapter extends RecyclerView.Adapter<TransactionHistoryListRVAdapter.ViewHolder> {
    Context c;
    ArrayList<TransactionHistory> histories;

    public TransactionHistoryListRVAdapter(Context c, ArrayList<TransactionHistory> histories) {
        this.c = c;
        this.histories = histories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View purchaseHistory = LayoutInflater.from(c).inflate(R.layout.list_layout_history, parent, false);
        return new ViewHolder(purchaseHistory);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionHistory tHis = histories.get(position);

        // Buat DateFormatter di sini!
        String datetime = tHis.getTransDate();
        String sDate = "", sTime = "";
        Locale locale = new Locale("en", "US");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat date = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat time = DateFormat.getTimeInstance(DateFormat.SHORT);
        Date newDate = null;
        try {
            newDate = sdf.parse(datetime);
            sDate = date.format(newDate);
            sTime = time.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtTranDate.setText(sDate);
        holder.txtTranTime.setText(sTime);
        holder.txtItemName.setText(tHis.getItemName());
        holder.txtItemQty.setText("Qty: " + tHis.getTransQty());
        holder.txtTotalPrice.setText("Rp " + (tHis.getTransQty() * tHis.getItemPrice()));
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTranDate,
                txtTranTime,
                txtItemName,
                txtItemQty,
                txtTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTranDate = itemView.findViewById(R.id.txtTranDateH);
            txtTranTime = itemView.findViewById(R.id.txtTranTimeH);
            txtItemName = itemView.findViewById(R.id.txtItemNameH);
            txtItemQty = itemView.findViewById(R.id.txtQtyH);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPriceH);
        }
    }
}
