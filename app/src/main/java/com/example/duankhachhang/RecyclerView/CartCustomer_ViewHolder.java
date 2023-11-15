package com.example.duankhachhang.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartCustomer_ViewHolder extends RecyclerView.ViewHolder {
    CircleImageView ivAvataShop;
    CheckBox cbxSelectionItem;
    TextView tvShopName,tvProductName,tvQuanlityProduct,tvPriceProduct;
    ImageView ivAddQuanlity, ivSubtractionQuanlity,ivProduct_ItemCartCustomer;
    public  ImageView ivDelete_ItemCartCustomer;
    public  LinearLayout vInformationItemCart;
    public CartCustomer_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvataShop = itemView.findViewById(R.id.ivAvataShop_ItemCartCustomer);
        cbxSelectionItem = itemView.findViewById(R.id.checkBoxItemProductCart_ItemCartCustomer);
        tvShopName = itemView.findViewById(R.id.tvNameShop_ItemCartCustomer);
        tvProductName = itemView.findViewById(R.id.tvNameProduct_ItemCartCustomer);
        tvQuanlityProduct = itemView.findViewById(R.id.tvQuanlitProduct_ItemCartCustomer);
        tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct_ItemCartCustomer);
        ivAddQuanlity = itemView.findViewById(R.id.ivAddQuanlityProduct_ItemCartCustomer);
        ivSubtractionQuanlity = itemView.findViewById(R.id.ivSubtractionQuanlityProduct_ItemCartCustomer);
        ivProduct_ItemCartCustomer = itemView.findViewById(R.id.ivProduct_ItemCartCustomer);
        ivDelete_ItemCartCustomer = itemView.findViewById(R.id.tvDelete_ItemCartCustomer);
        vInformationItemCart = itemView.findViewById(R.id.vInformationItemCart);
    }
}
