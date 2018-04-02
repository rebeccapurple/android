package rebeccapurple.android.firebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import rebeccapurple.Operator;
import rebeccapurple.exception.CancelledTaskException;

public class Get<T> extends Task<T> implements ValueEventListener {
    private DatabaseReference __reference;
    private Class<T> __class;
    private Operator.On<T> __operator;

    public Get(String path, Class<T> c, Operator.On<T> operator) {
        super(path);
        __reference = null;
        __class = c;
        __operator = operator;
    }

    @Override
    protected void on(DatabaseReference reference) {
        __reference = reference;
        reference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        try {
            __value = snapshot.getValue(__class);
            if(__operator != null){
                __operator.on(__value, null);
            }
        } catch(Throwable e){
            cancel(e);
        }
        cancel(new CancelledTaskException());
    }

    @Override
    public void onCancelled(DatabaseError error) {
        if(error!=null){
            functional.log.e(error, error.toException());
            cancel(error.toException());
        }
    }

    @Override
    public void cancel(Throwable exception) {
        super.cancel(exception);
        if(__reference != null){
            __reference.removeEventListener(this);
        }
        if(__operator != null){
            __operator.on(__value, exception);
        }
    }
}
