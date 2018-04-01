package rebeccapurple.android.messenger.operator;

import android.os.Message;
import android.os.Messenger;

public class Ping implements rebeccapurple.android.messenger.Operator {

    @Override
    public void call(Messenger from, Message in, On callback) {
        rebeccapurple.log.e(in);
        callback.on(from, rebeccapurple.android.message.ping(in.arg1), null);
    }
}
