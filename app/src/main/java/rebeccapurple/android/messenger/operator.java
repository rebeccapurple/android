package rebeccapurple.android.messenger;

import android.os.Message;
import android.os.Messenger;

public class operator {
    public static class type {
        public static final int ping = 1;
        public static final int pong = 2;
        public static final int tick = 3;
        public static final int tock = 4;
    }
    public static class command {
        public static final int quit = 1;
    }

    public static void init(Communicator communicator){
        communicator.add(type.ping, new Ping());
    }

    public static class Ping implements Operator {
        @Override
        public void call(Messenger from, Message in, On callback) {
            rebeccapurple.log.e(in);
            callback.on(from, rebeccapurple.android.message.pong(in.arg1), null);
        }
    }

    public static class Tick implements Operator {

        @Override
        public void call(Messenger from, Message in, On callback) {

        }
    }
}
