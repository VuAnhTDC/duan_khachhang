package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProduct_Adapter extends RecyclerView.Adapter<OrderProduct_ViewHolder> {
    private ArrayList<OrderData> arrOrderData = new ArrayList<>();
    private Context context;
    private Customer customer;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public OrderProduct_Adapter(ArrayList<OrderData> arrOrderData, Context context){
        this.arrOrderData = arrOrderData;
        this.context = context;
        this.customer = customer;
    }
    @NonNull
    @Override
    public OrderProduct_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new OrderProduct_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_orderproduct_pay,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProduct_ViewHolder holder, int position) {
       OrderData orderData = arrOrderData.get(position);
        setInformationShop(orderData.getIdShop_Order(),holder);
     setInformationProduct(orderData,holder);

    }

    private void setInformationShop(String idShop, OrderProduct_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Shop/"+idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    if (shopData.getUrlImgShopAvatar().isEmpty()){
                        holder.ivAvataShop_ItemOrderProduct.setImageResource(R.drawable.icon_personal);
                    }
                    else {
                        Picasso.get().load(shopData.getUrlImgShopAvatar()).into(holder.ivAvataShop_ItemOrderProduct);
                    }
                    holder.tvNameShop_ItemOrderProduct.setText(shopData.getShopName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi khi lấy data shop trong danh sách Order product: " + error.getMessage());
            }
        });

    }

    private void setInformationProduct(OrderData orderData, OrderProduct_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/" + orderData.getIdProduct_Order());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    holder.tvNameProduct_OrderProduct.setText(productData.getNameProduct());
                    holder.tvPriceProduct_ItemOrderProduct.setText(productData.getPriceProduct() + " VND");
                    holder.tvQuanlityProduct_OrderProduct.setText("Số lượng: " + orderData.getQuanlity_Order());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrOrderData.size();
    }


}
