package rebeccapurple.android.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

/**
 *
 * @see      <a href="https://developer.android.com/training/volley/index.html>Transmitting Network Data Using Volley</a>
 */

public class volley {

    public static class response {
        public static rebeccapurple.http.Response from(VolleyError error){ return error !=null ? from(error.networkResponse) : null; }
        public static rebeccapurple.http.Response from(NetworkResponse response){ return response != null ? new rebeccapurple.http.Response(response.statusCode, response.headers, response.data) : null; }
    }

    public static class method {
        public static int from(rebeccapurple.http.Method v){
            if(v != null) {
                switch (v) {
                    case GET: return com.android.volley.Request.Method.GET;
                    case POST: return com.android.volley.Request.Method.POST;
                    case PUT: return com.android.volley.Request.Method.PUT;
                    case DELETE: return com.android.volley.Request.Method.DELETE;
                    case HEAD: return com.android.volley.Request.Method.HEAD;
                    case OPTIONS: return com.android.volley.Request.Method.OPTIONS;
                    case TRACE: return com.android.volley.Request.Method.TRACE;
                    case PATCH: return com.android.volley.Request.Method.PATCH;
                }
            }
            return com.android.volley.Request.Method.DEPRECATED_GET_OR_POST;
        }
    }

    public static class Internal extends Request<rebeccapurple.http.Response> {
        private final Task __task;

        Task task(){ return __task; }

        public Internal(Task task){
            super(task.method(), task.uri(), task);
            __task = task;
        }

        @Override public Map<String, String> getHeaders() { return __task.headers(); }
        @Override public byte[] getBody() throws AuthFailureError { return __task.body()!=null ? __task.body() : super.getBody(); }
        @Override protected Response<rebeccapurple.http.Response> parseNetworkResponse(NetworkResponse response) { return Response.success(volley.response.from(response), HttpHeaderParser.parseCacheHeaders(response)); }
        @Override protected void deliverResponse(rebeccapurple.http.Response response) { __task.on(response); }
    }

    public static class Task extends rebeccapurple.http.Task implements Response.ErrorListener {
        private final Internal __internal;

        public int method(){ return method.from(__request.method()); }

        Internal internal(){ return __internal; }

        synchronized protected volley.Internal ready(){
            if(is(STATE.UNKNOWN)) {
                __state = STATE.READY;
                return __internal;
            } else {
                functional.log.e("is(STATE.UNKNOWN) == false");
            }
            return null;
        }

        @Override public void on(rebeccapurple.http.Response response){ super.on(response); }

        @Override
        public void cancel(Throwable exception) {
            synchronized (this) {
                if(!is(STATE.CANCELLED) && !is(STATE.COMPLETED)) {
                    __exception = exception;
                    __state = STATE.CANCELLED;
                    __internal.cancel();
                } else {
                    functional.log.e("is(STATE.CANCELLED) || is(STATE.COMPLETED)");
                }
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            synchronized (this) {
                if(!is(STATE.CANCELLED) && !is(STATE.COMPLETED)) {
                    __response = response.from(error);
                    __exception = error;
                    __state = STATE.CANCELLED;
                    __internal.cancel();
                } else {
                    functional.log.e("is(STATE.CANCELLED) || is(STATE.COMPLETED)");
                }
            }
        }

        public Task(rebeccapurple.http.Request request, rebeccapurple.Listener<rebeccapurple.http.Response> listener){
            super(request, listener);
            __internal = new Internal(this);
        }
    }
}
