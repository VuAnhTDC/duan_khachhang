package com.example.duankhachhang.RecyclerView;

import android.content.Context;
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
            getInforCustomer(orderData.getIdCustomer_Order(),holder);
            getInforProduct(orderData.getIdProduct_Order(),holder);
            getImageProduct(orderData.getIdProduct_Order(),holder);
            final int finalPosition = position;
            holder.linearLayout_ItemOrderList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Detailproduct.class);
                    OrderData orderData1 = arrayOrderData.get(finalPosition);
                    intent.putExtra("idProduct", orderData1.getIdProduct_Order());
                    context.startActivity(intent);
                }
            });
        }
    }



    private void getInforCustomer(String idCustomer, OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Customer/"+idCustomer);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Customer customer1 = snapshot.getValue(Customer.class);
                    holder.tvNameCustomer.setText(customer1.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getImageProduct(String idProduct,OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot imageItem : snapshot.getChildren()){
                        Image image = imageItem.getValue(Image.class);
                        Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.imgItemOrder_Product);
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getInforProduct(String idProduct, OrderItem_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/"+idProduct);
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
