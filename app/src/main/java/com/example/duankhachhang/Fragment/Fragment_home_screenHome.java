package com.example.duankhachhang.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.duankhachhang.Adapter.ProductBanerAdapter;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.R;

public class Fragment_home_screenHome extends Fragment {
    private Customer customer = new Customer();
    Context context;
    ViewPager viewPagerProductBanner_Home;
    RecyclerView rcvProductList_Home;
    private int count_Notification = 0;
    private int countUrl = 0;
    private int locationUrlImageProduct_ItemViewPager = 0;
    private int timeDeplay = 2000;
    private final Handler handler = new Handler();
    ProductBanerAdapter productBanerAdapter;

    public Fragment_home_screenHome() {
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.framelayout_itemhome_activity_screenhome,container,false);
        return view;
    }


}
