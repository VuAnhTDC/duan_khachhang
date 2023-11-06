package com.example.duankhachhang.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.duankhachhang.Adapter.ProductBanerAdapter;
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
    Context context;
    ViewPager viewPagerProductBanner_Home;
    RecyclerView rcvProductList_Home;
    private int count_Notification = 0;
    private int countUrl = 0;
    private int locationUrlImageProduct_ItemViewPager = 0;
    private int timeDeplay = 2000;
    private final Handler handler = new Handler();
    ProductBanerAdapter productBanerAdapter;

    public Fragment_home_screenHome() {
    }

    ArrayList<ProductData> arrProduct = new ArrayList<>();
    ProductListHome_Adapter productListHomeAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    ArrayList<String> arrUrl = new ArrayList<>();
    private View view;

    public Fragment_home_screenHome(ArrayList<ProductData> arrProduct){
        this.arrProduct = arrProduct;
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
//        getProductData();
        getLoadingUrlImageProduct(5);
        setEvent();
        locationUrlImageProduct_ItemViewPager = 0;
        return view;
    }
    private void setIntiazation() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        productListHomeAdapter = new ProductListHome_Adapter(arrProduct, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rcvProductList_Home.setLayoutManager(gridLayoutManager);
        rcvProductList_Home.setAdapter(productListHomeAdapter);

        productBanerAdapter = new ProductBanerAdapter(arrUrl,context);
        viewPagerProductBanner_Home.setAdapter(productBanerAdapter);
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

    public  void  setLocationUrlImageProduct_ItemViewPager_return(){
        this.locationUrlImageProduct_ItemViewPager = 0;
    }

    public void setArrProduct(ProductData product){
            this.arrProduct.add(product);
            productListHomeAdapter.notifyDataSetChanged();
    }

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
                    System.out.println("size arr url: " + productBanerAdapter.getCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProductData() {
        databaseReference = firebaseDatabase.getReference("Product");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productItem : snapshot.getChildren()) {
                        ProductData productData = productItem.getValue(ProductData.class);
                        arrProduct.add(productData);
                    }
                    productListHomeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
    }

    private void setControl() {
        rcvProductList_Home = view.findViewById(R.id.rcvProductList_Home);
        viewPagerProductBanner_Home = view.findViewById(R.id.viewPagerProductBanner_Home);
    }


}
