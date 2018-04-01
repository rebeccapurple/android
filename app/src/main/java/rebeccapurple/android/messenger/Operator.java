package rebeccapurple.android.messenger;

import android.os.Message;
import android.os.Messenger;

public interface Operator {
    interface On { void on(Messenger from, Message out, Throwable exception); }
    void call(Messenger from, Message in, On callback);
}
