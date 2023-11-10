package com.example.duankhachhang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.duankhachhang.Adapter.ProductBanerAdapter;
import com.example.duankhachhang.CartCustomer;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.ProductListHome_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_home_screenHome extends Fragment {
    private Customer customer = new Customer();
    TextView tvCountProductCart_FragmentHome,tvCountNotification_FragmentHome;
    Context context;
    ViewPager viewPagerProductBanner_Home;
    RecyclerView rcvProductList_Home;
    FrameLayout vCountCartCustomer_FragmentHome;
    private int countUrl = 0;
    private int locationUrlImageProduct_ItemViewPager = 0;
    private int timeDeplay = 2000;
    private int countProductInCart = 0;
    private final Handler handler = new Handler();
    ProductBanerAdapter productBanerAdapter;
    ArrayList<ProductData> arrProduct = new ArrayList<>();
    ProductListHome_Adapter productListHomeAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<String> arrUrl = new ArrayList<>();
    private View view;
    public Fragment_home_screenHome(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.framelayout_itemhome_activity_screenhome,container,false);
//        Intent intent = getIntent();
//        customer = (Customer)intent.getSerializableExtra("informationCustomer");
        customer = new Customer("0123456789", "demo address", "Demo", null);
        context = getContext();
        setControl();
        setIntiazation();
        setCountProductCartUser();
        getProductData();
        getLoadingUrlImageProduct(5);
        setEvent();
        locationUrlImageProduct_ItemViewPager = 0;
        return view;
    }

    private void setCountProductCartUser(){
        databaseReference = firebaseDatabase.getReference("CartCustomer/" + customer.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    countProductInCart = 0;
                    for (DataSnapshot itemCart:
                         snapshot.getChildren()) {
                       countProductInCart ++;
                    }
                    if (countProductInCart > 0){
                        tvCountProductCart_FragmentHome.setText(countProductInCart+"");
                        tvCountProductCart_FragmentHome.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    tvCountProductCart_FragmentHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setIntiazation() {
        productListHomeAdapter = new ProductListHome_Adapter(arrProduct, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rcvProductList_Home.setLayoutManager(gridLayoutManager);
        rcvProductList_Home.setAdapter(productListHomeAdapter);

        productBanerAdapter = new ProductBanerAdapter(arrUrl,context);
        viewPagerProductBanner_Home.setAdapter(productBanerAdapter);
        if (countProductInCart == 0){
            tvCountProductCart_FragmentHome.setVisibility(View.GONE);
        }
    }
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(productBanerAdapter.getCount() < 5 && locationUrlImageProduct_ItemViewPager > productBanerAdapter.getCount()){
                locationUrlImageProduct_ItemViewPager = 0;
            }
            if (locationUrlImageProduct_ItemViewPager == productBanerAdapter.getCount()){
                locationUrlImageProduct_ItemViewPager = 0;
            }
            viewPagerProductBanner_Home.setCurrentItem(locationUrlImageProduct_ItemViewPager,true);
            locationUrlImageProduct_ItemViewPager ++;
            handler.postDelayed(this,timeDeplay);
        }
    };


    private void getLoadingUrlImageProduct(int count){
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot imageItem: snapshot.getChildren()) {
                        if (countUrl != count){
                            Image image = imageItem.getValue(Image.class);
                            arrUrl.add(image.getUrlImage());
                            countUrl += 1;
                        }
                        else {
                            return;
                        }
                    }
                    productBanerAdapter.notifyDataSetChanged();
                    handler.postDelayed(runnable,timeDeplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductData() {
        databaseReference = firebaseDatabase.getReference("Product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrProduct.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot productItem : snapshot.getChildren()) {
                        ProductData productData = productItem.getValue(ProductData.class);
                        arrProduct.add(productData);
                        productListHomeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        vCountCartCustomer_FragmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CartCustomer.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        rcvProductList_Home = view.findViewById(R.id.rcvProductList_Home);
        viewPagerProductBanner_Home = view.findViewById(R.id.viewPagerProductBanner_FragmentHome);
        tvCountProductCart_FragmentHome = view.findViewById(R.id.tvCountProductCart_FragmentHome);
        tvCountNotification_FragmentHome = view.findViewById(R.id.tvCountNotification_FragmentHome);
        vCountCartCustomer_FragmentHome = view.findViewById(R.id.vCountCartCustomer_FragmentHome);
    }


}
