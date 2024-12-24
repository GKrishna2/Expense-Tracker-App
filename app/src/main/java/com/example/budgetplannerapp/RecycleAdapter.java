package com.example.budgetplannerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    Context context;
    ArrayList<Transaction> list;

    public RecycleAdapter(Context context, ArrayList<Transaction> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // context??
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction entry = list.get(position);
        holder.date.setText(entry.getDate());
        holder.vendor.setText(entry.getVendor());
        //holder.price.setText(entry.getPrice());

        holder.vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DeleteDialog", "onClick: Entered Dialog Listener");
                CharSequence[] options = new CharSequence[]{"Delete", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Record");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0)
                        {
                            Log.d("DeleteDialog", "onClick: Delete");
                            deleteFirebaseRecord(entry.getId());
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void deleteFirebaseRecord(String ids){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Transactions").child(ids);
        myRef.removeValue();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView vendor;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.itemdate);
            vendor = (TextView) itemView.findViewById(R.id.itemvendor);
            price = (TextView) itemView.findViewById(R.id.itemcost);
        }
    }

}
