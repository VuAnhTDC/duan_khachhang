package com.example.duankhachhang.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.duankhachhang.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NhapThongTinActivity extends AppCompatActivity {

    TextInputLayout til_phone,til_name,til_address;
    TextInputEditText ted_phone,ted_name,ted_address;
    Button btn_confirm;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_thong_tin);
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
                    Toast.makeText(NhapThongTinActivity.this, "chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phone)){
                                Toast.makeText(NhapThongTinActivity.this, "số đã được đăng ký", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child("Customer").child(phone).child("id").setValue(phone);
                                databaseReference.child("Customer").child(phone).child("imageUser").setValue("https://firebasestorage.googleapis.com/v0/b/duandd2.appspot.com/o/imageUserKhachHang%2Favatar1.jpg?alt=media&token=922b175b-f203-44e7-985e-f0b057465e37&_gl=1*1cut3mc*_ga*MTU3MTIzMDY2Ni4xNjgxMTE5NjUz*_ga_CW55HF8NVT*MTY5ODczMTYxNS4zNS4xLjE2OTg3MzgwNjkuNTEuMC4w");
                                databaseReference.child("Customer").child(phone).child("name").setValue(name);
                                databaseReference.child("Customer").child(phone).child("address").setValue(address);
                                Toast.makeText(NhapThongTinActivity.this, "tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
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
        til_phone = findViewById(R.id.til_soDienThoai_userDetail);
        til_name = findViewById(R.id.til_HoTen_userDetail);
        til_address = findViewById(R.id.til_DiaChi_userDetail);
        ted_phone = findViewById(R.id.edt_soDienThoai_userDetail);
        ted_name = findViewById(R.id.ted_HoTen_userDetail);
        ted_address = findViewById(R.id.ted_DiaChi_userDetail);
        btn_confirm = findViewById(R.id.btn_xacNhan_UserDetail);
    }
}