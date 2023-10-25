package com.example.duankhachhang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DoiMatKhauActivity extends AppCompatActivity {

    TextInputLayout til_matKhauMoi, til_nhapLaiMatKhau;
    TextInputEditText ted_matKhauMoi, ted_nhapLaijMatKhau;
    Button btn_xacnhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btn_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void setControl() {
        til_matKhauMoi = findViewById(R.id.til_matKhauMoi);
        til_nhapLaiMatKhau = findViewById(R.id.til_nhapLaimatKhau);
        ted_matKhauMoi = findViewById(R.id.ted_matKhauMoi);
        ted_nhapLaijMatKhau = findViewById(R.id.ted_nhapLaimatKhau);
        btn_xacnhan = findViewById(R.id.btn_XacNhan_DoiMatKhau);
    }
}