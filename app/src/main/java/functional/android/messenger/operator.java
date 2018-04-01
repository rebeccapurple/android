package functional.android.messenger;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

import rebeccapurple.android.messenger.Communicator;
import rebeccapurple.android.messenger.Operator;
import rebeccapurple.exception.CancelledTaskException;

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
        communicator.add(type.tick, new Tick());
    }

    public static class Ping implements Operator {
        @Override
        public void call(Messenger from, Message in, On callback) {
            functional.log.e(in);
            callback.on(from, functional.android.message.pong(in.arg1), null);
        }
    }

    public static class Tick implements Operator {
        private final LinkedHashMap<Messenger, Integer> __messengers = new LinkedHashMap<>();

        private void close(){
            for(Map.Entry<Messenger, Integer> entry : __messengers.entrySet()){
                Messenger from = entry.getKey();
                Integer unique = entry.getValue();
                try {
                    /** reply do not need */
                    from.send(functional.android.message.tock(unique, new CancelledTaskException()));
                } catch (RemoteException e) {
                    functional.log.e("fail to from.send(functional.android.message.tock(unique))", e);
                }
            }
            __messengers.clear();
        }

        private void run(){
            LinkedList<Messenger> exceptions = new LinkedList<>();
            for(Map.Entry<Messenger, Integer> entry : __messengers.entrySet()){
                Messenger from = entry.getKey();
                Integer unique = entry.getValue();
                try {
                    /** reply do not need */
                    from.send(functional.android.message.tock(unique));
                } catch (RemoteException e) {
                    functional.log.e("fail to from.send(functional.android.message.tock(unique))", e);
                    exceptions.add(from);
                }
            }
            functional.collection.remove(__messengers, exceptions);
        }

        private void tick(rebeccapurple.scheduler.Task task, Throwable exception, rebeccapurple.Operator.On<rebeccapurple.scheduler.Task> callback){
            if(exception == null) {
                functional.android.main.post(this::run);
            } else {
                functional.android.main.post(this::close);
            }
            if(callback != null){
                callback.on(task, exception);
            }
        }

        @Override
        public void call(Messenger from, Message in, On callback) {
            functional.log.e(in);
            if(in.arg2 == functional.android.messenger.operator.command.quit){
                __messengers.remove(from);
            } else {
                Integer previous = __messengers.put(from, in.arg1);
                if(previous != null) {
                    functional.log.e("previous != null");
                }
            }
            callback.on(from, null, null);
        }

        private Tick(){
            functional.scheduler.dispatch(new rebeccapurple.schedule.Tick(1000L, this::tick));
        }
    }
}
