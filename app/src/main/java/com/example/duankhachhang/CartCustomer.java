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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.example.duankhachhang.Class.CartData;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.RecyclerView.CartCustomer_Adapter;
import com.example.duankhachhang.RecyclerView.CartCustomer_ViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartCustomer extends AppCompatActivity {

    RecyclerView rcvCartListCustomer_CartCustomer;
    Toolbar toolBar_CartCustomer;
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
    private Runnable runnable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_customer);
        context = this;
        setControl();
        setIntiazational();
        getInformationCart_IdCustomer();
        setEvent();
    }

    private void setIntiazational() {
//        gán giá trị cho RecyclerView
        cartCustomerAdapter = new CartCustomer_Adapter(arrCart, context);
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
                if (itemHolderStart != null){
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
                handler.postDelayed(runnable,4000);
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
                                databaseReference = firebaseDatabase.getReference("CartCustomer/"+customer.getId()+"/"+arrCart.get(position).getIdProduct());
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

//       gán giá trị biến customer
        customer = new Customer("0123456789", "demo address", "Demo", null);

//        Kích hoạt toolbar
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
                }
                else {
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
    }

    private void setControl() {
        rcvCartListCustomer_CartCustomer = findViewById(R.id.rcvCartListCustomer_CartCustomer);
        toolBar_CartCustomer = findViewById(R.id.toolBar_CartCustomer);
    }
}