package com.example.duankhachhang.Class;

import java.io.Serializable;

public class CommentData implements Serializable {
    private String idCustomer;
    private String idProduct;
    private String contentComment;

    private String dateComment;

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
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

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public CommentData() {
        this.idCustomer = null;
        this.idProduct = null;
        this.contentComment = null;
        this.dateComment = null;
    }

    @Override
    public String toString() {
        return "CommentData{" +
                "idCustomer='" + idCustomer + '\'' +
                ", idProduct='" + idProduct + '\'' +
                ", contentComment='" + contentComment + '\'' +
                ", dateComment='" + dateComment + '\'' +
                '}';
    }

    public CommentData(String idCustomer, String idProduct, String contentComment, String dateComment) {
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.contentComment = contentComment;
        this.dateComment = dateComment;
    }
}
