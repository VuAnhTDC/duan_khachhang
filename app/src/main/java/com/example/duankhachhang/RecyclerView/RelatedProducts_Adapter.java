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

public class RelatedProducts_Adapter extends RecyclerView.Adapter<RealtedProducts_ViewHolder> {
    private ArrayList<ProductData> arrRelatedProducts = new ArrayList<>();
    private Context context;

    public RelatedProducts_Adapter(ArrayList<ProductData> arrRelatedProducts, Context context){
        this.arrRelatedProducts = arrRelatedProducts;
        this.context = context;
    }
    @NonNull
    @Override
    public RealtedProducts_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RealtedProducts_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_relatedproducts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RealtedProducts_ViewHolder holder, int position) {
        ProductData productData = arrRelatedProducts.get(position);
        holder.tvNameProductItem_RelatedProducts.setText(productData.getNameProduct());
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormatVND = NumberFormat.getCurrencyInstance(locale);
        holder.tvPriceProductItem_RelatedProducts.setText(numberFormatVND.format(productData.getPriceProduct()));
        holder.tvStarProductItem_RelatedProducts.setText(productData.getSumLike() + "");
        holder.tvCmtProductItem_RelatedProducts.setText(productData.getOverageCmtProduct() + "");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Shop").child(productData.getIdUserProduct());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    holder.tvAddressShopProductItem_RelatedProducts.setText(shopData.getShopAddress());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        isLoadingImageProductItem(productData,holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailproduct.class);
                intent.putExtra("idProduct",productData.getIdProduct());
                context.startActivity(intent);
            }
        });
    }

    private void isLoadingImageProductItem(ProductData productDataItem,RealtedProducts_ViewHolder holder){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
<<<<<<< HEAD
        DatabaseReference databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(productDataItem.getIdProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot imageItem:
                            snapshot.getChildren()) {
                        Image image = imageItem.getValue(Image.class);
                        Picasso.get().load(image.getUrlImage()).into(holder.ivProductItem_RelatedProducts);
                        return;
                    }
                }
            }
=======
        DatabaseReference databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(productDataItem.getIdProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot imageItem:
                            snapshot.getChildren()) {
                        Image image = imageItem.getValue(Image.class);
                        Picasso.get().load(image.getUrlImage()).into(holder.ivProductItem_RelatedProducts);
                        return;
                    }
                }
            }

>>>>>>> vuanh
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return arrRelatedProducts.size();
    }
}
