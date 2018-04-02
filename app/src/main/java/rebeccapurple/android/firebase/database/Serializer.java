package rebeccapurple.android.firebase.database;

import com.google.firebase.database.DatabaseError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class Serializer implements JsonSerializer<DatabaseError> {
    @Override
    public JsonElement serialize(DatabaseError o, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("code", new JsonPrimitive(o.getCode()));
        object.add("detail", new JsonPrimitive(o.getDetails()));
        object.add("message", new JsonPrimitive(o.getMessage()));
        return object;
    }
}
