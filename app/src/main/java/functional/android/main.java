package functional.android;

import android.os.Handler;
import android.os.Looper;

import rebeccapurple.Function;

public class main {
    private static Handler __handler = null;

    public static void init(){
        synchronized (main.class) {
            if(__handler == null){
                __handler = new Handler(Looper.getMainLooper());
            }
        }
    }

    public static <T> void post(T in, long delay, Function<T, Runnable> factory){
        try {
            post(factory.call(in), delay);
        } catch (Throwable e) {
            functional.log.e(in, e);
        }
    }

    public static void post(Runnable r){ __handler.post(r); }

    public static void post(Runnable r, long millisecond){ __handler.postDelayed(r, millisecond); }
}
