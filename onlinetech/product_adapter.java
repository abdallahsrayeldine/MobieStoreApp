package com.example.onlinetech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class product_adapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    int user_id;
    String[] item_name;
    String[] brand;
    String[] category;
    int[] price;

    public product_adapter(String[] item_name, String[] brand, String[] category, int[] price,int user_id, Context context) {
        this.item_name = item_name;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.context = context;
        this.user_id = user_id;
        inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item_name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row_view= inflater.inflate(R.layout.product_row,null);
        ((TextView)row_view.findViewById(R.id.item_name)).setText(item_name[i]);
        ((TextView)row_view.findViewById(R.id.price)).setText(String.valueOf(price[i]+"$"));
        ((TextView)row_view.findViewById(R.id.Brand)).setText(brand[i]);
        ((TextView)row_view.findViewById(R.id.category)).setText(category[i]);
        Button add = row_view.findViewById(R.id.button3);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView item_name = row_view.findViewById(R.id.item_name);
                TextView price = row_view.findViewById(R.id.price);
                TextView brand = row_view.findViewById(R.id.Brand);
                TextView category = row_view.findViewById(R.id.category);
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    "http://10.0.2.2:80/store/add_to_cart.php?user_id="+user_id+"&item_name="+item_name.getText().toString()+"&price="+price.getText().toString()+"&brand="+brand.getText().toString()+"&category="+category.getText().toString()+"&quantity="+1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.trim().equals("success")){
                                Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Error in adding", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener()  {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error in adding:" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                );
                queue.add(stringRequest);
            }
        });
        return row_view;
    }

}
