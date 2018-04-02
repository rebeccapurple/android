package rebeccapurple.android.firebase.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rebeccapurple.Listener;


public class Client implements rebeccapurple.communicator.Client<String> {
    private static Client __singleton = null;

    public static Client Get(){
        synchronized (Client.class){
            if(__singleton == null){
                return __singleton = new Client();
            }
        }
        return __singleton;
    }

    private FirebaseDatabase __database = null;
    Listener<rebeccapurple.communicator.Client<String>> __connect = null;
    Listener<rebeccapurple.communicator.Client<String>> __disconnect = null;

    protected <TASK extends rebeccapurple.communicator.Task<String>> String ready(TASK o) {
        if(o instanceof Task){
            Task task = (Task) o;
            return task.ready();
        }
        return null;
    }

    protected <TASK extends rebeccapurple.communicator.Task<String>> TASK on(TASK o, DatabaseReference reference) {
        if(o instanceof Task){
            Task task = (Task) o;
            task.on(reference);
            return o;
        }
        return null;
    }

    @Override
    public <TASK extends rebeccapurple.communicator.Task<String>> TASK send(TASK task) {
        String path = ready(task);
        if(path != null){
            if(__database != null){
                DatabaseReference reference = __database.getReference(path);
                if(reference != null) {
                    return on(task, reference);
                } else {
                    functional.log.e("reference == null");
                }
            } else {
                functional.log.e("__database == null");
            }
        } else {
            functional.log.e("(request instanceof Task)");
        }
        return null;
    }

    @Override
    public <REQUEST extends rebeccapurple.communicator.Request<String>> REQUEST send(REQUEST request) {
        if(request instanceof Subscription){

        } else {
            functional.log.e("(request instanceof Task)");
        }
        return null;
    }

    @Override
    public void connect() {
        if(__database == null){
            __database = FirebaseDatabase.getInstance();
            if(__connect != null){
                __connect.on(this);
            }
        }
    }

    @Override
    public void connect(Listener<rebeccapurple.communicator.Client<String>> listener) {
        __connect = listener;
        connect();
    }

    @Override
    public void disconnect() {
        if(__database != null){
            __database = null;
            if(__disconnect != null){
                __disconnect.on(this);
            }
        }
    }

    @Override
    public void disconnect(Listener<rebeccapurple.communicator.Client<String>> listener) {
        __disconnect = listener;
        disconnect();
    }

    @Override public void close() { disconnect(); }

    @Override
    public void cancel(rebeccapurple.communicator.Task<String> task) {

    }
}
