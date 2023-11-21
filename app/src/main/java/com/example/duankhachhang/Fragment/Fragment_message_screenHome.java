package com.example.duankhachhang.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.ItemPeopleMessage_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_message_screenHome extends Fragment {
    private View view;
    private Customer customer = new Customer();
    RecyclerView rcvPeopleMessgaeList_PeopleMessgae;
    CircleImageView ivAvataCustoemr_Chat;
    TextView tvNameCustomer_Chat;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<String> arrIsShopMessage = new ArrayList<>();
    ItemPeopleMessage_Adapter itemPeopleMessageAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.framelayout_itemmessage_activity_screenhome,container,false);
        setControl();
        setIntiazation();
        getPeopleMessage();
        return view;
    }

    private void setIntiazation() {
//    Lấy giá trị customer
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);

//        gán gía trị cho recyclerview
        itemPeopleMessageAdapter = new ItemPeopleMessage_Adapter(arrIsShopMessage,getContext());
        rcvPeopleMessgaeList_PeopleMessgae.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvPeopleMessgaeList_PeopleMessgae.setAdapter(itemPeopleMessageAdapter);

        Picasso.get().load(customer.getImageUser()).into(ivAvataCustoemr_Chat);
        tvNameCustomer_Chat.setText(customer.getName());
    }

    private void getPeopleMessage(){
        databaseReference = firebaseDatabase.getReference("Message/"+customer.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrIsShopMessage.clear();
                if (snapshot.exists()){
                    for (DataSnapshot itemUser:
                         snapshot.getChildren()) {
                        arrIsShopMessage.add(itemUser.getKey());
                        itemPeopleMessageAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvPeopleMessgaeList_PeopleMessgae = view.findViewById(R.id.rcvPeopleMessgaeList_PeopleMessgae);
        ivAvataCustoemr_Chat = view.findViewById(R.id.ivAvataCustoemr_Chat);
        tvNameCustomer_Chat = view.findViewById(R.id.tvNameCustomer_Chat);
    }
}
