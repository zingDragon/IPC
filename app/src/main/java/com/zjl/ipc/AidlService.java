package com.zjl.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class AidlService extends Service {
    private static final String TAG = "AidlService";

    private AtomicBoolean mIsServicesDestroyed = new AtomicBoolean(false);

    private RemoteCallbackList<IOnEventListenerInterface> mListeners = new RemoteCallbackList<>();

    private IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int sum(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public void registerOnEventListener(IOnEventListenerInterface listener)
                throws RemoteException {
            Log.d(TAG, "registerOnEventListener: ");
            mListeners.register(listener);
        }

        @Override
        public void unRegisterOnEventListener(IOnEventListenerInterface listener)
                throws RemoteException {
            Log.d(TAG, "unRegisterOnEventListener: ");
            mListeners.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServicesDestroyed.set(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void callOnEventListeners() {
        int n = mListeners.beginBroadcast();
        while (--n >= 0) {
            try {
                mListeners.getBroadcastItem(n).onEvent();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        mListeners.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServicesDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callOnEventListeners();
            }
        }
    }
}
