package com.example.duankhachhang.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.OrderItem_Adaper;
import com.example.duankhachhang.RecyclerView.OrderItem_ViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;


public class FragmentOrderDelivering extends Fragment {

    ArrayList<OrderData> arrayOrderData = new ArrayList<>();
    Context context;
    TextView tvNoOrderDelivering;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    OrderItem_Adaper orderAdaper;
    RecyclerView rcvOrderDelivering;
    Customer customerData = new Customer();

    private Runnable runnable = null;
    private Handler handler = new Handler();
    private Target<Drawable> itemTaget = null;
    private RequestBuilder<Drawable> requestBuilder = null;
    private OrderItem_ViewHolder itemViewHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_delivering, container, false);
        context = getContext();
        SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customerData = gson.fromJson(jsonShop, Customer.class);
        System.out.println("thông tin Customer từ SharedPreferences ở FragmentOrderDelivering: " + customerData.toString());
        setControl(view);
        setInitiazation();
        loadOrderItem();
        setEvent();
        return view;
    }
    private void setInitiazation() {

        orderAdaper = new OrderItem_Adaper(arrayOrderData, getContext());
        rcvOrderDelivering.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrderDelivering.setAdapter(orderAdaper);
        orderAdaper.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                orderAdaper.notifyDataSetChanged();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                handler.removeCallbacks(runnable);
                if (itemViewHolder != null) {
                    itemViewHolder.vCancellItemOrder_ItemOrderList.setVisibility(View.GONE);
                    itemViewHolder.vInformationOrder_ItemOrderList.setVisibility(View.VISIBLE);
                    Glide.with(context).clear(itemTaget);
                    orderAdaper.notifyDataSetChanged();
                }
                OrderItem_ViewHolder orderItemViewHolder = (OrderItem_ViewHolder) viewHolder;
                requestBuilder = Glide.with(context).load(R.drawable.icon_cancell);
                itemTaget = requestBuilder.into(orderItemViewHolder.ivIconCancelOrder_ItemOrderList);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        orderItemViewHolder.vInformationOrder_ItemOrderList.setVisibility(View.VISIBLE);
                        orderItemViewHolder.vCancellItemOrder_ItemOrderList.setVisibility(View.GONE);
                        orderAdaper.notifyDataSetChanged();
                    }
                };
                handler.postDelayed(runnable, 4000);
                orderAdaper.notifyDataSetChanged();
                orderItemViewHolder.vCancellItemOrder_ItemOrderList.setVisibility(View.VISIBLE);
                orderItemViewHolder.vInformationOrder_ItemOrderList.setVisibility(View.GONE);
                itemViewHolder = (OrderItem_ViewHolder) viewHolder;
                orderItemViewHolder.ivIconCancelOrder_ItemOrderList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn hủy đơn hàng này không ?");
                        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                OrderData itemOrder = arrayOrderData.get(position);
                                cancelItemOrder(itemOrder);
                                orderAdaper.notifyDataSetChanged();
                            }
                        });
                        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                orderAdaper.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            }
            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvOrderDelivering);
    }
    private void cancelItemOrder(OrderData orderData){
        orderData.setStatusOrder(5);
        databaseReference = firebaseDatabase.getReference("OrderProduct/" + orderData.getIdOrder());
        databaseReference.setValue(orderData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context,"Đã hủy đơn hàng",Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Hủy đơn hàng không thành công",Toast.LENGTH_SHORT);
            }
        });

    }

    private void setEvent() {

    }

    private void setControl(@NonNull View view) {
        rcvOrderDelivering = view.findViewById(R.id.rcvOrderDelivering);
        tvNoOrderDelivering = view.findViewById(R.id.tvNoOrderDelivering);
    }

    private void loadOrderItem(){
        databaseReference = firebaseDatabase.getReference("OrderCustomer/"+customerData.getId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayOrderData.clear();
                if(snapshot.exists()){
                    for (DataSnapshot itemIdOrder:
                            snapshot.getChildren()) {
                        String idOrderItem = itemIdOrder.getKey();
                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("OrderProduct/"+idOrderItem);
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    OrderData orderData = snapshot.getValue(OrderData.class);
                                    if (orderData.getStatusOrder() == 3){
                                        arrayOrderData.add(orderData);
                                    }
                                    else {
                                        arrayOrderData.removeIf(element->element.getIdOrder().equals(orderData.getIdOrder()));
                                    }
                                    Collections.reverse(arrayOrderData);
                                    orderAdaper.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}