package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.Dialog.EditDeliveryAddressCustomerDialogFragment;
import com.example.duankhachhang.Fragment.Fragment_home_screenHome;
import com.example.duankhachhang.Model.NotificationType;
import com.example.duankhachhang.Model.SendNotification;
import com.example.duankhachhang.RecyclerView.OrderProduct_Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Buyproduct extends AppCompatActivity {

    Toolbar toolBar_OrderProduct;
    TextView tvUserName_OrderProduct, tvNumberphone_OrderProduct, tvAddressUser_OrderProduct, tvSumPriceProduct_OrderProduct, tvVoucher_OrderProduct, tvSumMoney_OrderProduct;
    RecyclerView rcvOrderList_OrderProduct;
    TextInputEditText edtNote_OrderProduct;
    LinearLayout vDontVoucher_OrderProduct;
    Spinner spSelectedVocher_OrderProduct;
    Button btnPay_OrderProduct;
    ImageView ivEditDeliveryAddress;
    LinearLayout loading_BuyProduct;

    OrderProduct_Adapter orderProductAdapter;
    ArrayList<OrderData> arrOrderData = new ArrayList<>();
    Context context;
    Customer customer = new Customer();
    private int sumPriceAllProductOrder = 0;
    private int sumPriceAll = 0;
    private boolean changeDeliveryAddress = false;
    private String fcmToken = "";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference  databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyproduct);
        context = this;
        SendNotification.setContext(context);
        setControl();
        setIntiazation();
        setEvent();
    }

    private void setIntiazation() {
        setSupportActionBar(toolBar_OrderProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
        Intent intent = getIntent();
        this.arrOrderData = (ArrayList<OrderData>) intent.getSerializableExtra("arrOrder");
        orderProductAdapter = new OrderProduct_Adapter(arrOrderData, context);
        rcvOrderList_OrderProduct.setLayoutManager(new LinearLayoutManager(context));
        rcvOrderList_OrderProduct.setAdapter(orderProductAdapter);

        tvNumberphone_OrderProduct.setText(customer.getName());
        tvNumberphone_OrderProduct.setText(customer.getId());
        tvAddressUser_OrderProduct.setText(customer.getAddress());
        for (OrderData orderData : arrOrderData) {
            sumPriceAllProductOrder += orderData.getPrice_Order();
        }
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        tvSumPriceProduct_OrderProduct.setText(numberFormat.format(sumPriceAllProductOrder) +"VND");
        sumPriceAll = sumPriceAllProductOrder + 25000 + 0;
        tvSumMoney_OrderProduct.setText(numberFormat.format(sumPriceAll) + "VND");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEvent() {
        edtNote_OrderProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();

                }
                return false;
            }
        });
        btnPay_OrderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn mua sản phẩm này không");
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loading_BuyProduct.setVisibility(View.VISIBLE);
                        pushDataOrderToFirebase();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        ivEditDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDeliveryAddressCustomerDialogFragment.DeliveryAddress deliveryAddress = new EditDeliveryAddressCustomerDialogFragment.DeliveryAddress() {
                    @Override
                    public void getDeliveryAddress(String deliveryAddress) {
                        if(deliveryAddress != null && !deliveryAddress.isEmpty()){
                            tvAddressUser_OrderProduct.setText(deliveryAddress);
                            changeDeliveryAddress = true;
                        }
                        else {
                            changeDeliveryAddress = false;
                        }
                    }
                };
                EditDeliveryAddressCustomerDialogFragment editDeliveryAddressCustomerDialogFragment = new EditDeliveryAddressCustomerDialogFragment(deliveryAddress);
                editDeliveryAddressCustomerDialogFragment.show(getSupportFragmentManager(),"Sửa địa chỉ nhận hàng");
            }
        });
    }

    private void pushDataOrderToFirebase(){
        for (OrderData itemOrder:
             arrOrderData) {
            if (changeDeliveryAddress){
                itemOrder.setDeliveryAddress(tvAddressUser_OrderProduct.getText().toString());
            }
            databaseReference = firebaseDatabase.getReference("Product/"+itemOrder.getIdShop_Order() +"/"+itemOrder.getIdProduct_Order());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        ProductData productData = snapshot.getValue(ProductData.class);
                        productData.setQuanlityProduct(productData.getQuanlityProduct() - itemOrder.getQuanlity_Order());
                        databaseReference.setValue(productData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                databaseReference = firebaseDatabase.getReference("OrderProduct");
                                String keyPush = databaseReference.push().toString().substring(databaseReference.push().toString().lastIndexOf("/"));
                                itemOrder.setIdOrder(keyPush.substring(1));
                                databaseReference.child(itemOrder.getIdOrder()).setValue(itemOrder).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Thông báo");
                                        builder.setMessage("Lỗi khi mua hàng");
                                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }

                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        databaseReference = firebaseDatabase.getReference("CartCustomer/" + itemOrder.getIdCustomer_Order()+"/"+itemOrder.getIdProduct_Order());
                                        databaseReference.removeValue();
                                        sendMessageToShop(itemOrder);
                                        addOrderCustomer(itemOrder);
                                        addOrderToShop(itemOrder);

                                    }
                                });
                                Toast.makeText(context, "Đã mua hàng thành công",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        getAndSaveFCMToken();
    }

