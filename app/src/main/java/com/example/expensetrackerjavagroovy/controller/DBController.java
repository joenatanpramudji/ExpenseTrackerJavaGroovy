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

import org.bson.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class DBController{
    private String appId;
    private Context context;
    private String email;
    private String password;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    private User user;
    private MongoClient mongoClient;
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


    public DBController(String appId, Context context, String email, String password){
        this.appId = appId;
        this.context = context;
        this.email = email;
        this.password = password;
    }

    public App initRealm(){
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

                }else{
                    Log.v("User", "User failed to login");
                }
            }
        });

        return app;
    }

    public void insertData(String amount, String description, String date, String type){

        String day = date.substring(0,2);
        String month = date.substring(2,4);
        String year = date.substring(4);

        mongoCollection.insertOne(new Document(
                "userid", user.getId())
                .append("amount", amount)
                .append("date", day+"-"+month+"-"+year)
            )
            .getAsync(result -> {
            if(result.isSuccess()){
                Log.v("Data", "Data Inserted Successfully");
            }else{
                Log.v("Data", "Error: " + result.getError().toString());
            }
        });
    }
}
