package com.example.e_express.adupters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_express.R;
import com.example.e_express.models.Product;
import com.example.e_express.productdetails;

import java.util.ArrayList;

public class ProductAdupter extends  RecyclerView.Adapter<ProductAdupter.productviewholder> {
Context context;
ArrayList<Product> plist;

    public ProductAdupter(Context context, ArrayList<Product> plist) {
        this.context = context;
        this.plist = plist;
    }

    @NonNull
    @Override
    public ProductAdupter.productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false);
        return new productviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdupter.productviewholder holder, int position) {
        Product product= plist.get(position);
        Glide.with(context).load(product.getImage()).into(holder.pimageview);
        holder.ptextview1.setText(product.getName());
        holder.ptextview2.setText("(INR) RS "+product.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, productdetails.class);
                intent.putExtra("name",product.getName());
                intent.putExtra("image",product.getImage());
                intent.putExtra("id",product.getId());
                intent.putExtra("price",product.getPrice());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return plist.size();
    }

    public class productviewholder extends RecyclerView.ViewHolder {
        ImageView pimageview;
        TextView ptextview1;
        TextView ptextview2;

        public productviewholder(@NonNull View itemView) {
            super(itemView);
            pimageview = itemView.findViewById(R.id.ipimage);
            ptextview1 = itemView.findViewById(R.id.iptext1);
            ptextview2 =itemView.findViewById(R.id.iptext2);

        }
    }
}
