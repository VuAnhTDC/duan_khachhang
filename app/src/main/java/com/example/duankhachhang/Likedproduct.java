package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.duankhachhang.RecyclerView.LikeProductAdapter;

import com.example.duankhachhang.Class.LikeProductData;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Likedproduct extends AppCompatActivity {

    MaterialToolbar topAppBar_like_product;
    RecyclerView rcv_Like_products;
    LikeProductAdapter likeProductAdapter;
    ArrayList<LikeProductData> list = new ArrayList<>();
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    String idCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_product);
        context = this;
        sharedPreferences = context.getSharedPreferences("informationUserCustomer",Context.MODE_PRIVATE);
        idCustomer = sharedPreferences.getString("numberphone","");
        System.out.println("id customer từ sharedPreferences ở màn hình chi sản phẩm đã thích: " + idCustomer);
        setControl();
        setIntiazational();
        setInformationLikeProducts(idCustomer);
        setEvent();
    }

    private void setIntiazational() {
        likeProductAdapter = new LikeProductAdapter(list,context);
        rcv_Like_products.setLayoutManager(new LinearLayoutManager(context));
        rcv_Like_products.setAdapter(likeProductAdapter);
        System.out.println("setIntiazational ");
    }
    private void setInformationLikeProducts(String idCustomer){
        databaseReference = firebaseDatabase.getReference("LikeProduct/"+idCustomer);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()){
                    for (DataSnapshot itemLike: snapshot.getChildren()){
                        LikeProductData likeProduct = itemLike.getValue(LikeProductData.class);
                       list.add(likeProduct);
                       likeProductAdapter.notifyDataSetChanged();
                    }
                }else {
                    System.out.println("không tìm thấy " + idCustomer );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi khi đọc dữ liệu từ cơ sở dữ liệu (các sản phẩm đã thích): " + error.getMessage());
            }
        });
    }


    private void setEvent() {
        topAppBar_like_product.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        topAppBar_like_product = findViewById(R.id.topAppBar_like_product);
        rcv_Like_products = findViewById(R.id.rcv_Like_products);
    }
}