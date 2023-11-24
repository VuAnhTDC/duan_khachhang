package com.example.duankhachhang.Class;

public class VoucherCustomer {
    private String idVoucher;
    private String idShop;
    private String idProduct;
    private boolean status;

    public VoucherCustomer() {
    }

    public VoucherCustomer(String idVoucher, String idShop, String idProduct, boolean status) {
        this.idVoucher = idVoucher;
        this.idShop = idShop;
        this.idProduct = idProduct;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(String idVoucher) {
        this.idVoucher = idVoucher;
    }

    public String getIdShop() {
        return idShop;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
}
