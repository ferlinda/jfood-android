package com.example.jfood_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelesaiPesananActivity extends AppCompatActivity {
    int  currentUserId;
    TextView tvIdInvoice, tvNamaCustomer, tvNamaMakanan, tvTanggalPesanan, tvTotalBiaya, tvStatusInvoice;
    Button btnSelesai, btnBatal;
    String namaCustomer, namaMakanan, tanggalPesanan, statusInvoice, currentInvoiceId;
    int totalBiaya,idInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_selesai_pesanan);

        tvIdInvoice = findViewById(R.id.id_invoice);
        tvNamaCustomer = findViewById(R.id.nama_customer);
        tvNamaMakanan = findViewById(R.id.nama_makanan);
        tvTanggalPesanan = findViewById(R.id.tanggal_pesanan);
        tvTotalBiaya = findViewById(R.id.total_biaya);
        tvStatusInvoice = findViewById(R.id.status_invoice);
        btnBatal = findViewById(R.id.batal);
        btnSelesai = findViewById(R.id.selesai);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserId=extras.getInt("currentUserId");
        }

        fetchPesanan();

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(SelesaiPesananActivity.this, "This invoice is finished", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                                builder.setMessage("Operation Failed! Please try again").create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                            builder.setMessage("Operation Failed! Please try again").create().show();
                        }
                    }
                };
                PesananSelesaiRequest request = new PesananSelesaiRequest(currentInvoiceId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
                queue.add(request);
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject == null) {
                                Toast.makeText(SelesaiPesananActivity.this, "This invoice is canceled", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("currentUserId", currentUserId);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                                builder.setMessage("Operation Failed! Please try again").create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SelesaiPesananActivity.this, "This invoice is canceled", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("currentUserId", currentUserId);
                            startActivity(intent);
                        }
                    }
                };

                PesananBatalRequest request = new PesananBatalRequest(currentInvoiceId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
                queue.add(request);
            }
        });

    }

    public void fetchPesanan(){
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    JSONObject invoice = jsonResponse.getJSONObject(jsonResponse.length()-1);
                    JSONObject food = invoice.getJSONArray("foods").getJSONObject(0);
                    JSONObject customer = invoice.getJSONObject("customer");
                    currentInvoiceId = String.valueOf(invoice.getInt("id"));

                    tvIdInvoice.setText(String.valueOf(invoice.getInt("id")));
                    tvNamaCustomer.setText(customer.getString("name"));
                    tvNamaMakanan.setText(food.getString("name"));
                    tvTanggalPesanan.setText(invoice.getString("date"));
                    tvTotalBiaya.setText(String.valueOf(invoice.getInt("totalPrice")));
                    tvStatusInvoice.setText(invoice.getString("invoiceStatus"));

                    } catch (JSONException ex) {

                    ex.printStackTrace();
                }
            }
        };

        PesananFetchRequest request = new PesananFetchRequest(currentUserId, responseListener);
        RequestQueue queue = new Volley().newRequestQueue(SelesaiPesananActivity.this);
        queue.add(request);
    }


}
