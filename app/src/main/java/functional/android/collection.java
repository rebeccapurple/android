package functional.android;

import android.util.SparseArray;

import rebeccapurple.Listener;

public class collection extends functional.collection {
    public static <T> void each(SparseArray<? extends T> array, Listener<T> function){
        for(int i = 0; i < array.size(); i++){
            function.on(array.valueAt(i));
        }
    }
}
