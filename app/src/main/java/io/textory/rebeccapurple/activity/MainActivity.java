package io.textory.rebeccapurple.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        rebeccapurple.android.messenger.operator.init(__client);

        __client.connect(client -> {
            client.send(rebeccapurple.android.messenger.request.ping(rebeccapurple.android.message::log));
            client.send(rebeccapurple.android.messenger.request.ping(1L, rebeccapurple.android.message::log));
        });
        // __client.connect(client -> client.send(rebeccapurple.android.messenger.request.ping(rebeccapurple.android.message::log)));
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
