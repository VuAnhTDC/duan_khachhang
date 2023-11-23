package com.example.duankhachhang.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartCustomer_Adapter extends RecyclerView.Adapter<CartCustomer_ViewHolder> {
    public  interface CartDataSelectionItem_CartCustomer{
        public int getCartDataSelection(int position);
        public int getCartDataSelection_Cancel(int positon);
    }

    private CartCustomer_Adapter.CartDataSelectionItem_CartCustomer cartDataSelectionItemCartCustomer;
    private ArrayList<CartData> arrCartData = new ArrayList<>();
    private Context context ;
    private Customer customer = new Customer();
    private  int quanlity = 0;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    public CartCustomer_Adapter(ArrayList<CartData> arrCartData, Context context, CartDataSelectionItem_CartCustomer cartDataSelectionItemCartCustomer){
        this.arrCartData = arrCartData;
        this.context = context;
        this.cartDataSelectionItemCartCustomer = cartDataSelectionItemCartCustomer;
        SharedPreferences sharedPreferences = context.getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
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
        setInformationProductItemCart(cartData,holder);
//        Tăng số lượng sản phẩm trong giỏ hàng
        holder.ivAddQuanlity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 quanlity = cartData.getQuanlityProduct_Cart();
                if(quanlity >= 1 && quanlity <=99){
                    quanlity++;
                    checkQuanlityProduct(cartData,holder);
                }
            }
        });

//        Giảm số lượng sản phẩm trong giỏ hàng
        holder.ivSubtractionQuanlity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 quanlity = cartData.getQuanlityProduct_Cart();
                if(quanlity  >1){
                    quanlity --;
                    cartData.setQuanlityProduct_Cart(quanlity);
                    setItemCart(cartData);
                    holder.cbxSelectionItem.setChecked(false);
                }
            }
        });

        holder.cbxSelectionItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                   cartDataSelectionItemCartCustomer.getCartDataSelection(holder.getAdapterPosition());
                }
                else {
                    cartDataSelectionItemCartCustomer.getCartDataSelection_Cancel(holder.getAdapterPosition());
                }
            }
        });

    }

    private void checkQuanlityProduct(CartData cartData,CartCustomer_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/"+cartData.getIdShop()+"/"+cartData.getIdProduct());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    if (productData.getQuanlityProduct() < quanlity){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Sản phẩm hiển chỉ còn: " + productData.getQuanlityProduct() +" .Thật lòng xin lỗi vì đã không áp ứng đủ số lượng bạn cần");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                    else {
                        cartData.setQuanlityProduct_Cart(quanlity);
                        setItemCart(cartData);
                        holder.cbxSelectionItem.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//     lưu thông tin thay đổi giỏ hàng của khách hàng
    private void setItemCart(CartData cartData){
        databaseReference = firebaseDatabase.getReference("CartCustomer/" +customer.getId()+"/"+cartData.getIdProduct());
        databaseReference.setValue(cartData);
    }

//    lấy thông tin sản phẩm
    private void setInformationProductItemCart(CartData cartData, CartCustomer_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/"+cartData.getIdShop()+"/"+cartData.getIdProduct());
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
        databaseReference = firebaseDatabase.getReference("ImageProducts/"+idProduct+"/1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   String urlImage =  snapshot.child("urlImage").getValue().toString();
                   Picasso.get().load(urlImage).placeholder(R.drawable.icondowload).into(holder.ivProduct_ItemCartCustomer);
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
