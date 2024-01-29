package com.example.expensetrackerjavagroovy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetrackerjavagroovy.adapter.ExpenseListAdapter;
import com.example.expensetrackerjavagroovy.controller.DBController;
import com.example.expensetrackerjavagroovy.model.Record;
import com.example.expensetrackerjavagroovy.ui.gallery.GalleryFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackerjavagroovy.databinding.ActivityMainBinding;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public String appId = "application-0-ojwlu";

    public static String email = "okuwagapramudji@gmail.com";
    public static String password = "TEST123";

    private static final int RC_GET_AUTH_CODE = 9003;
    private EditText expenseAmount;
    private EditText expenseDate;
    private EditText expenseDescription;
    private EditText expenseType;
    private TextView totalExpense;
    private Button addExpenseButton;

    private RecyclerView recyclerView;

    private List<Record> recordList = new ArrayList<>();

    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;

    private App app;
    private DBController dbController;

    private static final String FAIL = "FAIL";
    private static final String ERROR = "ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        expenseAmount = findViewById(R.id.edit_text_expense_amount);
        expenseDate = findViewById(R.id.edit_text_expense_date);
        expenseType = findViewById(R.id.edit_text_expense_type);
        expenseDescription = findViewById(R.id.edit_text_expense_description);
        addExpenseButton = findViewById(R.id.add_expense_button);
        totalExpense = findViewById(R.id.text_total_expenses);

        try {
            dbController = new DBController(appId, this, email, password);
            app = dbController.initRealm(new LoginCallBack() {
                @Override
                public void onLoginSuccess() {
                    refreshTotalAmount();
                    refreshAmountList();
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
                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_LONG).show();
            }
        });

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        recyclerView = findViewById(R.id.expensesList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ExpenseListAdapter(getApplicationContext(), recordList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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

    private void refreshAmountList(){
        dbController.showAllData(new AllDataCallback() {
            @Override
            public void onSuccess() {
                for (Record record: dbController.getDataList()
                     ) {
                    Log.v("Amount List: ", String.valueOf(record.getAmount()));
                    recordList.add(new Record(record.getAmount(), record.getDescription(), record.getDate(), record.getType()));
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}