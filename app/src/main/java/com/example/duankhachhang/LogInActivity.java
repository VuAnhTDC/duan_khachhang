package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ShowMessage;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.TimeUnit;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LogInActivity";
    TextInputLayout til_soDienThoai, til_OTP;
    TextInputEditText ted_soDienThoai, ted_OTP;
    Button btn_xacNhan,btn_resendOTP,btn_gui;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private DatabaseReference reference;
    private Context context;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean isLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setControll();
        setEvent();
        context = this;
        ShowMessage.context = this;
        createChanelNotification();
        getAndSaveFCMToken();
    }
    private void createChanelNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("thongbao","Thong bao", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void getAndSaveFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String fcmToken = task.getResult();
                            System.out.println("fcmtoken: " + fcmToken);
                        } else {
                            System.out.println("Không lấy và lưu được fcm token");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private boolean isLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserLogin",Context.MODE_PRIVATE);
        boolean login = sharedPreferences.getBoolean("isLogin",false);
        if (login){
            return true;
        }else {
            return false;
        }
    }

    private void setEvent() {
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("mã xác minh hoàng tất:" + phoneAuthCredential);
                Toast.makeText(LogInActivity.this, "gửi mã thành công", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                System.out.println("mã xác minh thất bại " + e);
                Toast.makeText(LogInActivity.this, "gửi mã không thành công", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LogInActivity.this, "bạn chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
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
        if (user != null){
            String phoneNumber = user.getPhoneNumber();
            System.out.println("updateUI  phoneNumber: " + phoneNumber);
            String phone = "0"+phoneNumber.substring(3);
            System.out.println("updateUI  phone: " + phone);
            if (phone != null){
                reference = firebaseDatabase.getReference("Customer/"+phone);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            isLogin = true;
                            Customer customer1 = snapshot.getValue(Customer.class);
//                            System.out.println("updateUI  customer: " + customer1.toString());
                            saveSharedPreferences(customer1);
                            Intent intent = new Intent(LogInActivity.this, Home.class);
                            intent.putExtra("customer",customer1);
                            startActivity(intent);
                            finish();
                        }else {
                            System.out.println("updateUI  dữ liệu không tồn tại ");
                            Intent intent = new Intent(LogInActivity.this, EnterInformationActivity.class);
                            intent.putExtra("phoneUser", ted_soDienThoai.getText().toString());
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
    }
    private void saveSharedPreferences(Customer customer){
        SharedPreferences shePreferencesCustomer = getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        SharedPreferences shePreferencesLogin = getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editCustomer = shePreferencesCustomer.edit();
        SharedPreferences.Editor editLogin = shePreferencesLogin.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        System.out.println(json.toString());
        editCustomer.putString("informationUserCustomer", json);
        editCustomer.putString("numberphone", customer.getId());
        editCustomer.putString("name", customer.getName());
        editCustomer.putString("image", customer.getImageUser());
        editCustomer.putString("address", customer.getAddress());
        editCustomer.commit();
        editCustomer.apply();
        editLogin.putBoolean("isLogin", isLogin);
        editLogin.commit();
        editLogin.apply();
        SharedPreferences preferences = getSharedPreferences("myFCMToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcmToken", customer.getFcmToken());
        editCustomer.commit();
        editor.apply();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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