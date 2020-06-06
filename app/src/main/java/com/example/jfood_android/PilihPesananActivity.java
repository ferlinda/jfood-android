package com.example.jfood_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PilihPesananActivity extends AppCompatActivity {
    private static final String TAG = "PilihPesananActivity";
    RecyclerView foodListR;
    private FoodAdapter adapter;
    private List<Food> tempFoodList = new ArrayList<>();
    private int sellerId;
    private static int currentUserId;
//    private String foodList;
//    private String newFoodList;
//    private int foodPriceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}

        setContentView(R.layout.activity_pilih_pesanan);
        foodListR = findViewById(R.id.pilihpesanan);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            sellerId = extras.getInt("id_seller");
            currentUserId = extras.getInt("currentUserId");
//            foodList = extras.getString("foodList");
//            foodPriceList = extras.getInt("foodPriceList");
        }
        Log.d(TAG, String.valueOf(sellerId));
        refreshList();
    }

    protected void refreshList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i=0; i<jsonResponse.length(); i++) {
                        JSONObject food = jsonResponse.getJSONObject(i);
                        JSONObject seller = food.getJSONObject("seller");
                        JSONObject location = seller.getJSONObject("location");

                        Location newLocation = new Location(
                                location.getString("province"),
                                location.getString("description"),
                                location.getString("city")
                        );

                        Seller newSeller = new Seller(
                                seller.getInt("id"),
                                seller.getString("name"),
                                seller.getString("email"),
                                seller.getString("phoneNumber"),
                                newLocation
                        );

                        Food newFood = new Food(
                                food.getInt("id"),
                                food.getString("name"),
                                food.getInt("price"),
                                food.getString("category"),
                                newSeller
                        );

                        if(seller.getInt("id") == sellerId){
                            tempFoodList.add(newFood);
                        }
                    }
                    Log.d(TAG, String.valueOf(tempFoodList));
//                    adapter = new FoodAdapter(getApplicationContext(),tempFoodList, currentUserId, foodList, foodPriceList);
                    adapter = new FoodAdapter(getApplicationContext(),tempFoodList, currentUserId);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                    foodListR.setLayoutManager(gridLayoutManager);
                    foodListR.setAdapter(adapter);
                }
                catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PilihPesananActivity.this);
                    builder.setMessage("Load Data Failed.").create().show();
                }
            }
        };

        MenuRequest menuRequest = new MenuRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(PilihPesananActivity.this);
        queue.add(menuRequest);
        Log.d(TAG, String.valueOf(tempFoodList));
    }
}
