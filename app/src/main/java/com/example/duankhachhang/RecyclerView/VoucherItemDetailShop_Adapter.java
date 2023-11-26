package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duankhachhang.Class.ActionToGetVoucher;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.Voucher;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoucherItemDetailShop_Adapter extends RecyclerView.Adapter<VoucherItemDetailShop_ViewHolder>{

    private ArrayList<Voucher> arrVoucher = new ArrayList<>();
    private Context context;

    public VoucherItemDetailShop_Adapter(ArrayList<Voucher> arrVoucher, Context context) {
        this.arrVoucher = arrVoucher;
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherItemDetailShop_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherItemDetailShop_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_voucher,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherItemDetailShop_ViewHolder holder, int position) {
        Voucher voucher = arrVoucher.get(position);
        setInformationProduct(voucher.getIdProduct(),voucher.getIdShop(),holder);
        getActionToGetVoucher(voucher.getIdActionToGetVoucher(),holder);
        holder.itemVoucherCode_CustomerItemVoucher.setText(voucher.getIdVoucher());
        if (position % 2 == 0){
            holder.itemView.setBackgroundResource(R.drawable.bg_item01);
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.bg_item02);
        }

    }

//    hàm lấy hành động để được voucher
    private void getActionToGetVoucher(String idAction, VoucherItemDetailShop_ViewHolder holder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TakeActionToGetVoucher/" +idAction);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ActionToGetVoucher actionToGetVoucher = snapshot.getValue(ActionToGetVoucher.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setInformationProduct(String idProduct,String idShop, VoucherItemDetailShop_ViewHolder holder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product/"+idShop+"/"+idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData= snapshot.getValue(ProductData.class);
                    holder.idProduct_CustomerItemVoucher.setText("Mã sản phẩm áp dụng: "+ productData.getIdProduct());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrVoucher.size();
    }
}
