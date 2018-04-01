package io.textory.rebeccapurple.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import rebeccapurple.android.messenger.Server;

public class Messenger extends Service {
    private Server __server = null;

    @Override
    public void onCreate(){
        super.onCreate();
        rebeccapurple.log.e("create");
        __server = new Server();

        rebeccapurple.android.messenger.operator.init(__server);

        __server.listen();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        rebeccapurple.log.e("start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        rebeccapurple.log.e("bind");
        return __server != null ? __server.binder() : null;
    }

    @Override
    public void onDestroy(){
        __server.close();
        rebeccapurple.log.e("destroy");
        super.onDestroy();
    }
}
