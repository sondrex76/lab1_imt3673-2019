package com.example.sondre_lab_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TransferActivity extends AppCompatActivity {

    private String getSender() { // WIP
        final Spinner listElements = findViewById(R.id.spinnerFriend);
        return listElements.getSelectedItem().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        // Initializes the values in the drop-down
        final Spinner friends = findViewById(R.id.spinnerFriend);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, MainActivity.namelist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friends.setAdapter(adapter);

        // Sets the text in the label at the start, since no value will have been written in it
        final TextView tv = findViewById(R.id.lbl_amount_check);
        tv.setText("There is no value specified");

        findViewById(R.id.btn_pay).setEnabled(false); // disables button at the beginning

        final EditText transfer = findViewById(R.id.txt_amount);
        transfer.addTextChangedListener(filterTextWatcher);

        final Button pay = findViewById(R.id.btn_pay); // button press
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reduces amount of money on the user's account
                MainActivity.amountMoney -= (int)(100 * Float.parseFloat(transfer.getText().toString()));
                MainActivity.newTransaction((int)(100 * Float.parseFloat(transfer.getText().toString())), getSender());
                finish();
            }
        });
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final TextView tv = findViewById(R.id.lbl_amount_check);

            try {
                if (count + start > 0 && (Float.parseFloat(s.toString()) <= MainActivity.amountMoney)
                        && Float.parseFloat(s.toString()) > 0) { // checks that the text is valid
                    findViewById(R.id.btn_pay).setEnabled(true);
                    tv.setText("");
                } else { // The text is empty, hide the Pay button
                    findViewById(R.id.btn_pay).setEnabled(false);
                    if (count + start == 0)
                        tv.setText("There are no value specified");
                    else
                        tv.setText("The amount is outside of bounds");
                }
            } catch (NumberFormatException  e) {
                findViewById(R.id.btn_pay).setEnabled(false);
                tv.setText("The value specified is invalid!");
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}
