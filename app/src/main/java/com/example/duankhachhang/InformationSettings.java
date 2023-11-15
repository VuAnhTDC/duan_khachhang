package com.example.duankhachhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.ShowMessage;
import com.example.duankhachhang.Fragment.Fragment_personal_screenHome;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class InformationSettings extends AppCompatActivity {
    MaterialToolbar topAppBar_information_setting;
    TextInputLayout til_setting_name, til_setting_address;
    TextInputEditText ted_setting_name, ted_setting_address;
    Button btn_confirm;
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference;

    Customer customer = new Customer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_settings);
        context = this;
        setControl();
        setIntiazational();
        setEvent();
    }
    private void setIntiazational() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
        System.out.println("thông tin Customer từ SharedPreferences ở InformationSettings: " + customer.toString());
        ted_setting_name.setText(customer.getName());
        ted_setting_address.setText(customer.getAddress());
    }
    private void setEvent() {
        topAppBar_information_setting.setNavigationOnClickListener(view -> finish());
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ted_setting_name.getText().toString().isEmpty() || !ted_setting_address.getText().toString().isEmpty()){
                    final String name = ted_setting_name.getText().toString();
                    final String address = ted_setting_address.getText().toString();
                    upDateInformation(name,address);
                    reloadName(name);
                    ShowMessage.showMessage(context,"cập nhật thành công");
                }else {
                    ShowMessage.showMessage(context,"chưa nhập đủ thông tin");
                }
            }
        });
    }
    private void upDateInformation(String name, String address ){
        // cap nhat thong tin vao SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        Gson gsonnew = new Gson();
        Customer customer = gson.fromJson(jsonShop, Customer.class);
        editor.putString("name", name);
        editor.putString("address", address);
        customer.setName(name);
        customer.setAddress(address);
        String jsonnew = gsonnew.toJson(customer);
        editor.putString("informationUserCustomer", jsonnew);
        editor.commit();
        editor.apply();
        // cap nhat thong tin vaof firebase Realtime
        reference = firebaseDatabase.getReference("Customer");
        reference.child(customer.getId()).setValue(customer);
    }

    public static void reloadName(String name){
        Fragment_personal_screenHome.tv_nameCustomer.setText(name);
    }
    private void setControl() {
        topAppBar_information_setting = findViewById(R.id.topAppBar_like_product);
        til_setting_name = findViewById(R.id.til_HoTen_InformationSetting);
        ted_setting_name = findViewById(R.id.ted_HoTen_InformationSetting);
        til_setting_address = findViewById(R.id.til_DiaChi_InformationSetting);
        ted_setting_address = findViewById(R.id.ted_DiaChi_InformationSetting);
        btn_confirm = findViewById(R.id.btn_xacNhan_InformationSetting);
    }
}