package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UrlImageFirebase_Adapter extends RecyclerView.Adapter<UriImageFirebase_ViewHolder> {
    ArrayList<Image> arrImageProduct = new ArrayList();
    Context context;
    private OnclickListener itemClick;

    public UrlImageFirebase_Adapter(ArrayList<Image> arrImageProduct, Context context) {
        this.arrImageProduct = arrImageProduct;
        this.context = context;
    }

    public interface OnclickListener {
        void onItemClick(UriImageFirebase_ViewHolder listImageProductViewHolder, int position);
    }

    public void setOnclickListener(OnclickListener onclickListener) {
        itemClick = onclickListener;
    }

    @NonNull
    @Override
    public UriImageFirebase_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UriImageFirebase_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_imageproduct,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UriImageFirebase_ViewHolder holder, int position) {
        Image imageProduct = arrImageProduct.get(position);
        if (imageProduct.getUrlImage() != null) {
            // holder.ivImageItemProduct.setImageResource(R.drawable.icondowload);
            Picasso.get().load(imageProduct.getUrlImage()).into(holder.ivImageItemProduct);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(holder,holder.getAdapterPosition());
               // notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrImageProduct.size();
    }
}
