package rebeccapurple.android;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Locale;

public class message {
    public static final int QUIT = 0;

    public static class serializer implements JsonSerializer<android.os.Message> {
        private JsonArray arguments(android.os.Message message){
            JsonArray array = new JsonArray();
            array.add(message.arg1);
            array.add(message.arg2);
            return array;
        }

        private JsonParser parser = new JsonParser();

        @Override
        public JsonElement serialize(android.os.Message message, Type type, JsonSerializationContext context) {
            if(message != null) {
                JsonObject object = new JsonObject();
                object.add("what", new JsonPrimitive(message.what));
                object.add("arguments", arguments(message));
                Bundle data = message.getData();
                String json = data.getString("json");
                if(json != null) {
                    object.add("json", parser.parse(json));
                }
                String exception = data.getString("exception");
                if(exception != null){
                    object.add("json", parser.parse(exception));
                }
                return object;
            }
            return JsonNull.INSTANCE;
        }
    }

    public static class deserializer implements JsonDeserializer<android.os.Message> {

        @Override
        public android.os.Message deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            throw new RuntimeException();
        }
    }

    public static Message put(Message message, Throwable exception){
        if(exception != null) {
            Bundle data = message.getData();
            data.putString("exception", String.format(Locale.getDefault(), "{ \"exception\": { \"type\": \"%s\", \"message\": %s }}",
                                                                           rebeccapurple.string.from(exception.getClass(), false),
                                                                           exception.getMessage()));
            message.setData(data);
        }
        return message;
    }

    public static Message put(Message message, String key, String value){
        Bundle data = message.getData();
        data.putString(key, value);
        message.setData(data);
        return message;
    }

    public static Message complete(Message message, Messenger responsable){
        message.replyTo = responsable;
        return message;
    }

    public static Message complete(Message message, Messenger responsable, Throwable exception){ return put(complete(message, responsable), exception); }

    public static Message ping(int request){
        Message message = Message.obtain();
        message.what = rebeccapurple.android.messenger.operator.type.PING;
        message.arg1 = request;
        message.arg2 = 0;
        put(message, "json", String.format(Locale.getDefault(), "{ \"ping\": %d }", System.currentTimeMillis()));
        return message;
    }
}
