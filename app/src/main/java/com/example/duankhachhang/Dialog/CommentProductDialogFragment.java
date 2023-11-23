package com.example.duankhachhang.Dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duankhachhang.Class.CommentData;
import com.example.duankhachhang.R;
import com.example.duankhachhang.RecyclerView.CommentProduct_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentProductDialogFragment extends DialogFragment {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<CommentData> arrCommentProduct = new ArrayList<>();
    RecyclerView rcvListCommentProduct_CommentProduct;
    CommentProduct_Adapter commentProductAdapter;
    private String idProduct;
    private boolean loadingCmt = false;

    public CommentProductDialogFragment(String idProduct) {
        this.idProduct = idProduct;
        getDataComment();
    }

    private void getDataComment() {
        databaseReference = firebaseDatabase.getReference("CommentProduct/" +idProduct);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingCmt = true;
                if (snapshot.exists()) {
                    for (DataSnapshot commentItem :
                            snapshot.getChildren()) {
                        CommentData commentData = commentItem.getValue(CommentData.class);
                        arrCommentProduct.add(commentData);
                    }
                }
                if (loadingCmt){
                    commentProductAdapter.notifyDataSetChanged();
                    loadingCmt = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customer_dialogfragment_commentproduct, null);
        rcvListCommentProduct_CommentProduct = dialogView.findViewById(R.id.rcvListCommentProduct_CommentProduct);
        commentProductAdapter = new CommentProduct_Adapter(getContext(), arrCommentProduct);
        rcvListCommentProduct_CommentProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvListCommentProduct_CommentProduct.setAdapter(commentProductAdapter);
        builder.setView(dialogView);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width); // Sử dụng giá trị kích thước từ resources
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        getDialog().getWindow().setLayout(width,height);
    }
}
