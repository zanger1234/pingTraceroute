package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecyclerViewAdapterTraceRt extends RecyclerView.Adapter<RecyclerViewAdapterTraceRt.ViewHolder> {
    ArrayList<TracerouteContainer> tracerouteContainer;
    Context context;
    public static final String tag = "TraceroutePing";

    public RecyclerViewAdapterTraceRt(Context context, ArrayList<TracerouteContainer> tracerouteContainer) {
        this.context = context;
        this.tracerouteContainer = tracerouteContainer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_trace, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TracerouteContainer currentTrace = tracerouteContainer.get(position);
        holder.textViewNumber.setText(position + "");
        holder.textViewIp.setText(currentTrace.getHostname() + " (" + currentTrace.getIp() + ")");
        holder.textViewTime.setText(currentTrace.getMs() + "ms");

        if (currentTrace.isSuccessful()) {
            holder.imageViewStatusPing.setImageResource(R.drawable.check);
        } else {
            holder.imageViewStatusPing.setImageResource(R.drawable.cross);
        }
    }


    @Override
    public int getItemCount() {
        return tracerouteContainer.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber;
        TextView textViewIp;
        TextView textViewTime;
        ImageView imageViewStatusPing;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewIp = itemView.findViewById(R.id.textViewIp);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageViewStatusPing = itemView.findViewById(R.id.imageViewStatusPing);


        }
    }


}
