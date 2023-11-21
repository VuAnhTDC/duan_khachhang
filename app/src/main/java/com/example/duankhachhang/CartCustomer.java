package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.example.duankhachhang.Class.CartData;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.RecyclerView.CartCustomer_Adapter;
import com.example.duankhachhang.RecyclerView.CartCustomer_ViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartCustomer extends AppCompatActivity {

    RecyclerView rcvCartListCustomer_CartCustomer;
    Toolbar toolBar_CartCustomer;
    TextView tvAllPrice_ItemOrderSelection;
    Button btnBuyProduct_CartCustomer;
    CartCustomer_Adapter cartCustomerAdapter;
    ArrayList<CartData> arrCart = new ArrayList<>();
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private Customer customer = new Customer();
    private CartCustomer_ViewHolder itemHolderStart = null;
    private Target<Drawable> itemTaget = null;
    private RequestBuilder<Drawable> requestBuilder = null;
    private Handler handler = new Handler();
    private ArrayList<OrderData> arrOrderData = new ArrayList<>();
    private int allPrice = 0;
    private Runnable runnable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_customer);
        context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
        setControl();
        setIntiazational();
        getInformationCart_IdCustomer();
        setEvent();
    }

//    tổng số tiền
    private int getAllPrice(){
        allPrice = 0;
        for (OrderData orderData: arrOrderData) {
            allPrice += orderData.getPrice_Order();
        }
        return allPrice;
    }