//    thêm order cho của hàng
    private void addOrderToShop(OrderData orderData){
        databaseReference = firebaseDatabase.getReference("OrderShop");
        databaseReference.child(orderData.getIdShop_Order()).child(orderData.getIdOrder()).child("idItemOrder").setValue(orderData.getIdOrder());
    }

    private void addOrderCustomer(OrderData orderData){
        databaseReference = firebaseDatabase.getReference("OrderCustomer");
        databaseReference.child(orderData.getIdCustomer_Order()).child(orderData.getIdOrder()).child("idItemOrder").setValue(orderData.getIdOrder());
    }

    private void sendMessageToShop(OrderData orderData){
        databaseReference = firebaseDatabase.getReference("Shop/"+orderData.getIdShop_Order());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    SendNotification.getSendNotificationOrderSuccessFull(shopData.getFcmToken(),"Đơn đặt hàng","Đơn hàng có mã: " + orderData.getIdOrder() +"\n sản phẩm: " +orderData.getIdProduct_Order(),NotificationType.NotificationNormal());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAndSaveFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            fcmToken = task.getResult();
                            SendNotification.getSendNotificationOrderSuccessFull(fcmToken,"Kết quả đơn đặt hàng","Đơn đặt hàng của bạn đã được chuyển sang người bán hàng", NotificationType.NotificationOrder());
                            Intent intent = new Intent(context,Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            System.out.println("Không lấy và lưu được fcm token");
                        }
                    }
                });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setNoteOrder(String value){
        for (OrderData item : arrOrderData) {
            item.setNote_Order(value);
        }
    }

    private void setControl() {
        toolBar_OrderProduct = findViewById(R.id.toolBar_OrderProduct);
        tvUserName_OrderProduct = findViewById(R.id.tvUserName_OrderProduct);
        tvNumberphone_OrderProduct = findViewById(R.id.tvNumberphone_OrderProduct);
        tvAddressUser_OrderProduct = findViewById(R.id.tvAddressUser_OrderProduct);
        tvSumPriceProduct_OrderProduct = findViewById(R.id.tvSumPriceProduct_OrderProduct);
        tvVoucher_OrderProduct = findViewById(R.id.tvVoucher_OrderProduct);
        rcvOrderList_OrderProduct = findViewById(R.id.rcvOrderList_OrderProduct);
        edtNote_OrderProduct = findViewById(R.id.edtNote_OrderProduct);
        vDontVoucher_OrderProduct = findViewById(R.id.vDontVoucher_OrderProduct);
        spSelectedVocher_OrderProduct = findViewById(R.id.spSelectedVocher_OrderProduct);
        tvSumMoney_OrderProduct = findViewById(R.id.tvSumMoney_OrderProduct);
        btnPay_OrderProduct = findViewById(R.id.btnPay_OrderProduct);
        ivEditDeliveryAddress = findViewById(R.id.ivEditDeliveryAddress);
        loading_BuyProduct = findViewById(R.id.loading_BuyProduct);
    }
}