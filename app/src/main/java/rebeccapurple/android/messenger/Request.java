package rebeccapurple.android.messenger;

import android.os.Message;

public class Request extends Task implements rebeccapurple.commmunicator.Request<Message> {
    protected Message __out;
    protected Integer __unique;

    Message in(Message message){ return __in = message; }

    public Integer unique(){ return __unique; }

    public void on(Message message){

    }

    public void quit(Message message){

    }

    @Override public Message out() { return __out; }

    public Request(Message in) {
        super(in);
        __out = null;
    }

    public Request(Message in, long ttl) {
        super(in, ttl);
        __out = null;
    }
}
