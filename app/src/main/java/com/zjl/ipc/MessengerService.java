package com.zjl.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    public static final int MSG_SEND_SERVICE = 1;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SEND_SERVICE:
                Log.d(TAG, "handleMessage: receive message from client: "
                        + msg.getData().getString("msg"));
                Message replyMsg = Message.obtain();
                replyMsg.what = MessengerActivity.MESSAGE_SEND_TO_ME;
                Bundle b = new Bundle();
                b.putString("msg",
                        "This is MessengerService, I have received your message, thank you!");
                replyMsg.setData(b);

                Messenger reply = msg.replyTo;
                try {
                    reply.send(replyMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

            }
            super.handleMessage(msg);
        }
    }

    Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
