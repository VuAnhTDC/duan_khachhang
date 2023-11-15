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
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.OrderItem_Adaper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FragmentOrderDelivered extends Fragment {

    ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    Context context;
    TextView tvNoOrderDelivered;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    OrderItem_Adaper orderAdaper;
    RecyclerView rcvOrderDelivered;
    Customer customerData = new Customer();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_delivered, container, false);
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
        rcvOrderDelivered.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrderDelivered.setAdapter(orderAdaper);
        orderAdaper.notifyDataSetChanged();
    }

    private void setEvent() {

    }

    private void setControl(@NonNull View view) {
        rcvOrderDelivered = view.findViewById(R.id.rcvOrderDelivered);
        tvNoOrderDelivered = view.findViewById(R.id.tvNoOrderDelivered);
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
                                if (orderData.getStatusOrder() == 3) {
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
                    tvNoOrderDelivered.setVisibility(View.VISIBLE);
                    rcvOrderDelivered.setVisibility(View.GONE);
                } else {
                    tvNoOrderDelivered.setVisibility(View.GONE);
                    rcvOrderDelivered.setVisibility(View.VISIBLE);
                }
                orderAdaper.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}