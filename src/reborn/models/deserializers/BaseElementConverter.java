package reborn.models.deserializers;

import com.google.gson.*;
import reborn.models.BaseElement;

import java.lang.reflect.Type;

/**
 * Created by CeH9 on 13.07.2016.
 */
public class BaseElementConverter implements JsonSerializer<BaseElement>, JsonDeserializer<BaseElement> {

    private static final String CLASS_META_KEY = "CLASS_META_KEY";

    @Override
    public BaseElement deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String className = jsonObj.get(CLASS_META_KEY).getAsString();
        try {
            Class<?> clz = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, clz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }

    }

    @Override
    public JsonElement serialize(BaseElement item, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonEle = jsonSerializationContext.serialize(item, item.getClass());
        jsonEle.getAsJsonObject().addProperty(CLASS_META_KEY, item.getClass().getCanonicalName());
        return jsonEle;
    }
}
