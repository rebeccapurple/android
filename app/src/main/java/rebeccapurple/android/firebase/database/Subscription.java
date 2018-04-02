package rebeccapurple.android.firebase.database;

public class Subscription implements rebeccapurple.communicator.Request<String> {
    protected String __in;
    protected String __out;
    protected int __state;
    protected Throwable __exception;

    @Override public String in() { return __in; }
    @Override public int state() { return __state; }
    @Override public Throwable exception() { return __exception; }
    @Override public Long ttl() { return null; }
    @Override public String out() { return __out; }

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
}
