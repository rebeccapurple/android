package rebeccapurple.android.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import rebeccapurple.Listener;
import rebeccapurple.exception.CancelledTaskException;
import rebeccapurple.schedule.Timeout;

public class Client<SERVICE> extends Communicator implements rebeccapurple.communicator.Client<Message> {
    private final Context __context;
    private final Class<SERVICE> __class;

    private Messenger __server = null;
    private Listener<rebeccapurple.communicator.Client<Message>> __connect;
    private Listener<rebeccapurple.communicator.Client<Message>> __disconnect;

    private ServiceConnection __connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            __server = new Messenger(service);
            __messenger = new Messenger(__handler = new Handler(Client.this));
            if(__connect!=null){
                __connect.on(Client.this);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(__disconnect!=null){
                __disconnect.on(Client.this);
            }
            __server = null;
            __messenger = null;
            __handler = null;
        }
    };

    protected <REQUEST extends rebeccapurple.communicator.Request<Message>> Message message(REQUEST request){ return request != null ? request.in() : null; }
    protected <TASK extends rebeccapurple.communicator.Task<Message>> Message message(TASK task){ return task != null ? task.in() : null; }

    protected <REQUEST extends rebeccapurple.communicator.Request<Message>> REQUEST prepare(REQUEST o) throws ClassCastException {
        if(o.in() != null && !o.is(Task.STATE.CANCELLED) && !o.is(Task.STATE.COMPLETED)){
            if(o instanceof Request) {
                Request request = (Request) o;
                __requests.put(request.prepare(__integer.issue(this::validate), this), request);
            } else {
                throw new ClassCastException();
            }
        } else {
            functional.log.e("o.in() == null || o.is(Task.STATE.CANCELLED) || o.is(Task.STATE.COMPLETED)");
        }
        return o;
    }

    protected <TASK extends rebeccapurple.communicator.Task<Message>> TASK prepare(TASK o) throws ClassCastException {
        if(o.in() != null && !o.is(Task.STATE.CANCELLED) && !o.is(Task.STATE.COMPLETED)){
            if(o instanceof Task){
                Task task = (Task) o;
                Message message = task.ready(this);
                try {
                    __server.send(message);
                } catch(RemoteException e) {
                    functional.log.e("__server.send(message)", e);
                }
            } else {
                throw new ClassCastException();
            }
        } else {
            functional.log.e("o.in() == null || o.is(Task.STATE.CANCELLED) || o.is(Task.STATE.COMPLETED)");
        }
        return o;
    }

    @Override
    public <TASK extends rebeccapurple.communicator.Task<Message>> TASK send(TASK task) {
        if(__server != null){
            try {
                __server.send(message(prepare(task)));
                return task;
            } catch (RemoteException e) {
                functional.log.e("__server.send(rebeccapurple.android.message.complete(task.in(), __messenger))", e);
            }
        } else {
            functional.log.e("__server == null");
        }
        return null;
    }

    @Override
    public <REQUEST extends rebeccapurple.communicator.Request<Message>> REQUEST send(REQUEST request) {
        if(__server != null) {
            try {
                __server.send(message(prepare(request)));
                return request;
            } catch (RemoteException | ClassCastException e) {
                functional.log.e("__server.send(rebeccapurple.android.message.complete(request.in(), __messenger))", e);
            }
        } else {
            functional.log.e("__server == null");
        }
        return null;
    }

    @Override
    public void connect() {
        if(__server == null) {
            __context.bindService(new Intent(__context, __class), __connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void connect(Listener<rebeccapurple.communicator.Client<Message>> connect) {
        __connect = connect;
        connect();
    }

    @Override
    public void disconnect() {
        if(__server != null){
            __context.unbindService(__connection);
        }
    }

    @Override
    public void disconnect(Listener<rebeccapurple.communicator.Client<Message>> disconnect) {
        __disconnect = disconnect;
        disconnect();
    }

    public Client(Context context, Class<SERVICE> c) {
        super();
        __context = context;
        __class = c;
    }
}
