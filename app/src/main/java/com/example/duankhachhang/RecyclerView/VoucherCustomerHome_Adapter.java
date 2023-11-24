package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Buyproduct;
import com.example.duankhachhang.Class.ActionToGetVoucher;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.Voucher;
import com.example.duankhachhang.Class.VoucherCustomer;
import com.example.duankhachhang.Detailproduct;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class VoucherCustomerHome_Adapter extends RecyclerView.Adapter<VoucherItemDetailShop_ViewHolder> {

    private ArrayList<Voucher> arrVoucher = new ArrayList<>();
    private Context context;
    private Customer customer = new Customer();

    public VoucherCustomerHome_Adapter(ArrayList<Voucher> arrVoucher, Context context) {
        this.arrVoucher = arrVoucher;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product/"+voucher.getIdShop()+"/"+voucher.getIdProduct());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            ProductData productData = snapshot.getValue(ProductData.class);
                            Intent intent = new Intent(context, Detailproduct.class);
                            intent.putExtra("Product",productData);
                            intent.putExtra("idVoucher",voucher.getIdVoucher());
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    //    hàm lấy hành động để được voucher
    private void getActionToGetVoucher(String idAction, VoucherItemDetailShop_ViewHolder holder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TakeActionToGetVoucher/" +idAction);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ActionToGetVoucher actionToGetVoucher = snapshot.getValue(ActionToGetVoucher.class);
                    holder.tvAction_CustomerItemVoucher.setText(actionToGetVoucher.getValueAction());
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
