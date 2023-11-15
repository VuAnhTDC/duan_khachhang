package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.duankhachhang.Class.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

public class EnterInformationActivity extends AppCompatActivity {

    TextInputLayout til_phone,til_name,til_address;
    TextInputEditText ted_phone,ted_name,ted_address;
    Button btn_confirm;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    boolean isLogin = false;
    String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_information);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Intent intent = getIntent();
        final String phoneUer = intent.getStringExtra("phoneUser");
        System.out.println(phoneUer);
        ted_phone.setText(phoneUer);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = ted_phone.getText().toString();
                final String name = ted_name.getText().toString();
                final String address = ted_address.getText().toString();
                if (phone.isEmpty() || name.isEmpty() || address.isEmpty()){
                    Toast.makeText(EnterInformationActivity.this, "chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phone)){
                                Toast.makeText(EnterInformationActivity.this, "số đã được đăng ký", Toast.LENGTH_SHORT).show();
                            }else {
                                isLogin = true;
                                String avatar = "https://firebasestorage.googleapis.com/v0/b/duandd2.appspot.com/o/imageUserKhachHang%2Favatar1.jpg?alt=media&token=922b175b-f203-44e7-985e-f0b057465e37&_gl=1*1cut3mc*_ga*MTU3MTIzMDY2Ni4xNjgxMTE5NjUz*_ga_CW55HF8NVT*MTY5ODczMTYxNS4zNS4xLjE2OTg3MzgwNjkuNTEuMC4w";
                                databaseReference.child("Customer").child(phone).child("id").setValue(phone);
                                databaseReference.child("Customer").child(phone).child("imageUser").setValue(avatar);
                                databaseReference.child("Customer").child(phone).child("name").setValue(name.trim());
                                databaseReference.child("Customer").child(phone).child("address").setValue(address.trim());
                                Toast.makeText(EnterInformationActivity.this, "tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                                getAndSaveFCMToken(phone);
                                Customer customer = new Customer(phone,address,name,avatar,fcmToken);
                                saveSharedPreferences(customer);
                                Intent intent1 = new Intent(EnterInformationActivity.this, Home.class);
                                startActivity(intent1);
                                finish();
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
    }
    private void getAndSaveFCMToken(String idCustomer) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            fcmToken = task.getResult();
                            // Lưu FCM Token vào SharedPreferences hoặc gửi lên máy chủ
                            saveFCMTokenToSharedPreferences(fcmToken);
                            //Update fcmToken vào tài khoản đã đăng nhập thành công
                            databaseReference.child("Customer").child(idCustomer).child("fcmToken").setValue(fcmToken);

                        } else {
                            System.out.println("Không lấy và lưu được fcm token");
                        }
                    }
                });
    }
    private void saveFCMTokenToSharedPreferences(String fcmToken) {
        //Lưu FCM Token vào SharedPreferences
        SharedPreferences preferences = getSharedPreferences("myFCMToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcmToken", fcmToken);
        editor.commit();
        editor.apply();
    }

    private void setControl() {
        til_phone = findViewById(R.id.til_soDienThoai_userDetail);
        til_name = findViewById(R.id.til_HoTen_userDetail);
        til_address = findViewById(R.id.til_DiaChi_userDetail);
        ted_phone = findViewById(R.id.edt_soDienThoai_userDetail);
        ted_name = findViewById(R.id.ted_HoTen_userDetail);
        ted_address = findViewById(R.id.ted_DiaChi_userDetail);
        btn_confirm = findViewById(R.id.btn_xacNhan_UserDetail);
    }
}