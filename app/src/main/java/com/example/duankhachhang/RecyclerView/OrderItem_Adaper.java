package com.example.duankhachhang.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.FormatMoneyVietNam;
import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.OrderData;
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

import java.util.ArrayList;

public class OrderItem_Adaper extends RecyclerView.Adapter<OrderItem_ViewHolder>{
    FrameLayout frameLayout_ItemOrderList;
    ArrayList<OrderData> arrayOrderData;

    Context context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public OrderItem_Adaper(ArrayList<OrderData> arrayOrderData, Context context){
        this.arrayOrderData = arrayOrderData;
        this.context = context;
    }
    @NonNull
    @Override
    public OrderItem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItem_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_orderlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItem_ViewHolder holder, int position) {
        if(arrayOrderData.size()>0){
            OrderData orderData = arrayOrderData.get(position);
            holder.tvAmountProduct.setText(orderData.getQuanlity_Order()+"");
            holder.tvTotal.setText(FormatMoneyVietNam.formatMoneyVietNam(orderData.getPrice_Order())+"đ");
            getInformationShop(orderData.getIdShop_Order(),holder);
            getInforProduct(orderData,holder);
            getImageProduct(orderData.getIdProduct_Order(),holder);
            final int finalPosition = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference = firebaseDatabase.getReference("Product/"+orderData.getIdShop_Order()+"/"+orderData.getIdProduct_Order());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                ProductData productData = snapshot.getValue(ProductData.class);
                                Intent intent = new Intent(context, Detailproduct.class);
                                intent.putExtra("Product", productData);
                                intent.putExtra("idVoucher","");
                                context.startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Thông báo");
                                builder.setMessage("Sản phẩm không tồn tại. (Có thể là do cửa hàng đã xóa trên sàn giao dịch hoặc đã bị cấm bán)");
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }




    private void getInformationShop(String idShop, OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Shop/"+idShop);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    holder.tvNameCustomer.setText("Shop: " + shopData.getShopName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getImageProduct(String idProduct,OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("ImageProducts/"+idProduct+"/1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String urlImageFirst = snapshot.child("urlImage").getValue().toString();
                    Picasso.get().load(urlImageFirst).placeholder(R.drawable.icondowload).into(holder.imgItemOrder_Product);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getInforProduct(OrderData orderData, OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/"+orderData.getIdShop_Order()+"/"+orderData.getIdProduct_Order());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    holder.tvNameProduct.setText(productData.getNameProduct());
                    holder.tvPrice.setText(FormatMoneyVietNam.formatMoneyVietNam(productData.getPriceProduct())+ "đ");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayOrderData.size();
    }
}
