package com.backend.legisloop.serial;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toLocalDate().format(FORMATTER)); // Convert SQL Date to String
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	Date ret;
        try {
        	ret = Date.valueOf(LocalDate.parse(json.getAsString(), FORMATTER)); // Convert String to SQL Date
        } catch (Exception e) {
        	ret = null;
        }
        return ret;
    }
}
