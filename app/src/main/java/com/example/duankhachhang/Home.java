    package com.example.duankhachhang;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;

    import android.content.Context;
    import android.content.SharedPreferences;
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
    import com.google.gson.Gson;

    import java.util.ArrayList;

    public class Home extends AppCompatActivity {
    //    Biến thông tin người dùng
        private Customer customer = new Customer();
        Context context;
        FrameLayout framelayout_screenhome;
        BottomNavigationView bottomNavigationBar_Home;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;

    //    Khai báo Fragment
        Fragment_home_screenHome fragmentHomeScreenHome = new Fragment_home_screenHome();
        Fragment_message_screenHome fragmentMessageScreenHome = new Fragment_message_screenHome();
        Fragment_personal_screenHome fragmentPersonalScreenHome = new Fragment_personal_screenHome();

    //    Biến đếm số lượng hình ảnh cho phép
        private int countBanner = 1;
        private boolean loadScreen = true;

      //  hàm tạo
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            context = this;
    //        gọi hàm ánh xạ
            setControl();
            //        Lấy thông tin người dùng bến trong Preference
            SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
            String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
            Gson gson = new Gson();
    //        Gán giá trị mới biến customer
            customer = gson.fromJson(jsonShop, Customer.class);
            getDataProduct();
    //        Gọi hàm lây thông tin banner từ firebase ---> và số lượng lấy là 5
            getDataBanner(5);
            //        Gọi hàm lấy thông tin product từ firebase
            setEvent();
    //        Load Fragment Home khi chạy dự án lần đầu tiên
            loadingFragment(fragmentHomeScreenHome);
        }

        //    hàm bắt sự kiện
        private void setEvent() {

    //        Sự kiện bottom navigation bar
            bottomNavigationBar_Home.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //                Nếu nhấn váo item home của bottom navigation bar
                    if(item.getItemId() == R.id.itemHome_Navigationbar){
    //                    load Fragment home
                        loadingFragment(fragmentHomeScreenHome);
                        return true;
                    }
    //                Nếu nhấn vào item message của bottom navigation bar
                    else  if (item.getItemId() == R.id.itemMessage_Navigationbar){
    //                    load Fragment massage
                        loadingFragment(fragmentMessageScreenHome);
                        return true;
                    }
    //                Nếu nhấn vào item personal của bottom navigation bar
                    else if (item.getItemId() == R.id.itemPersonal_Navigationbar){
    //                    load Fragment personal
                        loadingFragment(fragmentPersonalScreenHome);
                        return true;
                    }
                    return false;
                }
            });

        }

    //    hàm ánh xạ
        private void setControl() {
            framelayout_screenhome = findViewById(R.id.framelayout_screenhome);
            bottomNavigationBar_Home = findViewById(R.id.bottomNavigationBar_Home);
        }

    //    hàm load Fragment ===> tham số truyền vào là một Fragment cần load
        private void loadingFragment(Fragment fragmentLoading){
            // Kiểm tra xem Fragment đã được thêm vào Back Stack hay chưa
            Fragment existingFragment = getSupportFragmentManager().findFragmentById(framelayout_screenhome.getId());

            if (existingFragment == null || !existingFragment.getClass().getName().equals(fragmentLoading.getClass().getName())) {
                // Fragment chưa được thêm vào Back Stack hoặc là một Fragment khác, thì thực hiện thay thế
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(framelayout_screenhome.getId(), fragmentLoading);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

    //    hàm lấy danh sách product database
        private void getDataProduct(){
    //        đi đến node Product trên Firebase
            databaseReference = firebaseDatabase.getReference("Product");
            System.out.println("load data");
    //        lấy sự danh sách product tại thời gian hiện tại
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
    //                nếu dữ liệu tồn tại ===> không bị trống
                    if (snapshot.exists()){
                        for (DataSnapshot itemSnap:
                             snapshot.getChildren()) {
                          //  Duyệt các item có trong node product
                            for (DataSnapshot item:
                                    itemSnap.getChildren()) {
                                ProductData productData = item.getValue(ProductData.class);
                                fragmentHomeScreenHome.addProductToArrProduct(productData);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    //    hàm lấy danh sách banner từ firebase dựa vào số lượng banner đã thiết lập
        private void getDataBanner(int count){
    //        đi tới node BannerData trên firebase
            databaseReference = firebaseDatabase.getReference("BanerData");
    //        Lấy dữ liệu trên firebase tại lúc thời gian yêu cầu lấy dữ liệu
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
    //                kiểm tra tính tồn tại của dữ liệu
                    if (snapshot.exists()){
                        //                    Duyệt từng item banner
                        for (DataSnapshot itemBanner:
                                snapshot.getChildren()) {
                            if (countBanner <= count){
                                String urlImage = itemBanner.getValue().toString();
                                fragmentHomeScreenHome.addUrlBannerToArrUrlBanner(urlImage);
                                countBanner ++;
                            }
                            else {
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }