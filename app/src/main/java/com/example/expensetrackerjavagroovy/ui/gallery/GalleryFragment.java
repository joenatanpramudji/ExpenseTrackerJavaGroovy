package com.example.expensetrackerjavagroovy.ui.gallery;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerjavagroovy.AllDataCallback;
import com.example.expensetrackerjavagroovy.HistoryFilterActivity;
import com.example.expensetrackerjavagroovy.LoginCallBack;
import com.example.expensetrackerjavagroovy.R;
import com.example.expensetrackerjavagroovy.adapter.ExpenseListAdapter;
import com.example.expensetrackerjavagroovy.controller.DBController;
import com.example.expensetrackerjavagroovy.databinding.FragmentGalleryBinding;
import com.example.expensetrackerjavagroovy.model.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.mongodb.App;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    List<Record> recordList = new ArrayList<>();

    private App app;
    private DBController dbController;

    private static final String FAIL = "FAIL";
    private static final String ERROR = "ERROR";
    RecyclerView recyclerView;

    TextView startDateFilter;

    TextView endDateFilter;

    DatePickerDialog datePickerDialog;

    List<String> months;

    {
        months = Collections.unmodifiableList(Arrays.asList(
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MenuHost menuHost = requireActivity();

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

                menu.clear();
                menu.add(0, Menu.NONE, Menu.NONE, R.string.action_filter);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = new Intent(getContext(),HistoryFilterActivity.class);
//                startActivityForResult(intent, 1);
                historyFilterAcivityLauncher.launch(intent);
                return false;
            }
        });

        startDateFilter = binding.startDate;
        endDateFilter = binding.endDate;

        startDateFilter.setOnClickListener(new View.OnClickListener() {
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
                                startDateFilter.setText(String.format("%02d", (dayOfMonth)) + "-"
                                        + String.format("%02d", (monthOfYear + 1)) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        startDateFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshAmountList(recyclerView);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshAmountList(recyclerView);
            }
        });

        endDateFilter.setOnClickListener(new View.OnClickListener() {
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
                                endDateFilter.setText(String.format("%02d", (dayOfMonth)) + "-"
                                        + String.format("%02d", (monthOfYear + 1)) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        endDateFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshAmountList(recyclerView);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshAmountList(recyclerView);
            }
        });

        return root;
    }

    ActivityResultLauncher<Intent> historyFilterAcivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                    }
                }
            }
    );

    @Override
    public void onResume() {
        super.onResume();
        recyclerView = binding.expensesList;

        try {
            dbController = new DBController(getContext());
            app = dbController.initRealm(new LoginCallBack() {
                @Override
                public void onLoginSuccess() {
                    refreshAmountList(recyclerView);
                }

                @Override
                public void onLoginFail() {
                    Log.v(FAIL,FAIL);
                }
            });
        }catch (Exception e){
            Log.i(ERROR, e.toString());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void refreshAmountList(RecyclerView recyclerView){
        dbController.showAllData(new AllDataCallback() {
            @Override
            public void onSuccess() {
                for (Record record: dbController.getDataList()
                ) {
                    Log.v("Amount List: ", String.valueOf(record.getAmount()));
                    if(!(startDateFilter.getHint().equals("Start Date...") && endDateFilter.getHint().equals("End Date..."))){
                        String[] resultDates = record.getDate().split("-");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date resultDate;
                        Date startFilterDate;
                        Date endFilterDate;
                        try {
                            resultDate = dateFormat.parse(record.getDate());
                            startFilterDate = dateFormat.parse(startDateFilter.getText().toString());
                            endFilterDate = dateFormat.parse(endDateFilter.getText().toString());

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        assert resultDate != null;
                        if(resultDate.compareTo(startFilterDate) >= 0 && resultDate.compareTo(endFilterDate) <= 0 ){
                            recordList.add(new Record(record.getId(), record.getAmount(), record.getDescription(), record.getDate(), record.getType()));
                        }
                    }else{
                        recordList.add(new Record(record.getId(), record.getAmount(), record.getDescription(), record.getDate(), record.getType()));
                    }

                }
                recyclerView.setAdapter(new ExpenseListAdapter(getContext(), recordList));
            }

            @Override
            public void onFailure(String errorMessage) {
            Log.v("FAIL", "FAIL");
            }
        });
    }
}