package com.example.expensetrackerjavagroovy.controller;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.expensetrackerjavagroovy.AllDataCallback;
import com.example.expensetrackerjavagroovy.LoginCallBack;
import com.example.expensetrackerjavagroovy.TotalAmountCallback;
import com.example.expensetrackerjavagroovy.model.Record;

import org.bson.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class DBController{

    public String appId = "application-0-ojwlu";

    public static String email = "okuwagapramudji@gmail.com";
    public static String password = "TEST123";

    private Context context;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    private User user;
    private MongoClient mongoClient;

    private double totalAmount = 0;

    private List<Record> dataList = new ArrayList<>();

    private static final String FIELD_AMOUNT = "amount";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_DESCRIPTION = "description";

    private static final String FAIL = "FAIL";
    private static final String ERROR = "ERROR";


    private String[] months = new String[]{
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
            "December"
    };


    public DBController(Context context){
//        this.appId = appId;
        this.context = context;
//        this.email = email;
//        this.password = password;
    }

    public DBController(){
    }

//    public void loadConfig(){
//        try {
//            initRealm(new LoginCallBack() {
//                @Override
//                public void onLoginSuccess() {
//                    refreshTotalAmount();
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
//    }

    public App initRealm(LoginCallBack loginCallBack){
        Realm.init(context);

        App app = new App(new AppConfiguration.Builder(appId).build());

        Credentials credentials = Credentials.emailPassword(email, password);

        app.loginAsync(credentials, new App.Callback<User>(){

            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    Log.v("User", "User Logged In Successfully");
                    user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("MonthlyExpenses");
                    mongoCollection = mongoDatabase.getCollection("Expenses");
                    loginCallBack.onLoginSuccess();

                }else{
                    Log.v("User", "User failed to login");
                    loginCallBack.onLoginFail();
                }
            }
        });

        return app;
    }

    public void initTotalAmount(TotalAmountCallback callback){

        RealmResultTask<MongoCursor<Document>> cursor = mongoCollection.find().iterator();

        cursor.getAsync(task -> {
            if(task.isSuccess()){
                totalAmount = 0;
                MongoCursor<Document> results = task.get();
                while(results.hasNext()){
                    Document currentDocument = results.next();
                    Double amount = currentDocument.getDouble(FIELD_AMOUNT);
                    if(amount != null){
                        totalAmount += amount;
                    }
                    Log.v("Result", "Adding");
                    Log.v("Current Amount : ", String.valueOf(totalAmount));
                }
                if(!results.hasNext()){
                    Log.v("Result", "Couldn't find");
                }
                callback.onSuccess();
            }else{
                Log.v("Task Error", task.getError().toString());
                callback.onFailure("FAIL");
            }
        });

    }

    public double getTotalAmount(){
        return totalAmount;
    }
    public List<Record> getDataList(){
        return dataList;
    }

    public void insertData(Record record){

        String day = record.getDate().substring(0,2);
        String month = record.getDate().substring(2,4);
        String year = record.getDate().substring(4);
        Document data = new Document(
//                "userid", user.getId())
                FIELD_AMOUNT, record.getAmount())
                .append(FIELD_DESCRIPTION, record.getDescription())
                .append(FIELD_TYPE, record.getType())
                .append(FIELD_DATE, day+"-"+month+"-"+year);
        mongoCollection.insertOne(data)
            .getAsync(result -> {
            if(result.isSuccess()){
                Log.v("Data", "Data Inserted Successfully");
            }else{
                Log.v("Data", "Error: " + result.getError().toString());
            }
        });
    }

//    public void editData(Record record){
//        Document queryFilter = new Document("userid", user.getId());
//        mongoCollection.findOneAndUpdate(queryFilter)
//    }

    public void showAllData(AllDataCallback callback){
        RealmResultTask<MongoCursor<Document>> cursor = mongoCollection.find().iterator();
        cursor.getAsync(task -> {
            if(task.isSuccess()){
                MongoCursor<Document> results = task.get();
                while(results.hasNext()){
                    Document currentDocument = results.next();
                    String description = currentDocument.getString(FIELD_DESCRIPTION);
                    String date = currentDocument.getString(FIELD_DATE);
                    String type = currentDocument.getString(FIELD_TYPE);
                    Double amount = currentDocument.getDouble(FIELD_AMOUNT);
                    Record record = new Record(amount, description, date, type);
                    dataList.add(record);
                }
                if(!results.hasNext()){
                    Log.v("Result", "Couldn't find");
                }
                Log.v("Result", dataList.toString());
                callback.onSuccess();
            }else{
                Log.v("Task Error", task.getError().toString());
                callback.onFailure("FAIL");
            }
        });
    }

    public String implementInitTotalAmount(){
        final String[] result = {""};
        initTotalAmount(new TotalAmountCallback() {
            @Override
            public void onSuccess() {
                Log.v("Total amount: ",String.valueOf(getTotalAmount()));
                result[0] = String.valueOf(getTotalAmount());
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.v(ERROR,ERROR);
            }
        });

        return result[0];
    }

//    private void refreshAmountList(){
//        showAllData(new AllDataCallback() {
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
