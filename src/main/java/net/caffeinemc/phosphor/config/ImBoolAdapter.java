package net.caffeinemc.phosphor.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import imgui.type.ImBoolean;

import java.lang.reflect.Type;

public class ImBoolAdapter implements JsonSerializer<ImBoolean>, JsonDeserializer<ImBoolean> {
    @Override
    public ImBoolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ImBoolean(json.getAsBoolean());
    }

    @Override
    public JsonElement serialize(ImBoolean src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.get());
    }
}
