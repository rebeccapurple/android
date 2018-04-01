package io.textory.rebeccapurple.application;

import android.os.Handler;

import rebeccapurple.Function;
import rebeccapurple.exception.CancellationScheduleException;
import rebeccapurple.schedule.Time;
import rebeccapurple.schedule.Timeout;

/**
 *
 * @author      sean <hyunsik-park@textory.com>
 * @since       0.0.1
 */

public class debug {
    private static Handler __handler;

    public static <T extends rebeccapurple.scheduler.Task> void scheduler(T schedule, long delay, Function<T, Runnable> factory){
        try {
            __handler.postDelayed(factory.call(schedule), delay);
        } catch (Throwable e) {
            rebeccapurple.log.e(schedule, e);
        }
    }

    public static void run(){
        __handler = new Handler();

        rebeccapurple.scheduler.dispatch(new Time(rebeccapurple.timestamp.after(1000L), rebeccapurple.scheduler::log));
        rebeccapurple.scheduler.dispatch(new Timeout(2000L, rebeccapurple.scheduler::log));
        scheduler(rebeccapurple.scheduler.dispatch(new Timeout(3000L, rebeccapurple.scheduler::log)), 1000L, schedule->()->schedule.cancel(new CancellationScheduleException()));
        scheduler(rebeccapurple.scheduler.dispatch(new Timeout(4000L, rebeccapurple.scheduler::log)), 2000L, schedule->()->rebeccapurple.scheduler.reset(schedule));
    }
}
