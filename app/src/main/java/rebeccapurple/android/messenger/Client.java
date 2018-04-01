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
import rebeccapurple.exception.CancellationTaskException;
import rebeccapurple.schedule.Timeout;

public class Client<SERVICE> extends Communicator implements rebeccapurple.commmunicator.Client<Message> {
    private final Context __context;
    private final Class<SERVICE> __class;

    private Messenger __server = null;

    private Listener<rebeccapurple.commmunicator.Client<Message>> __connect;
    private Listener<rebeccapurple.commmunicator.Client<Message>> __disconnect;

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

    protected Boolean validate(Integer v){ return v!=null && v!=0 && __requests.get(v)==null; }

    @Override
    public <TASK extends rebeccapurple.commmunicator.Task<Message>> TASK send(TASK task) {
        if(__server != null){
            if(task instanceof Task) {
                try {
                    __server.send(rebeccapurple.android.message.complete(task.in(), __messenger));
                    return task;
                } catch (RemoteException e) {
                    rebeccapurple.log.e("__server.send(rebeccapurple.android.message.complete(task.in(), __messenger))", e);
                }
            } else {
                rebeccapurple.log.e("(task instanceof Task) == false");
            }
        } else {
            rebeccapurple.log.e("__server == null");
        }
        return null;
    }

    protected <REQUEST extends rebeccapurple.commmunicator.Request<Message>> Message message(REQUEST request){ return request != null ? request.in() : null; }

    protected <REQUEST extends rebeccapurple.commmunicator.Request<Message>> REQUEST prepare(REQUEST o) throws ClassCastException {
        if(o.in() != null){
            if(o instanceof Request) {
                Request request = (Request) o;
                request.__in.arg1 = request.__unique = __integer.issue(this::validate);
                request.__in.replyTo = __messenger;
                request.__communicator = this;
                __requests.put(request.__unique, request);
                rebeccapurple.scheduler.dispatch(request.timeout());
            } else {
                throw new ClassCastException();
            }
        } else {
            rebeccapurple.log.e("request.__in == null");
        }
        return o;
    }

    @Override
    public <REQUEST extends rebeccapurple.commmunicator.Request<Message>> REQUEST send(REQUEST request) {
        if(__server != null) {
            try {
                __server.send(message(prepare(request)));
                return request;
            } catch (RemoteException | ClassCastException e) {
                rebeccapurple.log.e("__server.send(rebeccapurple.android.message.complete(request.in(), __messenger))", e);
            }
        } else {
            rebeccapurple.log.e("__server == null");
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
    public void connect(Listener<rebeccapurple.commmunicator.Client<Message>> connect) {
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
    public void disconnect(Listener<rebeccapurple.commmunicator.Client<Message>> disconnect) {
        __disconnect = disconnect;
        disconnect();
    }

    public Client(Context context, Class<SERVICE> c) {
        super();
        __context = context;
        __class = c;
    }
}
