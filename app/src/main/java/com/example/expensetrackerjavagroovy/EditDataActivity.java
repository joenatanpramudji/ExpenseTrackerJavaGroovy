package com.example.expensetrackerjavagroovy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.expensetrackerjavagroovy.controller.DBController;
import com.example.expensetrackerjavagroovy.model.Record;

import io.realm.mongodb.App;

public class EditDataActivity extends AppCompatActivity {

    private String id = "";
    private Double amount = 0.0;

    private EditText editAmount, editDescription, editType, editDate;

    private Button editButton;

    private App app;
    private DBController dbController;

    private static final String FAIL = "FAIL";
    private static final String ERROR = "ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.joe_color));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Bundle bundle = getIntent().getExtras();
        try {
            id = bundle.getString("_id");
            amount = Double.parseDouble(bundle.getString("amount"));
        }catch (Exception e){
            Log.v("NULL POINTER: ", e.toString());
        }

        Log.i("Id: ", id);
        Log.i("Amount: ", amount.toString());

        editAmount = findViewById(R.id.edit_data_expense_amount);
        editDescription = findViewById(R.id.edit_data_expense_description);
        editType = findViewById(R.id.edit_data_expense_type);
        editDate = findViewById(R.id.edit_data_expense_date);
        editButton = findViewById(R.id.edit_expense_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record record = new Record(Double.parseDouble(editAmount.getText().toString()),
                        editDescription.getText().toString(),
                        editDate.getText().toString(),
                        editType.getText().toString());
                dbController.editData(id, record);
            }
        });

        try {
            dbController = new DBController(this);
            app = dbController.initRealm(new LoginCallBack() {
                @Override
                public void onLoginSuccess() {

                }

                @Override
                public void onLoginFail() {
                    Log.v(FAIL,FAIL);
                }
            });
        }catch (Exception e){
            Log.i(ERROR, e.toString());
        }

    }
}