package com.example.e_express;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_express.adupters.CartAdapter;
import com.example.e_express.models.Product;
import com.example.e_express.utils.Constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class checkoutActivity extends AppCompatActivity {
    ArrayList<Product> CARlist;
    CartAdapter CARadapter;
    TextView Totalpriceobj,total;
    RecyclerView rview;
    Button checkoutBtn;
    EditText nameBox,emailBox,phoneBox,addressBox,dateBox,commentBox;
    double totalPrice = 0;
    final int tax = 11;
    ProgressDialog progressDialog;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setTitle("checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rview = findViewById(R.id.cartList);
        Totalpriceobj = findViewById(R.id.subtotal);
        total = findViewById(R.id.total);
        nameBox = findViewById(R.id.nameBox);
        emailBox = findViewById(R.id.emailBox);
        phoneBox = findViewById(R.id.phoneBox);
        addressBox = findViewById(R.id.addressBox);
        dateBox = findViewById(R.id.dateBox);
        commentBox = findViewById(R.id.commentBox);
        checkoutBtn = findViewById(R.id.checkoutBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        CARlist = new ArrayList<>();

         cart = TinyCartHelper.getCart();
        for(Map.Entry<Item, Integer> itemIntegerMap : cart.getAllItemsWithQty().entrySet()){
            Product product = (Product) itemIntegerMap.getKey();
            int quantity = itemIntegerMap.getValue();
            product.setQuantity(quantity);

            CARlist.add(product);
        }

        Totalpriceobj.setText("RS: "+String.valueOf(cart.getTotalPrice()));


        CARadapter = new CartAdapter(this, CARlist, new CartAdapter.Cartlistner() {
            @Override
            public void onquntitychnged() {
                Totalpriceobj.setText("RS: "+ String.valueOf(cart.getTotalPrice()));

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration =  new DividerItemDecoration(this,layoutManager.getOrientation());

        rview.setLayoutManager(layoutManager);
        rview.addItemDecoration(decoration);
        rview.setAdapter(CARadapter);
        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        total.setText("RS " + totalPrice);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processOrder();
            }
        });

    }

    void processOrder() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {

            productOrder.put("address",addressBox.getText().toString());
            productOrder.put("buyer",nameBox.getText().toString());
            productOrder.put("comment", commentBox.getText().toString());
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
            productOrder.put("email", emailBox.getText().toString());
            productOrder.put("phone", phoneBox.getText().toString());
            productOrder.put("serial", "cab8c1a4e4421a3b");
            productOrder.put("shipping", "");
            productOrder.put("shipping_location", "");
            productOrder.put("shipping_rate", "0.0");
            productOrder.put("status", "WAITING");
            productOrder.put("tax", tax);
            productOrder.put("total_fees", totalPrice);

            JSONArray product_order_detail = new JSONArray();
            for(Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                JSONObject productObj = new JSONObject();
                productObj.put("amount", quantity);
                productObj.put("price_item", product.getPrice());
                productObj.put("product_id", product.getId());
                productObj.put("product_name", product.getName());
                product_order_detail.put(productObj); // for all new item adding in recycler view
            }

            dataObject.put("product_order",productOrder);
            dataObject.put("product_order_detail",product_order_detail);

            Log.e("err", dataObject.toString());

        } catch (JSONException e) {}


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(checkoutActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                        String orderNumber = response.getJSONObject("data").getString("code");
                        String total_fees = response.getJSONObject("data").getString("total_fees");
                        new AlertDialog.Builder(checkoutActivity.this)
                                .setTitle("Order Successful")
                                .setCancelable(false)
                                .setMessage("Your order number is: " + orderNumber)
                                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(checkoutActivity.this, PaymentActivity.class);
                                        intent.putExtra("orderCode", orderNumber);
                                        intent.putExtra("total", total_fees);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else {
                        new AlertDialog.Builder(checkoutActivity.this)
                                .setTitle("Order Failed")
                                .setMessage("Something went wrong, please try again.")
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                        Toast.makeText(checkoutActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                    Log.e("res", response.toString());
                } catch (Exception e) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Security","secure_code");
                return headers;
            }
        } ;

        queue.add(request);

    }


    @Override
    public boolean onSupportNavigateUp() {
    finish();
        return super.onSupportNavigateUp();
    }

}