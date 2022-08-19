package com.example.e_express;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {
    TextView code, price ,info ;
    Button pay,ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Dialog dialog =new Dialog(PaymentActivity.this);
        dialog.setContentView(R.layout.payment_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        code = findViewById(R.id.tvcode);
        price = findViewById(R.id.tvlastprice);
        pay = findViewById(R.id.btnpay);
        info = dialog.findViewById(R.id.tvinfo);
        ok = dialog.findViewById(R.id.Btnok);


        String totalfees = getIntent().getStringExtra("total");
        String orderCode = getIntent().getStringExtra("orderCode");

        code.setText(orderCode);
        price.setText(totalfees);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setText("PAY AN AMMOUNT OF"+totalfees+" TO ON OF OUR BANK ACOOUNT NUMBER"+""+
                        "BANKACCOUNT NO : XXXXXXXXXXXXXXXX"+" "+ "BANKACCOUNT NO : XXXXXXXXXXXXXXXX"+
                        "AND WRITE YOUR ORDER ID "+"*"+orderCode+"*"+ "PAYMENT REFERANCE.  ");
                dialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });



    }
}