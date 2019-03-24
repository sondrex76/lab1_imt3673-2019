package com.example.sondre_lab_1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<MainActivity.Transaction> mData;
    private LayoutInflater mInflater;
    private View.OnLongClickListener mClickListener;

        // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<MainActivity.Transaction> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MainActivity.Transaction data = mData.get(position);
        holder.myTextView.setText(String.format("%-8s|%-10s|%-6s|%-6s", data.timeOfTransaction, data.participant, data.value / 100.0f, data.valueThen / 100.0f));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvInner);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            // if (mClickListener != null) mClickListener.onItemLongClick((AdapterView<?>)view.getParent(), view, this.getAdapterPosition(), 1); // old
            TransactionsActivity.makeToast(returnPosition(view), view);

            if (mClickListener != null) mClickListener.onLongClick(view);
            return true;
        }

        public int returnPosition(View view) {
            return this.getAdapterPosition();
        }
    }

    // convenience method for getting data at click position
    MainActivity.Transaction getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(View.OnLongClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        public boolean onItemLongClick(View view);
    }
}