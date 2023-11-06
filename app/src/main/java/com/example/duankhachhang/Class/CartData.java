package com.example.duankhachhang.Class;

import java.io.Serializable;

public class CartData implements Serializable {
    private String idCart;
    private String idCustomer;
    private String idProduct;
    private int quanlityProduct_Cart;

    public CartData(String idCart, String idCustomer, String idProduct, int quanlityProduct_Cart) {
        this.idCart = idCart;
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.quanlityProduct_Cart = quanlityProduct_Cart;
    }

    public CartData() {
        this.idCart = null;
        this.idCustomer = null;
        this.idProduct = null;
        this.quanlityProduct_Cart = 0;
    }

    @Override
    public String toString() {
        return "CartData{}";
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuanlityProduct_Cart() {
        return quanlityProduct_Cart;
    }

    public void setQuanlityProduct_Cart(int quanlityProduct_Cart) {
        this.quanlityProduct_Cart = quanlityProduct_Cart;
    }
}
