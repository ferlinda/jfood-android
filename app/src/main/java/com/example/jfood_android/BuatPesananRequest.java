package com.example.jfood_android;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BuatPesananRequest extends StringRequest {
    private static String URL_1 = "http://192.168.1.15:8080/invoice/createCashInvoice";
    private static String URL_2 = "http://192.168.1.15:8080/invoice/createCashlessInvoice";
    private Map<String, String> params;

    public BuatPesananRequest(String foodIdList, String customerId, Response.Listener<String> listener) {
        super(Method.POST, URL_1, listener, null);
        params = new HashMap<>();
        this.params.put("foodIdList", foodIdList);
        this.params.put("customerId",customerId);
        params.put("deliveryFee", "0");
    }

    public BuatPesananRequest(String foodIdList,String customerId, String promoCode, Response.Listener<String> listener) {
        super(Method.POST, URL_2, listener, null);
        params = new HashMap<>();
        params.put("foodIdList", foodIdList);
        params.put("customerId", customerId);
        params.put("promoCode", promoCode);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
