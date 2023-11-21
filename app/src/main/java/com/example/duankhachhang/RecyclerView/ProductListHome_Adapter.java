package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.Detailproduct;
import com.example.duankhachhang.R;
import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductListHome_Adapter extends RecyclerView.Adapter<ProductListHome_ViewHolder> {

    ArrayList<ProductData> arrProduct = new ArrayList<>();
    Context context;

    public ProductListHome_Adapter(ArrayList<ProductData> arrProduct, Context context) {
        this.arrProduct = arrProduct;
        this.context = context;
    }


    @NonNull
    @Override
    public ProductListHome_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductListHome_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_productitem_screenhome, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListHome_ViewHolder holder, int position) {
        ProductData productData = arrProduct.get(position);
        holder.tvNameProductItem_RecyclerViewProductList.setText(productData.getNameProduct());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormatVND = NumberFormat.getCurrencyInstance(locale);
        holder.tvPriceProductItem_RecyclerViewProductList.setText(numberFormatVND.format(productData.getPriceProduct()));
        holder.tvStarProductItem_RecyclerViewProductList.setText(productData.getSumLike() + "");
        holder.tvCmtProductItem_RecyclerViewProductList.setText(productData.getOverageCmtProduct() + "");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Shop").child(productData.getIdUserProduct());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    holder.tvAddressShopProductItem_RecyclerViewProductList.setText(shopData.getShopAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        isLoadingImageProductItem(productData, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailproduct.class);
                intent.putExtra("Product", productData);
                context.startActivity(intent);
            }
        });

    }

    private void isLoadingImageProductItem(ProductData productDataItem, ProductListHome_ViewHolder holder) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ImageProducts/"+productDataItem.getIdProduct() +"/1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   String urlImageFirst = snapshot.child("urlImage").getValue().toString();
                   Picasso.get().load(urlImageFirst).placeholder(R.drawable.icondowload).into(holder.ivProductItem_RecyclerViewProductList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrProduct.size();
    }
}
