package com.example.duankhachhang.Class;

public class Category {
    private String keyCategoryItem;

    public Category(String keyCategoryItem, String idCategory, String nameCategory) {
        this.keyCategoryItem = keyCategoryItem;
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
    }

    public String getKeyCategoryItem() {
        return keyCategoryItem;
    }

    public void setKeyCategoryItem(String keyCategoryItem) {
        this.keyCategoryItem = keyCategoryItem;
    }

    private String idCategory;
    private String nameCategory;

    @Override
    public String toString() {
        return "Category{" +
                "keyCategoryItem='" + keyCategoryItem + '\'' +
                ", idCategory='" + idCategory + '\'' +
                ", nameCategory='" + nameCategory + '\'' +
                '}';
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Category() {
    }

    public Category(String idCategory, String nameCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
    }
}
