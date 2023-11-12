package com.example.duankhachhang.Fragment;

import android.content.Intent;
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

import com.example.duankhachhang.LikedproductActivity;
import com.example.duankhachhang.R;
import com.google.firebase.auth.FirebaseAuth;


public class Fragment_personal_screenHome extends Fragment {

    private View view;
    Button btn_purchaseOrder, btn_likeProduct,btn_settingAddress,btn_LogOut;
    ImageView img_avatarCustomer, img_addPhoto;
    TextView tv_nameCustomer;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.framelayout_itempersonal_activity_screenhome,container,false);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        btn_likeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LikedproductActivity.class);
                startActivity(intent);
            }
        });
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