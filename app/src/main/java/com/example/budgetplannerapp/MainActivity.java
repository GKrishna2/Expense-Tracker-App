package com.example.budgetplannerapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    RecyclerView recyclerView;
    RecycleAdapter rvadapter;
    ArrayList<Transaction> list;
    private String sDate;
    private String[] entryTransacArray;
    FloatingActionButton entry_button;
    FirebaseDatabase database;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            DatabaseReference myRef = database.getReference().child("Transactions");

                            if(result == RESULT_OK) {
                                String new_vendor = data.getStringExtra("new_vendor");
                                String new_item = data.getStringExtra("new_item");
                                Double new_price = Double.parseDouble(data.getStringExtra("new_price"));
                                String new_category = data.getStringExtra("new_category");
                                String new_date = data.getStringExtra("new_date");
                                String new_cardType = data.getStringExtra("new_cardType");
                                Toast.makeText(MainActivity.this,new_vendor, Toast.LENGTH_SHORT).show();
                                Transaction new_tranac = new Transaction(new_vendor,new_date,new_price,new_cardType,new_category,new_item);
                                myRef.push().setValue(new_tranac);
                            }
                            else {
                                Toast.makeText(MainActivity.this,"Operation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.transaclist);
        entry_button = findViewById(R.id.floatingActionButton2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Transactions");

        //Transaction tranac = new Transaction("Home Depot","2024-07-10",30.50,"CIBC Mastercard", "Equipment", "Wet/Dry Vaccum");
        //myRef.push().setValue(tranac);

        list = new ArrayList<>();
        rvadapter = new RecycleAdapter(this,list);
        recyclerView.setAdapter(rvadapter);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(calendarView.getDate());

        String month0 = "";
        String day0 = "";
        if(cal.get(Calendar.MONTH) < 9) {month0 = "0";}
        if(cal.get(Calendar.DAY_OF_MONTH) < 10) {day0 = "0";}

        sDate = String.valueOf(cal.get(Calendar.YEAR))+"-"+month0+String.valueOf(cal.get(Calendar.MONTH)+1)+"-"+day0+String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        Toast.makeText(MainActivity.this, sDate, Toast.LENGTH_SHORT).show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Transaction tranac = dataSnapshot.getValue(Transaction.class);
                    Log.d("TransactionID", "onDataChange: " + dataSnapshot.getKey());
                    tranac.setId(dataSnapshot.getKey());
                    list.add(tranac);
                }
                rvadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String imonth0 = "";
                String iday0 = "";
                if(i1 < 9) {imonth0 = "0";}
                if(i2 < 10) {iday0 = "0";}
                sDate = String.valueOf(i)+"-"+imonth0+String.valueOf(i1+1)+"-"+iday0+String.valueOf(i2);
                Toast.makeText(MainActivity.this, sDate, Toast.LENGTH_SHORT).show();

                Query myRefFilter = database.getReference().child("Transactions").orderByChild("date").equalTo(sDate);
                myRefFilter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        Toast.makeText(MainActivity.this, "In specific date change"+sDate, Toast.LENGTH_SHORT).show();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Transaction tranac = dataSnapshot.getValue(Transaction.class);
                            list.add(tranac);
                        }
                        rvadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        entry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newEntryIntent = new Intent(getApplicationContext(),NewTransac.class);
                //startActivity(newEntryIntent);
                activityResultLauncher.launch(newEntryIntent);
            }
        });

        //Intent receiveEntryIntent = getIntent();
        //entryTransacArray = receiveEntryIntent.getStringArrayExtra("new_transaction");
        //Toast.makeText(MainActivity.this, entryTransacArray[0], Toast.LENGTH_SHORT).show();

    }

}