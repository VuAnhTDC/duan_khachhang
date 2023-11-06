package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duankhachhang.Class.Customer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.net.Inet4Address;

public class DangNhapActivity extends AppCompatActivity {

    TextInputLayout til_soDienThoai, til_matKhau;
    TextInputEditText ted_soDienThoai, ted_matKhau;
    Button btn_xacNhan, btn_dangKy;
    TextView tv_quenMatKhau;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://duandd2-default-rtdb.asia-southeast1.firebasedatabase.app");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setControll();
        setEven();
    }

    private void setEven() {
        ted_soDienThoai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String soDT = ted_soDienThoai.getText().toString();
                final String matKhau = ted_matKhau.getText().toString();

                if (soDT.isEmpty() || matKhau.isEmpty()){
                    Toast.makeText(DangNhapActivity.this, "bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("userKhachHang").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot customerItem:
                                     snapshot.getChildren()) {
                                    if (snapshot.hasChild(soDT)){
                                        final String getPass = snapshot.child(soDT).child("pass").getValue(String.class);
                                        if (matKhau.equals(getPass)){
                                            Customer customer = customerItem.getValue(Customer.class);
                                            SharedPreferences sharedPreferences1 = getSharedPreferences("InformationShop", Context.MODE_PRIVATE);
                                            Gson gson = new Gson();
                                            String json = gson.toJson(customerItem);
                                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                                            editor.putString("informationCustomer", json);
                                            editor.apply();
                                            startActivity(new Intent(DangNhapActivity.this,MainActivity.class).putExtra("informationCustomer",customer));
                                            finish();
                                        }else {
                                            Toast.makeText(DangNhapActivity.this, "đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(DangNhapActivity.this, "sai mật khẩu", Toast.LENGTH_SHORT).show();
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
        });
        btn_dangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });
    }

    // Xử lý khi người dùng click vào "Quên mật khẩu"
    public void onForgotPasswordClick(View view) {
        Intent intent = new Intent(this, QuenMatKhauActivity.class);
        startActivity(intent);
    }

    private void setControll() {
        til_soDienThoai = findViewById(R.id.til_soDienThoai_QuenMatKhau);
        til_matKhau = findViewById(R.id.til_matKhau);
        ted_soDienThoai = findViewById(R.id.edt_soDienThoai);
        ted_matKhau = findViewById(R.id.edt_matKhau);
        btn_xacNhan = findViewById(R.id.btn_xacNhan_DangNhap);
        btn_dangKy = findViewById(R.id.btn_dangKy);
        tv_quenMatKhau = findViewById(R.id.tv_quenMatKhau);
    }
}