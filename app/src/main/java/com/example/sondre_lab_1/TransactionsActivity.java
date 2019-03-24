package com.example.sondre_lab_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class TransactionsActivity extends AppCompatActivity {
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        // gets the RecyclerView element
        final RecyclerView recyclerViewTransaction = findViewById(R.id.recyclerViewTransaction);
        ArrayList transactions = MainActivity.transactions;

        // makes a LinearLayoutManager for managing the lines
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTransaction.setLayoutManager(linearLayoutManager);

        adapter = new MyRecyclerViewAdapter(this, transactions);
        recyclerViewTransaction.setAdapter(adapter);

        adapter.setClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    // Makes toast based on which value is selected
    static public void makeToast(int value, View v) {
        Toast.makeText(v.getContext(),
                ((MainActivity.Transaction)MainActivity.transactions.get(value)).returnParticipant() + " " +
                        ((MainActivity.Transaction)MainActivity.transactions.get(value)).returnValue() / 100.0f,
                Toast.LENGTH_SHORT).show();
    }
}
