package com.example.expensetrackerjavagroovy.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<RecyclerView> mRecyclerView;

    public GalleryViewModel() {
        mRecyclerView = new MutableLiveData<>();
//        mRecyclerView.setValue(new RecyclerView(this));
    }

    public LiveData<RecyclerView> getRecyclerView() {
        return mRecyclerView;
    }
}