package com.example.duankhachhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductBanerAdapter extends PagerAdapter {
    ArrayList<String> arrUrl = new ArrayList<>();
    Context context;

    public ProductBanerAdapter(ArrayList<String> arrUrl, Context context) {
        this.arrUrl = arrUrl;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        View view = LayoutInflater.from(context).inflate(R.layout.customer_productitem_screenhome,null);
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Picasso.get().load(arrUrl.get(position)).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }
}
