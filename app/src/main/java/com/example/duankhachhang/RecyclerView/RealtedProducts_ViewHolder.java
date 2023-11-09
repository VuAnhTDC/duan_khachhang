package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

public class RealtedProducts_ViewHolder extends RecyclerView.ViewHolder {
    ImageView ivProductItem_RelatedProducts;
    TextView tvNameProductItem_RelatedProducts, tvPriceProductItem_RelatedProducts,tvStarProductItem_RelatedProducts,tvAddressShopProductItem_RelatedProducts,tvCmtProductItem_RelatedProducts;
    public RealtedProducts_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivProductItem_RelatedProducts = itemView.findViewById(R.id.ivProductItem_RelatedProducts);
        tvNameProductItem_RelatedProducts = itemView.findViewById(R.id.tvNameProductItem_RelatedProducts);
        tvPriceProductItem_RelatedProducts = itemView.findViewById(R.id.tvPriceProductItem_RelatedProducts);
        tvStarProductItem_RelatedProducts = itemView.findViewById(R.id.tvStarProductItem_RelatedProducts);
        tvAddressShopProductItem_RelatedProducts = itemView.findViewById(R.id.tvAddressShopProductItem_RelatedProducts);
        tvCmtProductItem_RelatedProducts = itemView.findViewById(R.id.tvCmtProductItem_RelatedProducts);
    }
}
