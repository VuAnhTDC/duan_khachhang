package com.example.duankhachhang.Class;

import java.io.Serializable;

public class Image implements Serializable {
    private String idProduct;
    private String urlImage;

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Image() {
    }

    public Image(String idProduct, String urlImage) {
        this.idProduct = idProduct;
        this.urlImage = urlImage;
    }
}
