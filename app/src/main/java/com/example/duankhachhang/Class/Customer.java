package com.example.duankhachhang.Class;

import java.io.Serializable;

public class Customer implements Serializable {
    private String id;
    private String address;
    public String fcmToken;
    private String name;
    private String imageUser;

    public Customer(String id, String address, String name, String imageUser,String fcmToken) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.imageUser = imageUser;
        this.fcmToken = fcmToken;
    }


    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }


        public String getAddress () {
            return address;
        }

        public void setAddress (String address){
            this.address = address;
        }

        public String getName () {
            return name;
        }

        public void setName (String name){
            this.name = name;
        }

        public String getImageUser () {
            return imageUser;
        }

        public void setImageUser (String imageUser){
            this.imageUser = imageUser;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
        public String toString () {
            return "Customer{" +
                    "id='" + id + '\'' +
                    ", address='" + address + '\'' +
                    ", fcmToken='" + fcmToken + '\'' +
                    ", name='" + name + '\'' +
                    ", imageUser='" + imageUser + '\'' +
                    '}';
        }


    public Customer(String id, String address, String name, String imageUser) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.imageUser = imageUser;
    }

    public Customer() {
        this.id = null;
        this.address = null;
        this.name = null;
        this.imageUser = null;
    }
}
