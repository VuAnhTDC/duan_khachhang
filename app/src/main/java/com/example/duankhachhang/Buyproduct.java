package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.RecyclerView.OrderProduct_Adapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Buyproduct extends AppCompatActivity {

    Toolbar toolBar_OrderProduct;
    TextView tvUserName_OrderProduct, tvNumberphone_OrderProduct, tvAddressUser_OrderProduct, tvSumPriceProduct_OrderProduct, tvVoucher_OrderProduct, tvSumMoney_OrderProduct;
    RecyclerView rcvOrderList_OrderProduct;
    TextInputEditText edtNote_OrderProduct;
    LinearLayout vDontVoucher_OrderProduct;
    Spinner spSelectedVocher_OrderProduct;
    Button btnPay_OrderProduct;

    OrderProduct_Adapter orderProductAdapter;
    ArrayList<OrderData> arrOrderData = new ArrayList<>();
    Context context;
    Customer customer = new Customer();
    private int sumPriceAllProductOrder = 0;
    private int sumPriceAll = 0;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference  databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyproduct);
        context = this;
        setControl();
        setIntiazation();
        setEvent();
    }

    private void setIntiazation() {
        setSupportActionBar(toolBar_OrderProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.arrOrderData = (ArrayList<OrderData>) intent.getSerializableExtra("arrOrder");
        orderProductAdapter = new OrderProduct_Adapter(arrOrderData, context);
        rcvOrderList_OrderProduct.setLayoutManager(new LinearLayoutManager(context));
        rcvOrderList_OrderProduct.setAdapter(orderProductAdapter);

//        Customer demo
        customer = new Customer("0123456789", "demo address", "Demo", null);
        tvNumberphone_OrderProduct.setText(customer.getName());
        tvNumberphone_OrderProduct.setText(customer.getId());
        tvAddressUser_OrderProduct.setText(customer.getAddress());
        for (OrderData orderData : arrOrderData) {
            sumPriceAllProductOrder += orderData.getPrice_Order();
        }
        tvSumPriceProduct_OrderProduct.setText(sumPriceAllProductOrder + " Vnd");
        sumPriceAll = sumPriceAllProductOrder + 25000 + 0;
        tvSumMoney_OrderProduct.setText(sumPriceAll + "VND");

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
                pushDataOrderToFirebase();
            }
        });


    }

    private void pushDataOrderToFirebase(){
        for (OrderData itemOrder:
             arrOrderData) {
            databaseReference = firebaseDatabase.getReference("OrderPrduct");
            databaseReference.child(itemOrder.getIdCustomer_Order()+itemOrder.getIdProduct_Order()).setValue(itemOrder);
        }
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
    }
}