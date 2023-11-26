package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Detailproduct;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductSuggestedResultValueSearch_Adapter extends RecyclerView.Adapter<ProductSuggestedResultValueSearch_ViewHolder> {
    private ArrayList<ProductData> arrProductSuggested = new ArrayList<>();
    private Context context;

    public ProductSuggestedResultValueSearch_Adapter(ArrayList<ProductData> arrProductSuggested, Context context) {
        this.arrProductSuggested = arrProductSuggested;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductSuggestedResultValueSearch_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductSuggestedResultValueSearch_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_productsuggestedresult,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSuggestedResultValueSearch_ViewHolder holder, int position) {
        ProductData itemProduct = arrProductSuggested.get(position);
        holder.tvNameProduct_ItemProductSuggestResult.setText(itemProduct.getNameProduct());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormatVND = NumberFormat.getCurrencyInstance(locale);
        holder.tvPriceProduct_ItemProductSuggestResult.setText(numberFormatVND.format(itemProduct.getPriceProduct()));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ImageProducts/"+itemProduct.getIdProduct()+"/1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String url = snapshot.child("urlImage").getValue().toString();
                    Picasso.get().load(url).placeholder(R.drawable.icondowload).into(holder.ivProduct_ItemProductSuggestResult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailproduct.class);
                intent.putExtra("Product",itemProduct);
                intent.putExtra("idVoucher","");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrProductSuggested.size();
    }
}
