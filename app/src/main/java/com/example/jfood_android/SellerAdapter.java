package com.example.jfood_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.ViewHolder> {

    List<Seller> seller;
    LayoutInflater inflater;
    int currentUserId;
//    int foodPriceList;
//    String foodlist;

//    public Adapter(Context ctx, List<Seller> seller, int currentUserId, String foodlist, int foodPriceList)
    public SellerAdapter(Context ctx, List<Seller> seller, int currentUserId){
        this.seller = seller;
        this.currentUserId = currentUserId;
        this.inflater = LayoutInflater.from(ctx);
//        this.foodlist = foodlist;
//        this.foodPriceList = foodPriceList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.names.setText(seller.get(position).getName());
        holder.image.setImageResource(R.drawable.ic_food);
    }

    @Override
    public int getItemCount() {
        return seller.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView names;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.textView2);
            image = itemView.findViewById(R.id.imageView2);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PilihPesananActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id_seller", seller.get(getAdapterPosition()).getId());
                    intent.putExtra("currentUserId", currentUserId);

//                    intent.putExtra("foodList", foodlist);
//                    intent.putExtra("foodPriceList", foodPriceList);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}