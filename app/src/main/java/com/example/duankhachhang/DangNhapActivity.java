package com.example.duankhachhang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DangNhapActivity extends AppCompatActivity {

    TextInputLayout til_soDienThoai, til_matKhau;
    TextInputEditText ted_soDienThoai, ted_matKhau;
    Button btn_xacNhan, btn_dangKy;
    TextView tv_quenMatKhau;
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
                validatePhoneNumber(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInputPhoneVali() && isInputPassVali())  showEmptyPhoneDialog();
                if (isInputPhoneVali() && !isInputPassVali() ) showEmptyPassDialog();
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
    private boolean isInputPhoneVali() {
        String phone = ted_soDienThoai.getText().toString().trim();
        return TextUtils.isEmpty(phone) || phone.length() > 10;
    }
    private boolean isInputPassVali() {
        String pass = ted_matKhau.getText().toString().trim();
        return TextUtils.isEmpty(pass) || pass.length() < 6 ;
    }

    private void showEmptyPhoneDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Thông Báo");
        build.setIcon(R.drawable.ic_erorr);
        build.setMessage("bạn chưa nhập mật khẩu");
        build.setPositiveButton("OK", (dialog,which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = build.create();
        dialog.show();
    }
    private void showEmptyPassDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Thông Báo");
        build.setIcon(R.drawable.ic_erorr);
        build.setMessage("bạn chưa nhập số điện thoại");
        build.setPositiveButton("OK", (dialog,which) -> {
           dialog.dismiss();
        });
        AlertDialog dialog = build.create();
        dialog.show();
    }

    private void validatePhoneNumber(String phone){
        if ( phone.length() > 10 ){
            til_soDienThoai.setError("vượt quá 10 sô");
        }else if(TextUtils.isEmpty(phone)) {
            til_soDienThoai.setError("chưa nhập số điện thoại");
        }
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