package com.example.e_express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e_express.models.Product;
import com.example.e_express.utils.Constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;


public class productdetails extends AppCompatActivity {

    ImageView pdimage;
    TextView details;
    Button addcart;
    Product productobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        details = (TextView) findViewById(R.id.details);
        pdimage = (ImageView) findViewById(R.id.apimage);
        addcart = (Button)findViewById(R.id.apbutton);
        Cart cart = TinyCartHelper.getCart();

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id", 0);
        double price = getIntent().getDoubleExtra("price", 0);
        Glide.with(this).load(image).into(pdimage);
        getSupportActionBar().setTitle(name);

        getproductdetails(id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(productobj,1);
                addcart.setText("added succesfully");
                addcart.setEnabled(false);
            }
        });

        }//end of oncreate

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menuforcart,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mcart){
            startActivity(new Intent(this,cartactivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    void getproductdetails(int id ){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.GET, Constants.GET_PRODUCT_DETAILS_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("success")){
                        JSONObject proobj = obj.getJSONObject("product");
                        String data = proobj.getString("description");
                        details.setText(Html.fromHtml(data) );
                    // productobj need for cart
                        productobj = new Product(
                                proobj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL+proobj.getString("image"),
                                proobj.getString("status"),
                                proobj.getDouble("price"),
                                proobj.getDouble("price_discount"),
                                proobj.getInt("stock"),
                                proobj.getInt("id")     );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(req);

    }

}