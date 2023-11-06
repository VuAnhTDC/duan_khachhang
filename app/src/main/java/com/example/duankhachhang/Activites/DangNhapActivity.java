package com.example.duankhachhang.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.duankhachhang.Models.Customer;
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
import com.google.firebase.database.Query;
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
                Log.d(TAG, "mã xác minh hoàng tất:" + phoneAuthCredential);
                Toast.makeText(DangNhapActivity.this, "gửi mã thành công", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "mã xác minh thất bại", e);
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
                Log.d(TAG, "onCodeSent:" + s);
                mVerificationId = s;
                mResendToken = forceResendingToken;
                Log.d(TAG, "Token:" + mResendToken);
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
                    reference = firebaseDatabase.getReference("Customer").child(phone);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Customer customer = snapshot.getValue(Customer.class);
                                System.out.println(customer.toString());
                                if (customer.getId()==phone){
                                    SharedPreferences shePreferences = getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = shePreferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(customer);
                                    System.out.println(json.toString());
                                    edit.putString("informationUserCustomer", json);
                                    edit.apply();
                                }
                            }else {
                                System.out.println("không có dữ liệu");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

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
                            Log.d(TAG, "signInWithCredential:       thành công");
                            Toast.makeText(DangNhapActivity.this, "đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:        thất bại", task.getException());

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
        if (user != null){
            String phone = user.getPhoneNumber();
            if (phone.equals(ted_soDienThoai.getText().toString())){
                startActivity(new Intent(DangNhapActivity.this, HomePageMainActivity.class));
            }else {
                Intent intent = new Intent(DangNhapActivity.this,NhapThongTinActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
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