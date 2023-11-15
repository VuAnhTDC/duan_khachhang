package com.example.duankhachhang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.example.duankhachhang.Adapter.ProductBanerAdapter;
import com.example.duankhachhang.Class.CartData;
import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.Image;
import com.example.duankhachhang.Class.LikeProductData;
import com.example.duankhachhang.Class.OrderData;
import com.example.duankhachhang.Class.ProductData;
import com.example.duankhachhang.Class.ShopData;
import com.example.duankhachhang.Dialog.CommentProductDialogFragment;
import com.example.duankhachhang.RecyclerView.ProductListHome_Adapter;
import com.example.duankhachhang.RecyclerView.RelatedProducts_Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detailproduct extends AppCompatActivity {
    Context context;
    Toolbar toolbar;
    ViewPager viewPagerImageProduct_DetailProduct;
    ImageView ivChatShop_DetailProduct, ivLikeProduct_DetailProduct;
    CircleImageView ivAvataShop_DetailProduct;
    TextView tvNameProduct_DetailProduct, tvPriceProduct_DetailProduct, tvquanlityProduct_DetailProduct, tvDescriptionProduct_DetailProduct, tvQuanlityLike_DetailProduct, tvQuanlityCmt_DetailProduct;
    Button btnBuyProduct_DetailProduct, btnAddCart_DetailProduct;
    LinearLayout vAvataShop_DetailProduct, vSumlike_DetailProduct, vSumCmt_DetailProduct;

    RecyclerView rcvRelatedProducts;
    RelatedProducts_Adapter relatedProductsAdapter;
    private ProductData productData = new ProductData();
    private ProductBanerAdapter productBanerAdapter;
    private ArrayList<String> arrUrlImage = new ArrayList<>();
    private boolean isClickLike = false;
    private boolean isProductInCart = false;
    private Customer customer = new Customer();
    private String idProduct = "";
    private boolean productIsOutOfStock = false;
    private boolean loadingFirt = false;
    private int countRelatedProducts = 0;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private ArrayList<ProductData> arrRelatedProducts = new ArrayList<>();
    private int ao = 0;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailproduct);
        context = this;
