package com.example.chiunguo.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SnacBardemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snacbar);

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                showSnacBar();
            }
        });

    }
    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Context context = getApplicationContext();
            CharSequence text = "you click snacbar!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            // Code to undo the user's last action
        }
    }

     public  void showSnacBar(){
         Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                 R.string.app_name, Snackbar.LENGTH_SHORT);
         mySnackbar.setAction("action do do do", new MyUndoListener());
         mySnackbar.show();
//        Snackbar.make(findViewById(R.id.myCoordinatorLayout), R.string.app_name,
//                Snackbar.LENGTH_SHORT)
//                .show();
    }


}
