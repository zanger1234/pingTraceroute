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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class TraceActivity extends Activity {

	public static final String tag = "TraceroutePing";
	public static final String INTENT_TRACE = "INTENT_TRACE";

	private Button buttonLaunch;
	private Button buttonPing;

	private EditText editTextPing;
	private ProgressBar progressBarPing;
	private ListView listViewTraceroute;
	private TraceListAdapter traceListAdapter;

	private TracerouteWithPing tracerouteWithPing;
	private final int maxTtl = 40;

	private List<TracerouteContainer> traces;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);

		this.tracerouteWithPing = new TracerouteWithPing(this);
		this.traces = new ArrayList<>();
		this.buttonLaunch = this.findViewById(R.id.buttonLaunch);
		this.buttonPing = this.findViewById(R.id.buttonPing);
		this.editTextPing = this.findViewById(R.id.editTextTraceRt);
		this.listViewTraceroute = this.findViewById(R.id.listViewTraceroute);
		this.progressBarPing = this.findViewById(R.id.progressBarPing);

		initView();
		}


	private void initView() {
		buttonLaunch.setOnClickListener(v -> {
			if (editTextPing.getText().length() == 0) {
				Toast.makeText(TraceActivity.this, getString(R.string.no_text), Toast.LENGTH_SHORT).show();
			} else {
				traces.clear();
				traceListAdapter.notifyDataSetChanged();
				startProgressBar();
				hideSoftwareKeyboard(editTextPing);
				tracerouteWithPing.executeTraceroute(editTextPing.getText().toString(), maxTtl);
			}
		});
		buttonPing.setOnClickListener(v -> {
			Intent intent = new Intent(TraceActivity.this, MainActivity.class);
			startActivity(intent);
			finish();

		});
		traceListAdapter = new TraceListAdapter(getApplicationContext());
		listViewTraceroute.setAdapter(traceListAdapter);
	}

	/**
	 * Allows to refresh the listview of traces
	 *
	 * @param trace
	 *            The list of traces to refresh
	 */
	public void refreshList(TracerouteContainer trace) {
		final TracerouteContainer fTrace = trace;
        runOnUiThread(() -> {
			traces.add(fTrace);
			traceListAdapter.notifyDataSetChanged();
		});
	}


	public class TraceListAdapter extends BaseAdapter {

		private final Context context;

		public TraceListAdapter(Context c) {
			context = c;
		}

		public int getCount() {
			return traces.size();
		}

		public TracerouteContainer getItem(int position) {
			return traces.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.item_list_trace, null);

				TextView textViewNumber = convertView.findViewById(R.id.textViewNumber);
				TextView textViewIp = convertView.findViewById(R.id.textViewIp);
				TextView textViewTime = convertView.findViewById(R.id.textViewTime);
				ImageView imageViewStatusPing = convertView.findViewById(R.id.imageViewStatusPing);

				holder = new ViewHolder();
				holder.textViewNumber = textViewNumber;
				holder.textViewIp = textViewIp;
				holder.textViewTime = textViewTime;
				holder.imageViewStatusPing = imageViewStatusPing;

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TracerouteContainer currentTrace = getItem(position);

			if (position % 2 == 1) {
				convertView.setBackgroundResource(R.drawable.table_odd_lines);
			} else {
				convertView.setBackgroundResource(R.drawable.table_pair_lines);
			}

			if (currentTrace.isSuccessful()) {
				holder.imageViewStatusPing.setImageResource(R.drawable.check);
			} else {
				holder.imageViewStatusPing.setImageResource(R.drawable.cross);
			}

			holder.textViewNumber.setText(position + "");
			holder.textViewIp.setText(currentTrace.getHostname() + " (" + currentTrace.getIp() + ")");
			holder.textViewTime.setText(currentTrace.getMs() + "ms");

			return convertView;
		}

		class ViewHolder {
			TextView textViewNumber;
			TextView textViewIp;
			TextView textViewTime;
			ImageView imageViewStatusPing;
		}
	}

	/**
	 * Hides the keyboard
	 * 
	 * @param currentEditText
	 *            The current selected edittext
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
