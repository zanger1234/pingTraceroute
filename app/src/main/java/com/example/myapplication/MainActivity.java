package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;



public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O)
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
//            RequestBody formBody = new FormBody.Builder().add("sample", "text").build();
//            Request request = new Request.Builder().url("http://127.0.0.1:5000/").post(formBody).build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show());
//                }
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    if (Objects.requireNonNull(response.body()).string().equals("received")) {
//                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "data received", Toast.LENGTH_SHORT).show());
//                    }
//                }
//            });
            sendRequest("http://127.0.0.1:5000/");
        });
        findViewById(R.id.traceButton).setOnClickListener(v -> {
            Intent intentTrace = new Intent(MainActivity.this, TraceActivity.class);
            startActivity(intentTrace);
            finish();
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendRequest(String backendURL){
        try{
            URL url = new URL(backendURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String outputJSON = "{\"test\":\"test\"}";
            byte[] out = outputJSON.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = connection.getOutputStream();
            ((OutputStream) stream).write(out);
            connection.disconnect();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "cannot reach server", Toast.LENGTH_SHORT).show();
        }
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
