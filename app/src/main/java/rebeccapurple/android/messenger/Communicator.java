package rebeccapurple.android.messenger;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;

import rebeccapurple.commmunicator;
import rebeccapurple.exception.CancellationTaskException;

public class Communicator implements rebeccapurple.commmunicator.Base<Message> {
    public static class Handler extends android.os.Handler {
        private final Communicator __communicator;

        @Override public void handleMessage(Message message) { __communicator.on(message); }

        public Handler(Communicator communicator){ __communicator = communicator; }
    }

    protected SparseArray<Operator> __operators;
    protected SparseArray<Request> __requests;
    protected Messenger __messenger;
    protected Handler __handler;
    protected Operator __default;

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

    protected void on(Message message){
        Request request = __requests.get(message.arg1);
        if(request != null){

        } else {
            Operator operator = __operators.get(message.what);
            if(operator == null){
                operator = __default;
            }
            if(operator!=null){
                operator.call(message.replyTo, message, this::complete);
            } else {
                rebeccapurple.log.e("operator == null");
            }
        }
    }

    @Override
    public void close() {
        rebeccapurple.android.collection.each(__requests, this::cancel);
        __requests.clear();
    }

    @Override
    public void cancel(commmunicator.Task<Message> task) {
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
}
