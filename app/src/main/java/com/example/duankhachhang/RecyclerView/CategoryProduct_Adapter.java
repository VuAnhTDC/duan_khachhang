package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Category;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryProduct_Adapter extends RecyclerView.Adapter<CategoryProduct_ViewHolder> {
    ArrayList<Category> arrCategory = new ArrayList<>();
    Context context;
    CategoryProduct_Adapter.ButtonCategorySelection buttonCategorySelection;
    private int positionItemSelectionBefore = -1;
    public  interface ButtonCategorySelection{
        public void getPositionButtonSelectionInArrCategory(int position);
    }
    public CategoryProduct_Adapter(ArrayList<Category> arrCategory, Context context, ButtonCategorySelection buttonCategorySelection) {
        this.arrCategory = arrCategory;
        this.context = context;
        this.buttonCategorySelection = buttonCategorySelection;
    }

    @NonNull
    @Override
    public CategoryProduct_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryProduct_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_categoryproduct,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryProduct_ViewHolder holder, int position) {
        Category category = arrCategory.get(position);
        holder.btnCategoryProduct.setText(category.getNameCategory());
        holder.btnCategoryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCategorySelection.getPositionButtonSelectionInArrCategory(holder.getAdapterPosition());
                holder.btnCategoryProduct.setBackgroundResource(R.drawable.bg_button_press_01);
                notifyDataSetChanged();
            }
        });

    }

    private void setBackgroundButton(int position,View view){
        for (int i = 0;i<= arrCategory.size();i++){
            if (i == position){

            }
        }
    }

    @Override
    public int getItemCount() {
        return arrCategory.size();
    }
}
