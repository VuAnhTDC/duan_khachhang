package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.LikeProductData;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Detailproduct;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class LikeProductAdapter extends RecyclerView.Adapter<LikeProductViewHolder> {

    ArrayList<LikeProductData> list = new ArrayList<>();
    Context context;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public LikeProductAdapter(ArrayList<LikeProductData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LikeProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LikeProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_like_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikeProductViewHolder holder, int position) {
        LikeProductData likeProductData = list.get(position);
        setInformationLikeProductItem(likeProductData.getIdProduct_LikeProduct(),holder);
        setImageProduct(likeProductData.getIdProduct_LikeProduct(),holder);
        holder.card_item_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detailproduct.class);
                intent.putExtra("idProduct", likeProductData.getIdProduct_LikeProduct());
                context.startActivity(intent);
            }
        });
    }
    private void setInformationLikeProductItem(String idProduct, LikeProductViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product").child(idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData product = snapshot.getValue(ProductData.class);
                    holder.tv_name.setText(product.getNameProduct());
                    Locale locale = new Locale("vi","VN");
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                    holder.tv_price.setText(numberFormat.format(product.getPriceProduct())+" VND");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi khi đọc dữ liệu từ cơ sở dữ liệu (chi tiết sản phẩm đã thích): " + error.getMessage());
            }
        });
    }
    private void setInformationLikeProducts(String idCustomer ,LikeProductViewHolder holder){
        databaseReference = firebaseDatabase.getReference("LikeProduct/"+idCustomer);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot itemLike: snapshot.getChildren()){
                        LikeProductData likeProduct = itemLike.getValue(LikeProductData.class);
                        setInformationLikeProductItem(likeProduct.getIdProduct_LikeProduct(),holder);
                        setImageProduct(likeProduct.getIdProduct_LikeProduct(), holder);
                    }
                }else {
                    System.out.println("không tìm thấy " + idCustomer );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi khi đọc dữ liệu từ cơ sở dữ liệu (các sản phẩm đã thích): " + error.getMessage());
            }
        });
    }

    private void setImageProduct(String idProduct, LikeProductViewHolder holder){
        databaseReference = firebaseDatabase.getReference("ImageProducts/" +idProduct+"/1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String urlImageFirst = snapshot.child("urlImage").getValue().toString();
                    Picasso.get().load(urlImageFirst).placeholder(R.drawable.icondowload).into(holder.img_product);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi khi đọc dữ liệu từ cơ sở dữ liệu (Hình Ảnh): " + error.getMessage());
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}

class LikeProductViewHolder extends RecyclerView.ViewHolder{
    ImageView img_product,img_like;
    CardView card_item_product;
    TextView tv_name,tv_price;

    public LikeProductViewHolder(@NonNull View itemView) {
        super(itemView);
        img_product = itemView.findViewById(R.id.img_like_product);
        img_like = itemView.findViewById(R.id.img_like_item);
        card_item_product = itemView.findViewById(R.id.card_like_product);
        tv_name = itemView.findViewById(R.id.tv_name_like_product);
        tv_price = itemView.findViewById(R.id.tv_price_like_product);
    }
}

