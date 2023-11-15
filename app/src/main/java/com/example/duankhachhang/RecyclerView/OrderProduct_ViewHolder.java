package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderProduct_ViewHolder extends RecyclerView.ViewHolder {
    ImageView ivProduct_ItemOrderProduct;
    CircleImageView ivAvataShop_ItemOrderProduct;
    TextView tvNameShop_ItemOrderProduct,tvNameProduct_OrderProduct,tvPriceProduct_ItemOrderProduct,tvQuanlityProduct_OrderProduct;
    public OrderProduct_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvataShop_ItemOrderProduct = itemView.findViewById(R.id.ivAvataShop_ItemOrderProduct);
        ivProduct_ItemOrderProduct = itemView.findViewById(R.id.ivProduct_ItemOrderProduct);
        tvNameShop_ItemOrderProduct = itemView.findViewById(R.id.tvNameShop_ItemOrderProduct);
        tvNameProduct_OrderProduct = itemView.findViewById(R.id.tvNameProduct_OrderProduct);
        tvPriceProduct_ItemOrderProduct = itemView.findViewById(R.id.tvPriceProduct_ItemOrderProduct);
        tvQuanlityProduct_OrderProduct = itemView.findViewById(R.id.tvQuanlityProduct_OrderProduct);

    }
}
