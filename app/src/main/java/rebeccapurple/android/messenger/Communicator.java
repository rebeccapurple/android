package rebeccapurple.android.messenger;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;

import functional.android.messenger.operator;
import rebeccapurple.exception.CancelledTaskException;

public abstract class Communicator implements rebeccapurple.communicator.Base<Message> {
    public static class Handler extends android.os.Handler {
        private final Communicator __communicator;

        @Override public void handleMessage(Message message) { __communicator.on(message); }

        Handler(Communicator communicator){ __communicator = communicator; }
    }

    protected final SparseArray<Operator> __operators;
    protected final SparseArray<Request> __requests;
    protected final functional.integer __integer;
    protected Messenger __messenger;
    protected Handler __handler;

    public IBinder binder(){ return __messenger!=null ? __messenger.getBinder() : null; }

    Boolean validate(Integer v){ return v!=null && v!=0 && __requests.get(v)==null; }

    private void callback(Messenger from, Message out, Throwable exception){
        if(out != null) {
            if (from != null) {
                try {
                    from.send(functional.android.message.complete(out, __messenger, exception));
                } catch (RemoteException e) {
                    functional.log.e("from.send(rebeccapurple.android.message.complete(out, __messenger, exception))", e);
                }
            } else {
                functional.log.e("from == null");
            }
        }
    }

    public void add(int type, Operator operator){
        if(operator!=null){
            __operators.put(type, operator);
        } else {
            __operators.remove(type);
        }
    }

    protected void on(Message message){
        Request request = __requests.get(message.arg1);
        if(request != null){
            if(message.arg2 == operator.command.quit) {
                __requests.delete(message.arg1);
                request.quit(message);
            } else {
                request.on(message);
            }
        } else {
            Operator operator = __operators.get(message.what);
            if(operator!=null){
                operator.call(message.replyTo, message, this::callback);
            } else {
                functional.log.e("operator == null");
            }
        }
    }

    @Override
    public void close() {
        functional.android.collection.each(__requests, this::cancel);
        __requests.clear();
    }

    @Override
    public void cancel(rebeccapurple.communicator.Task<Message> task) {
        if(task instanceof Request) {
            Request request = (Request) task;
            Integer unique = request.complete(new CancelledTaskException());
            if(unique != null) {
                __requests.remove(unique);
            } else {
                functional.log.e("unique == null");
            }
        } else {
            functional.log.e("(task instanceof Request) == false");
        }
    }

    Communicator(){
        __operators = new SparseArray<>();
        __requests = new SparseArray<>();
        __integer = new functional.integer();
        __messenger = null;
        __handler = null;
    }
}
