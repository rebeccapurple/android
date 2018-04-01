package rebeccapurple.android.messenger;

import android.os.Message;

public class Request extends Task implements rebeccapurple.commmunicator.Request<Message> {
    protected Message __out;
    protected Integer __unique;

    public Integer unique(){ return __unique; }

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
