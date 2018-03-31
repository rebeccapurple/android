package rebeccapurple.android;

import android.os.Bundle;
import android.os.Message;

import com.google.gson.Gson;
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

public class message {
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
}
