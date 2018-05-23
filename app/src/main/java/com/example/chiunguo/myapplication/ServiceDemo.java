package com.example.chiunguo.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.chiunguo.myapplication.service.LocalService;
import com.example.chiunguo.myapplication.service.LocalService.LocalBinder;
import com.example.chiunguo.myapplication.service.MyBroadcastReceiver;
import com.example.chiunguo.myapplication.service.TestService1;


public class ServiceDemo extends AppCompatActivity {

    private Intent mServiceIntent;
    private static final int RSS_JOB_ID = 1000;
    private Button start;
    private Button stop;

    private final String TAGBind = "LocalService";

    private BroadcastReceiver br;

    LocalService mService;
    boolean mBound = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_demo);

        start = (Button) findViewById(R.id.btnstart);
        stop = (Button) findViewById(R.id.btnstop);
        //创建启动Service的Intent,以及Intent属性
        final Intent intent = new Intent(this, TestService1.class);
       // intent.setAction(".service.TEST_SERVICE1");
        //为两个按钮设置点击事件,分别是启动与停止service
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent);
            }
        });

        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);

            }
        });
    // register activity in  oncreate()
        br=new MyBroadcastReceiver() ;
       // IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.service.MY_NOTIFICATION");
        this.registerReceiver(br, filter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAGBind,"onStart");
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();

        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAGBind,"onStop");
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        unbindService(mConnection);
        mBound = false;

        unregisterReceiver(br);
        Log.i(TAGBind,"unregisterReceiver");
        Toast.makeText(this, "unregisterReceiver", Toast.LENGTH_SHORT).show();
    }

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) */
    public void GetServiceValue(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    public void sendToBroadcast(View v){
//        Intent intent = new Intent();
//        intent.setAction("com.example.service.MY_NOTIFICATION");
//        sendBroadcast(intent);
        Log.i(TAGBind,"click and send");
        sendBroadcast(new Intent("com.example.service.MY_NOTIFICATION"));
    }



}
