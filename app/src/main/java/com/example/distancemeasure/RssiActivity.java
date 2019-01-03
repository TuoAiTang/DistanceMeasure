package com.example.distancemeasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RssiActivity extends AppCompatActivity {

    private TextView tv_rssi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssi);

        tv_rssi = findViewById(R.id.rssi);
        String rssi = getIntent().getStringExtra("rssi_info");
        tv_rssi.setText(rssi);
    }
}
