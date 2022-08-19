package com.example.e_express;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_express.adupters.CartAdapter;
import com.example.e_express.models.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class cartactivity extends AppCompatActivity {
    RecyclerView CARview;
    ArrayList<Product>CARlist;
    CartAdapter CARadapter;
    TextView Totalpriceobj ;
    Button continu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartactivity);
        CARview = findViewById(R.id.carview);
        Totalpriceobj = findViewById(R.id.totalprice);
        continu = findViewById(R.id.continuebutton);


        CARlist = new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();
        for(Map.Entry<Item, Integer> itemIntegerMap : cart.getAllItemsWithQty().entrySet()){
            Product product = (Product) itemIntegerMap.getKey();
            int quantity = itemIntegerMap.getValue();
            product.setQuantity(quantity);

            CARlist.add(product);
        }

        Totalpriceobj.setText("RS: "+String.valueOf(cart.getTotalPrice()));

        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cartactivity.this,checkoutActivity.class);
                startActivity(intent);
            }
        });

        CARadapter = new CartAdapter(this, CARlist, new CartAdapter.Cartlistner() {
            @Override
            public void onquntitychnged() {
                Totalpriceobj.setText("RS: "+ String.valueOf(cart.getTotalPrice()));

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration =  new DividerItemDecoration(this,layoutManager.getOrientation());

        CARview.setLayoutManager(layoutManager);
        CARview.addItemDecoration(decoration);
        CARview.setAdapter(CARadapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}