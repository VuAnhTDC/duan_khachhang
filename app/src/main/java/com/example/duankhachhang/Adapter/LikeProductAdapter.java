package com.example.duankhachhang.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

import java.util.List;

public class LikeProductAdapter extends RecyclerView.Adapter<LikeProductViewHolder> {



    @NonNull
    @Override
    public LikeProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LikeProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class LikeProductViewHolder extends RecyclerView.ViewHolder{
    ImageView img_product,img_like;
    CardView card_item_product;
    TextView tv_name,tv_price;

    public LikeProductViewHolder(@NonNull View itemView) {
        super(itemView);
        img_product = itemView.findViewById(R.id.img_like_product);
        img_like = itemView.findViewById(R.id.img_like_item);
        card_item_product = itemView.findViewById(R.id.card_like_product);
        tv_name = itemView.findViewById(R.id.tv_name_like_product);
        tv_price = itemView.findViewById(R.id.tv_price_like_product);
    }
}

