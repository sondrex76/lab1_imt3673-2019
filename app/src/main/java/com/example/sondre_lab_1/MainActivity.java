package com.example.sondre_lab_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int amountMoney = 0;

    // Transaction element
    public static class Transaction{
        String timeOfTransaction;
        String participant;
        int value;      // Stored as int 100 times larger then actual value to not lose precision
        int valueThen;  // Same as the above

        public int returnValue() {
            return value;
        }

        public String returnParticipant() {
            return participant;
        }
    }

    // formats floats to have only two decimals
    public static float formatFloat(float f) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return Float.parseFloat(df.format(f));
    }

    // List of transactions
    public static ArrayList transactions = new ArrayList<Transaction>();
    // List of names
    public static String[] namelist = new String[]{"Alice", "Bob", "Charlie", "Dawn", "Elvis", "Frode"};

    //
    // Functions returning the various single element of a transaction
    //
    public static String transactionReturnTime(Transaction t) {
        return t.timeOfTransaction;
    }
    public static String transactionReturnParticipant(Transaction t) {
        return t.participant;
    }
    public static int transactionReturnValue(Transaction t) {
        return t.value;
    }
    public static int transactionReturnValueThen(Transaction t) {
        return t.valueThen;
    }

    // Creates a new transaction
    public static void newTransaction(int valueTransferred, String sender) {
        Transaction currentTransaction = new Transaction();

        // Formats time to HH:mm:ss, which removes milliseconds from the value
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        currentTransaction.timeOfTransaction = formatter.format(LocalTime.now());

        currentTransaction.participant = sender;
        currentTransaction.value = valueTransferred;
        currentTransaction.valueThen = amountMoney;

        // Crashes when the following line is executed!
        transactions.add(currentTransaction);
    }

    // Sets new transaction into list from onRestoreInstanceState
    public static void newTransaction(String timeOfTransaction, String participant, int value, int valueThen) {
        Transaction currentTransaction = new Transaction();

        currentTransaction.timeOfTransaction = timeOfTransaction;

        currentTransaction.participant = participant;
        currentTransaction.value = value;
        currentTransaction.valueThen = valueThen;

        // Crashes when the following line is executed!
        transactions.add(currentTransaction);
    }

    public void updateMoney() {
        final TextView tv = findViewById(R.id.lbl_balance);
        tv.setText("" + formatFloat(amountMoney / 100.0f));
    }

    // Code executed at the start of the program
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactions.clear(); // clears the list in case teh screen is rotated
        Random rand = new Random();

        // Sets the starting value to a value between 90 and 110
        amountMoney = (rand.nextInt(20) + 90) * 100;
        newTransaction(amountMoney, "Angel"); // Adds a transaction for the initial money

        updateMoney();

        final Button transfer = findViewById(R.id.btn_transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TransferActivity.class));
            }
        });

        final Button transactions = findViewById(R.id.btn_transactions);
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateMoney();
    }

    // saves data on rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentMoney", amountMoney);       // money
        outState.putInt("NumItems", transactions.size());   // Number of transactions

        // all transactions
        for (int i = 0; i < transactions.size(); i++) {
            outState.putString("Time" + i, transactionReturnTime((Transaction)transactions.get(i)));
            outState.putString("Participant" + i, transactionReturnParticipant((Transaction)transactions.get(i)));
            outState.putInt("Value" + i, transactionReturnValue((Transaction)transactions.get(i)));
            outState.putInt("ValueThen" + i, transactionReturnValueThen((Transaction)transactions.get(i)));
        }

        super.onSaveInstanceState((outState));
    }

    // gets values on rotation
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // empties the list before adding new ones
        transactions.clear();

        amountMoney = (int)savedInstanceState.get("currentMoney"); // gets the current amount of money
        int numTransactions = (int)savedInstanceState.get("NumItems");

        for (int i = 0; i < numTransactions; i++) {
            // Sets in transaction
            newTransaction(
                    (String)savedInstanceState.get("Time" + i),
                    (String)savedInstanceState.get("Participant" + i),
                    (int)savedInstanceState.get("Value" + i),
                    (int)savedInstanceState.get("ValueThen" + i));
        }
        updateMoney();
    }
}
