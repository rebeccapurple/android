package rebeccapurple.android.messenger.operator;

import android.os.Message;
import android.os.Messenger;

public class Pong implements rebeccapurple.android.messenger.Operator {
    @Override
    public void call(Messenger from, Message in, On callback) {
        rebeccapurple.log.e(in);
    }
}
