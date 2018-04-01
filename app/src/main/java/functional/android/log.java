package functional.android;

import rebeccapurple.log.Type;

public class log implements rebeccapurple.log.Method {
    private static functional.android.log __singleton = null;

    public static functional.android.log get(){
        synchronized (functional.android.log.class){
            if(__singleton == null) {
                __singleton = new functional.android.log();
            }
        }
        return __singleton;
    }


    @Override
    public void out(int classification, String type, rebeccapurple.log.Date current, long thread, String tag, String message, Throwable exception) {
        switch(classification){
            case Type.ERROR:        android.util.Log.e(tag, message, exception); break;
            case Type.WARNING:      android.util.Log.w(tag, message, exception); break;
            case Type.CAUTION:      android.util.Log.w(tag, message, exception); break;
            case Type.NOTICE:       android.util.Log.i(tag, message, exception); break;
            case Type.INFORMATION:  android.util.Log.i(tag, message, exception); break;
            case Type.DEBUG:        android.util.Log.d(tag, message, exception); break;
            case Type.VERBOSE:      android.util.Log.v(tag, message, exception); break;
            case Type.FLOW:         android.util.Log.v(tag, message, exception); break;
            case Type.USER:         android.util.Log.i(type + "/" + tag, message, exception); break;
            default:                android.util.Log.v(tag, message, exception); break;
        }
    }

    private log(){}
}
