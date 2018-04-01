package functional.android.messenger;

import android.os.Message;

import rebeccapurple.Listener;
import rebeccapurple.android.messenger.Request;

public class request {
    public static Request ping(Listener<Message> reply){ return new Request(functional.android.message.ping(), reply); }
    public static Request ping(long timeout, Listener<Message> reply){ return new Request(functional.android.message.ping(), timeout, reply); }
    public static Request tick(Listener<Message> reply){ return new Request(functional.android.message.tick(), reply); }
    public static Request tick(long timeout, Listener<Message> reply){ return new Request(functional.android.message.tick(), timeout, reply); }
}
