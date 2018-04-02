package rebeccapurple.android.firebase.database;

import com.google.firebase.database.DatabaseReference;

public abstract class Task<T> implements rebeccapurple.communicator.Task<String> {
    protected String __in;
    protected T __value;
    protected int __state;
    protected Throwable __exception;

    synchronized protected String ready(){
        if(is(STATE.UNKNOWN)) {
            __state = STATE.READY;
            return __in;
        }
        return null;
    }

    @Override public String in() { return __in; }
    @Override public int state() { return __state; }
    @Override public Throwable exception() { return __exception; }
    @Override public Long ttl() { return null; }

    public T out() { return __value; }

    protected abstract void on(DatabaseReference reference);

    @Override
    public void cancel(Throwable exception) {
        synchronized (this) {
            if(!is(STATE.COMPLETED) && !is(STATE.CANCELLED)) {
                __state = STATE.CANCELLED;
                __exception = exception;
                /** todo: implement cancellation */
            } else {
                functional.log.e("is(STATE.COMPLETED) || is(STATE.CANCELLED)");
            }
        }
    }

    public Task(String path){
        __in = path;
        __value = null;
    }

    public Task(String path, T o){
        __in = path;
        __value = o;
    }
}
