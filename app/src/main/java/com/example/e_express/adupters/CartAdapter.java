package com.example.e_express.adupters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_express.R;
import com.example.e_express.models.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartviewholder> {

    Context context;
    ArrayList<Product>crproductlist;
    Cartlistner cartlistner;
    Cart cart;


    public interface Cartlistner{
        public void onquntitychnged();
    }
    public CartAdapter(Context context, ArrayList<Product> crproductlist ,Cartlistner cartlistner) {
        this.context = context;
        this.crproductlist = crproductlist;
        this.cartlistner = cartlistner;
        cart = TinyCartHelper.getCart();
    }



    @NonNull
    @Override
    public cartviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new cartviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartviewholder holder, int position) {
        Product product = crproductlist.get(position);
        Glide.with(context).load(product.getImage()).into(holder.CAimage);
        holder.CAtext1.setText(product.getName());
        holder.CAtext3.setText("RS : "+product.getPrice());
        holder.CAtext2.setText(product.getQuantity()+"items(s)");

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           Dialog dialog;
           Button ADD,SUB,SAVE;
           TextView Productname,Stock ,Quntity;



           @Override
           public void onClick(View view) {
               dialog = new Dialog(context);
               dialog.setContentView(R.layout.quntity_dialog);
               dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                ADD = dialog.findViewById(R.id.plusBtn);
                SUB = dialog.findViewById(R.id.minusBtn);
                SAVE = dialog.findViewById(R.id.saveBtn);
                Productname= dialog.findViewById(R.id.productName);
               Stock= dialog.findViewById(R.id.productStock);
               Quntity  =  dialog.findViewById(R.id.quantity);

               Stock.setText("stock"+product.getStock());
               Productname.setText(product.getName());
               Quntity.setText(String.valueOf(product.getQuantity()));
               int stock = product.getStock();




                ADD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quntity = product.getQuantity();
                        quntity++;

                        if(quntity>product.getStock()) {
                            Toast.makeText(context, "max stock avilable is" + product.getStock(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                                product.setQuantity(quntity);
                                Quntity.setText(String.valueOf(product.getQuantity()));
                            }

                    }
                });
                SUB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quntity = product.getQuantity();
                        if (quntity>2){
                            quntity--;
                            product.setQuantity(quntity);
                            Quntity.setText(String.valueOf(product.getQuantity()));
                        }


                    }
                });
                SAVE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyDataSetChanged();
                        cart.updateItem(product,product.getQuantity());
                        cartlistner.onquntitychnged();
                        dialog.dismiss();

                    }
                });

               dialog.show();
           }
       });
    }

    @Override
    public int getItemCount() {
        return crproductlist.size();
    }

    public class cartviewholder extends RecyclerView.ViewHolder{
        ImageView CAimage;
        TextView CAtext1 ;
        TextView CAtext2;
        TextView CAtext3;

        public cartviewholder(@NonNull View itemView) {
            super(itemView);
            CAimage = itemView.findViewById(R.id.criimage);
            CAtext1 = itemView.findViewById(R.id.product);
            CAtext2 =itemView.findViewById(R.id.item);
            CAtext3 =itemView.findViewById(R.id.price);

        }
    }
}
