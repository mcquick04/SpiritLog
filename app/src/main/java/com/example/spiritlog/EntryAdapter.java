package com.example.spiritlog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
    public interface OnEntryClickListener {
        void onEntryClick(Entry entry);
    }

    private List<Entry> entries;
    private OnEntryClickListener listener;

    public EntryAdapter(List<Entry> entries, OnEntryClickListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry, parent, false);
        return new EntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.bind(entry, listener);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemLocationDate;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemLocationDate = itemView.findViewById(R.id.tvItemLocationDate);
        }

        public void bind(Entry entry, OnEntryClickListener listener) {

            String dateTime = entry.getDateTime() == null ? "" : entry.getDateTime();
            String location = entry.getLocation() == null ? "" : entry.getLocation();

            String combined;
            if (!dateTime.isEmpty() && !location.isEmpty()) {
                combined = dateTime + " - " + location;
                }

            else if (!dateTime.isEmpty()) {
                combined = dateTime;
                }

            else {
                combined = location;
            }

            tvItemLocationDate.setText(combined);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEntryClick(entry);
                }
            });
        }
    }
}
