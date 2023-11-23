package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorBoundsInfo;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duankhachhang.Adapter.ProductBanerAdapter;
import com.example.duankhachhang.Class.CommentData;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.RecyclerView.CommentProduct_Adapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class InputCommentProduct extends AppCompatActivity {

    Toolbar toolBarCommentProduct;
    CircleImageView ivAvataShop_CmtProduct;
    TextView tvNameShop_CmtProduct,tvNameProduct,tvSumLove,tvSumCmt;
    ViewPager viewPagerImageProduct_CmtProduct;
    RecyclerView rcvCmtBuyer;
    EditText edtCmtUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ProductBanerAdapter productBanerAdapter;
    CommentProduct_Adapter commentProductAdapter;
    Context context;
    private OrderData orderData = new OrderData();
    private ArrayList<String> arrUrlImageProduct = new ArrayList<>();
    private ArrayList<CommentData> arrCommentData = new ArrayList<>();
    private int sumLoveProduct = 0;
    private int sumCmtProduct = 0;
    private Customer customer = new Customer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_comment_product);
        context = this;
        setControl();
        setIntiazation();
        getInformationProduct();
        getInformationShop();
        getImageProduct();
        getCommnetProduct();
        setEvent();
    }

    private void setEvent() {
        edtCmtUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    if (edtCmtUser.getText().toString() != null && !edtCmtUser.getText().toString().isEmpty()){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                        Date currentDate = new Date();
                        String currentTime = dateFormat.format(currentDate);
                        CommentData commentData = new CommentData(customer.getId(),orderData.getIdProduct_Order(),edtCmtUser.getText().toString(),currentTime);
                        databaseReference = firebaseDatabase.getReference("CommentProduct/"+orderData.getIdProduct_Order()+"/"+customer.getId());
                        databaseReference.setValue(commentData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                                làm rỗng giá trị edt commnent
                                edtCmtUser.setText("");
                            }
                        });
                    }
                }
                return false;
            }
        });
    }

    private void setControl() {
        toolBarCommentProduct = findViewById(R.id.toolBarCommentProduct);
        ivAvataShop_CmtProduct = findViewById(R.id.ivAvataShop_CmtProduct);
        tvNameShop_CmtProduct = findViewById(R.id.tvNameShop_CmtProduct);
        tvNameProduct = findViewById(R.id.tvNameProduct);
        viewPagerImageProduct_CmtProduct = findViewById(R.id.viewPagerImageProduct_CmtProduct);
        rcvCmtBuyer = findViewById(R.id.rcvCmtBuyer);
        edtCmtUser = findViewById(R.id.edtCmtUser);
        tvSumLove = findViewById(R.id.tvSumLove);
        tvSumCmt = findViewById(R.id.tvSumCmt);

    }

    private void setIntiazation() {
        Intent intent= getIntent();
        orderData = (OrderData)intent.getSerializableExtra("orderData");

//        kích hoạt nút back
        setSupportActionBar(toolBarCommentProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productBanerAdapter = new ProductBanerAdapter(arrUrlImageProduct,context);
        viewPagerImageProduct_CmtProduct.setAdapter(productBanerAdapter);

//        gán giá trị cho comment adapter
        commentProductAdapter = new CommentProduct_Adapter(context,arrCommentData);
        rcvCmtBuyer.setLayoutManager(new LinearLayoutManager(context));
        rcvCmtBuyer.setAdapter(commentProductAdapter);

        //        Lấy thông tin người dùng bến trong Preference
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
//        Gán giá trị mới biến customer
        customer = gson.fromJson(jsonShop, Customer.class);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCommnetProduct(){
        databaseReference = firebaseDatabase.getReference("CommentProduct/" + orderData.getIdProduct_Order());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrCommentData.clear();
                sumCmtProduct = 0;
                for (DataSnapshot itemSnap:
                     snapshot.getChildren()) {
                    System.out.println("item snap: " + itemSnap);
                    CommentData commentData = itemSnap.getValue(CommentData.class);
                    arrCommentData.add(commentData);
                    sumCmtProduct ++;
                    commentProductAdapter.notifyDataSetChanged();
                }
                databaseReference = firebaseDatabase.getReference("Product/"+orderData.getIdShop_Order()+"/"+orderData.getIdProduct_Order());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            ProductData productData = snapshot.getValue(ProductData.class);
                            productData.setOverageCmtProduct(sumCmtProduct);
                            databaseReference.setValue(productData);
                            tvSumLove.setText(productData.getSumLike() +"");
                            tvSumCmt.setText(productData.getOverageCmtProduct() +" đánh giá");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImageProduct(){
        databaseReference = firebaseDatabase.getReference("ImageProducts/" + orderData.getIdProduct_Order());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot itemSnap:
                         snapshot.getChildren()) {
                        String urlImageProduct = itemSnap.child("urlImage").getValue().toString();
                        arrUrlImageProduct.add(urlImageProduct);
                        productBanerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getInformationProduct(){
        databaseReference = firebaseDatabase.getReference("Product/"+orderData.getIdShop_Order() +"/" + orderData.getIdProduct_Order());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    tvNameProduct.setText(productData.getNameProduct());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getInformationShop(){
        databaseReference = firebaseDatabase.getReference("Shop/"+orderData.getIdShop_Order());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    tvNameShop_CmtProduct.setText(shopData.getShopName());
                   if(shopData.getUrlImgShopAvatar().isEmpty()){
                       Picasso.get().load(shopData.getUrlImgShopAvatar()).into(ivAvataShop_CmtProduct);
                   }
                   else {
                       ivAvataShop_CmtProduct.setImageResource(R.drawable.icon_personal);
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}