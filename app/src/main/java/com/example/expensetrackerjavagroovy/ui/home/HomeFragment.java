package com.example.expensetrackerjavagroovy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetrackerjavagroovy.LoginCallBack;
import com.example.expensetrackerjavagroovy.TotalAmountCallback;
import com.example.expensetrackerjavagroovy.controller.DBController;
import com.example.expensetrackerjavagroovy.databinding.FragmentHomeBinding;
import com.example.expensetrackerjavagroovy.model.Record;

import io.realm.mongodb.App;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private App app;
    private DBController dbController;

    private static final String FAIL = "FAIL";
    private static final String ERROR = "ERROR";

     TextView textView;
     EditText expenseAmount;
     EditText expenseDate;
     EditText expenseDescription;
     EditText expenseType;
     TextView totalExpense;
     Button addExpenseButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textTotalExpenses;
        expenseAmount = binding.editTextExpenseAmount;
        expenseDate = binding.editTextExpenseDate;
        expenseDescription = binding.editTextExpenseDescription;
        expenseType = binding.editTextExpenseType;
        totalExpense = binding.textTotalExpenses;
        addExpenseButton = binding.addExpenseButton;

        try {
            dbController = new DBController(getContext());
            app = dbController.initRealm(new LoginCallBack() {
                @Override
                public void onLoginSuccess() {
                    refreshTotalAmount();
//                    refreshAmountList();
                }

                @Override
                public void onLoginFail() {
                    Log.v(FAIL,FAIL);
                }
            });
        }catch (Exception e){
            Log.i(ERROR, e.toString());
        }

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record record = new Record(Double.parseDouble(expenseAmount.getText().toString()),
                        expenseDescription.getText().toString(),
                        expenseDate.getText().toString(),
                        expenseType.getText().toString());
                dbController.insertData(record);
                expenseAmount.setText("");
                expenseDate.setText("");
                expenseDescription.setText("");
                expenseType.setText("");
                refreshTotalAmount();
                Toast.makeText(getContext(), "Inserted", Toast.LENGTH_LONG).show();
            }
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void refreshTotalAmount(){
        dbController.initTotalAmount(new TotalAmountCallback() {
            @Override
            public void onSuccess() {
                Log.v("Total amount: ",String.valueOf(dbController.getTotalAmount()));
                totalExpense.setText(String.valueOf(dbController.getTotalAmount()));
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.v(ERROR,ERROR);
            }
        });
    }
}