//        customer = new Customer("0123456789", "demo address", "Demo", null);
        setControl();
        setIniazation();
        getDataProduct();
        checkLikeProductOfCustomer();
        checkProductInCart();
        setEvent();
    }

    private void getDataProduct() {
        databaseReference = firebaseDatabase.getReference("Product/" + idProduct);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    productData = snapshot.getValue(ProductData.class);
                    if (productData.getQuanlityProduct() < 1) {
                        productIsOutOfStock = true;
                    }
                    getImageProduct();
                    getInformationProduct();
                    if (!loadingFirt) {
                        setAvataShop(productData.getIdUserProduct());
                        getRealtedProducts(productData.getKeyCategoryProduct(), 10);
                        loadingFirt = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //    hàm khởi tạo
    private void setIniazation() {
        Intent intent = getIntent();
        idProduct = intent.getStringExtra("idProduct");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        khởi tạo productbanner
        productBanerAdapter = new ProductBanerAdapter(arrUrlImage, context);
        viewPagerImageProduct_DetailProduct.setAdapter(productBanerAdapter);

//        gán giá trị adapter recyclerview sản phẩm liên quan
        relatedProductsAdapter = new RelatedProducts_Adapter(arrRelatedProducts, context);
        rcvRelatedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvRelatedProducts.setAdapter(relatedProductsAdapter);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float x = sensorEvent.values[0];
                    float y = sensorEvent.values[1];
                    float z = sensorEvent.values[2];
                    float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
                    if (acceleration> 30.0f) {
                      if (ao == 0){
                          isClickLike = !isClickLike;
                          ao = 1;
                          RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(R.drawable.icon_love_animation);
                          Target<Drawable> target = requestBuilder.into(ivLikeProduct_DetailProduct);
                          new Handler().postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  Glide.with(context).clear(target);
                                  ivLikeProduct_DetailProduct.setImageResource(R.drawable.icon_love);
                              }
                          }, 1000);
                          databaseReference = firebaseDatabase.getReference("LikeProduct");
                          LikeProductData likeProductData = new LikeProductData(customer.getId(), productData.getIdProduct());
                          databaseReference.child(customer.getId() + "/" + customer.getId() + productData.getIdProduct()).setValue(likeProductData);
                          productData.setSumLike(productData.getSumLike() + 1);
                          databaseReference = firebaseDatabase.getReference("Product");
                          databaseReference.child(productData.getIdProduct()).setValue(productData);

                      }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Huỷ đăng ký lắng nghe khi Activity bị huỷ
        if (sensorManager != null && sensorEventListener != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void getImageProduct() {
        databaseReference = firebaseDatabase.getReference("ImageProducts/"+idProduct);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot imageItem :
                            snapshot.getChildren()) {
                            arrUrlImage.add(imageItem.child("urlImage").getValue().toString());
                            productBanerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Lỗi lấy hình ảnh trong chi tiết sản phẩm");
            }
        });
    }


    private void checkLikeProductOfCustomer() {
        databaseReference = firebaseDatabase.getReference("LikeProduct");
        Query query = databaseReference.child(customer.getId() + "/" + customer.getId() + idProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isClickLike = true;
                    ivLikeProduct_DetailProduct.setImageResource(R.drawable.icon_love);
                    vSumCmt_DetailProduct.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkProductInCart() {
        databaseReference = firebaseDatabase.getReference("CartCustomer/" + customer.getId() + "/" + idProduct);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    btnAddCart_DetailProduct.setText("Đã thêm giỏ hàng");
                    btnAddCart_DetailProduct.setBackgroundResource(R.drawable.bg_button_press_01);
                    isProductInCart = true;
                }
                else {
                    btnAddCart_DetailProduct.setText("Thêm vào giỏ hàng");
                    btnAddCart_DetailProduct.setBackgroundResource(R.drawable.event_press_button_01);
                    isProductInCart = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getInformationProduct() {
        tvNameProduct_DetailProduct.setText(productData.getNameProduct());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormatVND = NumberFormat.getCurrencyInstance(locale);
        tvPriceProduct_DetailProduct.setText(numberFormatVND.format(productData.getPriceProduct()));
        if (productData.getQuanlityProduct() > 0) {
            tvquanlityProduct_DetailProduct.setText("Số lượng: " + productData.getQuanlityProduct());
        } else {
            tvquanlityProduct_DetailProduct.setText("Sản phẩm hết hàng");
        }
        tvDescriptionProduct_DetailProduct.setText(productData.getDescriptionProduct());
        tvQuanlityLike_DetailProduct.setText(productData.getSumLike() + "");
        tvQuanlityCmt_DetailProduct.setText(productData.getOverageCmtProduct() + "");

    }

    private void setAvataShop(String idShop) {
        databaseReference = firebaseDatabase.getReference("Shop/" + idShop);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ShopData shopData = snapshot.getValue(ShopData.class);
                    Picasso.get().load(shopData.getUrlImgShopAvatar()).placeholder(R.drawable.icon_personal).into(ivAvataShop_DetailProduct);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //    xuất danh sách sản phẩm liên quan
    private void getRealtedProducts(String keyCategoryProduct, int count) {
        databaseReference = firebaseDatabase.getReference("Product");
        Query query = databaseReference.orderByChild("keyCategoryProduct").equalTo(keyCategoryProduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item :
                            snapshot.getChildren()) {
                        if (count == countRelatedProducts) {
                            return;
                        }
                        ProductData productData1 = item.getValue(ProductData.class);
                        arrRelatedProducts.add(productData1);
                        relatedProductsAdapter.notifyDataSetChanged();
                        countRelatedProducts++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //    hàm ánh xạ
    private void setControl() {
        toolbar = findViewById(R.id.toolBar_DetalProduct);
        viewPagerImageProduct_DetailProduct = findViewById(R.id.viewPagerImageProduct_DetailProduct);
        ivChatShop_DetailProduct = findViewById(R.id.ivChatShop_DetailProduct);
        vAvataShop_DetailProduct = findViewById(R.id.vAvataShop_DetailProduct);
        vSumlike_DetailProduct = findViewById(R.id.vSumlike_DetailProduct);
        vSumCmt_DetailProduct = findViewById(R.id.vSumCmt_DetailProduct);
        tvNameProduct_DetailProduct = findViewById(R.id.tvNameProduct_DetailProduct);
        tvPriceProduct_DetailProduct = findViewById(R.id.tvPriceProduct_DetailProduct);
        tvquanlityProduct_DetailProduct = findViewById(R.id.tvquanlityProduct_DetailProduct);
        tvDescriptionProduct_DetailProduct = findViewById(R.id.tvDescriptionProduct_DetailProduct);
        btnAddCart_DetailProduct = findViewById(R.id.btnAddCart_DetailProduct);
        btnBuyProduct_DetailProduct = findViewById(R.id.btnBuyProduct_DetailProduct);
        tvQuanlityLike_DetailProduct = findViewById(R.id.tvQuanlityLike_DetailProduct);
        tvQuanlityCmt_DetailProduct = findViewById(R.id.tvQuanlityCmt_DetailProduct);
        ivLikeProduct_DetailProduct = findViewById(R.id.ivLikeProduct_DetailProduct);
        ivAvataShop_DetailProduct = findViewById(R.id.ivAvataShop_DetailProduct);
        rcvRelatedProducts = findViewById(R.id.rcvRelatedProducts);
    }

    //    hàm bắt sự kiện
    private void setEvent() {
        vSumlike_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vSumlike_DetailProduct.setEnabled(false);
                isClickLike = !isClickLike;
                if (!isClickLike) {
                    ao = 0;
                    ivLikeProduct_DetailProduct.setImageResource(R.drawable.icon_nolove);
                    databaseReference = firebaseDatabase.getReference("LikeProduct");
                    databaseReference.child(customer.getId() + "/" + customer.getId() + productData.getIdProduct()).removeValue();
                    productData.setSumLike(productData.getSumLike() - 1);
                    databaseReference = firebaseDatabase.getReference("Product");
                    databaseReference.child(productData.getIdProduct()).setValue(productData);
                } else {
                    ao = 1;
                    RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(R.drawable.icon_love_animation);
                    Target<Drawable> target = requestBuilder.into(ivLikeProduct_DetailProduct);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(context).clear(target);
                            ivLikeProduct_DetailProduct.setImageResource(R.drawable.icon_love);
                        }
                    }, 1000);
                    databaseReference = firebaseDatabase.getReference("LikeProduct");
                    LikeProductData likeProductData = new LikeProductData(customer.getId(), productData.getIdProduct());
                    databaseReference.child(customer.getId() + "/" + customer.getId() + productData.getIdProduct()).setValue(likeProductData);
                    productData.setSumLike(productData.getSumLike() + 1);
                    databaseReference = firebaseDatabase.getReference("Product");
                    databaseReference.child(productData.getIdProduct()).setValue(productData);
                }
                vSumlike_DetailProduct.setEnabled(true);
            }
        });
        vSumCmt_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentProductDialogFragment commentProductDialogFragment = new CommentProductDialogFragment(idProduct);
                commentProductDialogFragment.show(getSupportFragmentManager(), "Đánh giá");
            }
        });

        btnBuyProduct_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productIsOutOfStock) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Sản phẩm đã hết hàng");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ArrayList<OrderData> arrOrder = new ArrayList<>();
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String formattedDate = dateFormat.format(date);
                    int priceOrder = productData.getPriceProduct() * 1;
                    OrderData orderData = new OrderData(customer.getId() + idProduct, customer.getId(), idProduct, productData.getIdUserProduct(), "", 0, formattedDate, customer.getAddress(), customer.getId(), "", 1, priceOrder, "", "");
                    arrOrder.add(orderData);
                    Intent intent = new Intent(context, Buyproduct.class);
                    intent.putExtra("arrOrder", arrOrder);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnAddCart_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProductInCart) {
                    Intent intent = new Intent(context, CartCustomer.class);
                    startActivity(intent);
                } else {
                    CartData cartData = new CartData(productData.getIdProduct(), customer.getId(), productData.getIdProduct(), 1);
                    databaseReference = firebaseDatabase.getReference("CartCustomer");
                    databaseReference.child(customer.getId() + "/" + cartData.getIdCart()).setValue(cartData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    btnAddCart_DetailProduct.setText("Đã thêm giỏ hàng");
                                    btnAddCart_DetailProduct.setBackgroundResource(R.drawable.bg_button_press_01);
                                    Toast.makeText(context,"Thêm vào giỏ hàng thành công",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Thông báo");
                                    builder.setMessage("Thêm sản phẩm không thành công");
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT);
                                }
                            });
                }
            }
        });
        ivAvataShop_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailShop.class);
                intent.putExtra("idShop", productData.getIdUserProduct());
                startActivity(intent);
            }
        });
        ivChatShop_DetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("idUser",productData.getIdUserProduct());
                startActivity(intent);
            }
        });

    }
}