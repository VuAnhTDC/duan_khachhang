package com.example.duankhachhang.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.Customer;
import com.example.duankhachhang.Class.Voucher;
import com.example.duankhachhang.Class.VoucherCustomer;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.VoucherCustomerHome_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyVoucherCustomerDialogFragment extends DialogFragment {
    View viewDialog;
    RecyclerView rcvCutomerVoucher_Dialog;
    ArrayList<Voucher> arrVoucher= new ArrayList<>();
    VoucherCustomerHome_Adapter voucherCustomerHomeAdapter;
    private Customer customer = new Customer();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("informationUserCustomer", Context.MODE_PRIVATE);
        String jsonShop = sharedPreferences.getString("informationUserCustomer", "");
        Gson gson = new Gson();
        customer = gson.fromJson(jsonShop, Customer.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        viewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.customer_dialogframent_customervoucher,null);
        voucherCustomerHomeAdapter = new VoucherCustomerHome_Adapter(arrVoucher,getContext());
        rcvCutomerVoucher_Dialog = viewDialog.findViewById(R.id.rcvCutomerVoucher_Dialog);
        rcvCutomerVoucher_Dialog.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvCutomerVoucher_Dialog.setAdapter(voucherCustomerHomeAdapter);
        getVoucherProduct();
        builder.setView(viewDialog);
        return builder.create();
    }

    private void getVoucherProduct(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("VoucherCustomer/"+customer.getId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemVoucher:
                     snapshot.getChildren()) {
                    VoucherCustomer voucherCustomer = itemVoucher.getValue(VoucherCustomer.class);
                  if (voucherCustomer.isStatus()){
                      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Voucher/"+ voucherCustomer.getIdShop()+"/"+voucherCustomer.getIdProduct()+"/"+voucherCustomer.getIdVoucher());
                      databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              if (snapshot.exists()){
                                  Voucher voucher =snapshot.getValue(Voucher.class);
                                  arrVoucher.add(voucher);
                                  voucherCustomerHomeAdapter.notifyDataSetChanged();
                              }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });
                  }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width); // Sử dụng giá trị kích thước từ resources
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        getDialog().getWindow().setLayout(width,height);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_container_dialog);
    }
}
