package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Fragment.Fragment_home_screenHome;
import com.example.duankhachhang.Fragment.Fragment_message_screenHome;
import com.example.duankhachhang.Fragment.Fragment_personal_screenHome;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private Customer customer = new Customer();
    Context context;
    FrameLayout framelayout_screenhome;
    BottomNavigationView bottomNavigationBar_Home;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<ProductData> arrProduct = new ArrayList<>();
    ArrayList<String> arrUrl = new ArrayList<>();
    private int countUrl = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        customer = new Customer("0123456789", "demo address", "Demo", null);
        setControl();
        setEvent();
        loadingFragment(new Fragment_home_screenHome());
    }

    private void setEvent() {
        bottomNavigationBar_Home.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.itemHome_Navigationbar){
                    loadingFragment(new Fragment_home_screenHome());
                    return true;
                }
                else  if (item.getItemId() == R.id.itemMessage_Navigationbar){
                    loadingFragment(new Fragment_message_screenHome());
                    return true;
                }
                else if (item.getItemId() == R.id.itemPersonal_Navigationbar){
                    loadingFragment(new Fragment_personal_screenHome());
                    return true;
                }
                return false;
            }
        });

    }

    private void setControl() {
        framelayout_screenhome = findViewById(R.id.framelayout_screenhome);
        bottomNavigationBar_Home = findViewById(R.id.bottomNavigationBar_Home);
    }

    private void loadingFragment(Fragment fragmentLoading){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(framelayout_screenhome.getId(),fragmentLoading);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    private void getDataProduct(){
//        databaseReference = firebaseDatabase.getReference("Product");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                Fragment_home_screenHome fragmentHomeScreenHome = new Fragment_home_screenHome();
////                loadingFragment(fragmentHomeScreenHome);
//                if (snapshot.exists()) {
//                    for (DataSnapshot productItem : snapshot.getChildren()) {
//                        ProductData productData = productItem.getValue(ProductData.class);
//                        arrProduct.add(productData);
//                        loadingFragment(new Fragment_home_screenHome(arrProduct));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                System.out.println("Lối lấy danh sách sản phẩm");
//            }
//        });
//    }

}