//    hàm xóa orderData
    private void setDeleteOrderDataItem(int position){
        String idProduct = arrCart.get(position).getIdProduct();
        for (OrderData itemOrder :
                arrOrderData) {
            if (itemOrder.getIdProduct_Order().equals(idProduct)) {
                arrOrderData.remove(itemOrder);
                Locale locale = new Locale("vi","VN");
                NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                tvAllPrice_ItemOrderSelection.setText(numberFormat.format(getAllPrice()) + " VND");
                return;
            }
        }
    }

    // hàm lấy thông tin sản phẩm vừa chọn
    private void getInformationProductSelection(int position) {
        databaseReference = firebaseDatabase.getReference("Product/" + arrCart.get(position).getIdShop()+"/"+arrCart.get(position).getIdProduct());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ProductData productData = snapshot.getValue(ProductData.class);
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String formattedDate = dateFormat.format(date);
                    int price = productData.getPriceProduct() * arrCart.get(position).getQuanlityProduct_Cart();
                    OrderData orderData = new OrderData("", customer.getId(), productData.getIdProduct(), productData.getIdUserProduct(), "", 0, formattedDate, customer.getAddress(), customer.getId(), "", arrCart.get(position).getQuanlityProduct_Cart(), price, "", "");
                    arrOrderData.add(orderData);
                    Locale locale = new Locale("vi","VN");
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                    tvAllPrice_ItemOrderSelection.setText(numberFormat.format(getAllPrice()) +" VND");
                     if (arrOrderData.size() > 0){
                         btnBuyProduct_CartCustomer.setEnabled(true);
                     }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setIntiazational() {
        CartCustomer_Adapter.CartDataSelectionItem_CartCustomer cartDataSelectionItemCartCustomer = new CartCustomer_Adapter.CartDataSelectionItem_CartCustomer() {
            @Override
            public int getCartDataSelection(int position) {
                getInformationProductSelection(position);
                return 0;
            }

            @Override
            public int getCartDataSelection_Cancel(int positon) {
                setDeleteOrderDataItem(positon);
                if (arrOrderData.size() <= 0){
                    btnBuyProduct_CartCustomer.setEnabled(false);
                }
                return 0;
            }
        };
//        gán giá trị cho RecyclerView
        cartCustomerAdapter = new CartCustomer_Adapter(arrCart, context, cartDataSelectionItemCartCustomer);
        rcvCartListCustomer_CartCustomer.setLayoutManager(new LinearLayoutManager(context));
        rcvCartListCustomer_CartCustomer.setAdapter(cartCustomerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                cartCustomerAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                handler.removeCallbacks(runnable);
                if (itemHolderStart != null) {
                    itemHolderStart.ivDelete_ItemCartCustomer.setVisibility(View.GONE);
                    itemHolderStart.vInformationItemCart.setVisibility(View.VISIBLE);
                    Glide.with(context).clear(itemTaget);
                    cartCustomerAdapter.notifyDataSetChanged();
                }
                CartCustomer_ViewHolder cartCustomerViewHolder = (CartCustomer_ViewHolder) viewHolder;
                requestBuilder = Glide.with(context).load(R.drawable.icon_delete_cart_amination);
                itemTaget = requestBuilder.into(cartCustomerViewHolder.ivDelete_ItemCartCustomer);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        cartCustomerViewHolder.vInformationItemCart.setVisibility(View.VISIBLE);
                        cartCustomerViewHolder.ivDelete_ItemCartCustomer.setVisibility(View.GONE);
                        cartCustomerAdapter.notifyDataSetChanged();
                    }
                };
                handler.postDelayed(runnable, 4000);
                cartCustomerAdapter.notifyDataSetChanged();
                cartCustomerViewHolder.ivDelete_ItemCartCustomer.setVisibility(View.VISIBLE);
                cartCustomerViewHolder.vInformationItemCart.setVisibility(View.GONE);
                itemHolderStart = (CartCustomer_ViewHolder) viewHolder;
                cartCustomerViewHolder.ivDelete_ItemCartCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn muốn xóa sản phẩm này ra khỏi giỏ hàng không ?");
                        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setDeleteOrderDataItem(position);
                                Locale locale = new Locale("vi","VN");
                                NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                                tvAllPrice_ItemOrderSelection.setText(numberFormat.format(getAllPrice()) + " VND");
                                databaseReference = firebaseDatabase.getReference("CartCustomer/" + customer.getId() + "/" + arrCart.get(position).getIdProduct());
                                databaseReference.removeValue();
                                cartCustomerViewHolder.vInformationItemCart.setVisibility(View.VISIBLE);
                                cartCustomerViewHolder.ivDelete_ItemCartCustomer.setVisibility(View.GONE);
                                cartCustomerAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cartCustomerViewHolder.ivDelete_ItemCartCustomer.setVisibility(View.GONE);
                                cartCustomerViewHolder.vInformationItemCart.setVisibility(View.VISIBLE);
                                cartCustomerAdapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            }
        });
        itemTouchHelper.attachToRecyclerView(rcvCartListCustomer_CartCustomer);
        setSupportActionBar(toolBar_CartCustomer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //    lấy danh sách gior hàng của người dùng
    private void getInformationCart_IdCustomer() {
        databaseReference = firebaseDatabase.getReference("CartCustomer/" + customer.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    arrCart.clear();
                    for (DataSnapshot itemCart :
                            snapshot.getChildren()) {
                        CartData cartData = itemCart.getValue(CartData.class);
                        arrCart.add(cartData);
                        cartCustomerAdapter.notifyDataSetChanged();
                    }
                } else {
                    arrCart.clear();
                    cartCustomerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi lấy danh sách giỏ hàng của người dùng: " + error.getMessage());
            }
        });
    }

    private void setEvent() {
        btnBuyProduct_CartCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrOrderData.size() > 0){
                    Intent intent = new Intent(context,Buyproduct.class);
                    intent.putExtra("arrOrder",arrOrderData);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setControl() {
        rcvCartListCustomer_CartCustomer = findViewById(R.id.rcvCartListCustomer_CartCustomer);
        toolBar_CartCustomer = findViewById(R.id.toolBar_CartCustomer);
        tvAllPrice_ItemOrderSelection = findViewById(R.id.tvAllPrice_ItemOrderSelection);
        btnBuyProduct_CartCustomer = findViewById(R.id.btnBuyProduct_CartCustomer);
    }
}