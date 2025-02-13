package com.example.onlinetech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinetech.databinding.ActivitySignInBinding;

public class SignIn extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySignInBinding binding;
    int user_id;
    TextView name,password;
    Button submit,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        submit=findViewById(R.id.submit);
        signup=findViewById(R.id.signup);
        name=findViewById(R.id.editTextName);
        password=findViewById(R.id.editTextPassword);

        connect();
        SharedPreferences user= getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor e = user.edit();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = name.getText().toString();
                String pass = password.getText().toString();
                if(username.isEmpty()){
                    name.setError("Please enter your name");
                } else if (pass.isEmpty()) {
                    password.setError("Please enter a password");
                }else{
                RequestQueue queue = Volley.newRequestQueue(SignIn.this);
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        "http://10.0.2.2:80/store/login.php?username="+username+"&password="+pass,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("-1")) {
                                    Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                                }else{
                                    user_id=Integer.parseInt(response);
                                    e.putInt("user_id",user_id);
                                    e.commit();
                                    Intent i = new Intent(SignIn.this,homepage.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        },
                        new Response.ErrorListener()  {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                queue.add(stringRequest);}
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignIn.this,signup.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void connect(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://10.0.2.2:80/store/connection.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error in connecting", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(stringRequest);
    }
}