package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

public class ProductSuggestedResultValueSearch_ViewHolder extends RecyclerView.ViewHolder {
    ImageView ivProduct_ItemProductSuggestResult;
    TextView tvNameProduct_ItemProductSuggestResult,tvPriceProduct_ItemProductSuggestResult;
    public ProductSuggestedResultValueSearch_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivProduct_ItemProductSuggestResult = itemView.findViewById(com.example.duankhachhang.R.id.ivProduct_ItemProductSuggestResult);
        tvNameProduct_ItemProductSuggestResult = itemView.findViewById(com.example.duankhachhang.R.id.tvNameProduct_ItemProductSuggestResult);
        tvPriceProduct_ItemProductSuggestResult = itemView.findViewById(com.example.duankhachhang.R.id.tvPriceProduct_ItemProductSuggestResult);
    }
}
