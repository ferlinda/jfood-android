package com.example.jfood_android;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class PesananFetchRequest extends StringRequest {
    private static final String URL = "http://192.168.1.15:8080/invoice/customer/";
    public PesananFetchRequest(int id_customer, Response.Listener<String> listener) {
        super(Method.GET, URL+id_customer, listener, null);
        Log.d("", "PesananFetchRequest: "+id_customer);
    }

}
