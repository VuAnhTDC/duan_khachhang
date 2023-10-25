package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class QuenMatKhauActivity extends AppCompatActivity {

    TextInputLayout til_soDienThoai,til_OTP;
    TextInputEditText ted_soDienThoai,ted_OTP;
    Button btn_xacNhan;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://duandd2-default-rtdb.asia-southeast1.firebasedatabase.app");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String idSMS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        setControl();
        setEven();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setEven() {
        til_soDienThoai.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String soDT = ted_soDienThoai.getText().toString().trim();

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+84" + soDT.substring(1))
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(QuenMatKhauActivity.this)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        Toast.makeText(QuenMatKhauActivity.this, "mã đang được gửi tới ", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(QuenMatKhauActivity.this, "đã xảy ra lỗi trong quá trình gửi mã ", Toast.LENGTH_SHORT).show();
                                        Log.e("Error", e.getMessage());
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(s, forceResendingToken);
                                        idSMS = s;
                                    }
                                })
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        btn_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(QuenMatKhauActivity.this, DoiMatKhauActivity.class));
            }
        });
    }

    private void setControl() {
        til_soDienThoai = findViewById(R.id.til_soDienThoai_QuenMatKhau);
        til_OTP = findViewById(R.id.til_OTP);
        ted_soDienThoai = findViewById(R.id.ted_soDienThoai);
        ted_OTP = findViewById(R.id.ted_OTP);
        btn_xacNhan = findViewById(R.id.btn_xacNhan_QuenMatKhau);
    }
}