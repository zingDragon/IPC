package com.zjl.ipc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
        case R.id.local_binder:
            startActivity(new Intent(this, BindingActivity.class));
            break;
        case R.id.remote_messenger:
            startActivity(new Intent(this, MessengerActivity.class));
            break;
        case R.id.remote_aidl:
            startActivity(new Intent(this, AidlActivity.class));
            break;
        }
    }
}
