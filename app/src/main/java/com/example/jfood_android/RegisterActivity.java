package com.example.jfood_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
        setContentView(R.layout.activity_register);

        final EditText mEmail      = findViewById(R.id.editEmail);
        final EditText mPassword   = findViewById(R.id.editPassword);
        final EditText mName       = findViewById(R.id.editName);
        Button mRegister   = findViewById(R.id.buttonRegister);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regexEmail = "^([\\w\\&\\*_~]+\\.{0,1})+@[\\w][\\w\\-]*(\\.[\\w\\-]+)+$";
                String regexPassword = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$";

                Log.d(TAG, "Button Pressed");
                String name = mName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(TextUtils.isEmpty(name)){
                    mName.setError("Name is Required.");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(!Pattern.matches(regexEmail, email)){
                    mEmail.setError("Email is not valid");
                    return;
                }

                if(!Pattern.matches(regexPassword, password)){
                    mPassword.setError("Weak Password. ");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }


                Response.Listener<String> responseListener = new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                Toast.makeText(RegisterActivity.this, "Register Successful",
                                        Toast.LENGTH_LONG).show();}
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this, "Register Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, email, password,
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });


    }
}