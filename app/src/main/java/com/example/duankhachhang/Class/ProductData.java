package com.example.duankhachhang.Class;

import java.io.Serializable;

public class ProductData implements Serializable {
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
    private String keyProductItem;

    public ProductData() {
    }

    public ProductData(String descriptionProduct, String idProduct, String idUserProduct, String keyCategoryProduct, String keyManufaceProduct, String nameProduct, int overageCmtProduct, int priceProduct, int quanlityProduct, int sumLike) {
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


    public String getKeyProductItem() {
        return keyProductItem;
    }

    public void setKeyProductItem(String keyProductItem) {
        this.keyProductItem = keyProductItem;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
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
    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
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

    public void setQuanlityProduct(int quanlityProduct) {
        this.quanlityProduct = quanlityProduct;
    }

    public int getSumLike() {
        return sumLike;
    }

    public void setSumLike(int sumLike) {
        this.sumLike = sumLike;
    }

    public ProductData(String idProduct, String nameProduct, int priceProduct, String keyCategoryProduct, String keyManufaceProduct, int quanlityProduct, String descriptionProduct, int sumLike, int overageCmtProduct, String idUserProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
//        this.urlImageProduct = urlImageProduct;
        this.priceProduct = priceProduct;
        this.keyCategoryProduct = keyCategoryProduct;
        this.keyManufaceProduct = keyManufaceProduct;
        this.quanlityProduct = quanlityProduct;
        this.descriptionProduct = descriptionProduct;
        this.sumLike = sumLike;
        this.overageCmtProduct = overageCmtProduct;
        this.idUserProduct = idUserProduct;
    }

    public String getIdUserProduct() {
        return idUserProduct;
    }

    public void setIdUserProduct(String idUserProduct) {
        this.idUserProduct = idUserProduct;
    }

    @Override
    public String toString() {
        return "ProductData{" +
                "idProduct='" + idProduct + '\'' +
                ", nameProduct='" + nameProduct + '\'' +
                ", priceProduct=" + priceProduct +
                ", keyCategoryProduct='" + keyCategoryProduct + '\'' +
                ", keyManufaceProduct='" + keyManufaceProduct + '\'' +
                ", quanlityProduct=" + quanlityProduct +
                ", descriptionProduct='" + descriptionProduct + '\'' +
                ", sumLike=" + sumLike +
                ", overageCmtProduct=" + overageCmtProduct +
                ", idUser_Product='" + idUserProduct.toString() + '\'' +
                '}';

    }


    public void getQuanlityProduct() {
    }
}