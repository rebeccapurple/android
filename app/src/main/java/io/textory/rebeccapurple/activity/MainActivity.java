package io.textory.rebeccapurple.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import functional.android.messenger.operator;
import functional.android.messenger.request;

import io.textory.rebeccapurple.R;
import io.textory.rebeccapurple.service.Messenger;

import rebeccapurple.android.messenger.Client;

public class MainActivity extends AppCompatActivity {

    private Client<Messenger> __client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        __client = new Client<>(this, Messenger.class);

        operator.init(__client);

        __client.connect(client -> {
            client.send(request.ping(functional.android.message::log));
            client.send(request.ping(1L, functional.android.message::log));
        });
    }

    @Override
    protected void onDestroy(){
        if(__client != null){
            __client.disconnect();
            __client = null;
        }
        super.onDestroy();
    }
}
