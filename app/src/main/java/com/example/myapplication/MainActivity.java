package com.example.myapplication;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OkHttpClient okHttpClient = new OkHttpClient();
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
            RequestBody formBody = new FormBody.Builder().add("sample", "text").build();
            Request request = new Request.Builder().url("http://192.168.100.2:5000/").post(formBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show());
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (Objects.requireNonNull(response.body()).string().equals("received")) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "data received", Toast.LENGTH_SHORT).show());
                    }
                }
            });
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
        }
        p.destroy();
        if (res.toString().equals("")) {
            throw new IllegalArgumentException();
        }
        return res.toString();
    }

}
