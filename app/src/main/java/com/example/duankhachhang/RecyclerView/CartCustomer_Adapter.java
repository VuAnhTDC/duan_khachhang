package com.example.duankhachhang.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duankhachhang.CartCustomer;
import com.example.duankhachhang.Class.CartData;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartCustomer_Adapter extends RecyclerView.Adapter<CartCustomer_ViewHolder> {
    private ArrayList<CartData> arrCartData = new ArrayList<>();
    private Context context ;
    private Customer customer = new Customer();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    public CartCustomer_Adapter(ArrayList<CartData> arrCartData, Context context){
        this.arrCartData = arrCartData;
        this.context = context;
        customer = new Customer("0123456789", "demo address", "Demo", null);
    }
    @NonNull
    @Override
    public CartCustomer_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartCustomer_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_cartcustomer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartCustomer_ViewHolder holder, int position) {
        CartData cartData = arrCartData.get(position);
        holder.tvQuanlityProduct.setText(cartData.getQuanlityProduct_Cart() + "");
        setInformationProductItemCart(cartData.getIdProduct(),holder);
//        Tăng số lượng sản phẩm trong giỏ hàng
        holder.ivAddQuanlity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quanlity = cartData.getQuanlityProduct_Cart();
                if(quanlity >= 1 && quanlity <=99){
                    quanlity ++;
                    cartData.setQuanlityProduct_Cart(quanlity);
                    setItemCart(cartData);
                }
            }
        });

//        Giảm số lượng sản phẩm trong giỏ hàng
        holder.ivSubtractionQuanlity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quanlity = cartData.getQuanlityProduct_Cart();
                if(quanlity  >1){
                    quanlity --;
                    cartData.setQuanlityProduct_Cart(quanlity);
                    setItemCart(cartData);
                }
            }
        });

    }

//     lưu thông tin thay đổi giỏ hàng của khách hàng
    private void setItemCart(CartData cartData){
        databaseReference = firebaseDatabase.getReference("CartCustomer/" +customer.getId()+"/"+cartData.getIdProduct());
        databaseReference.setValue(cartData);
    }

//    lấy thông tin sản phẩm
    private void setInformationProductItemCart(String idProduct, CartCustomer_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/"+idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    holder.tvProductName.setText(productData.getNameProduct());
                    Locale locale = new Locale("vi","VN");
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
                    holder.tvPriceProduct.setText(numberFormat.format(productData.getPriceProduct()));
                    setInformationShopData(productData.getIdUserProduct(),holder);
                    setImageProduct(productData.getIdProduct(),holder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("lỗi lấy thông tin sản phẩm trong item cart: " + error.getMessage());
            }
        });
    }

//    lấy hình sản phẩm
    private void setImageProduct(String idProduct, CartCustomer_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("ImageProducts");
        Query query = databaseReference.orderByChild("idProduct").equalTo(idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot itemImage:
                         snapshot.getChildren()) {
                        Image image = itemImage.getValue(Image.class);
                        Picasso.get().load(image.getUrlImage()).placeholder(R.drawable.icondowload).into(holder.ivProduct_ItemCartCustomer);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    Lấy thông tin shop
    private void setInformationShopData(String idShopData, CartCustomer_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Shop/" + idShopData);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    Picasso.get().load(shopData.getUrlImgShopAvatar()).placeholder(R.drawable.icondowload).into(holder.ivAvataShop);
                    holder.tvShopName.setText(shopData.getShopName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi lấy thông tin shop trong item cart: " +error.getMessage() );
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrCartData.size();
    }
}
