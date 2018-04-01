package io.textory.rebeccapurple.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import functional.android.messenger.operator;
import rebeccapurple.android.messenger.Server;

public class Messenger extends Service {
    private Server __server = null;

    @Override
    public void onCreate(){
        super.onCreate();
        functional.log.e("create");
        __server = new Server();

        operator.init(__server);

        __server.listen();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        functional.log.e("start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        functional.log.e("bind");
        return __server != null ? __server.binder() : null;
    }

    @Override
    public void onDestroy(){
        __server.close();
        functional.log.e("destroy");
        super.onDestroy();
    }
}
