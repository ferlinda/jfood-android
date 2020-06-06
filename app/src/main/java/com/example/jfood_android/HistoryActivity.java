package com.example.jfood_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    RecyclerView historyList;
    List<Integer> hId = new ArrayList<>();
    List<String> hSeller = new ArrayList<>();
    List<String> hFood = new ArrayList<>();
    List<Integer> hPrices = new ArrayList<>();
    List<String> hTime = new ArrayList<>();
    List<String> hPayment = new ArrayList<>();
    List<String> hStatus = new ArrayList<>();
    HistoryAdapter historyAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
        setContentView(R.layout.activity_history);
        historyList = findViewById(R.id.historyList);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            currentUserId = extras.getInt("currentUserId");
        }
        refreshListHistory();
        Log.d(TAG, String.valueOf(currentUserId));

        findViewById(R.id.back_to_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                intent.putExtra("currentUserId", currentUserId);
                startActivity(intent);
            }
        });
    }

    protected void refreshListHistory() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i=0; i<jsonResponse.length(); i++) {
                        Log.d(TAG, String.valueOf(jsonResponse.length()));
                        JSONObject invoice = jsonResponse.getJSONObject(i);

                        int historyId = invoice.getInt("id");
                        String historySeller = invoice.getJSONArray("foods").getJSONObject(0).getJSONObject("seller").getString("name");
                        Log.d(TAG, "seller berhasil");
                        String historyFood = invoice.getJSONArray("foods").getJSONObject(0).getString("name");
                        Log.d(TAG, "foods berhasil");
                        int historyPrice = invoice.getInt("totalPrice");
                        Log.d(TAG, "price berhasil");
                        String historyTime = invoice.getString("date");
                        String historyPayment = invoice.getString("paymentType");
                        String historyStatus = invoice.getString("invoiceStatus");

                        hId.add(historyId);
                        hSeller.add(historySeller);
                        hFood.add(historyFood);
                        hPrices.add(historyPrice);
                        hTime.add(historyTime);
                        hPayment.add(historyPayment);
                        hStatus.add(historyStatus);
                        Log.d(TAG, String.valueOf(hId));
                        Log.d(TAG, String.valueOf(hSeller));
                        Log.d(TAG, String.valueOf(hFood));
                        Log.d(TAG, String.valueOf(hPrices));
                        Log.d(TAG, String.valueOf(hTime));
                        Log.d(TAG, String.valueOf(hPayment));
                        Log.d(TAG, String.valueOf(hStatus));

                    }
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    historyAdapter = new HistoryAdapter(getApplicationContext(),hId, hSeller, hFood, hPrices, hTime, hPayment, hStatus);
                    historyList.setLayoutManager(mLayoutManager);
                    historyList.setAdapter(historyAdapter);
                }
                catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                    builder.setMessage("Load Data Failed.").create().show();
                }
            }
        };

        PesananFetchRequest fetchRequest = new PesananFetchRequest(currentUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(HistoryActivity.this);
        queue.add(fetchRequest);

    }
}
