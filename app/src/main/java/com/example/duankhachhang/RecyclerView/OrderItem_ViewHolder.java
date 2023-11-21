package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

public class OrderItem_ViewHolder extends RecyclerView.ViewHolder{
    ImageView imgItemOrder_Product;
    TextView tvNameCustomer, tvNameProduct, tvPrice, tvAmountProduct, tvTotal;
   public LinearLayout vCancellItemOrder_ItemOrderList,vCommnetOrder_ItemOrderList,vInformationOrder_ItemOrderList;
   public ImageView ivIconCancelOrder_ItemOrderList,ivIconCommentOrder_ItemOrderList;
    public OrderItem_ViewHolder(@NonNull View itemView) {
        super(itemView);
        imgItemOrder_Product = itemView.findViewById(R.id.imgItemOrder_Product);
        tvNameCustomer = itemView.findViewById(R.id.tvNameCustomer);
        tvNameProduct = itemView.findViewById(R.id.tvNameProduct_OrderDetail);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvAmountProduct = itemView.findViewById(R.id.tvAmountProduct_OrderDetail);
        tvTotal = itemView.findViewById(R.id.tvTotal_OrderDetail);
        vCancellItemOrder_ItemOrderList = itemView.findViewById(R.id.vCancellItemOrder_ItemOrderList);
        vCommnetOrder_ItemOrderList = itemView.findViewById(R.id.vCommnetOrder_ItemOrderList);
        ivIconCancelOrder_ItemOrderList = itemView.findViewById(R.id.ivIconCancelOrder_ItemOrderList);
        ivIconCommentOrder_ItemOrderList = itemView.findViewById(R.id.ivIconCommentOrder_ItemOrderList);
        vInformationOrder_ItemOrderList = itemView.findViewById(R.id.vInformationOrder_ItemOrderList);
    }
}
