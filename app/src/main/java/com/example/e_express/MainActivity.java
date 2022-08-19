package com.example.e_express;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_express.adupters.CategoryAdapter;
import com.example.e_express.adupters.ProductAdupter;
import com.example.e_express.models.Category;
import com.example.e_express.models.Product;
import com.example.e_express.utils.Constants;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselOnScrollListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

 CategoryAdapter ctadpter;
 ArrayList<Category> clist;

 ProductAdupter proadupter;
 ArrayList<Product> pAlist;

 RecyclerView rview;
 RecyclerView prview;

 ImageCarousel carousel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
rview =findViewById(R.id.categories);

initCategories();

prview = findViewById(R.id.products);
        initProducts();

carousel = findViewById(R.id.maincarousel);
initcarouselItem();
    }

    void initCategories(){
        clist = new ArrayList<>();
        getCatgories();
        ctadpter = new CategoryAdapter(clist,this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        rview.setLayoutManager((gridLayoutManager));
        rview.setAdapter(ctadpter);

    }
    void initProducts(){
        pAlist =new ArrayList<>();

        getproducts();
        proadupter = new ProductAdupter(this,pAlist);

        GridLayoutManager glmanager = new GridLayoutManager(this,2);
        prview.setLayoutManager(glmanager);
        prview.setAdapter(proadupter);

    }
    void initcarouselItem(){
        getoffer();
    }

    void getCatgories(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request  = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    if(mainobject.getString("status").equals("success")){
                        JSONArray Categoriesarray = mainobject.getJSONArray("categories");
                        for(int i = 0; i<Categoriesarray.length(); i++){
                            JSONObject object = Categoriesarray.getJSONObject(i);
                            Category category= new Category(
                                    object.getString("name"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id"),
                                    Constants.CATEGORIES_IMAGE_URL+object.getString("icon")   );
                            clist.add(category);
                        }
                        ctadpter.notifyDataSetChanged();
                    }
                    else {
                        //nothing
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
        requestQueue.add(request);

    }
    void getproducts(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request  = new StringRequest(Request.Method.GET, Constants.GET_PRODUCTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    if(mainobject.getString("status").equals("success")){
                        JSONArray productArray = mainobject.getJSONArray("products");
                        for(int i = 0; i<productArray.length(); i++){
                            JSONObject object = productArray.getJSONObject(i);
                            Product product= new Product(
                                    object.getString("name"),
                                    Constants.PRODUCTS_IMAGE_URL+object.getString("image"),
                                    object.getString("status"),
                                    object.getDouble("price"),
                                    object.getDouble("price_discount"),
                                    object.getInt("stock"),
                                            object.getInt("id")     );
                            pAlist.add(product);
                        }
                        proadupter.notifyDataSetChanged();
                    }
                    else {
                        //nothing
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
        requestQueue.add(request);
    }
    void getoffer(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request  = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    if(mainobject.getString("status").equals("success")){
                        JSONArray offerArray = mainobject.getJSONArray("news_infos");
                        for(int i = 0; i<offerArray.length(); i++){
                            JSONObject object = offerArray.getJSONObject(i);
                          carousel.addData(
                                  new CarouselItem(
                                  Constants.NEWS_IMAGE_URL+object.getString("image"),
                                          object.getString("title") )    );

                        }

                    }
                    else {
                        //nothing
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
        requestQueue.add(request);
    }

    }


