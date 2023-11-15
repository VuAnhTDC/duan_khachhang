package com.example.duankhachhang.Class;

import java.io.Serializable;

public class ShopData implements Serializable {
    private int status;
    private String idShop, shopPhoneNumber, password, shopOwner, shopName,
            shopAddress, shopEmail, taxCode, urlImgCCCDFront, urlImgCCCDBack, urlImgShopAvatar;
    private String fcmToken;

    public ShopData(int status, String idShop, String shopPhoneNumber, String password, String shopOwner, String shopName, String shopAddress, String shopEmail, String taxCode, String urlImgCCCDFront, String urlImgCCCDBack, String urlImgShopAvatar, String fcmToken) {
        this.status = status;
        this.idShop = idShop;
        this.shopPhoneNumber = shopPhoneNumber;
        this.password = password;
        this.shopOwner = shopOwner;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.taxCode = taxCode;
        this.urlImgCCCDFront = urlImgCCCDFront;
        this.urlImgCCCDBack = urlImgCCCDBack;
        this.urlImgShopAvatar = urlImgShopAvatar;
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }


    public ShopData(int status, String idShop, String shopPhoneNumber, String password, String shopOwner, String shopName, String shopAddress, String shopEmail, String taxCode, String urlImgCCCDFront, String urlImgCCCDBack, String urlImgShopAvatar) {
        this.status = status;
        this.idShop = idShop;
        this.shopPhoneNumber = shopPhoneNumber;
        this.password = password;
        this.shopOwner = shopOwner;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.taxCode = taxCode;
        this.urlImgCCCDFront = urlImgCCCDFront;
        this.urlImgCCCDBack = urlImgCCCDBack;
        this.urlImgShopAvatar = urlImgShopAvatar;
    }

    public ShopData(int status, String idShop, String shopPhoneNumber, String password, String shopOwner, String shopName, String shopAddress, String shopEmail, String taxCode) {
        this.status = status;
        this.idShop = idShop;
        this.shopPhoneNumber = shopPhoneNumber;
        this.password = password;
        this.shopOwner = shopOwner;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopEmail = shopEmail;
        this.taxCode = taxCode;
    }

    //    public ShopData(String idShop, String shopPhoneNumber, String shopOwner, String shopName, String shopAddress, String shopEmail, String taxCode, String urlImgCCCDFront, String urlImgCCCDBack) {
//        this.idShop = idShop;
//        this.shopPhoneNumber = shopPhoneNumber;
//        this.shopOwner = shopOwner;
//        this.shopName = shopName;
//        this.shopAddress = shopAddress;
//        this.shopEmail = shopEmail;
//        this.taxCode = taxCode;
//        this.urlImgCCCDFront = urlImgCCCDFront;
//        this.urlImgCCCDBack = urlImgCCCDBack;
//    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdShop() {
        return idShop;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public String getShopPhoneNumber() {
        return shopPhoneNumber;
    }

    public void setShopPhoneNumber(String shopPhoneNumber) {
        this.shopPhoneNumber = shopPhoneNumber;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopOwner() {
        return shopOwner;
    }

    public void setShopOwner(String shopOwner) {
        this.shopOwner = shopOwner;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getUrlImgCCCDFront() {
        return urlImgCCCDFront;
    }

    public void setUrlImgCCCDFront(String urlImgCCCDFront) {
        this.urlImgCCCDFront = urlImgCCCDFront;
    }

    public String getUrlImgCCCDBack() {
        return urlImgCCCDBack;
    }

    public void setUrlImgCCCDBack(String urlImgCCCDBack) {
        this.urlImgCCCDBack = urlImgCCCDBack;
    }

    public String getUrlImgShopAvatar() {
        return urlImgShopAvatar;
    }

    public void setUrlImgShopAvatar(String urlImgShopAvatar) {
        this.urlImgShopAvatar = urlImgShopAvatar;
    }

    public ShopData() {
        this.status = 0;
        this.idShop = null;
        this.shopPhoneNumber = null;
        this.password = null;
        this.shopOwner = null;
        this.shopName = null;
        this.shopAddress = null;
        this.shopEmail = null;
        this.taxCode = null;
        this.urlImgCCCDFront = null;
        this.urlImgCCCDBack = null;
        this.urlImgShopAvatar = null;
    }

    public ShopData(String shopPhoneNumber, String password) {
        this.shopPhoneNumber = shopPhoneNumber;
        this.password = password;
    }

    public ShopData(String shopPhoneNumber) {
        this.shopPhoneNumber = shopPhoneNumber;
    }

    @Override
    public String toString() {
        return "ShopData{" +
                "status=" + status +
                ", idShop='" + idShop + '\'' +
                ", shopPhoneNumber='" + shopPhoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", shopOwner='" + shopOwner + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopEmail='" + shopEmail + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", urlImgCCCDFront='" + urlImgCCCDFront + '\'' +
                ", urlImgCCCDBack='" + urlImgCCCDBack + '\'' +
                ", urlImgShopAvatar='" + urlImgShopAvatar + '\'' +
                '}';
    }


}
