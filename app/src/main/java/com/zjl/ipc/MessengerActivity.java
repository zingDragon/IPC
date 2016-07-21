package com.zjl.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity";
    public static final int MESSAGE_SEND_TO_ME = 1;

    private boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBound = true;
            mOutMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private Messenger mInMessenger = new Messenger(new IncomingHandler());
    private Messenger mOutMessenger;

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: receive message from service: "
                    + msg.getData().getString("msg"));
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            bindService(new Intent(this, MessengerService.class), mConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void onButtonClick(View view) {
        if (mBound && mOutMessenger != null) {
            Message msg = Message.obtain();
            msg.what = MessengerService.MSG_SEND_SERVICE;
            Bundle bundle = new Bundle();
            bundle.putString("msg", "I am messengerActivity client, how are you?");
            msg.setData(bundle);
            msg.replyTo = mInMessenger;

            try {
                mOutMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
