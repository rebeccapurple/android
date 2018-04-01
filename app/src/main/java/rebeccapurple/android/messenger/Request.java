package rebeccapurple.android.messenger;

import android.os.Message;

import rebeccapurple.Listener;
import rebeccapurple.exception.CancelledScheduleException;
import rebeccapurple.schedule.Timeout;

public class Request extends Task implements rebeccapurple.communicator.Request<Message> {
    protected static class timeout {
        protected static void cancel(rebeccapurple.scheduler.Task task, Throwable exception, rebeccapurple.Operator.On<rebeccapurple.scheduler.Task> callback){

        }
    }

    protected Message __out;
    protected Integer __unique;
    protected Listener<Message> __reply;
    protected Timeout __timeout;

    Message in(Message message){ return __in = message; }

    public Integer unique(){ return __unique; }

    synchronized int prepare(int unique, Communicator communicator){
        __state = (__state == STATE.UNKNOWN ? STATE.READY : __state);
        __in.arg1 = __unique = unique;
        __in.replyTo = communicator.__messenger;
        __communicator = communicator;

        functional.scheduler.dispatch(timeout());

        return __unique;
    }

    synchronized Integer complete(Throwable exception){
        if(!is(STATE.COMPLETED) && !is(STATE.CANCELLED)) {
            __state = STATE.CANCELLED;
            __exception = exception;
            __communicator = null;
            return __unique;
        }
        return null;
    }

    private Timeout timeout(){
        if(__ttl != null){
            if(__timeout != null) {
                __timeout.ttl(__ttl);
            } else {
                __timeout = new Timeout(__ttl, (task, exception, callback) -> {
                    if(task != null){
                        if(callback != null){
                            callback.on(task, exception);
                            __communicator.cancel(Request.this);
                        } else {
                            functional.log.e("callback == null", exception);
                        }
                    } else {
                        functional.log.e("task == null", exception);
                    }
                });
            }
        } else if(__timeout != null) {
            __timeout.cancel(new CancelledScheduleException());
            __timeout = null;
        }
        return __timeout;
    }

    synchronized protected void on(Message message){
        if(!is(STATE.CANCELLED) && !is(STATE.COMPLETED)) {
            functional.log.e("progress");
            __state = STATE.INPROGRESS;
            functional.scheduler.reset(__timeout);
            if(__reply!=null){
                __reply.on(message);
            }
        } else {
            functional.scheduler.cancel(__timeout);
        }
    }

    synchronized protected void quit(Message message){
        if(!is(STATE.CANCELLED) && !is(STATE.COMPLETED)) {
            functional.log.e("quit");
            __state = STATE.COMPLETED;
            if(__reply!=null){
                __reply.on(message);
            }
        }
        functional.scheduler.cancel(__timeout);
    }

    @Override public Message out() { return __out; }

    public Request(Message in) {
        super(in);
        __out = null;
        __reply = null;
        __timeout = null;
        __unique = null;
    }

    public Request(Message in, Listener<Message> reply) {
        super(in);
        __out = null;
        __reply = reply;
        __timeout = null;
        __unique = null;
    }

    public Request(Message in, long ttl) {
        super(in, ttl);
        __out = null;
        __reply = null;
        __timeout = null;
        __unique = null;
    }

    public Request(Message in, long ttl, Listener<Message> reply) {
        super(in, ttl);
        __out = null;
        __reply = reply;
        __timeout = null;
        __unique = null;
    }
}
