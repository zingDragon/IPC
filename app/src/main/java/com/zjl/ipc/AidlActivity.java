package com.zjl.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AidlActivity extends AppCompatActivity {
    private static final String TAG = "AidlActivity";
    private IMyAidlInterface mInterface = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mInterface = IMyAidlInterface.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: isProxy=" + (mInterface.asBinder() != mInterface));

            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                mInterface.registerOnEventListener(mOnEventListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            mInterface = null;
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied: ");
            if (mInterface != null) {
                boolean result = mInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
                Log.d(TAG, "binderDied: unlinkToDeath result=" + result);
                mInterface = null;
            }

        }
    };

    private IOnEventListenerInterface mOnEventListener = new IOnEventListenerInterface.Stub() {
        @Override
        public void onEvent() throws RemoteException {
            Log.d(TAG, "onEvent: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);

        bindService(new Intent(this, AidlService.class), mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInterface != null && mInterface.asBinder().isBinderAlive()) {
            try {
                mInterface.unRegisterOnEventListener(mOnEventListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
        mInterface = null;
    }

    public void onButtonClick(View view) {
        if (mInterface != null) {
            try {
                int r = mInterface.sum(8, 9);
                Toast.makeText(this, "sum=" + r, Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
