package com.example.jfood_android;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PesananBatalRequest extends StringRequest {

    private static final String URL = "http://192.168.1.15:8080/invoice/invoiceStatus/";
    private Map<String, String> params;

    public PesananBatalRequest(String id, Response.Listener<String> listener) {
        super(Method.PUT, URL+id, listener, null);
        params = new HashMap<>();
        params.put("id", id);
        params.put("invoiceStatus", "Cancelled");
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }

}
