package com.example.expensetrackerjavagroovy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerjavagroovy.EditDataActivity;
import com.example.expensetrackerjavagroovy.R;
import com.example.expensetrackerjavagroovy.model.Record;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListViewHolder> {

    Context context;

    List<Record> records;

    String recordAmt = "", recordDesc = "", recordType = "", recordDate = "";

    public ExpenseListAdapter(Context context, List<Record> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ExpenseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpenseListViewHolder(LayoutInflater.from(context).inflate(R.layout.expense_list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListViewHolder holder, int position) {

        try {
            recordAmt = records.get(position).getAmount().toString();
            recordDesc = records.get(position).getDescription().toString();
            recordType = records.get(position).getType().toString();
            recordDate = records.get(position).getDate().toString();
        }catch (Exception e){
            Log.v("NULL POINTER ERROR", e.toString());
        }

        holder.amountView.setText(String.format("Amount: %s", recordAmt));
        holder.descriptionView.setText(String.format("Description: %s", recordDesc));
        holder.typeView.setText(String.format("Type: %s", recordType));
        holder.dateView.setText(String.format("Date: %s", recordDate));
        holder.btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditDataActivity.class);
                String id = records.get(position).getId();
                intent.putExtra("_id", id);
                intent.putExtra("amount", recordAmt);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }
}
