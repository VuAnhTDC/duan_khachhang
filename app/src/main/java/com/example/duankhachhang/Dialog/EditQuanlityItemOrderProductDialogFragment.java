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

public class EditQuanlityItemOrderProductDialogFragment extends DialogFragment {
    private EditQuanlityItemOrderProductDialogFragment.EditQuanlityOrderProductItem quanlityOrderProductItem;
    View viewDialog;
    public  interface EditQuanlityOrderProductItem {
        public void getQuanlityOrderProductItem(int quanlityOrderProductItem);
    }

    public EditQuanlityItemOrderProductDialogFragment(EditQuanlityItemOrderProductDialogFragment.EditQuanlityOrderProductItem quanlityOrderProductItem){
        this.quanlityOrderProductItem = quanlityOrderProductItem;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.customer_dialogfragment_updatequanlityorderproduct,null);
        EditText edtQuanlityOrderPRoduct_UpdateQuanlityOrderProduct = viewDialog.findViewById(R.id.edtQuanlityOrderPRoduct_UpdateQuanlityOrderProduct);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Integer.parseInt(edtQuanlityOrderPRoduct_UpdateQuanlityOrderProduct.getText().toString()) > 0){
                    quanlityOrderProductItem.getQuanlityOrderProductItem(Integer.parseInt(edtQuanlityOrderPRoduct_UpdateQuanlityOrderProduct.getText().toString()));
                }
                dialogInterface.dismiss();
            }
        });
        setEvent();
        builder.setView(viewDialog);
        return builder.create();
    }

    private void setEvent() {

    }
}
