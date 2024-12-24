package com.example.budgetplannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class NewTransac extends AppCompatActivity {
    Button submit_button;
    EditText vendor_text;
    EditText item_text;
    EditText price_text;
    Spinner category_spinner;
    DatePicker date_datepicker;
    RadioButton visa_radio;
    RadioButton mastercard_radio;
    String[] categoryArr = {"Utilities","Food","Grocery","Electronics","Transportation","Education","Fun","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transac);

        submit_button = findViewById(R.id.input_submitEntry);
        vendor_text = findViewById(R.id.input_vendor);
        item_text = findViewById(R.id.input_item);
        price_text = findViewById(R.id.input_price);
        category_spinner = findViewById(R.id.input_category);
        date_datepicker = findViewById(R.id.input_date);
        visa_radio = findViewById(R.id.input_cardVs);
        mastercard_radio = findViewById(R.id.input_cardMc);

        ArrayAdapter<String> spinadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categoryArr);
        category_spinner.setAdapter(spinadapter);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vendor = vendor_text.getText().toString();
                String item = item_text.getText().toString();
                String price = price_text.getText().toString();
                String category = category_spinner.getSelectedItem().toString();
                //String category = "category";
                String date = String.valueOf(date_datepicker.getYear()) + "-" + String.valueOf(date_datepicker.getMonth()) + "-" + String.valueOf(date_datepicker.getDayOfMonth());
                String cardType = "";
                if(visa_radio.isChecked() == true) {cardType = visa_radio.getText().toString();}
                if(mastercard_radio.isChecked() == true) {cardType = mastercard_radio.getText().toString();}

                Intent intent = new Intent(NewTransac.this,MainActivity.class);

                intent.putExtra("new_vendor",vendor);
                intent.putExtra("new_item",item);
                intent.putExtra("new_price",price);
                intent.putExtra("new_category",category);
                intent.putExtra("new_date",date);
                intent.putExtra("new_cardType",cardType);

                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}

