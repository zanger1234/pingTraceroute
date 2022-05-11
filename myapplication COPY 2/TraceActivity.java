/*
This file is part of the project TraceroutePing, which is an Android library
implementing Traceroute with ping under GPL license v3.
Copyright (C) 2013  Olivier Goutay

TraceroutePing is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

TraceroutePing is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with TraceroutePing.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TraceActivity extends Activity {

    public static final String tag = "TraceroutePing";

    private Button buttonLaunch;
    private EditText editTextPing;
    private ProgressBar progressBarPing;
    private RecyclerViewAdapterTraceRt RecyclerViewAdapterTraceRt;
    private TracerouteWithPing tracerouteWithPing;
    private final int maxTtl = 40;
    RecyclerView recyclerView;
    ArrayList<TracerouteContainer> tracerouteContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        this.tracerouteWithPing = new TracerouteWithPing(this);
        tracerouteContainer = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewTraceRt);
        this.buttonLaunch = this.findViewById(R.id.buttonTraceRt);
        this.editTextPing = this.findViewById(R.id.editTextPing);
        this.progressBarPing = this.findViewById(R.id.progressBarPing);

        buttonLaunch.setOnClickListener(v -> {
            if (editTextPing.getText().length() == 0) {
                Toast.makeText(TraceActivity.this, getString(R.string.no_text), Toast.LENGTH_SHORT).show();
            } else {
                tracerouteContainer.clear();
                startProgressBar();
                hideSoftwareKeyboard(editTextPing);
                tracerouteWithPing.executeTraceroute(editTextPing.getText().toString(), maxTtl);
            }
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            Intent intentMain = new Intent(TraceActivity.this, MainActivity.class);
            startActivity(intentMain);
            finish();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(TraceActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewAdapterTraceRt = new RecyclerViewAdapterTraceRt(TraceActivity.this, tracerouteContainer);
        recyclerView.setAdapter(RecyclerViewAdapterTraceRt);
    }


    public void refreshList(TracerouteContainer trace) {
        final TracerouteContainer fTrace = trace;
        runOnUiThread(() -> tracerouteContainer.add(fTrace));
    }


    /**
     * Hides the keyboard
     *
     * @param currentEditText The current selected edittext
     */
    public void hideSoftwareKeyboard(EditText currentEditText) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(currentEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void startProgressBar() {
        progressBarPing.setVisibility(View.VISIBLE);
    }

    public void stopProgressBar() {
        progressBarPing.setVisibility(View.GONE);
    }

}

