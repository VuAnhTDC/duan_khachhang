package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.Class.Voucher;
import com.example.duankhachhang.RecyclerView.ProductListHome_Adapter;
import com.example.duankhachhang.RecyclerView.VoucherItemDetailShop_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailShop extends AppCompatActivity {

    CircleImageView ivAvataShop_DetailShop;
    TextView tvNameShop_DetailShop, tvAddressShop_DetailShop, tvAddressEmailShop_DetailShop, tvPhoneShop_DetailShop, tvLinkFacebook_DetailShop;
    RecyclerView rcvListProductBuy_DetailShop,rcvMyVoucher_Shop;
    Toolbar toolBar_DetailShop;
    ProductListHome_Adapter productListAdapter;
    LinearLayout vFaceBook_DetailShop;
    ArrayList<ProductData> arrProduct = new ArrayList<>();
    ArrayList<Voucher> arrVoucher = new ArrayList<>();
    VoucherItemDetailShop_Adapter voucherItemAdapter;
    String idShop = "";
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    SwipeRefreshLayout swipFresh;
    private String linkFaceBookShop = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_shop);
        context = this;
        setControl();
        setIntiazation();
        getDataProduct_IdShop();
        getDataShop_IdShop();
        getLinkFacebook_Shop();
        getVoucherShop();
        setEvent();
    }

    private void setIntiazation() {
// Khởi tạo
        productListAdapter = new ProductListHome_Adapter(arrProduct, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rcvListProductBuy_DetailShop.setLayoutManager(gridLayoutManager);
        rcvListProductBuy_DetailShop.setAdapter(productListAdapter);

//        lấy id shop
        Intent intent = getIntent();
        idShop = intent.getStringExtra("idShop");

//        Gán giá trị cho VoucherAdapter
        voucherItemAdapter = new VoucherItemDetailShop_Adapter(arrVoucher,context);
        rcvMyVoucher_Shop.setLayoutManager(new LinearLayoutManager(context));
        rcvMyVoucher_Shop.setAdapter(voucherItemAdapter);
        setSupportActionBar(toolBar_DetailShop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    hàm lấy danh sach voucher của shop
    private void getVoucherShop(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Voucher/"+idShop);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot itemProduct:
                            snapshot.getChildren()) {
                        for (DataSnapshot itemVoucherOfProduct:
                                itemProduct.getChildren()) {
                            Voucher itemVoucher = itemVoucherOfProduct.getValue(Voucher.class);
                            arrVoucher.add(itemVoucher);
                            voucherItemAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataShop_IdShop() {
        databaseReference = firebaseDatabase.getReference("Shop/" + idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    tvNameShop_DetailShop.setText(shopData.getShopName());
                    tvAddressShop_DetailShop.setText("Địa chỉ shop: " + shopData.getShopAddress());
                    tvAddressEmailShop_DetailShop.setText("Email: " + shopData.getShopEmail());
                    tvPhoneShop_DetailShop.setText("SDT: " + shopData.getShopPhoneNumber());
                    Picasso.get().load(shopData.getUrlImgShopAvatar()).placeholder(R.drawable.icon_personal).into(ivAvataShop_DetailShop);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //    lấy linkfacebook cửa hàng
    private void getLinkFacebook_Shop() {
        databaseReference = firebaseDatabase.getReference("Accountlink/" + idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    linkFaceBookShop = snapshot.child("facebook").getValue().toString();
                    tvLinkFacebook_DetailShop.setText(linkFaceBookShop);

                } else {
                    linkFaceBookShop = "";
                    tvLinkFacebook_DetailShop.setText("Chưa liên kết FaceBook");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataProduct_IdShop() {
        databaseReference = firebaseDatabase.getReference("Product/"+idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    arrProduct.clear();
                    for (DataSnapshot productItem :
                            snapshot.getChildren()) {
                        ProductData productData = productItem.getValue(ProductData.class);
                        arrProduct.add(productData);
                        productListAdapter.notifyDataSetChanged();
                    }
                } else {
                    arrProduct.clear();
                }
                swipFresh.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lấy sản phẩm của cửa hàng bị lỗi: " + error.getMessage());
            }
        });
    }

    private void setEvent() {
        swipFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataProduct_IdShop();
            }
        });
        tvLinkFacebook_DetailShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!linkFaceBookShop.isEmpty()) {
                    try {
                        // Kiểm tra xem ứng dụng Facebook đã được cài đặt chưa
                        PackageManager packageManager = context.getPackageManager();
                        packageManager.getPackageInfo("com.facebook.katana", 0);

                        // Tạo URI deeplink để mở trang cá nhân của người dùng trong ứng dụng Facebook
                        Uri uri = Uri.parse("fb://page/683123653912697/");

                        // Tạo Intent và mở ứng dụng Facebook
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        // Nếu có lỗi (chưa cài đặt Facebook hoặc có lỗi khác), mở trình duyệt để xem trang cá nhân trên web
                        String userProfileLink = "https://www.facebook.com/" + "683123653912697";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userProfileLink));
                        intent.setPackage("com.android.chrome");

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // Mở trang web trong trình duyệt Chrome
                            startActivity(intent);
                        } else {
                            // Nếu trình duyệt Chrome không khả dụng, mở trang web trong trình duyệt mặc định
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(userProfileLink)));
                        }
                    }
                }
            }
        });
    }

    private void setControl() {
        ivAvataShop_DetailShop = findViewById(R.id.ivAvataShop_DetailShop);
        tvNameShop_DetailShop = findViewById(R.id.tvNameShop_DetailShop);
        rcvListProductBuy_DetailShop = findViewById(R.id.rcvListProductBuy_DetailShop);
        toolBar_DetailShop = findViewById(R.id.toolBar_DetailShop);
        tvAddressShop_DetailShop = findViewById(R.id.tvAddressShop_DetailShop);
        tvAddressEmailShop_DetailShop = findViewById(R.id.tvAddressEmailShop_DetailShop);
        tvPhoneShop_DetailShop = findViewById(R.id.tvPhoneShop_DetailShop);
        swipFresh = findViewById(R.id.swipFresh);
        vFaceBook_DetailShop = findViewById(R.id.vFaceBook_DetailShop);
        tvLinkFacebook_DetailShop = findViewById(R.id.tvLinkFacebook_DetailShop);
        rcvMyVoucher_Shop = findViewById(R.id.rcvMyVoucher_Shop);
    }
}