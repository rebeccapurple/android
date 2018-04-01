package rebeccapurple.android;

public class log implements rebeccapurple.log.Method {
    private static log __singleton = null;

    public static log get(){
        synchronized (log.class){
            if(__singleton == null) {
                __singleton = new log();
            }
        }
        return __singleton;
    }


    @Override
    public void out(int classification, String type, rebeccapurple.log.Date current, long thread, String tag, String message, Throwable exception) {
        switch(classification){
            case rebeccapurple.log.TYPE.ERROR:          android.util.Log.e(tag, message, exception); break;
            case rebeccapurple.log.TYPE.WARNING:        android.util.Log.w(tag, message, exception); break;
            case rebeccapurple.log.TYPE.CAUTION:        android.util.Log.w(tag, message, exception); break;
            case rebeccapurple.log.TYPE.NOTICE:         android.util.Log.i(tag, message, exception); break;
            case rebeccapurple.log.TYPE.INFORMATION:    android.util.Log.i(tag, message, exception); break;
            case rebeccapurple.log.TYPE.DEBUG:          android.util.Log.d(tag, message, exception); break;
            case rebeccapurple.log.TYPE.VERBOSE:        android.util.Log.v(tag, message, exception); break;
            case rebeccapurple.log.TYPE.FLOW:           android.util.Log.v(tag, message, exception); break;
            case rebeccapurple.log.TYPE.USER:           android.util.Log.i(type + "/" + tag, message, exception); break;
            default:                                    android.util.Log.v(tag, message, exception); break;
        }
    }

    private log(){}
}
