package com.example.resort;

// LOGIN ACTIVITY JAVA FILE


import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.resort.R;

import com.example.resort.Util.CurrentUser;
import com.example.resort.Util.Reader;
import com.example.resort.Util.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class LoginAcitvity extends AppCompatActivity {
    EditText namer;
    DbHandler db = new DbHandler(LoginAcitvity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitvity);
    }

    public String loadJSONFromAsset(String name) {
        String json = null;
        try {
            java.io.InputStream is = getApplicationContext().getAssets().open("users.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //LOGIN METHOD
    public void doLogin(View view){
//        User user = null;
//        user = Reader.getUserList(getApplicationContext());
        namer = findViewById(R.id.username);

        String name = namer.getText().toString();
        ArrayList<HashMap<String, String>> user = db.GetUserByUserId(name);




        if(user.size()!=0)
        {
            CurrentUser cuser = new CurrentUser(name);
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"You are Authorized! ",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getApplicationContext(),"You are Not Authorized! ",Toast.LENGTH_LONG).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doRegister(View view) throws JSONException, IOException {
//        User user = null;
//        user = Reader.getUserList(getApplicationContext());
        namer = findViewById(R.id.username);
        String name = namer.getText().toString();
        ArrayList<HashMap<String, String>> user = db.GetUserByUserId(name);

        if(user.size()!=0)
        {

            Toast.makeText(getApplicationContext(),"Already a Member",Toast.LENGTH_LONG).show();
        }
        else{

            db.insertUserDetails(name);
            CurrentUser cuser = new CurrentUser(name);
            Toast.makeText(getApplicationContext(),"New Member Registered Succesfully",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }

}
