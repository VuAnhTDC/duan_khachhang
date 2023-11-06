package com.example.duankhachhang;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.example.duankhachhang.Adapter.ProductBanerAdapter;
import com.example.duankhachhang.Class.Category;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.LikeProductData;
import com.example.duankhachhang.Class.Manuface;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Dialog.CommentProductDialogFragment;
import com.example.duankhachhang.RecyclerView.UriImageFirebase_ViewHolder;
import com.example.duankhachhang.RecyclerView.UrlImageFirebase_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Detailproduct extends AppCompatActivity {
    Context context;
    Toolbar toolbar;
    ViewPager viewPagerImageProduct_DetailProduct;
    ImageView ivChatShop_DetailProduct,ivLikeProduct_DetailProduct,ivAddCartProduct_Detail;
    TextView tvNameProduct_DetailProduct,tvPriceProduct_DetailProduct,tvquanlityProduct_DetailProduct,tvDescriptionProduct_DetailProduct,tvQuanlityLike_DetailProduct,tvQuanlityCmt_DetailProduct;
    Button btnBuyProduct_DetailProduct,btnAddCart_DetailProduct;
    LinearLayout vAvataShop_DetailProduct,vSumlike_DetailProduct,vSumCmt_DetailProduct;
    private ProductData productData = new ProductData();
    private ProductBanerAdapter productBanerAdapter;
    private ArrayList<String> arrUrlImage = new ArrayList<>();
    private boolean isClickLike = false;
    private Customer customer = new Customer();
    private String idProduct = "";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailproduct);
        context = this;
        customer = new Customer("0123456789", "demo address", "Demo", null);
        setControl();
        setIniazation();
        getDataProduct();
        checkLikeProductOfCustomer();
        setEvent();
    }

    private void getDataProduct() {
        databaseReference = firebaseDatabase.getReference("Product");
        Query query = databaseReference.orderByChild("idProduct").equalTo(idProduct);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot productItem:
                         snapshot.getChildren()) {
                        productData = productItem.getValue(ProductData.class);
                        getImageProduct();
                        getInformationProduct();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    hàm khởi tạo
    private void setIniazation() {
        Intent intent = getIntent();
        idProduct = intent.getStringExtra("idProduct");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        khởi tạo productbanner
        productBanerAdapter = new ProductBanerAdapter(arrUrlImage,context);
        viewPagerImageProduct_DetailProduct.setAdapter(productBanerAdapter);
    }

    private void getImageProduct(){
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(productData.getIdProduct());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot imageItem:
                         snapshot.getChildren()) {
                        Image image = imageItem.getValue(Image.class);
                        arrUrlImage.add(image.getUrlImage());
                        productBanerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi lấy hình ảnh trong chi tiết sản phẩm");
            }
        });
    }



    private void checkLikeProductOfCustomer(){
        databaseReference = firebaseDatabase.getReference("LikeProduct");
        Query query = databaseReference.child(customer.getId()+idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    isClickLike = true;
                    ivLikeProduct_DetailProduct.setImageResource(R.drawable.icon_love);
                    vSumCmt_DetailProduct.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getInformationProduct(){
        tvNameProduct_DetailProduct.setText(productData.getNameProduct());
        tvPriceProduct_DetailProduct.setText(productData.getPriceProduct() + " VND");
        if (productData.getQuanlityProduct() > 0){
            tvquanlityProduct_DetailProduct.setText("Số lượng: " + productData.getQuanlityProduct());
        }
        else {
            tvquanlityProduct_DetailProduct.setText("Sản phẩm hết hàng");
        }
        tvDescriptionProduct_DetailProduct.setText(productData.getDescriptionProduct());
        tvQuanlityLike_DetailProduct.setText(productData.getSumLike() + "");
        tvQuanlityCmt_DetailProduct.setText(productData.getOverageCmtProduct() + "");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //    hàm ánh xạ
    private void setControl() {
        toolbar = findViewById(R.id.toolBar_DetalProduct);
        viewPagerImageProduct_DetailProduct = findViewById(R.id.viewPagerImageProduct_DetailProduct);
        ivChatShop_DetailProduct = findViewById(R.id.ivChatShop_DetailProduct);
        vAvataShop_DetailProduct = findViewById(R.id.vAvataShop_DetailProduct);
        vSumlike_DetailProduct = findViewById(R.id.vSumlike_DetailProduct);
        vSumCmt_DetailProduct = findViewById(R.id.vSumCmt_DetailProduct);
        tvNameProduct_DetailProduct = findViewById(R.id.tvNameProduct_DetailProduct);
        tvPriceProduct_DetailProduct = findViewById(R.id.tvPriceProduct_DetailProduct);
        tvquanlityProduct_DetailProduct = findViewById(R.id.tvquanlityProduct_DetailProduct);
        tvDescriptionProduct_DetailProduct = findViewById(R.id.tvDescriptionProduct_DetailProduct);
        btnAddCart_DetailProduct =  findViewById(R.id.btnAddCart_DetailProduct);
        btnBuyProduct_DetailProduct =  findViewById(R.id.btnBuyProduct_DetailProduct);
        tvQuanlityLike_DetailProduct = findViewById(R.id.tvQuanlityLike_DetailProduct);
        tvQuanlityCmt_DetailProduct = findViewById(R.id.tvQuanlityCmt_DetailProduct);
        ivLikeProduct_DetailProduct = findViewById(R.id.ivLikeProduct_DetailProduct);
    }
    //    hàm bắt sự kiện
    private void setEvent() {
        vSumlike_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vSumlike_DetailProduct.setEnabled(false);
                isClickLike = !isClickLike;
                if (!isClickLike){
                    ivLikeProduct_DetailProduct.setImageResource(R.drawable.icon_nolove);
                    databaseReference = firebaseDatabase.getReference("LikeProduct");
                   databaseReference.child(customer.getId()+productData.getIdProduct()).removeValue();
                    productData.setSumLike(productData.getSumLike()-1);
                    databaseReference = firebaseDatabase.getReference("Product");
                    databaseReference.child(productData.getIdProduct()).setValue(productData);
                }
                else {
                    RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(R.drawable.icon_love_animation);
                    Target<Drawable> target = requestBuilder.into(ivLikeProduct_DetailProduct);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(context).clear(target);
                            ivLikeProduct_DetailProduct.setImageResource(R.drawable.icon_love);
                        }
                    },1000);
                    databaseReference = firebaseDatabase.getReference("LikeProduct");
                    LikeProductData likeProductData = new LikeProductData(customer.getId(),productData.getIdProduct());
                    databaseReference.child(customer.getId()+productData.getIdProduct()).setValue(likeProductData);
                    productData.setSumLike(productData.getSumLike()+1);
                    databaseReference = firebaseDatabase.getReference("Product");
                    databaseReference.child(productData.getIdProduct()).setValue(productData);
                }
                vSumlike_DetailProduct.setEnabled(true);
            }
        });
        vSumCmt_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentProductDialogFragment commentProductDialogFragment = new CommentProductDialogFragment(idProduct);
                commentProductDialogFragment.show(getSupportFragmentManager(),"Đánh giá");
            }
        });
        btnAddCart_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}