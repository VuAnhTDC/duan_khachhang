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

public class FragmentOrderWaitForTakeGoods extends Fragment {
    ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    Context context;
    TextView tvNoOrderWaitTakeGoods;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    OrderItem_Adaper orderAdaper;
    RecyclerView rcvOrderWaitTakeGoods;
    private Customer customerData = new Customer();
    private String shopPhoneNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_wait_for_take_goods, container, false);
        SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customerData = gson.fromJson(jsonShop, Customer.class);
        System.out.println("thông tin Customer từ SharedPreferences ở FragmentOrderDelivering: " + customerData.toString());
        loadOrderItem();
        setControl(view);
        setInitiazation();
        setEvent();
        // Inflate the layout for this fragment
        return view;
    }
    private void setInitiazation() {

        orderAdaper = new OrderItem_Adaper(arrayOrderData, getContext());
        rcvOrderWaitTakeGoods.setLayoutManager(new LinearLayoutManager(getContext())); // Sử dụng tên biến đúng
        rcvOrderWaitTakeGoods.setAdapter(orderAdaper);
        orderAdaper.notifyDataSetChanged();
    }

    private void setEvent() {

    }

    private void setControl(@NonNull View view) {
        rcvOrderWaitTakeGoods = view.findViewById(R.id.rcvOrderWaitTakeGoods);
        tvNoOrderWaitTakeGoods = view.findViewById(R.id.tvNoOrderWaitTakeGoods);
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
                                if (orderData.getStatusOrder() == 4) {
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
                    tvNoOrderWaitTakeGoods.setVisibility(View.VISIBLE);
                    rcvOrderWaitTakeGoods.setVisibility(View.GONE);
                } else {
                    tvNoOrderWaitTakeGoods.setVisibility(View.GONE);
                    rcvOrderWaitTakeGoods.setVisibility(View.VISIBLE);
                }
                orderAdaper.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}