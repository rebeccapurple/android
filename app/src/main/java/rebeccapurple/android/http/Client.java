package rebeccapurple.android.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import rebeccapurple.Listener;
import rebeccapurple.communicator.Request;
import rebeccapurple.communicator.Task;
import rebeccapurple.exception.CancelledScheduleException;
import rebeccapurple.http.Message;

public class Client implements rebeccapurple.http.Client {
    private static Client __singleton = null;

    public static rebeccapurple.http.Task Factory(rebeccapurple.http.Request request, Listener<rebeccapurple.http.Response> listener){
        return new volley.Task(request, listener);
    }

    public static Client Get(Context context){
        synchronized (Client.class){
            synchronized (Client.class) {
                if(__singleton == null) {
                    __singleton = new Client(Volley.newRequestQueue(context));
                }
            }
        }
        return __singleton;
    }

    private final RequestQueue __queue;
    private Listener<rebeccapurple.communicator.Client<Message>> __disconnect;

    public <TASK extends Task<Message>> volley.Internal ready(TASK o) {
        if(o instanceof volley.Task){
            volley.Task task = (volley.Task) o;
            return task.ready();
        }
        return null;
    }

    @Override
    public <TASK extends Task<Message>> TASK send(TASK task) {
        volley.Internal internal = ready(task);
        if(internal != null) {
            __queue.add(internal);
            return task;
        } else {
            functional.log.e("internal == null");
        }
        return null;
    }

    @Override public <REQUEST extends Request<Message>> REQUEST send(REQUEST request) {
        volley.Internal internal = ready(request);
        if(internal != null) {
            __queue.add(internal);
            return request;
        } else {
            functional.log.e("internal == null");
        }
        return null;
    }

    @Override public void connect() { functional.log.e("connect"); }

    @Override
    public void connect(Listener<rebeccapurple.communicator.Client<Message>> listener) {
        connect();
    }

    @Override public void disconnect() { __queue.cancelAll(request -> true); }

    @Override
    public void disconnect(Listener<rebeccapurple.communicator.Client<Message>> listener) {
        disconnect();
    }

    @Override public void close() { disconnect(); }

    @Override
    public void cancel(Task<Message> task) {
        __queue.cancelAll(request -> {
            if(request instanceof volley.Internal){
                volley.Internal internal = (volley.Internal) request;
                volley.Task o = internal.task();
                o.cancel(new CancelledScheduleException());
                return true;
            }
            return false;
        });
    }

    private Client(RequestQueue queue){
        __queue = queue;
    }
}
