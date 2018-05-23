package com.example.chiunguo.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestService1 extends Service {

        private final String TAG = "TestService1";
        //必须要实现的方法
        @Override
        public IBinder onBind(Intent intent) {
            Toast.makeText(this, "onBind方法被调用g", Toast.LENGTH_SHORT).show();

           Log.i(TAG, "onBind方法被调用!");
            return null;
        }

        //Service被创建时调用
        @Override
        public void onCreate() {
            Toast.makeText(this, "onCreate方法被调用 ", Toast.LENGTH_SHORT).show();

            Log.i(TAG, "onCreate方法被调用!");
            super.onCreate();
        }

        //Service被启动时调用
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Toast.makeText(this, "onStartCommand方法被调用", Toast.LENGTH_SHORT).show();

           Log.i(TAG, "onStartCommand方法被调用!");
            return super.onStartCommand(intent, flags, startId);
        }

        //Service被关闭之前回调
        @Override
        public void onDestroy() {
            Toast.makeText(this, "onDestory方法被调用", Toast.LENGTH_SHORT).show();

           Log.i(TAG, "onDestory方法被调用!");
            super.onDestroy();
        }
    }

