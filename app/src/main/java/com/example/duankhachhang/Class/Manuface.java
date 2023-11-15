package com.example.duankhachhang.Class;

public class Manuface {
    private String keyManufaceItem;
    private String idManuface;
    private String nameManuface;

    private String keyManufacve_Category;

    public Manuface(String keyManufaceItem, String idManuface, String nameManuface, String keyManufacve_Category) {
        this.keyManufaceItem = keyManufaceItem;
        this.idManuface = idManuface;
        this.nameManuface = nameManuface;
        this.keyManufacve_Category = keyManufacve_Category;
    }

    @Override
    public String toString() {
        return "Manuface{" +
                "keyManufaceItem='" + keyManufaceItem + '\'' +
                ", idManuface='" + idManuface + '\'' +
                ", nameManuface='" + nameManuface + '\'' +
                '}';
    }

    public String getKeyManufaceItem() {
        return keyManufaceItem;
    }

    public void setKeyManufaceItem(String keyManufaceItem) {
        this.keyManufaceItem = keyManufaceItem;
    }

    public String getIdManuface() {
        return idManuface;
    }

    public void setIdManuface(String idManuface) {
        this.idManuface = idManuface;
    }

    public String getNameManuface() {
        return nameManuface;
    }

    public void setNameManuface(String nameManuface) {
        this.nameManuface = nameManuface;
    }

    public Manuface() {
    }

    public Manuface(String keyManufaceItem, String idManuface, String nameManuface) {
        this.keyManufaceItem = keyManufaceItem;
        this.idManuface = idManuface;
        this.nameManuface = nameManuface;
    }
}
