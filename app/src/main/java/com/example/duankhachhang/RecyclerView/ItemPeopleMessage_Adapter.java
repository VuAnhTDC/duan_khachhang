package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ItemMessage;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.MessageActivity;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemPeopleMessage_Adapter extends RecyclerView.Adapter<ItemPropleMessage_ViewHolder> {
    ArrayList<String> arrIdShopMess = new ArrayList<>();
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private Customer customer = new Customer();

    public ItemPeopleMessage_Adapter(ArrayList<String> arrIdShopMessage, Context context) {
        this.arrIdShopMess = arrIdShopMessage;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
    }

    @NonNull
    @Override
    public ItemPropleMessage_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemPropleMessage_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_peoplemessage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPropleMessage_ViewHolder holder, int position) {
        String idItemShop = arrIdShopMess.get(position);
        setBackgroundInformationShopMessage(idItemShop, holder);
        contentMessage(idItemShop, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("idUser",idItemShop);
                context.startActivity(intent);
            }
        });
    }

    private void contentMessage(String idShop, ItemPropleMessage_ViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("Message/" + customer.getId() + "/" + idShop);
        databaseReference.orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item:
                         snapshot.getChildren()) {
                        databaseReference = firebaseDatabase.getReference("ItemMessage/"+item.getKey().toString());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ItemMessage itemMessage = snapshot.getValue(ItemMessage.class);
                                String nameTable = itemMessage.getIdSender().split("-")[1];
                                if (nameTable.equals("Shop")){
                                    holder.tvContent_CustomerItemUserMessage.setText(itemMessage.getContentMessage());
                                }
                                else {
                                    holder.tvContent_CustomerItemUserMessage.setText("Bạn: "+itemMessage.getContentMessage());
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

    private void setBackgroundInformationShopMessage(String idShop, ItemPropleMessage_ViewHolder holder) {
        System.out.println("asdf: " + idShop);
        databaseReference = firebaseDatabase.getReference("Shop/" + idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    System.out.println("s: " + shopData.toString());
                    if (shopData.getUrlImgShopAvatar().isEmpty()) {
                        holder.ivAvataUser_CustomerItemUserMessage.setImageResource(R.drawable.icon_personal);
                    } else {
                        Picasso.get().load(shopData.getUrlImgShopAvatar()).into(holder.ivAvataUser_CustomerItemUserMessage);
                    }
                    holder.tvNameUser_CustomerItemUserMessage.setText(shopData.getShopName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrIdShopMess.size();
    }
}
