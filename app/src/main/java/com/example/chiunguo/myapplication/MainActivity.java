package com.example.chiunguo.myapplication;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText ;
import android.widget.ToggleButton;

import com.example.chiunguo.myapplication.demo.Login;
import com.example.chiunguo.myapplication.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

      public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
      private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        login=findViewById(R.id.gotologin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login2();
            }
        });

     //button toggle
//        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                  Log.v("info","enable");
//                } else {
//                    // The toggle is disabled
//                    Log.v("info","disables");
//                }
//            }
//        });


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setTooltipText("Send an email");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

  /** Called when the user taps the Send button */
    public void sendMessage(android.view.View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        // Do something in response to button
    }

    public void gotoGridView(View view){
        Intent intent = new Intent(this, HelloGridView.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
       // String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void gotoSnacbar(View view){
        Intent intent = new Intent(this, SnacBardemo.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void gotoMenu(View view){
        Intent intent = new Intent(this, menudemo.class);
        //Intent intent = new Intent(this, Menutest.class);

        //EditText editText = (EditText) findViewById(R.id.editText);
        // String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


    public void showdialogone(View view){
        FireMissilesDialogFragment fireMissilesDialogFragment=new FireMissilesDialogFragment();
        fireMissilesDialogFragment.showNow(getSupportFragmentManager(),"ds");
    }

    public void showAlertDiag(View view){
        AlertDiagFragment alertDiagFragment=new AlertDiagFragment();
        alertDiagFragment.show(getFragmentManager(),"alert");
    }

    public void showAlertDiag2(View view ){
        AlertDiagFragment2 alertDiagFragment2=new AlertDiagFragment2();
        alertDiagFragment2.show(getFragmentManager(),"alert");
    }
    public void gotoOpenGlDemo(View view){
        Intent intent = new Intent(this, OpenGLDemo.class);
        startActivity(intent);
    }

    public void gotoServiceDemo(View view){
        Intent intent = new Intent(this, ServiceDemo.class);
        startActivity(intent);
    }

    public void gotodata(View view){
        Intent intent = new Intent(this, SharedPreferencesDemo.class);
        startActivity(intent);
    }


    public void login(){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    public void login2(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


}
