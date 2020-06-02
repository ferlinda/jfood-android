package com.example.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuatPesananActivity extends AppCompatActivity {
    private static final String TAG = "BuatPesanan";
    private int currentUserId;
    private int id_food;
    private String foodName;
    private String foodCategory;
    private int foodPrice;
    private String foodList;
    private String newFoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buat_pesanan);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            currentUserId = extras.getInt("currentUserId");
            id_food = extras.getInt("food_id");
            foodName = extras.getString("food_name");
            foodCategory = extras.getString("food_category");
            foodPrice = extras.getInt("food_price");
        }

        TextView food_name = findViewById(R.id.food_name);
        TextView food_category = findViewById(R.id.food_category);
        final TextView food_price = findViewById(R.id.food_price);
        final TextView total_price = findViewById(R.id.total_price);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final EditText promoCode = findViewById(R.id.promo_code);
        final TextView textCode = findViewById(R.id.textCode);
        final Button hitung = findViewById(R.id.hitung);
        final Button pesan = findViewById(R.id.pesan);

        textCode.setVisibility(View.INVISIBLE);
        promoCode.setVisibility(View.INVISIBLE);

        hitung.setEnabled(false);
        pesan.setEnabled(false);

        food_name.setText(foodName);
        food_category.setText(foodCategory);
        food_price.setText("Rp. " + String.valueOf((int) foodPrice));
        total_price.setText("Rp. " + "0");

        hitung.setVisibility(View.VISIBLE);
        pesan.setVisibility(View.GONE);

        foodList = getIntent().getExtras().getString("foodList");
        if(foodList == null){
            foodList = "";
        }
        newFoodList = foodList + id_food + ",";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                hitung.setVisibility(View.VISIBLE);
                pesan.setVisibility(View.GONE);
                RadioButton radioButton = findViewById(i);
                String selected = radioButton.getText().toString().trim();
                switch (selected) {
                    case "Via CASH":
                        textCode.setVisibility(View.GONE);
                        promoCode.setVisibility(View.GONE);
                        hitung.setEnabled(true);
                        break;
                    case "Via CASHLESS":
                        textCode.setVisibility(View.VISIBLE);
                        promoCode.setVisibility(View.VISIBLE);
                        hitung.setEnabled(true);
                        break;
                }
            }
        });

        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitung.setVisibility(View.GONE);
                pesan.setVisibility(View.VISIBLE);
                int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadio = findViewById(selectedRadioId);
                String selected = selectedRadio.getText().toString().trim();
                pesan.setEnabled(true);
                switch (selected) {
                    case "Via CASH":
                        total_price.setText(food_price.getText().toString());
                        break;
                    case "Via CASHLESS":
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonResponse = new JSONArray(response);
                                    for(int i = 0; i <jsonResponse.length(); i++){
                                        JSONObject promo = jsonResponse.getJSONObject(i);
                                        if(promoCode.getText().toString().equals(promo.getString("code")) && promo.getBoolean("active")){
                                            if(foodPrice > promo.getInt("minPrice")){
                                                int priceRequest = promo.getInt("discount");
                                                total_price.setText(String.valueOf(foodPrice - priceRequest));
                                            }
                                        }
                                    }
                                }
                                catch (JSONException e){
                                    Log.d(TAG, "Load data failed.");
                                }
                            }
                        };

                        CheckPromoRequest promoRequest = new CheckPromoRequest(responseListener);
                        RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                        queue.add(promoRequest);
                        break;
                }

                hitung.setVisibility(View.GONE);
                pesan.setVisibility(View.VISIBLE);

                Log.d(TAG, "count clicked");

            }
        });

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadio = findViewById(selectedRadioId);
                String selected = selectedRadio.getText().toString().trim();
                String kode = promoCode.getText().toString().trim();
                BuatPesananRequest request = null;

                Log.d(TAG, selected);

                Response.Listener<String> responseListenerOrder = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (response != null){
                                Toast.makeText(BuatPesananActivity.this, "Order successful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(BuatPesananActivity.this, "Order failed", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }
                    }
                };

                if(selected.equals("Via CASH")){
                    request = new BuatPesananRequest(newFoodList.substring(0, newFoodList.length()-1), currentUserId+"", responseListenerOrder);
                    Log.d(TAG, String.valueOf(currentUserId));
                }
                else if(selected.equals("Via CASHLESS")){
                    request = new BuatPesananRequest(newFoodList.substring(0, newFoodList.length()-1), currentUserId+"", promoCode.getText().toString(), responseListenerOrder);
                    Log.d(TAG, String.valueOf(currentUserId));
                }

                RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                queue.add(request);
            }
        });

    }
}
