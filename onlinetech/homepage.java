package com.example.onlinetech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.onlinetech.databinding.ActivityHomepageBinding;

public class homepage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomepageBinding binding;
    RadioGroup rg;
    Spinner sp;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id",-1);

        rg = findViewById(R.id.rg);
        sp = findViewById(R.id.brand_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(homepage.this, R.array.phones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.phone){
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(homepage.this, R.array.phones, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(adapter);
                } else if (i==R.id.laptop) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(homepage.this, R.array.laptops, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(adapter);
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    { // Handle presses on the action bar items
        if (item.getItemId() == R.id.action_setting) {// do something, for example start an activity
            Intent i = new Intent(homepage.this,edit_items.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    public void next(View view){
        RadioButton selectedRadioButton = findViewById(rg.getCheckedRadioButtonId());
        String category = selectedRadioButton.getText().toString();
        String brand = sp.getSelectedItem().toString();
        Intent i = new Intent(homepage.this, productpage_listView.class);
        i.putExtra("category",category);
        i.putExtra("brand",brand);
        startActivity(i);
    }
}