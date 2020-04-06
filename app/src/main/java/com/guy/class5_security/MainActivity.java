package com.guy.class5_security;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar home_PRG_tests;
    private TextView home_LBL_info;
    private Button home_BTN_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_PRG_tests = findViewById(R.id.home_PRG_tests);
        home_LBL_info = findViewById(R.id.home_LBL_info);
        home_BTN_start = findViewById(R.id.home_BTN_start);
        home_PRG_tests.setVisibility(View.INVISIBLE);


        home_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                //uploudToFireBase();
            }
        });


        SharedPreferences.Editor editor = getSharedPreferences("Class5", MODE_PRIVATE).edit();
        long now = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(now);
        editor.putString("Last_Open", date);
        editor.putInt("idName", 12);
        editor.apply();
    }

    private void start() {
        home_PRG_tests.setVisibility(View.VISIBLE);
//        final String API_GET_PLAYLISTS = "https://pastebin.com/raw/nhrgQRZZ";
//        String api1 = API_GET_PLAYLISTS;
//        MyApis.callAPI(this, api1, new MyApis.CallBack_Response() {
//            @Override
//            public void CallBack_ResponseReturned(final boolean isSuccess, final String response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        afterApiCall(isSuccess, response);
//                    }
//                });
//            }
//        });

        final String API_WIKIPEDIA = "https://pastebin.com/raw/RNQj7Add";
        JsonTask jsonTask = new JsonTask(this, API_WIKIPEDIA, new JsonTask.CallBack_Response() {
            @Override
            public void response(final boolean success, final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        afterApiCall(success, response);
                    }
                });
            }
        });
        jsonTask.execute();
    }

    private void afterApiCall(boolean isSuccess, String response) {
        home_PRG_tests.setVisibility(View.INVISIBLE);

        if (isSuccess) {
            response = response.replaceAll("\\\\\"", "\"");
            generateDataFromJson(response);
            home_LBL_info.setText("Success\n\n" + response);

            //home_PRG_tests.setVisibility(View.INVISIBLE);
        } else {
            home_LBL_info.setText("unSuccess\n\n Error=" + response);
            //home_PRG_tests.setVisibility(View.INVISIBLE);
        }
    }

    private void generateDataFromJson(String response) {
        Log.d("pttt", "response= " + response);
    }



    private void uploudToFireBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }
}
