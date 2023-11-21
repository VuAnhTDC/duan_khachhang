package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

public class CategoryProduct_ViewHolder extends RecyclerView.ViewHolder {
   public Button btnCategoryProduct;
    public CategoryProduct_ViewHolder(@NonNull View itemView) {
        super(itemView);
        btnCategoryProduct = itemView.findViewById(R.id.btnCategoryProduct);
    }
}
