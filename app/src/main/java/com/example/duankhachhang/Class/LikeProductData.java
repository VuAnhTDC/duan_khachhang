package com.example.duankhachhang.Class;

import java.io.Serializable;

public class LikeProductData implements Serializable {
    private String idCustomer_LikeProduct;
    private String idProduct_LikeProduct;
    private String idShop;

    @Override
    public String toString() {
        return "LikeProductData{" +
                "idCustomer_LikeProduct='" + idCustomer_LikeProduct + '\'' +
                ", idProduct_LikeProduct='" + idProduct_LikeProduct + '\'' +
                '}';
    }

    public String getIdCustomer_LikeProduct() {
        return idCustomer_LikeProduct;
    }

    public void setIdCustomer_LikeProduct(String idCustomer_LikeProduct) {
        this.idCustomer_LikeProduct = idCustomer_LikeProduct;
    }

    public String getIdProduct_LikeProduct() {
        return idProduct_LikeProduct;
    }

    public void setIdProduct_LikeProduct(String idProduct_LikeProduct) {
        this.idProduct_LikeProduct = idProduct_LikeProduct;
    }

    public LikeProductData() {
    }

    public String getIdShop() {
        return idShop;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public LikeProductData(String idCustomer_LikeProduct, String idProduct_LikeProduct,String idShop) {
        this.idCustomer_LikeProduct = idCustomer_LikeProduct;
        this.idProduct_LikeProduct = idProduct_LikeProduct;
        this.idShop = idShop;
    }
}
