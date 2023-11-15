package com.example.duankhachhang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.OrderItem_Adaper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class FragmentOrderDelivering extends Fragment {

    ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    Context context;
    TextView tvNoOrderDelivering;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    OrderItem_Adaper orderAdaper;
    RecyclerView rcvOrderDelivering;
    Customer customerData = new Customer();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_delivering, container, false);
        SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customerData = gson.fromJson(jsonShop, Customer.class);
        System.out.println("thông tin Customer từ SharedPreferences ở FragmentOrderDelivering: " + customerData.toString());
        loadOrderItem();
        setControl(view);
        setInitiazation();
        setEvent();
        return view;
    }
    private void setInitiazation() {

        orderAdaper = new OrderItem_Adaper(arrayOrderData, getContext());
        rcvOrderDelivering.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrderDelivering.setAdapter(orderAdaper);
        orderAdaper.notifyDataSetChanged();
    }

    private void setEvent() {

    }

    private void setControl(@NonNull View view) {
        rcvOrderDelivering = view.findViewById(R.id.rcvOrderDelivering);
        tvNoOrderDelivering = view.findViewById(R.id.tvNoOrderDelivering);
    }

    private void loadOrderItem(){
        //String fullPath = "OrderProduct/";
        databaseReference = firebaseDatabase.getReference("OrderProduct");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayOrderData.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot shopItem : snapshot.getChildren()) {
                        if (shopItem.exists()){
                            for (DataSnapshot oderItem : shopItem.getChildren()){
                                OrderData orderData = oderItem.getValue(OrderData.class);
                                if (orderData.getStatusOrder() == 2) {
                                    if (orderData.getIdCustomer_Order().equals(customerData.getId())){
                                        arrayOrderData.add(orderData);
                                        System.out.println("order item: " + orderData.toString());
                                    }
                                }
                            }
                        }

                    }
                }
                if (arrayOrderData.size() <= 0) {
                    tvNoOrderDelivering.setVisibility(View.VISIBLE);
                    rcvOrderDelivering.setVisibility(View.GONE);
                } else {
                    tvNoOrderDelivering.setVisibility(View.GONE);
                    rcvOrderDelivering.setVisibility(View.VISIBLE);
                }
                orderAdaper.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}