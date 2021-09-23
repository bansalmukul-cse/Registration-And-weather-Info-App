package com.example.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;



public class weatherActivity extends AppCompatActivity {

    EditText edtxt;
    Button btsubmit;
    TextView tvaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        edtxt= findViewById(R.id.City);
        btsubmit=findViewById(R.id.search);
        tvaddress=findViewById(R.id.res);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address= edtxt.getText().toString();
                GeoLocation geoLocation= new GeoLocation();
                geoLocation.getAddress(address,getApplicationContext(),new GeoHandler());
            }
        });
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            String address;
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    break;
                default:
                    address = null;
            }
            tvaddress.setText(address);
        }
    }
}