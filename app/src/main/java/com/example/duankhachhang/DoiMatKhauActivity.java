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
        ted_matKhauMoi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassNew(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void validatePassNew(String str){
        if (str.length() < 6){
            til_matKhauMoi.setError("không được nhập dưới 6 kí tự");
            til_matKhauMoi.setErrorIconDrawable(R.drawable.error);

        }else if(TextUtils.isEmpty(str)){
            til_matKhauMoi.setError("chưa nhập số điện thoại");
        }
    }
    private void showEmptyPassDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_erorr);
        builder.setTitle("Thông Báo Lỗi");
        builder.setMessage("chưa nhập dữ liệu");
        builder.setPositiveButton("OK",(dialog,which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void setControl() {
        til_matKhauMoi = findViewById(R.id.til_matKhauMoi);
        til_nhapLaiMatKhau = findViewById(R.id.til_nhapLaimatKhau);
        ted_matKhauMoi = findViewById(R.id.ted_matKhauMoi);
        ted_nhapLaijMatKhau = findViewById(R.id.ted_nhapLaimatKhau);
        btn_xacnhan = findViewById(R.id.btn_XacNhan_DoiMatKhau);
    }
}