package com.example.expensetrackerjavagroovy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerjavagroovy.R;
import com.example.expensetrackerjavagroovy.model.Record;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListViewHolder> {

    Context context;

    List<Record> records;

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
        holder.amountView.setText(records.get(position).getAmount().toString());
        holder.descriptionView.setText(records.get(position).getDescription().toString());
        holder.typeView.setText(records.get(position).getType().toString());
        holder.dateView.setText(records.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }
}
