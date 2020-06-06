package com.example.jfood_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    RecyclerView sellerList;
    SellerAdapter adapter;
    List<Seller> listSeller;
    private ArrayList<Food> foodIdList = new ArrayList<>();
    private static int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserId = extras.getInt("currentUserId");
        }

        sellerList = findViewById(R.id.sellerlist);
        listSeller = new ArrayList<>();

        refreshListMain();

    }

    protected void refreshListMain() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    for (int i=0; i<jsonResponse.length(); i++) {
                        Log.d(TAG, String.valueOf(jsonResponse.length()));
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

                        foodIdList.add(newFood);

                        boolean tempStatus = true;
                        for(Seller sellerPtr : listSeller) {
                            if(sellerPtr.getId() == newSeller.getId()){
                                tempStatus = false;
                            }
                        }
                        if(tempStatus){
                            listSeller.add(newSeller);
                        }

                    }
                    adapter = new SellerAdapter(getApplicationContext(),listSeller,currentUserId);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                    sellerList.setLayoutManager(gridLayoutManager);
                    sellerList.setAdapter(adapter);
                }
                catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Load Data Failed.").create().show();
                }
            }
        };

        MenuRequest menuRequest = new MenuRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(menuRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.checkout) {
            Intent intent = new Intent(MainActivity.this, SelesaiPesananActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            startActivity(intent);
        } else if (id == R.id.logout) {
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
