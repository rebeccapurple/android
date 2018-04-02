package functional.android.firebase;

import rebeccapurple.Operator;
import rebeccapurple.android.firebase.database.Client;
import rebeccapurple.android.firebase.database.Set;
import rebeccapurple.android.firebase.database.Get;

public class database {
    private static Client __client = null;

    public static void init(){
        synchronized (database.class){
            if(__client == null){
                __client = Client.Get();
                __client.connect();
            }
        }
    }

    public static <T> Get<T> get(String path, Class<T> v, Operator.On<T> operator){
        return __client.send(new Get<>(path, v, operator));
    }

    public static <T> Set<T> set(String path, T v){
        return __client.send(new Set<>(path, v));
    }
}
