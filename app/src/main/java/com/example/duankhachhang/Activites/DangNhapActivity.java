package com.example.duankhachhang.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.duankhachhang.Home;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class DangNhapActivity extends AppCompatActivity {
    private static final String TAG = "DangNhapActivity";
    TextInputLayout til_soDienThoai, til_OTP;
    TextInputEditText ted_soDienThoai, ted_OTP;
    Button btn_xacNhan,btn_resendOTP,btn_gui;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private DatabaseReference reference;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        setControll();
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("mã xác minh hoàng tất:" + phoneAuthCredential);
                Toast.makeText(DangNhapActivity.this, "gửi mã thành công", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                System.out.println("mã xác minh thất bại " + e);
                Toast.makeText(DangNhapActivity.this, "gửi mã không thành công", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e(TAG,"yêu cầu không hợp lệ: " + e.getMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.e(TAG,"Hạn ngạch SMS cho dự án đã bị vượt quá: "+ e.getMessage());
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    Log.e(TAG,"Đã thử xác minh reCAPTCHA với Hoạt động rỗng: "+ e.getMessage());
                }
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mResendToken = forceResendingToken;
                System.out.println("onCodeSent:" + s);
                System.out.println("Token:" + mResendToken);
            }
        };
        setEvent();
    }

    private void setEvent() {
        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = ted_soDienThoai.getText().toString();
                startPhoneNumberVerification(phone);
            }
        });
        btn_xacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = ted_soDienThoai.getText().toString();
                final String code = ted_OTP.getText().toString();
                if (phone.isEmpty() && code.isEmpty()){
                    Toast.makeText(DangNhapActivity.this, "banj chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    verifyPhoneNumberWithCode(mVerificationId,code);
                }

            }
        });
        btn_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = ted_soDienThoai.getText().toString();
                resendVerificationCode(phone,mResendToken);
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber.substring(1))
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Tag" + " : đăng nhập thành công");
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        } else {
                            System.out.println("Tag" + " : đăng nhập thất bại: " + task.getException());
                        }
                    }
                });
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+phoneNumber.substring(1))
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void updateUI(FirebaseUser user) {
        String phone = user.getPhoneNumber();
        if (user != null){
            reference = FirebaseDatabase.getInstance().getReference("Customer").child(phone);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Intent intent = new Intent(DangNhapActivity.this, Home.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(DangNhapActivity.this, NhapThongTinActivity.class);
                        intent.putExtra("phoneUser",phone);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void setControll() {
        til_soDienThoai = findViewById(R.id.til_soDienThoai_login);
        til_OTP = findViewById(R.id.til_OTP_Login);
        ted_soDienThoai = findViewById(R.id.edt_soDienThoai_login);
        ted_OTP = findViewById(R.id.edt_OTP_login);
        btn_xacNhan = findViewById(R.id.btn_xacNhan_login);
        btn_resendOTP = findViewById(R.id.btn_resend_login);
        btn_gui = findViewById(R.id.btn_gui_login);
    }
}