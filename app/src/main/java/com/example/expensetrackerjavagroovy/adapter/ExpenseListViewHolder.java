package com.example.expensetrackerjavagroovy.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerjavagroovy.R;

public class ExpenseListViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;

    TextView amountView, descriptionView, typeView, dateView;

    Button btnEditData;

    public ExpenseListViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.expenseListLogo);
        amountView = itemView.findViewById(R.id.expenseAmount);
        descriptionView = itemView.findViewById(R.id.expenseDescription);
        typeView = itemView.findViewById(R.id.expenseDate);
        dateView = itemView.findViewById(R.id.expenseType);
        btnEditData = itemView.findViewById(R.id.editDataButton);
    }
}
