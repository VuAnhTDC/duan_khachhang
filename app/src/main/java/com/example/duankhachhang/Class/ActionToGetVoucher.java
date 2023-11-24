package com.example.duankhachhang.Class;

public class ActionToGetVoucher {
    private String idAction;
    private String valueAction;

    public ActionToGetVoucher() {
    }

    public ActionToGetVoucher(String idAction, String valueAction) {
        this.idAction = idAction;
        this.valueAction = valueAction;
    }

    public String getIdAction() {
        return idAction;
    }

    public void setIdAction(String idAction) {
        this.idAction = idAction;
    }

    public String getValueAction() {
        return valueAction;
    }

    public void setValueAction(String valueAction) {
        this.valueAction = valueAction;
    }
}
