package com.example.duankhachhang.RecyclerView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.duankhachhang.Fragment.FragmentOrderCancelled;
import com.example.duankhachhang.Fragment.FragmentOrderDelivered;
import com.example.duankhachhang.Fragment.FragmentOrderDelivering;
import com.example.duankhachhang.Fragment.FragmentOrderWaitForConfirm;
import com.example.duankhachhang.Fragment.FragmentOrderWaitForTakeGoods;


public class Order_ListViewPagerAdapter extends FragmentStatePagerAdapter {
    public Order_ListViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentOrderWaitForConfirm();
            case 1:
                return new FragmentOrderWaitForTakeGoods();
            case 2:
                return new FragmentOrderDelivering();
            case 3:
                return new FragmentOrderDelivered();
            case 4:
                return new FragmentOrderCancelled();
        }
        return new FragmentOrderWaitForConfirm();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Chờ xác nhận";
                break;
            case 1:
                title = "Chờ lấy hàng";
                break;
            case 2:
                title = "Đang giao";
                break;
            case 3:
                title = "Đã giao";
                break;
            case 4:
                title = "Đã hủy";
                break;
        }
        return title;
    }
}
