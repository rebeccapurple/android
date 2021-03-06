package rebeccapurple.android;

import android.os.Handler;
import android.os.Looper;

public class Scheduler extends rebeccapurple.scheduler.Loop {
    private static Scheduler __singleton = null;

    public static Scheduler Get() {
        synchronized (Scheduler.class) {
            if(__singleton == null) {
                __singleton = new Scheduler();
            }
        }
        return __singleton;
    }

    private static final long DefaultTick = 1L;

    private Looper __looper = null;
    private Handler __handler = null;

    @Override
    protected void onecycle(){
        super.onecycle();
        lock();
        if(__thread!=null) {
            __handler.postDelayed(this::onecycle, DefaultTick);
        }
        unlock();
    }

    @Override
    protected void loop() {
        Looper.prepare();
        lock();
        __looper = Looper.myLooper();
        __handler = new Handler(__looper);
        __handler.postDelayed(this::onecycle, DefaultTick);
        resume();
        unlock();
        Looper.loop();
    }

    @Override
    public void off() {
        lock();
        if(__thread != null){
            __thread = null;
            __handler = null;
            __looper = null;
        }
        unlock();
    }

    private Scheduler(){}
}
