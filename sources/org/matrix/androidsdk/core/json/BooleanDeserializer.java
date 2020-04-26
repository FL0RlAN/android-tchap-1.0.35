package org.matrix.androidsdk.core.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import org.matrix.androidsdk.core.Log;

public class BooleanDeserializer implements JsonDeserializer<Boolean> {
    private static final String LOG_TAG = BooleanDeserializer.class.getSimpleName();
    private final boolean mCanReturnNull;

    public BooleanDeserializer(boolean z) {
        this.mCanReturnNull = z;
    }

    public Boolean deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        boolean isJsonPrimitive = jsonElement.isJsonPrimitive();
        boolean z = false;
        Boolean valueOf = Boolean.valueOf(false);
        if (isJsonPrimitive) {
            JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (asJsonPrimitive.isBoolean()) {
                return Boolean.valueOf(asJsonPrimitive.getAsBoolean());
            }
            if (asJsonPrimitive.isNumber()) {
                Log.w(LOG_TAG, "Boolean detected as a number");
                if (asJsonPrimitive.getAsInt() == 1) {
                    z = true;
                }
                return Boolean.valueOf(z);
            } else if (asJsonPrimitive.isString()) {
                Log.w(LOG_TAG, "Boolean detected as a string");
                String asString = asJsonPrimitive.getAsString();
                if ("1".equals(asString) || "true".equals(asString)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            } else {
                Log.e(LOG_TAG, "Unknown primitive");
                if (this.mCanReturnNull) {
                    return null;
                }
                return valueOf;
            }
        } else if (!jsonElement.isJsonNull()) {
            Log.w(LOG_TAG, "Boolean detected as not a primitive type");
            if (this.mCanReturnNull) {
                return null;
            }
            return valueOf;
        } else if (this.mCanReturnNull) {
            return null;
        } else {
            Log.w(LOG_TAG, "Boolean is null, but not allowed to return null");
            return valueOf;
        }
    }
}
