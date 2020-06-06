package com.example.jfood_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<Integer> hId;
    List<String> hSeller;
    List<String> hFood;
    List<Integer> hPrices;
    List<String> hTime;
    List<String> hPayment;
    List<String> hStatus;
    LayoutInflater inflater;

    public HistoryAdapter(Context ctx, List<Integer> hId, List<String> hSeller, List<String> hFood, List<Integer> hPrices, List<String> hTime, List<String> hPayment, List<String> hStatus){
        this.hId = hId;
        this.hSeller = hSeller;
        this.hFood = hFood;
        this.hPrices = hPrices;
        this.hTime = hTime;
        this.hPayment = hPayment;
        this.hStatus = hStatus;
        this.inflater = LayoutInflater.from(ctx);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_layout_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ids.setText((hId.get(position)).toString());
        holder.sellers.setText(hSeller.get(position));
        holder.foods.setText(hFood.get(position));
        holder.prices.setText((hPrices.get(position)).toString());
        holder.times.setText(hTime.get(position));
        holder.payments.setText(hPayment.get(position));
        holder.status.setText(hStatus.get(position));
    }

    @Override
    public int getItemCount() {
        return hId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ids, sellers, foods, prices, times, payments, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ids = itemView.findViewById(R.id.historyId);
            sellers = itemView.findViewById(R.id.historySeller);
            foods = itemView.findViewById(R.id.HistoryFood);
            prices = itemView.findViewById(R.id.HistoryPrice);
            times = itemView.findViewById(R.id.HistoryTime);
            payments = itemView.findViewById(R.id.HistoryPayment);
            status = itemView.findViewById(R.id.HistoryStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Invoice ID " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}