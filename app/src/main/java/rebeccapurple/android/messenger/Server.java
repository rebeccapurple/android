package rebeccapurple.android.messenger;

import android.os.Message;
import android.os.Messenger;

import rebeccapurple.Listener;
import rebeccapurple.commmunicator;

public class Server extends Communicator implements rebeccapurple.commmunicator.Server<Message> {
    private Listener<commmunicator.Server<Message>> __listen = null;

    @Override
    public void listen() {
        if(__messenger == null){
            __messenger = new Messenger(__handler = new Handler(this));
            if(__listen != null) {
                __listen.on(this);
            }
        }
    }

    @Override
    public void listen(Listener<commmunicator.Server<Message>> listen) {
        __listen = listen;
        listen();
    }

    @Override
    public void close(){
        super.close();
        __handler = null;
        __messenger = null;
    }
}
