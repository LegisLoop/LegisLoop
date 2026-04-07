package com.backend.legisloop.serial;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BooleanSerializer implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

    @Override
    public JsonElement serialize(Boolean arg0, Type arg1, JsonSerializationContext arg2) {
        return new JsonPrimitive(arg0 ? 1 : 0);
    }

    @Override
    /**
     * Turn 1 or 0 into a boolean for {@link RollCall#passed}
     */
    public Boolean deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) {
        return arg0.getAsInt() == 1;
    }
}
