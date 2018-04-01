package rebeccapurple.android.messenger;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;

import rebeccapurple.exception.CancellationTaskException;

public abstract class Communicator implements rebeccapurple.commmunicator.Base<Message> {
    public static class Handler extends android.os.Handler {
        private final Communicator __communicator;

        @Override public void handleMessage(Message message) { __communicator.on(message); }

        Handler(Communicator communicator){ __communicator = communicator; }
    }

    protected final SparseArray<Operator> __operators;
    protected final SparseArray<Request> __requests;
    protected final rebeccapurple.integer __integer;
    protected Messenger __messenger;
    protected Handler __handler;

    protected void complete(Messenger from, Message out, Throwable exception){
        if(out != null) {
            if (from != null) {
                try {
                    from.send(rebeccapurple.android.message.complete(out, __messenger, exception));
                } catch (RemoteException e) {
                    rebeccapurple.log.e("from.send(rebeccapurple.android.message.complete(out, __messenger, exception))", e);
                }
            } else {
                rebeccapurple.log.e("from == null");
            }
        }
    }

    public void add(int type, Operator operator){
        __operators.put(type, operator);
    }

    protected void on(Message message){
        Request request = __requests.get(message.arg1);
        if(request != null){
            if(message.what == rebeccapurple.android.message.QUIT) {
                __requests.delete(message.arg1);
                request.quit(message);
            } else {
                request.on(message);
            }
        } else {
            Operator operator = __operators.get(message.what);
            if(operator!=null){
                operator.call(message.replyTo, message, this::complete);
            } else {
                rebeccapurple.log.e(message);
            }
        }
    }

    @Override
    public void close() {
        rebeccapurple.android.collection.each(__requests, this::cancel);
        __requests.clear();
    }

    @Override
    public void cancel(rebeccapurple.commmunicator.Task<Message> task) {
        if(task instanceof Request) {
            Request request = (Request) task;
            Integer unique = request.unique();
            if(unique != null) {
                __requests.remove(unique);
                request.cancel(new CancellationTaskException());
            } else {
                rebeccapurple.log.e("unique == null");
            }
        } else {
            rebeccapurple.log.e("(task instanceof Request) == false");
        }
    }

    Communicator(){
        __operators = new SparseArray<>();
        __requests = new SparseArray<>();
        __integer = new rebeccapurple.integer();
        __messenger = null;
        __handler = null;
    }
}
