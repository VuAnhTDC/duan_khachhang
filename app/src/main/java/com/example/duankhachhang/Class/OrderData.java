package com.example.duankhachhang.Class;

import java.io.Serializable;

public class OrderData implements Serializable {
    private String idOrder;
    private String idCustomer_Order;
    private String idProduct_Order;
    private String  idShop_Order;
    private String idShiper_Order;
    private int quanlity_Order;
    private String statusOrder;
    private String dateOrder;
    private String deliveryAddress;
    private String numberPhone_Customer;
    private int price_Order;
    private String note_Order;

    public OrderData() {
        this.idOrder = null;
        this.idCustomer_Order = null;
        this.idProduct_Order = null;
        this.idShop_Order = null;
        this.idShiper_Order = null;
        this.statusOrder = null;
        this.dateOrder = null;
        this.deliveryAddress = null;
        this.numberPhone_Customer = null;
        this.quanlity_Order = 0;
        this.price_Order = 0;
        this.note_Order = null;
    }

    public OrderData(String idOrder, String idCustomer_Order, String idProduct_Order, String idShop_Order, String idShiper_Order, String statusOrder, String dateOrder, String deliveryAddress, String numberPhone_Customer, String note_Order, int quanlity, int price_order) {
        this.idOrder = idOrder;
        this.idCustomer_Order = idCustomer_Order;
        this.idProduct_Order = idProduct_Order;
        this.idShop_Order = idShop_Order;
        this.idShiper_Order = idShiper_Order;
        this.statusOrder = statusOrder;
        this.dateOrder = dateOrder;
        this.deliveryAddress = deliveryAddress;
        this.numberPhone_Customer = numberPhone_Customer;
        this.quanlity_Order = quanlity;
        this.price_Order = price_order;
        this.note_Order =note_Order;
    }

    @Override
    public String toString() {
        return "ItemOrder{" +
                "idOrder='" + idOrder + '\'' +
                ", idCustomer_Order='" + idCustomer_Order + '\'' +
                ", idProduct_Order='" + idProduct_Order + '\'' +
                ", idShop_Order='" + idShop_Order + '\'' +
                ", idShiper_Order='" + idShiper_Order + '\'' +
                ", quanlity_Order=" + quanlity_Order +
                ", statusOrder='" + statusOrder + '\'' +
                ", dateOrder='" + dateOrder + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", numberPhone_Customer='" + numberPhone_Customer + '\'' +
                ", price_Order=" + price_Order +
                ", note_Order='" + note_Order + '\'' +
                '}';
    }

    public String getNote_Order() {
        return note_Order;
    }

    public void setNote_Order(String note_Order) {
        this.note_Order = note_Order;
    }

    public int getPrice_Order() {
        return price_Order;
    }

    public void setPrice_Order(int price_Order) {
        this.price_Order = price_Order;
    }

    public int getQuanlity_Order() {
        return quanlity_Order;
    }

    public void setQuanlity_Order(int quanlity_Order) {
        this.quanlity_Order = quanlity_Order;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdCustomer_Order() {
        return idCustomer_Order;
    }

    public void setIdCustomer_Order(String idCustomer_Order) {
        this.idCustomer_Order = idCustomer_Order;
    }

    public String getIdProduct_Order() {
        return idProduct_Order;
    }

    public void setIdProduct_Order(String idProduct_Order) {
        this.idProduct_Order = idProduct_Order;
    }

    public String getIdShop_Order() {
        return idShop_Order;
    }

    public void setIdShop_Order(String idShop_Order) {
        this.idShop_Order = idShop_Order;
    }

    public String getIdShiper_Order() {
        return idShiper_Order;
    }

    public void setIdShiper_Order(String idShiper_Order) {
        this.idShiper_Order = idShiper_Order;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNumberPhone_Customer() {
        return numberPhone_Customer;
    }

    public void setNumberPhone_Customer(String numberPhone_Customer) {
        this.numberPhone_Customer = numberPhone_Customer;
    }
}
