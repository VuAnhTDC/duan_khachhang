package com.example.duankhachhang.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private String descriptionProduct;
    private String idProduct;
    private String idUserProduct;
    private String keyCategoryProduct;
    private String keyManufaceProduct;
    private String nameProduct;
    private int overageCmtProduct;
    private int priceProduct;
    private int quanlityProduct;
    private int sumLike;

    public Product() {
    }

    public Product(String descriptionProduct, String idProduct, String idUserProduct, String keyCategoryProduct, String keyManufaceProduct, String nameProduct, int overageCmtProduct, int priceProduct, int quanlityProduct, int sumLike) {
        this.descriptionProduct = descriptionProduct;
        this.idProduct = idProduct;
        this.idUserProduct = idUserProduct;
        this.keyCategoryProduct = keyCategoryProduct;
        this.keyManufaceProduct = keyManufaceProduct;
        this.nameProduct = nameProduct;
        this.overageCmtProduct = overageCmtProduct;
        this.priceProduct = priceProduct;
        this.quanlityProduct = quanlityProduct;
        this.sumLike = sumLike;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdUserProduct() {
        return idUserProduct;
    }

    public void setIdUserProduct(String idUserProduct) {
        this.idUserProduct = idUserProduct;
    }

    public String getKeyCategoryProduct() {
        return keyCategoryProduct;
    }

    public void setKeyCategoryProduct(String keyCategoryProduct) {
        this.keyCategoryProduct = keyCategoryProduct;
    }

    public String getKeyManufaceProduct() {
        return keyManufaceProduct;
    }

    public void setKeyManufaceProduct(String keyManufaceProduct) {
        this.keyManufaceProduct = keyManufaceProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getOverageCmtProduct() {
        return overageCmtProduct;
    }

    public void setOverageCmtProduct(int overageCmtProduct) {
        this.overageCmtProduct = overageCmtProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getQuanlityProduct() {
        return quanlityProduct;
    }

    public void setQuanlityProduct(int quanlityProduct) {
        this.quanlityProduct = quanlityProduct;
    }

    public int getSumLike() {
        return sumLike;
    }

    public void setSumLike(int sumLike) {
        this.sumLike = sumLike;
    }
}
