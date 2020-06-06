package com.example.jfood_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    List<Food> food;
    LayoutInflater inflater;
    Context context;
    int currentUserId;
//    int foodPriceList;
//    String foodlist;
//    public FoodAdapter(Context ctx, List<Food> food, int currentUserId, String foodlist, int foodPriceList)
    public FoodAdapter(Context ctx, List<Food> food, int currentUserId){
        this.context = ctx;
        this.food = food;
        this.currentUserId = currentUserId;
        this.inflater = LayoutInflater.from(ctx);
//        this.foodlist = foodlist;
//        this.foodPriceList = foodPriceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_layout_ori,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.names.setText(food.get(position).getName());
        holder.category.setText(food.get(position).getCategory());
        holder.price.setText(String.valueOf(food.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return food.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView names, category, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.foodName);
            category = itemView.findViewById(R.id.foodCategory);
            price = itemView.findViewById(R.id.foodPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BuatPesananActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id_food", food.get(getAdapterPosition()).getId());
                    intent.putExtra("foodName", food.get(getAdapterPosition()).getName());
                    intent.putExtra("foodCategory", food.get(getAdapterPosition()).getCategory());
                    intent.putExtra("foodPrice", food.get(getAdapterPosition()).getPrice());
                    intent.putExtra("currentUserId", currentUserId);
//                    intent.putExtra("foodList", foodlist);
//                    intent.putExtra("foodPriceList", foodPriceList);

                    v.getContext().startActivity(intent);
//                    Toast.makeText(v.getContext(), "Clicked -> " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}