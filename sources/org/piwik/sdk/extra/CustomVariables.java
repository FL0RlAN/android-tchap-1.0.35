package org.piwik.sdk.extra;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.QueryParams;
import org.piwik.sdk.TrackMe;
import timber.log.Timber;

public class CustomVariables {
    private static final String LOGGER_TAG = "PIWIK:CustomVariables";
    protected static final int MAX_LENGTH = 200;
    private final Map<String, JSONArray> mVars = new ConcurrentHashMap();

    public CustomVariables() {
    }

    public CustomVariables(CustomVariables customVariables) {
        this.mVars.putAll(customVariables.mVars);
    }

    public CustomVariables(String str) {
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str2 = (String) keys.next();
                    put(str2, jSONObject.getJSONArray(str2));
                }
            } catch (JSONException e) {
                Timber.tag("ContentValues").e(e, "Failed to create CustomVariables from JSON", new Object[0]);
            }
        }
    }

    public CustomVariables putAll(CustomVariables customVariables) {
        this.mVars.putAll(customVariables.mVars);
        return this;
    }

    public CustomVariables put(int i, String str, String str2) {
        String str3 = LOGGER_TAG;
        if (i > 0) {
            if ((str != null) && (str2 != null)) {
                if (str.length() > 200) {
                    Timber.tag(str3).w("Name is too long %s", str);
                    str = str.substring(0, 200);
                }
                if (str2.length() > 200) {
                    Timber.tag(str3).w("Value is too long %s", str2);
                    str2 = str2.substring(0, 200);
                }
                put(Integer.toString(i), new JSONArray(Arrays.asList(new String[]{str, str2})));
                return this;
            }
        }
        Timber.tag(str3).w("Index is out of range or name/value is null", new Object[0]);
        return this;
    }

    public CustomVariables put(String str, JSONArray jSONArray) {
        if (jSONArray.length() != 2 || str == null) {
            Timber.tag(LOGGER_TAG).w("values.length() should be equal 2", new Object[0]);
        } else {
            this.mVars.put(str, jSONArray);
        }
        return this;
    }

    public String toString() {
        JSONObject jSONObject = new JSONObject(this.mVars);
        if (jSONObject.length() > 0) {
            return jSONObject.toString();
        }
        return null;
    }

    public int size() {
        return this.mVars.size();
    }

    public TrackMe injectVisitVariables(TrackMe trackMe) {
        trackMe.set(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES, toString());
        return trackMe;
    }

    public TrackMe toVisitVariables() {
        return injectVisitVariables(new TrackMe());
    }
}
