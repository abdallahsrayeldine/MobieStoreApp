package com.example.onlinetech;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.onlinetech.databinding.ActivityCartBinding;
//import com.example.onlinetech.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class cart extends AppCompatActivity {
    int total;
    int user_id;
    TextView tv6;
    Button button,back;
    ListView cart_list;
//    private @NonNull ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
//        binding = ActivityCartBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        setSupportActionBar(binding.toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id",-1);

        tv6 = findViewById(R.id.textView6);
        button = findViewById(R.id.button);
        cart_list = findViewById(R.id.Cart_list);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(cart.this, homepage.class);
                startActivity(i);
                finish();
            }
        });
        getcart();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });

    }
    public void getcart(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://10.0.2.2:80/store/get_cart.php?user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] parts = response.split("\\|");
                        if(parts.length==2) {
                            JSONArray jsonArray;

                            try {
                                jsonArray = new JSONArray(parts[0]);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            String[] names = new String[jsonArray.length()];
                            int[] prices = new int[jsonArray.length()];
                            String[] brand = new String[jsonArray.length()];
                            String[] category = new String[jsonArray.length()];
                            int[] quantity = new int[jsonArray.length()];
                            for(int i =0;i<jsonArray.length();i++){
                                try {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    names[i]=jsonObject.getString("item_name");
                                    prices[i]=jsonObject.getInt("price");
                                    brand[i]=jsonObject.getString("brand");
                                    category[i]=jsonObject.getString("category");
                                    quantity[i]=jsonObject.getInt("quantity");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            cart_list.setAdapter(new cart_adapter(names,brand,category,prices,quantity,user_id,cart.this));

                            total = Integer.parseInt(parts[1]);
                            tv6.setText(String.valueOf(total)+"$");
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
    public void checkout(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://10.0.2.2:80/store/checkout.php?user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.add(stringRequest);
        cart.this.refreshActivity();
        AlertDialog.Builder mb = new AlertDialog.Builder(cart.this);
        mb.setTitle("Checkout");
        if(total == 0) mb.setMessage("Nothing in cart!!");
        else mb.setMessage("Your Total is " +total+"$. Please pay on delivery.");
        mb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getcart();
            }
        });
        Dialog d = mb.create();
        d.show();
    }

    public void refreshActivity(){
        getcart();
    }
}
