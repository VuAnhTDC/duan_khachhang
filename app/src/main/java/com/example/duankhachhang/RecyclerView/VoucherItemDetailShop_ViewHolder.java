package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duankhachhang.R;

public class VoucherItemDetailShop_ViewHolder extends RecyclerView.ViewHolder {
TextView itemVoucherCode_CustomerItemVoucher,idProduct_CustomerItemVoucher,tvAction_CustomerItemVoucher;


    public VoucherItemDetailShop_ViewHolder(@NonNull View itemView) {
        super(itemView);
        itemVoucherCode_CustomerItemVoucher = itemView.findViewById(R.id.itemVoucherCode_CustomerItemVoucher);
        idProduct_CustomerItemVoucher = itemView.findViewById(R.id.idProduct_CustomerItemVoucher);
        tvAction_CustomerItemVoucher = itemView.findViewById(R.id.tvAction_CustomerItemVoucher);

    }
}
