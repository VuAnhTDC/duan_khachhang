package com.example.duankhachhang.Class;

import java.io.Serializable;

public class ItemMessage implements Serializable {
    private String idItemMessage;
    private String idSender;
    private String contentMessage;

    @Override
    public String toString() {
        return "ItemMessage{" +
                "idItemMessage='" + idItemMessage + '\'' +
                ", idSender='" + idSender + '\'' +
                ", contentMessage='" + contentMessage + '\'' +
                '}';
    }

    public String getIdItemMessage() {
        return idItemMessage;
    }

    public void setIdItemMessage(String idItemMessage) {
        this.idItemMessage = idItemMessage;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public ItemMessage() {
    }

    public ItemMessage(String idItemMessage, String idSender, String contentMessage) {
        this.idItemMessage = idItemMessage;
        this.idSender = idSender;
        this.contentMessage = contentMessage;
    }
}
