package rebeccapurple.android.firebase.database;

import com.google.firebase.database.DatabaseReference;

public class Set<T> extends Task<T> {
    com.google.android.gms.tasks.Task<Void> __internal = null;
    @Override
    protected void on(DatabaseReference reference) {
        try {
            __internal = reference.setValue(__value);
        } catch(Throwable e){
            functional.log.e("reference.setValue(__value)", e);
            cancel(e);
        }
        /** TODO: IMPLEMENT MANAGING TASK */
    }

    public Set(String path, T o){
        super(path, o);
    }
}
