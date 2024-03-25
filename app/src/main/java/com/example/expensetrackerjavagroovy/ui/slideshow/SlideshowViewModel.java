package com.example.expensetrackerjavagroovy.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Joe's Expense Tracker v00.00.01");
    }

    public LiveData<String> getText() {
        return mText;
    }
}