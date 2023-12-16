package com.example.expensetrackerjavagroovy.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetrackerjavagroovy.model.Record;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        DecimalFormat df = new DecimalFormat("#.00");
        List<Record> records = new ArrayList<>();
        double totalExpenses = 1500.333333;
        mText = new MutableLiveData<>();
        mText.setValue("Total Expenses: " + df.format(totalExpenses));
    }

    public LiveData<String> getText() {
        return mText;
    }
}