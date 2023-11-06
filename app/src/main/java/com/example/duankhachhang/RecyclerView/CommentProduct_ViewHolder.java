package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentProduct_ViewHolder extends RecyclerView.ViewHolder {
    CircleImageView ivAvataUserComment_ItemUserComment;
    TextView tvUserNameProduct_ItemUserComment,tvContentCommet_ItemUserComment,tvDateComment_ItemuserComment;
    public CommentProduct_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvataUserComment_ItemUserComment = itemView.findViewById(R.id.ivAvataUserComment_ItemUserComment);
        tvUserNameProduct_ItemUserComment = itemView.findViewById(R.id.tvUserNameProduct_ItemUserComment);
        tvContentCommet_ItemUserComment = itemView.findViewById(R.id.tvContentCommet_ItemUserComment);
        tvDateComment_ItemuserComment = itemView.findViewById(R.id.tvDateComment_ItemuserComment);
    }
}
