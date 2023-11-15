package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message_ViewHolder extends RecyclerView.ViewHolder {
    CircleImageView ivAvataUser_CustomerItemMessage;
    TextView tvContentItem_CustomerItemMessage;
    LinearLayout vItemMessage;
    public Message_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvataUser_CustomerItemMessage = itemView.findViewById(R.id.ivAvataUser_CustomerItemMessage);
        tvContentItem_CustomerItemMessage = itemView.findViewById(R.id.tvContentItem_CustomerItemMessage);
        vItemMessage = itemView.findViewById(R.id.vItemMessage);
    }
}
