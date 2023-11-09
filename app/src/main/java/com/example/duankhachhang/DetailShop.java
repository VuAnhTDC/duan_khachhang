package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.RecyclerView.ProductListHome_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailShop extends AppCompatActivity {

    CircleImageView ivAvataShop_DetailShop;
    TextView tvNameShop_DetailShop,tvAddressShop_DetailShop,tvAddressEmailShop_DetailShop,tvPhoneShop_DetailShop;
    RecyclerView rcvListProductBuy_DetailShop;
    Toolbar toolBar_DetailShop;
    ProductListHome_Adapter productListAdapter;
    ArrayList<ProductData> arrProduct = new ArrayList<>();
    String idShop  = "";
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);
        context = this;
        setControl();
        setIntiazation();
        getDataProduct_IdShop();
        getDataShop_IdShop();
        setEvent();
    }

    private void setIntiazation() {
// Khởi tạo
        productListAdapter = new ProductListHome_Adapter(arrProduct,context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rcvListProductBuy_DetailShop.setLayoutManager(gridLayoutManager);
        rcvListProductBuy_DetailShop.setAdapter(productListAdapter);

//        lấy id shop
        Intent intent = getIntent();
        idShop = intent.getStringExtra("idShop");

        setSupportActionBar(toolBar_DetailShop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getDataShop_IdShop(){
        databaseReference = firebaseDatabase.getReference("Shop/" + idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    tvNameShop_DetailShop.setText(shopData.getShopName());
                    tvAddressShop_DetailShop.setText("Địa chỉ shop: " +shopData.getShopAddress());
                    tvAddressEmailShop_DetailShop.setText("Email: "+shopData.getShopEmail());
                    tvPhoneShop_DetailShop.setText("SDT: " + shopData.getShopPhoneNumber());
                    Picasso.get().load(shopData.getUrlImgShopAvatar()).placeholder(R.drawable.icon_personal).into(ivAvataShop_DetailShop);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getDataProduct_IdShop(){
        databaseReference = firebaseDatabase.getReference("Product");
        Query query = databaseReference.orderByChild("idUserProduct").equalTo(idShop);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot productItem:
                         snapshot.getChildren()) {
                        ProductData productData = productItem.getValue(ProductData.class);
                        arrProduct.add(productData);
                        productListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lấy sản phẩm của cửa hàng bị lỗi: "+error.getMessage());
            }
        });
    }

    private void setEvent() {
    }

    private void setControl() {
        ivAvataShop_DetailShop = findViewById(R.id.ivAvataShop_DetailShop);
        tvNameShop_DetailShop = findViewById(R.id.tvNameShop_DetailShop);
        rcvListProductBuy_DetailShop = findViewById(R.id.rcvListProductBuy_DetailShop);
        toolBar_DetailShop = findViewById(R.id.toolBar_DetailShop);
        tvAddressShop_DetailShop = findViewById(R.id.tvAddressShop_DetailShop);
        tvAddressEmailShop_DetailShop = findViewById(R.id.tvAddressEmailShop_DetailShop);
        tvPhoneShop_DetailShop = findViewById(R.id.tvPhoneShop_DetailShop);
    }
}