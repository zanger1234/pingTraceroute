package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            try {
                textPing.setText(ping("8.8.8.8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.traceButton).setOnClickListener(v -> {
            Intent intentTrace = new Intent(MainActivity.this, TraceActivity.class);
            startActivity(intentTrace);
            finish();
        });
    }

    public String ping(String url) throws IOException {
        Process p = null;
        int ttl = 1;
        String command = "ping -c 4 ";

        Log.d(TraceActivity.tag, "Will launch : " + command + url);
        try {
            p = Runtime.getRuntime().exec(command + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert p != null;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s;
        StringBuilder res = new StringBuilder();
        while ((s = stdInput.readLine()) != null) {
            res.append(s).append("\n");
//        String str = "/system/bin/ping -c 4 " + url;
//        java.util.Scanner s = null;
//        try {
//            s = new java.util.Scanner(Runtime.getRuntime().exec(str).getInputStream()).useDelimiter("\\A");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        assert s != null;
//        return s.hasNext() ? s.next() : "";
        }
        p.destroy();
        if (res.toString().equals("")) {
            throw new IllegalArgumentException();
        }
        return res.toString();
    }

}
