package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

public class ProductListHome_ViewHolder extends RecyclerView.ViewHolder {
    ImageView ivProductItem_RecyclerViewProductList;
    TextView tvNameProductItem_RecyclerViewProductList, tvPriceProductItem_RecyclerViewProductList,tvStarProductItem_RecyclerViewProductList,tvAddressShopProductItem_RecyclerViewProductList,tvCmtProductItem_RecyclerViewProductList;
    public ProductListHome_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivProductItem_RecyclerViewProductList = itemView.findViewById(R.id.ivProductItem_RecyclerViewProductList);
        tvNameProductItem_RecyclerViewProductList = itemView.findViewById(R.id.tvNameProductItem_RecyclerViewProductList);
        tvPriceProductItem_RecyclerViewProductList = itemView.findViewById(R.id.tvPriceProductItem_RecyclerViewProductList);
        tvStarProductItem_RecyclerViewProductList = itemView.findViewById(R.id.tvStarProductItem_RecyclerViewProductList);
        tvAddressShopProductItem_RecyclerViewProductList = itemView.findViewById(R.id.tvAddressShopProductItem_RecyclerViewProductList);
        tvCmtProductItem_RecyclerViewProductList = itemView.findViewById(R.id.tvCmtProductItem_RecyclerViewProductList);
    }
}
