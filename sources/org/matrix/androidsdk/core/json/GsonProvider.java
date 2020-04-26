package org.matrix.androidsdk.core.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.matrix.androidsdk.rest.json.MatrixFieldNamingStrategy;

public class GsonProvider {
    private static Gson gson = new GsonBuilder().setFieldNamingStrategy(new MatrixFieldNamingStrategy()).excludeFieldsWithModifiers(2, 8).registerTypeAdapter(Boolean.TYPE, new BooleanDeserializer(false)).registerTypeAdapter(Boolean.class, new BooleanDeserializer(true)).create();

    public static Gson provideGson() {
        return gson;
    }
}
