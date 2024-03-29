package com.example.jfood_android;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SellerRequest extends StringRequest {

    private static String URL = "http://192.168.1.15:8080/seller";
    private Map<String, String> params;

    public SellerRequest(Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
