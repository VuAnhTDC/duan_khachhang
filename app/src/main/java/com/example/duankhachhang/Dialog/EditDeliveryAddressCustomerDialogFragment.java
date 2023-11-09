package com.example.duankhachhang.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duankhachhang.R;

public class EditDeliveryAddressCustomerDialogFragment extends DialogFragment {
    private  EditDeliveryAddressCustomerDialogFragment.DeliveryAddress deliveryAddress;
    View viewDialog;
    EditText edtDeliveryAddress_UpdateDeliveryAddressCustomer;

    public EditDeliveryAddressCustomerDialogFragment(EditDeliveryAddressCustomerDialogFragment.DeliveryAddress deliveryAddress){
        this.deliveryAddress = deliveryAddress;
    }

   public interface DeliveryAddress{
        public void getDeliveryAddress(String deliveryAddress);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        viewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.customer_dialogfragment_updateaddresscustomer,null);
        edtDeliveryAddress_UpdateDeliveryAddressCustomer = viewDialog.findViewById(R.id.edtDeliveryAddress_UpdateDeliveryAddressCustomer);
        edtDeliveryAddress_UpdateDeliveryAddressCustomer.requestFocus();
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if (edtDeliveryAddress_UpdateDeliveryAddressCustomer.getText().toString() != null && !edtDeliveryAddress_UpdateDeliveryAddressCustomer.getText().toString().isEmpty()){
                   deliveryAddress.getDeliveryAddress(edtDeliveryAddress_UpdateDeliveryAddressCustomer.getText().toString());
                   Toast.makeText(getContext(),"Thay đổi địa chỉ nhận hàng thành công",Toast.LENGTH_LONG).show();
               }
                dialogInterface.dismiss();
            }
        });
        builder.setView(viewDialog);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_container_dialog);
    }
}
