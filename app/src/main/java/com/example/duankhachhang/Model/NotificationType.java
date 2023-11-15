package com.example.duankhachhang.Model;

public class NotificationType {
//    Thông báo chat ---> bấm vào sẽ vào chat
    public static String NotificationChat(){
        return "chat";
    }

//    Thông báo đơn hàng ===> bấm vào sẽ vào đơn hàng
    public static String NotificationOrder(){
        return "order";
    }

//    Thông báo bth bấm vào ---> chuyẻn sang màn hình hiện tất cả các thông báo
    public static String NotificationNormal(){
        return "normal";
    }
}
