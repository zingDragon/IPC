package com.zjl.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class LocalService extends Service {
    private IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void serviceFunc() {
        Toast.makeText(getApplicationContext(), "serviceFunc being called", Toast.LENGTH_LONG)
                .show();
    }
}
