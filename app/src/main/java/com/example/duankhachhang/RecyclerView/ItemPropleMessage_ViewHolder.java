package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemPropleMessage_ViewHolder extends RecyclerView.ViewHolder {
    CircleImageView ivAvataUser_CustomerItemUserMessage;
    TextView tvNameUser_CustomerItemUserMessage,tvContent_CustomerItemUserMessage;
    public ItemPropleMessage_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvataUser_CustomerItemUserMessage = itemView.findViewById(R.id.ivAvataShop_ItemShopMessage);
        tvNameUser_CustomerItemUserMessage = itemView.findViewById(R.id.tvNameUser_CustomerItemUserMessage);
        tvContent_CustomerItemUserMessage = itemView.findViewById(R.id.tvContent_CustomerItemUserMessage);
    }
}
