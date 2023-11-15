package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ItemMessage;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Message_Adapter extends RecyclerView.Adapter<Message_ViewHolder> {
    private ArrayList<String> arrIdItemMessage = new ArrayList<>();
    private Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public Message_Adapter(ArrayList<String> arrIdItemMessage, Context context) {
        this.arrIdItemMessage = arrIdItemMessage;
        this.context = context;
    }

    @NonNull
    @Override
    public Message_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Message_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Message_ViewHolder holder, int position) {
        String itemMessage = arrIdItemMessage.get(position);
        getItemMessage(itemMessage,holder);
    }

    private void getItemMessage(String idItemMessage,Message_ViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("ItemMessage" + idItemMessage);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ItemMessage itemMessage = snapshot.getValue(ItemMessage.class);
                    String nameTable = itemMessage.getIdSender().toString().split("-")[1];
                    String idUser = itemMessage.getIdSender().toString().split("-")[0];
                    if (nameTable.equals("Shop")){
                        holder.tvContentItem_CustomerItemMessage.setText(itemMessage.getContentMessage());
                        holder.vItemMessage.setGravity(Gravity.RIGHT);
                        holder.ivAvataUser_CustomerItemMessage.setVisibility(View.GONE);
                        holder.tvContentItem_CustomerItemMessage.setBackgroundResource(R.drawable.bg_tvcontent_user01);
                    } else if (nameTable.equals("Customer")) {
                        setInformationCustomer(idUser, holder);
                        holder.ivAvataUser_CustomerItemMessage.setVisibility(View.VISIBLE);
                        holder.tvContentItem_CustomerItemMessage.setText(itemMessage.getContentMessage());
                        holder.tvContentItem_CustomerItemMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        holder.vItemMessage.setGravity(Gravity.LEFT);
                        holder.tvContentItem_CustomerItemMessage.setBackgroundResource(R.drawable.bg_tvcontent_user02);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setInformationCustomer(String idCustomer, Message_ViewHolder holder) {
        databaseReference = firebaseDatabase.getReference("Customer/" + idCustomer);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    if (customer.getImageUser().isEmpty()) {
                        Picasso.get().load(R.drawable.icon_personal).into(holder.ivAvataUser_CustomerItemMessage);
                    } else {
                        Picasso.get().load(customer.getImageUser()).into(holder.ivAvataUser_CustomerItemMessage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrIdItemMessage.size();
    }
}
