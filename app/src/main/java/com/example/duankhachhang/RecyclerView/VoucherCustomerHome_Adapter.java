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
import com.example.duankhachhang.Class.PercentDiscount;
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
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class VoucherCustomerHome_Adapter extends RecyclerView.Adapter<VoucherCustomerHome_ViewHolder> {

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
    public VoucherCustomerHome_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherCustomerHome_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_customervoucher,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherCustomerHome_ViewHolder holder, int position) {
        Voucher itemVoucher = arrVoucher.get(position);
        getPercentVoucher(itemVoucher,holder);
        holder.itemView.setBackgroundResource(R.drawable.bg_container_dialog);
        getInformationProduct(itemVoucher.getIdProduct(),itemVoucher.getIdShop(),holder,itemVoucher.getIdItemPercentDiscount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product/"+itemVoucher.getIdShop()+"/"+itemVoucher.getIdProduct());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ProductData productData = snapshot.getValue(ProductData.class);
                            Intent intent = new Intent(context, Detailproduct.class);
                            intent.putExtra("Product",productData);
                            intent.putExtra("idVoucher",itemVoucher.getIdVoucher());
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

//    hàm lấy thông tin sản phẩm
    private void getInformationProduct(String idProduct,String idShop,VoucherCustomerHome_ViewHolder holder, String keyPercent){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product/"+idShop +"/"+idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    holder.tvNameProduct_ItemProductVoucher.setText(productData.getNameProduct());
                    Locale locale = new Locale("vi", "VN");
                    NumberFormat numberFormatVND = NumberFormat.getCurrencyInstance(locale);
                    holder.tvPridceProductBeforVoucher.setText(numberFormatVND.format(productData.getPriceProduct()));

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ImageProducts/"+productData.getIdProduct()+"/1");
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String url = snapshot.child("urlImage").getValue().toString();
                                Picasso.get().load(url).placeholder(R.drawable.icondowload).into(holder.ivProduct_ItemProductVoucher);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("PercentVoucher/"+keyPercent);
                    databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                PercentDiscount percentDiscount = snapshot.getValue(PercentDiscount.class);
                                System.out.println("percent: " + percentDiscount.toString());
                                holder.tvPridceProductAfterVoucher.setText(numberFormatVND.format(productData.getPriceProduct() - (productData.getPriceProduct()* percentDiscount.getPercent()/100)));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    hàm lấy thông tin percent voucher
    private void getPercentVoucher(Voucher voucher,VoucherCustomerHome_ViewHolder holder){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PercentVoucher/"+voucher.getIdItemPercentDiscount());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    PercentDiscount percentDiscount = snapshot.getValue(PercentDiscount.class);
                    holder.tvTitleItemVoucher.setText("Giảm giá "+percentDiscount.getPercent() +"%");
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
