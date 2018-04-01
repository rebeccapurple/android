package rebeccapurple.android;

import android.util.SparseArray;

import rebeccapurple.Listener;

public class collection extends rebeccapurple.collection {
    public static <T> void each(SparseArray<T> array, Listener<T> function){
        for(int i = 0; i < array.size(); i++){
            function.on(array.valueAt(i));
        }
    }
}
