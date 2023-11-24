package com.example.duankhachhang.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.Dialog.EditQuanlityItemOrderProductDialogFragment;
import com.example.duankhachhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProduct_Adapter extends RecyclerView.Adapter<OrderProduct_ViewHolder> {
    private ArrayList<OrderData> arrOrderData = new ArrayList<>();
    private Context context;
    OrderProduct_Adapter.SetQuanlityProduct setQuanlityProduct;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;

    public interface SetQuanlityProduct{
        public void getQuanlytiProduct(int price,int positon,int quanlity);
    }

    public OrderProduct_Adapter(ArrayList<OrderData> arrOrderData, Context context,OrderProduct_Adapter.SetQuanlityProduct setQuanlityProduct){
        this.arrOrderData = arrOrderData;
        this.context = context;
        this.setQuanlityProduct =setQuanlityProduct;
    }
    @NonNull
    @Override
    public OrderProduct_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new OrderProduct_ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item_orderproduct_pay,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProduct_ViewHolder holder, int position) {
       OrderData orderData = arrOrderData.get(position);
        setInformationShop(orderData.getIdShop_Order(),holder);
        setInformationProduct(orderData,holder);
        setImageProductItem(orderData,holder);
     holder.tvQuanlityProduct_OrderProduct.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             EditQuanlityItemOrderProductDialogFragment.EditQuanlityOrderProductItem editQuanlityOrderProductItem = new EditQuanlityItemOrderProductDialogFragment.EditQuanlityOrderProductItem() {
                 @Override
                 public void getQuanlityOrderProductItem(int quanlityOrderProductItem) {
                     if (quanlityOrderProductItem > 0){
                         databaseReference = firebaseDatabase.getReference("Product/"+orderData.getIdShop_Order()+"/"+orderData.getIdProduct_Order());
                         databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 if (snapshot.exists()){
                                     ProductData productData =  snapshot.getValue(ProductData.class);
                                     if ((productData.getQuanlityProduct() - quanlityOrderProductItem) >=0){
                                         orderData.setQuanlity_Order(quanlityOrderProductItem);
                                         holder.tvQuanlityProduct_OrderProduct.setText("Số lượng: "+ orderData.getQuanlity_Order());
                                         Toast.makeText(context,"Thay đổi số lượng sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                         int price = productData.getPriceProduct()*quanlityOrderProductItem;
                                         setQuanlityProduct.getQuanlytiProduct(price,holder.getAdapterPosition(),quanlityOrderProductItem);
                                     }
                                     else {
                                         AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                         builder.setTitle("Thông báo");
                                         builder.setMessage("Số lượng sản phẩm hiện tại chỉ còn " + productData.getQuanlityProduct() + ". Vui lòng chọn lại số lượng");
                                         builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                 dialogInterface.dismiss();
                                             }
                                         });

                                         AlertDialog alertDialog = builder.create();
                                         alertDialog.show();
                                     }
                                 }
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });
                     }
                 }
             };
             EditQuanlityItemOrderProductDialogFragment editQuanlityItemOrderProductDialogFragment = new EditQuanlityItemOrderProductDialogFragment(editQuanlityOrderProductItem);
             editQuanlityItemOrderProductDialogFragment.show(((AppCompatActivity)context).getSupportFragmentManager(),"Sửa số lượng sản phẩm");
         }
     });

    }

    private void setInformationShop(String idShop, OrderProduct_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Shop/"+idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    if (shopData.getUrlImgShopAvatar().isEmpty()){
                        holder.ivAvataShop_ItemOrderProduct.setImageResource(R.drawable.icon_personal);
                    }
                    else {
                        Picasso.get().load(shopData.getUrlImgShopAvatar()).into(holder.ivAvataShop_ItemOrderProduct);
                    }
                    holder.tvNameShop_ItemOrderProduct.setText(shopData.getShopName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi khi lấy data shop trong danh sách Order product: " + error.getMessage());
            }
        });

    }
    private void setImageProductItem(OrderData orderData, OrderProduct_ViewHolder holder){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ImageProducts/"+orderData.getIdProduct_Order()+"/1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String urlImageFirst = snapshot.child("urlImage").getValue().toString();
                    Picasso.get().load(urlImageFirst).placeholder(R.drawable.icondowload).into(holder.ivProduct_ItemOrderProduct);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setInformationProduct(OrderData orderData, OrderProduct_ViewHolder holder){
        databaseReference = firebaseDatabase.getReference("Product/" + orderData.getIdShop_Order() + "/"+orderData.getIdProduct_Order());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData productData = snapshot.getValue(ProductData.class);
                    holder.tvNameProduct_OrderProduct.setText(productData.getNameProduct());
                    holder.tvPriceProduct_ItemOrderProduct.setText(productData.getPriceProduct() + " VND");
                    holder.tvQuanlityProduct_OrderProduct.setText("Số lượng: " + orderData.getQuanlity_Order());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Xảy ra lỗi trong qua trình update giao diện sản phẩm lên RecyclerView");
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrOrderData.size();
    }


}
