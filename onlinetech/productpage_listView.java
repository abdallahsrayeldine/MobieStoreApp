package com.example.onlinetech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinetech.databinding.ActivityProductpageListViewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class productpage_listView extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityProductpageListViewBinding binding;
    int user_id;
    ListView listView;
    String brand,category;
    Button cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductpageListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        brand = i.getStringExtra("brand");
        category = i.getStringExtra("category");

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id",-1);
        listView = findViewById(R.id.listView);

        load_items();

        cart = findViewById(R.id.mycart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(productpage_listView.this,cart.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void load_items(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://10.0.2.2:80/store/get_all_items.php?brand="+brand+"&category="+category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray;

                        try {
                            jsonArray = new JSONArray(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        String[] names = new String[jsonArray.length()];
                        int[] prices = new int[jsonArray.length()];
                        String[] brand = new String[jsonArray.length()];
                        String[] category = new String[jsonArray.length()];
                        for(int i =0;i<jsonArray.length();i++){
                            try {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                names[i]=jsonObject.getString("name");
                                prices[i]=jsonObject.getInt("price");
                                brand[i]=jsonObject.getString("brand");
                                category[i]=jsonObject.getString("category");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        listView.setAdapter(new product_adapter(names,brand,category,prices,user_id,productpage_listView.this));

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_settings){
                Intent i = new Intent(productpage_listView.this, edit_items.class);
                startActivity(i);
                finish();
            }
            else if(item.getItemId()== android.R.id.home){
                this.finish();
            }
        return true;
    }

}