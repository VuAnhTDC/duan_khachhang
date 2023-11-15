package com.example.duankhachhang.Model;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SendNotification implements Serializable {
    private static Context context;
    private static String urlServerId = "AAAAqZ6PuB4:APA91bHPKQTz-ePylkxFZRLUWeHP514Q-9SCxxB-lrAKHN-KRlCHcaX0P5jWgi-eFbc-icBo_SBKJFD0gud4yZ01imxLCre2nYCr3-y8DbYzZs6kikvortOvix0mYdD5VaXC6rCM7Tr-";

    private static String urlSendMessage = "https://fcm.googleapis.com/fcm/send";
    public static void setContext(Context context){
        SendNotification.context = context;
    }

    public static void getSendNotificationOrderSuccessFull(String fcmToken,String title,String content,String tag){
        JSONObject jsonNotificationData = new JSONObject();
        try {
            jsonNotificationData.put("title",title);
            jsonNotificationData.put("body",content);
            jsonNotificationData.put("tag",tag);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonRequestData = new JSONObject();
        try {
            jsonRequestData.put("to",fcmToken);
            jsonRequestData.put("notification",jsonNotificationData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlSendMessage, jsonRequestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Yêu cầu gửi thông báo thất bại: "+ error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + urlServerId);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
