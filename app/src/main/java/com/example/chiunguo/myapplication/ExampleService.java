package com.example.chiunguo.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ExampleService extends Service {
    public ExampleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
