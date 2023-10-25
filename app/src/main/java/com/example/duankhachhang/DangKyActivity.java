package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class DangKyActivity extends AppCompatActivity {


    TextInputLayout til_soDienThoai_DangKy, til_MatKhau_DangKy,til_HoTen_DangKy,til_DiaChi_DangKy;
    TextInputEditText ted_soDienThoai_DangKy,ted_MatKhau_DangKy,ted_HoTen_DangKy,ted_DiaChi_DangKy;
    Button btn_XacNhan_DangKy;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://duandd2-default-rtdb.asia-southeast1.firebasedatabase.app");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        setControl();
        setEven();

    }

    private void setEven() {


        btn_XacNhan_DangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String soDT = ted_soDienThoai_DangKy.getText().toString();
                final String matKhau = ted_MatKhau_DangKy.getText().toString();
                final String hoTen = ted_HoTen_DangKy.getText().toString();
                final String diaChi = ted_DiaChi_DangKy.getText().toString();

                if(soDT.isEmpty() || matKhau.isEmpty() ||hoTen.isEmpty() || diaChi.isEmpty()){
                    Toast.makeText(DangKyActivity.this, "bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("userKhachHang").addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(soDT)){
                                til_soDienThoai_DangKy.setHelperText("số điện thoại đã được đăng ký");
                                til_soDienThoai_DangKy.setHelperTextColor(ColorStateList.valueOf(R.color.red));
                                til_soDienThoai_DangKy.setErrorEnabled(true);
                            }else {
                                databaseReference.child("userKhachHang").child(soDT).child("id").setValue(soDT);
                                databaseReference.child("userKhachHang").child(soDT).child("pass").setValue(matKhau);
                                databaseReference.child("userKhachHang").child(soDT).child("imageUser").setValue("url");
                                databaseReference.child("userKhachHang").child(soDT).child("name").setValue(hoTen);
                                databaseReference.child("userKhachHang").child(soDT).child("andress").setValue(diaChi);

                                Toast.makeText(DangKyActivity.this, "tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void setControl() {
        til_soDienThoai_DangKy = findViewById(R.id.til_soDienThoai_DangKy);
        til_MatKhau_DangKy = findViewById(R.id.til_MatKhau_DangKy);
        til_HoTen_DangKy = findViewById(R.id.til_HoTen_DangKy);
        til_DiaChi_DangKy = findViewById(R.id.til_DiaChi_DangKy);
        ted_soDienThoai_DangKy = findViewById(R.id.ted_soDienThoai_DangKy);
        ted_MatKhau_DangKy = findViewById(R.id.ted_MatKhau_DangKy);
        ted_HoTen_DangKy = findViewById(R.id.ted_HoTen_DangKy);
        ted_DiaChi_DangKy = findViewById(R.id.ted_DiaChi_DangKy);
        btn_XacNhan_DangKy = findViewById(R.id.btn_XacNhan_DangKy);
    }
}