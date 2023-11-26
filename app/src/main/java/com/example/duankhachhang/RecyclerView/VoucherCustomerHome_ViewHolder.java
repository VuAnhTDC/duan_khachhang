package com.example.duankhachhang.RecyclerView;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

public class VoucherCustomerHome_ViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitleItemVoucher,tvPridceProductAfterVoucher,tvNameProduct_ItemProductVoucher,tvPridceProductBeforVoucher;
    ImageView ivProduct_ItemProductVoucher;
    public VoucherCustomerHome_ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitleItemVoucher = itemView.findViewById(R.id.tvTitleItemVoucher);
        ivProduct_ItemProductVoucher = itemView.findViewById(R.id.ivProduct_ItemProductVoucher);
        tvNameProduct_ItemProductVoucher = itemView.findViewById(R.id.tvNameProduct_ItemProductVoucher);
        tvPridceProductAfterVoucher = itemView.findViewById(R.id.tvPridceProductAfterVoucher);
        tvPridceProductBeforVoucher = itemView.findViewById(R.id.tvPridceProductBeforVoucher);
        tvPridceProductBeforVoucher.setPaintFlags(tvPridceProductBeforVoucher.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
