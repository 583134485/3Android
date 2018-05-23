package com.example.chiunguo.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class SharedPreferencesDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences_demo);
    }

    public void showdata(View v){
        SharedPreferences sharedPref = getSharedPreferences("sp_demo",Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);
       // int highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), defaultValue);
        int data=sharedPref.getInt("666",555);
        String a=Integer.toString(data);
        Toast.makeText(this,a , Toast.LENGTH_SHORT).show();

    }

    public void setdata(View v){
        SharedPreferences sharedPref = getSharedPreferences("sp_demo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("666", 666);
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();

        editor.commit();
    }
}
