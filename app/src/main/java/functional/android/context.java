package functional.android;

import android.content.Context;
import android.os.Handler;

public class context {
    private static Handler __handler = null;
    public static void init(Context context){
        synchronized (context.class) {
            if(__handler == null) {
                __handler = new Handler(context.getMainLooper());
            }
        }
    }

    public static void delay(Runnable f, long millisecond){ __handler.postDelayed(f, millisecond); }
}
