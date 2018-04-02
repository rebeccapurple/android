package functional.android.http;

import android.content.Context;

public class client {
    private static rebeccapurple.android.http.Client __client = null;

    public static void init(Context context){
        synchronized (client.class) {
            if(__client == null){
                __client = rebeccapurple.android.http.Client.Get(context);
            }
        }
    }

    public static rebeccapurple.android.http.Client get(){ return __client; }
}
