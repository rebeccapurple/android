package io.textory.rebeccapurple.application;

import android.support.multidex.MultiDexApplication;

import com.google.gson.GsonBuilder;

// import ;

/**
 *
 * @author      sean <hyunsik-park@textory.com>
 * @since       0.0.1
 * @date        2018. 3. 23.
 * @stereotype  class
 */

public class Application extends MultiDexApplication {

    private static void InitializeJson(GsonBuilder builder){
        builder.registerTypeAdapter(android.os.Message.class, new rebeccapurple.android.message.serializer());
        builder.registerTypeAdapter(android.os.Message.class, new rebeccapurple.android.message.deserializer());
    }


    @Override
    public void onCreate(){
        super.onCreate();

        rebeccapurple.log.date(false);
        rebeccapurple.log.stacktrace(false);
        rebeccapurple.log.add(rebeccapurple.android.log.get());

        rebeccapurple.android.functional.init(this.getApplicationContext());
        rebeccapurple.json.init(Application::InitializeJson);

        rebeccapurple.scheduler.init(rebeccapurple.android.scheduler.get());
        rebeccapurple.scheduler.on();

        io.textory.rebeccapurple.application.debug.run();
    }

//        rebeccapurple.android.Scheduler.On();
//        Scheduler scheduler = rebeccapurple.android.Scheduler.Get();
//        goldenrod.log.e("hello");
//        scheduler.dispatch(System.currentTimeMillis() + 5000L, ()-> goldenrod.log.e("hello"));
////        rebeccapurple.android.Scheduler.Dispatch()
//        rebeccapurple.android.http.volley.Client.init(this);                                /** initialize android volley client */
//        rebeccapurple.http.Client.init(rebeccapurple.android.http.volley.Client.get());     /** initialize default http client */
//        rebeccapurple.json.init();

    @Override
    public void onTerminate(){
//        rebeccapurple.android.Scheduler.Off();
        /** scheduler off */
        rebeccapurple.scheduler.off();

        super.onTerminate();
    }
}
