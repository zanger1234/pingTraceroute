package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        String rssi = "The RSSI value is: " + info.getRssi();

        findViewById(R.id.button).setOnClickListener(v -> {
            TextView textSSID = findViewById(R.id.textViewSSID);
            textSSID.setText(ssid);
            TextView textRSSI = findViewById(R.id.textViewRSSI);
            textRSSI.setText(rssi);
            TextView textPing = findViewById(R.id.textViewPing);
            textPing.setText(ping("8.8.8.8"));

        });

        findViewById(R.id.traceButton).setOnClickListener(v -> {
            Intent intentTrace = new Intent(MainActivity.this, TraceActivity.class);
            startActivity(intentTrace);
            finish();
        });
    }


    public String ping(String url) {
        String str = "/system/bin/ping -c 4 " + url;
        java.util.Scanner s = null;
        try {
            s = new java.util.Scanner(Runtime.getRuntime().exec(str).getInputStream()).useDelimiter("\\A");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert s != null;
        return s.hasNext() ? s.next() : "";
    }
}