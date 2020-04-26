package org.matrix.androidsdk.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

public class JsonUtility {
    private static final String LOG_TAG = JsonUtility.class.getSimpleName();
    private static final Gson basicGson = new Gson();
    private static final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(2, 8).create();
    private static final Gson gsonWithNullSerialization = new GsonBuilder().excludeFieldsWithModifiers(2, 8).serializeNulls().create();
    private static final Gson gsonWithoutHtmlEscaping = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithModifiers(2, 8).create();

    public static JsonElement canonicalize(JsonElement jsonElement) {
        if (jsonElement == null) {
            return null;
        }
        if (jsonElement instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) jsonElement;
            JsonArray jsonArray2 = new JsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonArray2.add(canonicalize(jsonArray.get(i)));
            }
            return jsonArray2;
        } else if (!(jsonElement instanceof JsonObject)) {
            return jsonElement;
        } else {
            JsonObject jsonObject = (JsonObject) jsonElement;
            JsonObject jsonObject2 = new JsonObject();
            TreeSet treeSet = new TreeSet();
            for (Entry key : jsonObject.entrySet()) {
                treeSet.add(key.getKey());
            }
            Iterator it = treeSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                jsonObject2.add(str, canonicalize(jsonObject.get(str)));
            }
            return jsonObject2;
        }
    }

    public static String getCanonicalizedJsonString(Object obj) {
        String str;
        if (obj == null) {
            return null;
        }
        if (obj instanceof JsonElement) {
            str = gsonWithoutHtmlEscaping.toJson(canonicalize((JsonElement) obj));
        } else {
            Gson gson2 = gsonWithoutHtmlEscaping;
            str = gson2.toJson(canonicalize(gson2.toJsonTree(obj)));
        }
        return str != null ? str.replace("\\/", "/") : str;
    }

    public static Gson getBasicGson() {
        return basicGson;
    }

    public static Gson getGson(boolean z) {
        return z ? gsonWithNullSerialization : gson;
    }

    public static <T> T toClass(JsonElement jsonElement, Class<T> cls) {
        T t;
        String str = "## toClass failed ";
        try {
            t = gson.fromJson(jsonElement, cls);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            t = null;
        }
        if (t != null) {
            return t;
        }
        try {
            return cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable th) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(th.getMessage());
            Log.e(str3, sb2.toString(), th);
            return t;
        }
    }

    public static <T> T toClass(String str, Class<T> cls) {
        T t;
        String str2 = "## toClass failed ";
        try {
            t = gson.fromJson(str, cls);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
            t = null;
        }
        if (t != null) {
            return t;
        }
        try {
            return cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable th) {
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(th.getMessage());
            Log.e(str4, sb2.toString(), th);
            return t;
        }
    }
}
