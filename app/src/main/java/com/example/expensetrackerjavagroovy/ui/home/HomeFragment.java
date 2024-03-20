package com.example.expensetrackerjavagroovy.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetrackerjavagroovy.LoginCallBack;
import com.example.expensetrackerjavagroovy.R;
import com.example.expensetrackerjavagroovy.TotalAmountCallback;
import com.example.expensetrackerjavagroovy.controller.DBController;
import com.example.expensetrackerjavagroovy.databinding.FragmentHomeBinding;
import com.example.expensetrackerjavagroovy.model.Record;

import java.util.Calendar;

import io.realm.mongodb.App;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentHomeBinding binding;

    private App app;
    private DBController dbController;

    private static final String FAIL = "FAIL";
    private static final String ERROR = "ERROR";

     TextView textView;
     EditText expenseAmount;
     TextView expenseDate;
     EditText expenseDescription;
//     EditText expenseType;

    Spinner expenseType;
     TextView totalExpense;
     Button addExpenseButton;

     String expenseTypeText = "";

    DatePickerDialog datePickerDialog;

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
//        expenseType = binding.editTextExpenseType;
        expenseType = binding.expenseTypeSpinner;
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.expense_type_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseType.setAdapter(adapter);
        expenseType.setOnItemSelectedListener(this);

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Record record = new Record(Double.parseDouble(expenseAmount.getText().toString()),
                            expenseDescription.getText().toString(),
                            expenseDate.getText().toString(),
                            expenseTypeText);
                    dbController.insertData(record);
                    expenseAmount.setText("");
                    expenseDate.setText("Select Date...");
                    expenseDescription.setText("");
//                expenseType.setText("");
                    refreshTotalAmount();
                    Toast.makeText(getContext(), "Inserted", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    String warning = "Please fill everything";
                    Toast.makeText(getContext(),warning, Toast.LENGTH_SHORT).show();
                }
            }
        });


        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                expenseDate.setText(String.format("%02d", (dayOfMonth)) + "-"
                                        + String.format("%02d", (monthOfYear + 1)) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        expenseTypeText = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}