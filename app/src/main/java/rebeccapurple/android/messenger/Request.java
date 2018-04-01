package rebeccapurple.android.messenger;

import android.os.Message;

import rebeccapurple.Listener;
import rebeccapurple.exception.CancellationScheduleException;
import rebeccapurple.schedule.Timeout;

public class Request extends Task implements rebeccapurple.commmunicator.Request<Message> {
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

    Timeout timeout(){
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
                            rebeccapurple.log.e("callback == null", exception);
                        }
                    } else {
                        rebeccapurple.log.e("task == null", exception);
                    }
                });
            }
        } else if(__timeout != null) {
            __timeout.cancel(new CancellationScheduleException());
            __timeout = null;
        }
        return __timeout;
    }

    protected void on(Message message){
        if(__reply!=null){
            __reply.on(message);
        }
        rebeccapurple.log.e("progress");
        rebeccapurple.scheduler.reset(__timeout);
    }

    protected void quit(Message message){
        if(__reply!=null){
            __reply.on(message);
        }
        rebeccapurple.log.e("quit");
        rebeccapurple.scheduler.cancel(__timeout);
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
