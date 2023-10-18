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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class QuenMatKhauActivity extends AppCompatActivity {

    TextInputLayout til_soDienThoai,til_OTP;
    TextInputEditText ted_soDienThoai,ted_OTP;
    Button btn_xacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        setControl();
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
        til_soDienThoai.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputPhoneValid();
            }
        });
        btn_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(QuenMatKhauActivity.this, DoiMatKhauActivity.class));
            }
        });
    }

    private void inputPhoneValid() {
        String phone = ted_soDienThoai.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            showEmptyPhoneDialog();
        }else if(phone.length() > 10){
            showErorrPhoneDialog();
        }
    }

    private void validatePhoneNumber(String phone){
        if (phone.length() > 10){
            til_soDienThoai.setError("nhập hơn 10 số");
        }else if(TextUtils.isEmpty(phone)){
            til_soDienThoai.setError("chưa nhập số điện thoại");
        }
    }

    private void showEmptyPhoneDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_erorr);
        builder.setTitle("Thông Báo Lỗi");
        builder.setMessage("chưa nhập số điện thoại");
        builder.setPositiveButton("OK",(dialog,which) -> {
           dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showErorrPhoneDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_erorr);
        builder.setTitle("Thông Báo Lỗi");
        builder.setMessage("nhập hơn 10 số điện thoại");
        builder.setPositiveButton("OK",(dialog,which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setControl() {
        til_soDienThoai = findViewById(R.id.til_soDienThoai_QuenMatKhau);
        til_OTP = findViewById(R.id.til_OTP);
        ted_soDienThoai = findViewById(R.id.ted_soDienThoai);
        ted_OTP = findViewById(R.id.ted_OTP);
        btn_xacNhan = findViewById(R.id.btn_xacNhan_QuenMatKhau);
    }
}