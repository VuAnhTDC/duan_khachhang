package com.example.duankhachhang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duankhachhang.LogInActivity;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.InformationSettings;
import com.example.duankhachhang.Likedproduct;
import com.example.duankhachhang.PurchaseOrder;
import com.example.duankhachhang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class Fragment_personal_screenHome extends Fragment {

    private View view;
    Button btn_purchaseOrder, btn_likeProduct,btn_settingAddress,btn_LogOut;
    ImageView img_avatarCustomer, img_addPhoto;
    public static TextView tv_nameCustomer;
    private  FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Customer customer;
    Context context;
    boolean isLogin = false;

    public Fragment_personal_screenHome(){
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.framelayout_itempersonal_activity_screenhome,container,false);
        Customer customer1 = (Customer) getArguments().getSerializable("customer");
        SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences1.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
        setControl();
        setInitiazation();
        setEvent();
        return view;
    }
    private void setInitiazation() {
        Picasso.get().load(customer.getImageUser()).placeholder(R.drawable.icon_personal).into(img_avatarCustomer);
        tv_nameCustomer.setText(customer.getName());
    }

    private void setEvent() {
        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Logout();
            }
        });
        btn_likeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Likedproduct.class);
                startActivity(intent);
            }
        });
        btn_purchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PurchaseOrder.class);
                startActivity(intent);
            }
        });
        btn_settingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InformationSettings.class);
                startActivity(intent);
            }
        });
    }

    private void Logout(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        SharedPreferences shePreferencesLogin = getContext().getSharedPreferences("UserLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorLogin = shePreferencesLogin.edit();
        editorLogin.putBoolean("isLogin",isLogin);
        editorLogin.commit();
        editorLogin.apply();
        Intent intent = new Intent(getActivity(), LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();

    }

    private void setControl() {
        btn_purchaseOrder = view.findViewById(R.id.btn_purchase_order);
        btn_likeProduct = view.findViewById(R.id.btn_like_product);
        btn_settingAddress = view.findViewById(R.id.btn_setting_address);
        btn_LogOut = view.findViewById(R.id.btn_Log_out);
        img_avatarCustomer = view.findViewById(R.id.img_customer);
        img_addPhoto = view.findViewById(R.id.img_add_photo);
        tv_nameCustomer = view.findViewById(R.id.tv_name_customer);
    }

}