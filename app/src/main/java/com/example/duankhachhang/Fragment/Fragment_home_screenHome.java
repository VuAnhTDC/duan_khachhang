package com.example.duankhachhang.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.duankhachhang.Adapter.ProductBanerAdapter;
import com.example.duankhachhang.CartCustomer;
import com.example.duankhachhang.Class.CartData;
import com.example.duankhachhang.Class.Category;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.Voucher;
import com.example.duankhachhang.Class.VoucherCustomer;
import com.example.duankhachhang.Dialog.MyVoucherCustomerDialogFragment;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.CategoryProduct_Adapter;
import com.example.duankhachhang.RecyclerView.CategoryProduct_ViewHolder;
import com.example.duankhachhang.RecyclerView.ProductListHome_Adapter;
import com.example.duankhachhang.RecyclerView.ProductSuggestedResultValueSearch_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment_home_screenHome extends Fragment {
    //    Biến lưu giữ thông tin người dùng app
    private Customer customer = new Customer();
    //    khai bao biến giao diện
    TextView tvCountProductCart_FragmentHome, tvCountNotification_FragmentHome,tvCountVoucher;
    TextInputEditText edtSearch_FragmentHome;
    Context context;
    ViewPager viewPagerProductBanner_Home;
    RecyclerView rcvProductList_Home, rcvCategory_Product,rcvResultSearch_FragmentHome;
    FrameLayout vCountCartCustomer_FragmentHome,vCountVoucherCustomer;
    SwipeRefreshLayout swipRefresh_FragmentHome;

    //    Khai báo biến quản lý
    private int countUrl = 0;
    private int locationUrlImageProduct_ItemViewPager = 0;
    private int timeDeplay = 2000;
    private int countProductInCart = 0;
    private int countVoucher = 0;
    private final Handler handler = new Handler();
    private int positionCategorySelection = -1;
    ProductBanerAdapter productBanerAdapter;
    ArrayList<ProductData> arrProduct;
    ArrayList<String> arrUrlBanner;
    ArrayList<Category> arrCategory = new ArrayList<>();
    ArrayList<ProductData> arrProductSuggestedResult_ValueSearch = new ArrayList<>();
    ProductListHome_Adapter productListHomeAdapter;
    CategoryProduct_Adapter categoryProductAdapter;
    ProductSuggestedResultValueSearch_Adapter productSuggestedResultValueSearchAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private View view;

    //    hàm khỏi tạo
    public Fragment_home_screenHome() {
        this.arrProduct = new ArrayList<>();
        this.arrUrlBanner = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //    hàm thêm Product
    public void addProductToArrProduct(ProductData productData) {
        this.arrProduct.add(productData);
        Collections.reverse(this.arrProduct);
        productListHomeAdapter.notifyDataSetChanged();
    }

    //    hàm thêm banner vào danh sách chứa banner
    public void addUrlBannerToArrUrlBanner(String urlBanner) {
        this.arrUrlBanner.add(urlBanner);
        productBanerAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.framelayout_itemhome_activity_screenhome, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
//        gán giá trị cho biến context
        context = getContext();
//        gọi hàm ánh xạ
        setControl();
//        gọi hàm khởi tạo
        setIntiazation();
        getCategory();
//        set title số lượng sản phẩm giỏ hàng
        setCountProductCartUser();
        getCountVoucher();
//        gọi hàm bắt sự kiện
        setEvent();
//        tự động đổi banner
        handler.postDelayed(runnable, timeDeplay);
        return view;
    }
    private void getCountVoucher(){
        databaseReference = firebaseDatabase.getReference("VoucherCustomer/"+customer.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countVoucher = 0;
                if (snapshot.exists()){
                    for (DataSnapshot item:snapshot.getChildren()){
                        VoucherCustomer voucherCustomer = item.getValue(VoucherCustomer.class);
                        if (voucherCustomer.isStatus()){
                           DatabaseReference databaseReference1 = firebaseDatabase.getReference("Product/"+voucherCustomer.getIdShop()+"/"+voucherCustomer.getIdProduct());
                           databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if (snapshot.exists()){
                                       countVoucher ++;
                                       if (countVoucher > 0){
                                           tvCountVoucher.setVisibility(View.VISIBLE);
                                           tvCountVoucher.setText(countVoucher + "");
                                       }
                                       else {
                                           tvCountVoucher.setVisibility(View.GONE);
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                        }
                    }
                }
              else {
                  tvCountVoucher.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, timeDeplay);
    }

    //    hàm lấy số lượng sản phẩm trong giỏ hàng --- người dùng
    private void setCountProductCartUser() {

//        Truy cập bảng giỏ hàng vào node người dùng trên firebase
        databaseReference = firebaseDatabase.getReference("CartCustomer/" + customer.getId());
//        lắng nghe sự kiện
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countProductInCart = 0;
//                kiểm tra giỏ hàng người dùng có tồn tại không ---> nếu tồn tại
                if (snapshot.exists()) {
//                    gán giá trị biến đếm số lượng sản phẩm trong giỏ hàng người dùng
//                    duyệt danh sách giỏ hàng người dùng
                    for (DataSnapshot itemCart :
                            snapshot.getChildren()) {
                       CartData cartData = itemCart.getValue(CartData.class);
                       DatabaseReference databaseReference1 = firebaseDatabase.getReference("Product/"+cartData.getIdShop()+"/"+cartData.getIdProduct());
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                   countProductInCart ++;
                                   // nếu biến đếm số lượng hàng trong giỏ hàng người dùng > 0
                                    if (countProductInCart > 0) {
//                        hiển thị số lượng hàng hóa trong giỏ hàng
                                        tvCountProductCart_FragmentHome.setText(countProductInCart + "");
                                        tvCountProductCart_FragmentHome.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
//

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
        productBanerAdapter = new ProductBanerAdapter(arrUrlBanner, context);
//        Thiết lập Adapter cho ViewPager
        viewPagerProductBanner_Home.setAdapter(productBanerAdapter);
//        Kiểm tra số lượng sản phẩm trong giỏ hàng
        if (countProductInCart == 0) {
//            ẩn title số lượng sản phẩm trong giỏ hàng nếu trong giỏ hàng người dùng không chứa sản phẩm nào
            tvCountProductCart_FragmentHome.setVisibility(View.GONE);
        }
        CategoryProduct_Adapter.ButtonCategorySelection buttonCategorySelection = new CategoryProduct_Adapter.ButtonCategorySelection() {
            @Override
            public void getPositionButtonSelectionInArrCategory(int position) {
                Category category = arrCategory.get(position);
                getProductWithCategory(category.getIdCategory());
                for (int i = 0;i<arrCategory.size();i++){
                    if (i != position){
                        RecyclerView.ViewHolder viewHolder = rcvCategory_Product.findViewHolderForLayoutPosition(i);
                        CategoryProduct_ViewHolder itemCategoryViewHolder = (CategoryProduct_ViewHolder) viewHolder;
                        itemCategoryViewHolder.btnCategoryProduct.setBackgroundResource(R.drawable.bg_button_normal);
                    }
                }
            }
        };
        categoryProductAdapter = new CategoryProduct_Adapter(arrCategory, context, buttonCategorySelection);;
        rcvCategory_Product.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        rcvCategory_Product.setAdapter(categoryProductAdapter);

//        gán giá trị cho productSuggestedResultAdapter
        productSuggestedResultValueSearchAdapter = new ProductSuggestedResultValueSearch_Adapter(arrProductSuggestedResult_ValueSearch,context);
        rcvResultSearch_FragmentHome.setLayoutManager(new LinearLayoutManager(context));
        rcvResultSearch_FragmentHome.setAdapter(productSuggestedResultValueSearchAdapter);
    }

    //    hàm lấy danh sách category
    private void getCategory() {
        databaseReference = firebaseDatabase.getReference("Category");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrCategory.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot itemSnap :
                            snapshot.getChildren()) {
                        Category category = itemSnap.getValue(Category.class);
                        arrCategory.add(category);
                        categoryProductAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    hàm set thời gian để chạy từng item trong Viewpager
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (productBanerAdapter.getCount() < 5 && locationUrlImageProduct_ItemViewPager > productBanerAdapter.getCount()) {
                locationUrlImageProduct_ItemViewPager = 0;
            }
            if (locationUrlImageProduct_ItemViewPager == productBanerAdapter.getCount()) {
                locationUrlImageProduct_ItemViewPager = 0;
            }
            viewPagerProductBanner_Home.setCurrentItem(locationUrlImageProduct_ItemViewPager, true);
            locationUrlImageProduct_ItemViewPager++;
            handler.postDelayed(this, timeDeplay);
        }
    };

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    //    hàm lấy dữ liệu trên firebase về (Sản phẩm)
    private void getProductData() {
//        Trước khi load lại tất cả dữ liệu bỏ item category đã chọn
        for (int i = 0;i<arrCategory.size();i++){
            RecyclerView.ViewHolder viewHolder = rcvCategory_Product.findViewHolderForLayoutPosition(i);
            CategoryProduct_ViewHolder itemCategoryViewHolder = (CategoryProduct_ViewHolder) viewHolder;
            itemCategoryViewHolder.btnCategoryProduct.setBackgroundResource(R.drawable.bg_button_normal);
        }
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
                    for (DataSnapshot itemsnap :
                            snapshot.getChildren()) {
                        //                    Duyệt từng item trong bảng đó
                        for (DataSnapshot productItem : itemsnap.getChildren()) {
//                        Lấy thông tin item trong bảng Product
                            ProductData productData = productItem.getValue(ProductData.class);
//                        Thêm product vừa lấy được vào danh sách product
                            arrProduct.add(productData);
                            Collections.reverse(arrProduct);
//                        Cập nhật lại product vừa lấy vào RecyclerView
                            productListHomeAdapter.notifyDataSetChanged();
                        }
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
//        bắt sự kiện nhấn vào voucher
        vCountVoucherCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyVoucherCustomerDialogFragment myVoucherCustomerDialogFragment = new MyVoucherCustomerDialogFragment();
                myVoucherCustomerDialogFragment.show(getActivity().getSupportFragmentManager(), "Voucher");
            }
        });
        edtSearch_FragmentHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSearch_FragmentHome.getText().toString().length() > 0){
                    rcvResultSearch_FragmentHome.setVisibility(View.VISIBLE);
                    getSuggestedResult(edtSearch_FragmentHome.getText().toString());
                }
                else {
                    rcvResultSearch_FragmentHome.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

//    Hiển thị sản phẩm gợi ý theo từ khóa người dùng nhập vào
    private void getSuggestedResult(String valueSearch){
        arrProductSuggestedResult_ValueSearch.clear();
        for (ProductData itemProduct:
             arrProduct) {
            if (itemProduct.getNameProduct().toLowerCase().contains(valueSearch)){
                arrProductSuggestedResult_ValueSearch.add(itemProduct);
                productSuggestedResultValueSearchAdapter.notifyDataSetChanged();
            }
        }
        if (arrProductSuggestedResult_ValueSearch.size() < 1){
            rcvResultSearch_FragmentHome.setVisibility(View.GONE);
        }
    }

    private void getProductWithCategory(String idCategory) {
        databaseReference = firebaseDatabase.getReference("Product");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrProduct.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot itemShopManagerProduct :
                            snapshot.getChildren()) {
                        String idShopManagerProduct = itemShopManagerProduct.getKey();
                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Product/" + idShopManagerProduct);
                        Query query = databaseReference1.orderByChild("keyCategoryProduct").equalTo(idCategory);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot itemProduct:
                                         snapshot.getChildren()) {
                                        ProductData productData = itemProduct.getValue(ProductData.class);
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
                }
                productListHomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchProduct() {
        databaseReference = firebaseDatabase.getReference("Product");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot itemPd :
                            snapshot.getChildren()) {
                        ProductData productData = itemPd.getValue(ProductData.class);
                        if (productData.getNameProduct() != null && productData.getNameProduct().contains(edtSearch_FragmentHome.getText().toString())) {
                            arrProduct.add(productData);
                            productBanerAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        edtSearch_FragmentHome = view.findViewById(R.id.edtSearch_FragmentHome);
        rcvCategory_Product = view.findViewById(R.id.rcvCategory_Product);
        vCountVoucherCustomer = view.findViewById(R.id.vCountVoucherCustomer);
        tvCountVoucher = view.findViewById(R.id.tvCountVoucher);
        rcvResultSearch_FragmentHome = view.findViewById(R.id.rcvResultSearch_FragmentHome);
    }


}
