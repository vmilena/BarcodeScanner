package com.example.barcodedemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodedemo.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextResultsAdapter extends RecyclerView.Adapter<TextResultsAdapter.TextResultViewHolder> {

    private Context context;
    private ArrayList<String> items;
    OnItemClickListener onItemClickListener;

    public TextResultsAdapter(Context context, ArrayList<String> items, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.items = items;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TextResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextResultViewHolder(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextResultViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TextResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView textView;

        public TextResultViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String item) {
            textView.setText(item);
            textView.setOnClickListener(view -> onItemClickListener.itemClicked(item));
        }
    }
}
