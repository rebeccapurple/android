package rebeccapurple.android.messenger;

import android.os.Message;

import rebeccapurple.Listener;

public class request {
    public static Request ping(Listener<Message> reply){ return new Request(rebeccapurple.android.message.ping(), reply); }
    public static Request ping(long timeout, Listener<Message> reply){ return new Request(rebeccapurple.android.message.ping(), timeout, reply); }
}
