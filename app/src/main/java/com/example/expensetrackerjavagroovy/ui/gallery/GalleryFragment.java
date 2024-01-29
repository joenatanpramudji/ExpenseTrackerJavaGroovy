package com.example.expensetrackerjavagroovy.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerjavagroovy.AllDataCallback;
import com.example.expensetrackerjavagroovy.LoginCallBack;
import com.example.expensetrackerjavagroovy.R;
import com.example.expensetrackerjavagroovy.adapter.ExpenseListAdapter;
import com.example.expensetrackerjavagroovy.controller.DBController;
import com.example.expensetrackerjavagroovy.databinding.FragmentGalleryBinding;
import com.example.expensetrackerjavagroovy.model.Record;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    List<Record> recordList = new ArrayList<>();

//    private App app;
//    private DBController dbController;
//
//    private static final String FAIL = "FAIL";
//    private static final String ERROR = "ERROR";
//
//    public String appId = "application-0-ojwlu";
//
//    public static String email = "okuwagapramudji@gmail.com";
//    public static String password = "TEST123";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        try {
//            dbController = new DBController(appId, this, email, password);
//            app = dbController.initRealm(new LoginCallBack() {
//                @Override
//                public void onLoginSuccess() {
//                    refreshAmountList();
//                }
//
//                @Override
//                public void onLoginFail() {
//                    Log.v(FAIL,FAIL);
//                }
//            });
//        }catch (Exception e){
//            Log.i(ERROR, e.toString());
//        }

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

//        final RecyclerView recyclerView = binding.expensesList;
////
//        List<Record> recordList = new ArrayList<>();
////
////        recyclerView = findViewById(R.id.expensesList);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new ExpenseListAdapter(getContext(), recordList));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


//    private void refreshAmountList(){
//        dbController.showAllData(new AllDataCallback() {
//            @Override
//            public void onSuccess() {
//                for (Record record: dbController.getDataList()
//                ) {
//                    Log.v("Amount List: ", String.valueOf(record.getAmount()));
//                    recordList.add(new Record(record.getAmount(), record.getDescription(), record.getDate(), record.getType()));
//                }
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//
//            }
//        });
//    }
}