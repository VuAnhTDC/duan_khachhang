package com.example.duankhachhang.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.duankhachhang.Adapter.ProductBanerAdapter;
import com.example.duankhachhang.CartCustomer;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.ProductListHome_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment_home_screenHome extends Fragment {
//    Biến lưu giữ thông tin người dùng app
    private Customer customer = new Customer();
//    khai bao biến giao diện
    TextView tvCountProductCart_FragmentHome,tvCountNotification_FragmentHome;
    Context context;
    ViewPager viewPagerProductBanner_Home;
    RecyclerView rcvProductList_Home;
    FrameLayout vCountCartCustomer_FragmentHome;
    SwipeRefreshLayout swipRefresh_FragmentHome;

//    Khai báo biến quản lý
    private int countUrl = 0;
    private int locationUrlImageProduct_ItemViewPager = 0;
    private int timeDeplay = 2000;
    private int countProductInCart = 0;
    private final Handler handler = new Handler();
    public static final String CHANEL_ID = "notificationnormal";
    ProductBanerAdapter productBanerAdapter;
    ArrayList<ProductData> arrProduct = new ArrayList<>();
    ProductListHome_Adapter productListHomeAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<String> arrUrlBanner = new ArrayList<>();
    private View view;
    public Fragment_home_screenHome(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.framelayout_itemhome_activity_screenhome,container,false);
//        Intent intent = getIntent();
//        customer = (Customer)intent.getSerializableExtra("informationCustomer");

//        Cấp dữ liệu ảo
        customer = new Customer("0123456789", "demo address", "Demo", null);
//        gán giá trị cho biến context
        context = getContext();
//        gọi hàm ánh xạ
        setControl();
//        gọi hàm khởi tạo
        setIntiazation();
//        set title số lượng sản phẩm giỏ hàng
        setCountProductCartUser();
//         lấy danh sách sản phẩm trên firebase về
        getProductData();
        getLoadingUrlImageProduct(5);
//        gọi hàm bắt sự kiện
        setEvent();
//        Gán giá trị cho biến số lượng hình ảnh ở banner
        locationUrlImageProduct_ItemViewPager = 0;
        createChanelNotification();
        return view;
    }
    private void createChanelNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID,"pushNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

//    hàm lấy số lượng sản phẩm trong giỏ hàng --- người dùng
    private void setCountProductCartUser(){
//        Truy cập bảng giỏ hàng vào node người dùng trên firebase
        databaseReference = firebaseDatabase.getReference("CartCustomer/" + customer.getId());
//        lắng nghe sự kiện
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                kiểm tra giỏ hàng người dùng có tồn tại không ---> nếu tồn tại
                if (snapshot.exists()){
//                    gán giá trị biến đếm số lượng sản phẩm trong giỏ hàng người dùng
                    countProductInCart = 0;
//                    duyệt danh sách giỏ hàng người dùng
                    for (DataSnapshot itemCart:
                         snapshot.getChildren()) {
//                        Tăng số lượng biến đếm lên 1 đơn vị
                       countProductInCart ++;
                    }
//                    nếu biến đếm số lượng hàng trong giỏ hàng người dùng > 0
                    if (countProductInCart > 0){
//                        hiển thị số lượng hàng hóa trong giỏ hàng
                        tvCountProductCart_FragmentHome.setText(countProductInCart+"");
                        tvCountProductCart_FragmentHome.setVisibility(View.VISIBLE);
                    }

                }
//                ngược lại
                else {
//                    ẩn số lượng giỏ hàng
                    tvCountProductCart_FragmentHome.setVisibility(View.GONE);
                }
            }

//            Lỗi trong qua trình lấy dữ liệu
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Xuất lỗi dưới dạng logcat
                System.out.println("Lỗi đếm số lượng sản phẩm trong giỏ hàng người dùng -- FrangmentHome: " + error.getMessage());
            }
        });
    }

//    hàm khởi tạo
    private void setIntiazation() {
//        Gán giá trị cho biến ProductListHomeAdapter ----> sử dụng với RecyclerView
        productListHomeAdapter = new ProductListHome_Adapter(arrProduct, context);
//        Thiết lập số cột hiển thị mỗi hàng (Thiết lập là 2)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
//        thiết lậo layoutManager cho RecyclerView
        rcvProductList_Home.setLayoutManager(gridLayoutManager);
//        cập giá trị adapter cho RecyclerView
        rcvProductList_Home.setAdapter(productListHomeAdapter);


//        Gán giá trị Adapter cho banner
        productBanerAdapter = new ProductBanerAdapter(arrUrlBanner,context);
//        Thiết lập Adapter cho ViewPager
        viewPagerProductBanner_Home.setAdapter(productBanerAdapter);
//        Kiểm tra số lượng sản phẩm trong giỏ hàng
        if (countProductInCart == 0){
//            ẩn title số lượng sản phẩm trong giỏ hàng nếu trong giỏ hàng người dùng không chứa sản phẩm nào
            tvCountProductCart_FragmentHome.setVisibility(View.GONE);
        }
    }

//    hàm set thời gian để chạy từng item trong Viewpager
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
//    hàm load hình ảnh lên banner
    private void getLoadingUrlImageProduct(int count){
        databaseReference = firebaseDatabase.getReference("BanerData");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot imageItem: snapshot.getChildren()) {
                        arrUrlBanner.add(imageItem.getValue().toString());
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

//    hàm lấy dữ liệu trên firebase về (Sản phẩm)
    private void getProductData() {
//        Truy cập đến bảng Product trên firebase
        databaseReference = firebaseDatabase.getReference("Product");
//        Lắng nghe sự kiện
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                làm rỗng danh sách sản phẩm trước khi thêm vào
                arrProduct.clear();
//                Kiếm tra bảng trồn tại không ---> Nếu tồn tại
                if (snapshot.exists()) {
//                    Duyệt từng item trong bảng đó
                    for (DataSnapshot productItem : snapshot.getChildren()) {
//                        Lấy thông tin item trong bảng Product
                        ProductData productData = productItem.getValue(ProductData.class);
//                        Thêm product vừa lấy được vào danh sách product
                        arrProduct.add(productData);
                        Collections.reverse(arrProduct);
//                        Cập nhật lại product vừa lấy vào RecyclerView
                        productListHomeAdapter.notifyDataSetChanged();
                    }
                }
                swipRefresh_FragmentHome.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    hàm bắt sự kiện
    private void setEvent() {
//        Bắt sự kiện nhấn vào view giỏ hàng
        vCountCartCustomer_FragmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CartCustomer.class);
                startActivity(intent);
            }
        });
        swipRefresh_FragmentHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProductData();
            }
        });
    }

//    hàm ánh xạ
    private void setControl() {
        rcvProductList_Home = view.findViewById(R.id.rcvProductList_Home);
        viewPagerProductBanner_Home = view.findViewById(R.id.viewPagerProductBanner_FragmentHome);
        tvCountProductCart_FragmentHome = view.findViewById(R.id.tvCountProductCart_FragmentHome);
        tvCountNotification_FragmentHome = view.findViewById(R.id.tvCountNotification_FragmentHome);
        vCountCartCustomer_FragmentHome = view.findViewById(R.id.vCountCartCustomer_FragmentHome);
        swipRefresh_FragmentHome = view.findViewById(R.id.swipRefresh_FragmentHome);
    }


}
