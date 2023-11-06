package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.CommentData;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentProduct_Adapter extends RecyclerView.Adapter<CommentProduct_ViewHolder> {

    private ArrayList<CommentData> arrCommentProduct = new ArrayList<>();
    private Context context;
    private String idProduct;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    public  CommentProduct_Adapter(Context context,ArrayList<CommentData> arrCommentProduct){
        this.context = context;
     this.arrCommentProduct = arrCommentProduct;
    }

    @NonNull
    @Override
    public CommentProduct_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentProduct_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_user_commentproduct,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentProduct_ViewHolder holder, int position) {
        CommentData commentData = arrCommentProduct.get(position);
        setInformationUserComment(commentData,holder);
    }

    private void setInformationUserComment(CommentData commentData,CommentProduct_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Customer").child(commentData.getIdCustomer());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Customer customer = snapshot.getValue(Customer.class);
                    holder.tvUserNameProduct_ItemUserComment.setText(customer.getName());
                    holder.tvContentCommet_ItemUserComment.setText(commentData.getContentComment());
                    holder.tvDateComment_ItemuserComment.setText(commentData.getDateComment());
                    if (!customer.getImageUser().isEmpty()){
                        Picasso.get().load(customer.getImageUser()).into(holder.ivAvataUserComment_ItemUserComment);
                    }
                    else {
                        holder.ivAvataUserComment_ItemUserComment.setImageResource(R.drawable.icon_personal);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return arrCommentProduct.size();
    }
}
