package rebeccapurple.android.messenger;

import android.os.Message;

public class Task implements rebeccapurple.commmunicator.Task<Message> {
    protected Message __in;
    protected int __state;
    protected Throwable __exception;
    protected Long __ttl;
    protected Communicator __communicator;

    @Override public Message in() { return __in; }
    @Override public int state() { return __state; }
    @Override public Throwable exception() { return __exception; }
    @Override public Long ttl() { return __ttl; }

    @Override
    public void cancel(Throwable exception) {
        synchronized (this) {
            if(!is(STATE.CANCELLED) && !is(STATE.COMPLETED)) {
                __state = STATE.CANCELLED;
                __exception = exception;
                if(__communicator != null){
                    __communicator.cancel(this);
                    __communicator = null;
                }
            } else {
                rebeccapurple.log.e("is(STATE.CANCELLED) || is(STATE.COMPLETED)", exception);
            }
        }
    }

    public Task(Message in){
        __in = in;
        __state = STATE.UNKNOWN;
        __exception = null;
        __ttl = null;
    }

    public Task(Message in, long ttl){
        __in = in;
        __state = STATE.UNKNOWN;
        __exception = null;
        __ttl = ttl;
    }
}
