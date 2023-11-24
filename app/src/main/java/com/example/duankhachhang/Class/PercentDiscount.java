package com.example.duankhachhang.Class;

public class PercentDiscount {
    private String keyPercentDiscount;
    private int percent;

    @Override
    public String toString() {
        return "PercentDiscount{" +
                "keyPercentDiscount='" + keyPercentDiscount + '\'' +
                ", percent='" + percent + '\'' +
                '}';
    }

    public String getKeyPercentDiscount() {
        return keyPercentDiscount;
    }

    public void setKeyPercentDiscount(String keyPercentDiscount) {
        this.keyPercentDiscount = keyPercentDiscount;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public PercentDiscount() {
    }

    public PercentDiscount(String keyPercentDiscount, int percent) {
        this.keyPercentDiscount = keyPercentDiscount;
        this.percent = percent;
    }
}
