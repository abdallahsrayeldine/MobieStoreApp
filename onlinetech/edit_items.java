package com.example.onlinetech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class edit_items extends AppCompatActivity {

    TextView name,price,category,brand;
    Button add,back,logout;
    int user_id;
    RadioGroup rg;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        user_id = sharedPreferences.getInt("user_id",-1);

        name= findViewById(R.id.itemname);
        price= findViewById(R.id.itemprice);
        add = findViewById(R.id.Add);
        back = findViewById(R.id.goback);
        logout = findViewById(R.id.logout);

        rg = findViewById(R.id.rg);
        sp = findViewById(R.id.brand_spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(edit_items.this, R.array.phones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.phone){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(edit_items.this, R.array.phones, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(adapter);
                } else if (i==R.id.laptop) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(edit_items.this, R.array.laptops, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(adapter);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(edit_items.this, homepage.class);
                startActivity(i);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(edit_items.this);
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        "http://10.0.2.2:80/store/logout.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);

                                if(response.equals("-1")){
                                    Toast.makeText(getApplicationContext(), "Failed to logout", Toast.LENGTH_SHORT).show();
                                }else if(response.equals("0")){
                                    e.remove("user_id");
                                    e.commit();
                                    Toast.makeText(getApplicationContext(),"Logged out", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(edit_items.this,SignIn.class);
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
                queue.add(stringRequest);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty()){
                    name.setError("Please fill this field");
                }else if(price.getText().toString().isEmpty()){
                    price.setError("Please fill this field");
                }
                else{
                    RadioButton selectedRadioButton = findViewById(rg.getCheckedRadioButtonId());

                    String item_name = name.getText().toString();
                    String brand = sp.getSelectedItem().toString();
                    String category = selectedRadioButton.getText().toString();
                    String prc = price.getText().toString();

                    RequestQueue queue = Volley.newRequestQueue(edit_items.this);
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            "http://10.0.2.2:80/store/add_item.php?user_id="+user_id+"&item_name="+item_name+"&brand="+brand+"&category="+category+"&price="+prc,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("-1")){
                                        Toast.makeText(getApplicationContext(), "Access Denied: You are not admin", Toast.LENGTH_SHORT).show();
                                    }else if(response.equals("Error")){
                                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(response.equals("1")){
                                        Toast.makeText(getApplicationContext(),"Item Added", Toast.LENGTH_SHORT).show();
                                        name.setText("");
                                        price.setText("");
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
                    queue.add(stringRequest);
                }
            }
        });
    }
}