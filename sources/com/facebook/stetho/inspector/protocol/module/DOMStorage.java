package com.facebook.stetho.inspector.protocol.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.facebook.stetho.inspector.console.CLog;
import com.facebook.stetho.inspector.domstorage.DOMStoragePeerManager;
import com.facebook.stetho.inspector.domstorage.SharedPreferencesHelper;
import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;
import com.facebook.stetho.inspector.jsonrpc.JsonRpcPeer;
import com.facebook.stetho.inspector.jsonrpc.JsonRpcResult;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsMethod;
import com.facebook.stetho.inspector.protocol.module.Console.MessageLevel;
import com.facebook.stetho.inspector.protocol.module.Console.MessageSource;
import com.facebook.stetho.json.ObjectMapper;
import com.facebook.stetho.json.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public class DOMStorage implements ChromeDevtoolsDomain {
    private final Context mContext;
    private final DOMStoragePeerManager mDOMStoragePeerManager;
    private final ObjectMapper mObjectMapper = new ObjectMapper();

    private static class DOMStorageAssignmentException extends Exception {
        public DOMStorageAssignmentException(String str) {
            super(str);
        }
    }

    public static class DomStorageItemAddedParams {
        @JsonProperty(required = true)
        public String key;
        @JsonProperty(required = true)
        public String newValue;
        @JsonProperty(required = true)
        public StorageId storageId;
    }

    public static class DomStorageItemRemovedParams {
        @JsonProperty(required = true)
        public String key;
        @JsonProperty(required = true)
        public StorageId storageId;
    }

    public static class DomStorageItemUpdatedParams {
        @JsonProperty(required = true)
        public String key;
        @JsonProperty(required = true)
        public String newValue;
        @JsonProperty(required = true)
        public String oldValue;
        @JsonProperty(required = true)
        public StorageId storageId;
    }

    public static class DomStorageItemsClearedParams {
        @JsonProperty(required = true)
        public StorageId storageId;
    }

    private static class GetDOMStorageItemsResult implements JsonRpcResult {
        @JsonProperty(required = true)
        public List<List<String>> entries;

        private GetDOMStorageItemsResult() {
        }
    }

    public static class StorageId {
        @JsonProperty(required = true)
        public boolean isLocalStorage;
        @JsonProperty(required = true)
        public String securityOrigin;
    }

    public DOMStorage(Context context) {
        this.mContext = context;
        this.mDOMStoragePeerManager = new DOMStoragePeerManager(context);
    }

    @ChromeDevtoolsMethod
    public void enable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        this.mDOMStoragePeerManager.addPeer(jsonRpcPeer);
    }

    @ChromeDevtoolsMethod
    public void disable(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        this.mDOMStoragePeerManager.removePeer(jsonRpcPeer);
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult getDOMStorageItems(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) throws JSONException {
        StorageId storageId = (StorageId) this.mObjectMapper.convertValue(jSONObject.getJSONObject("storageId"), StorageId.class);
        ArrayList arrayList = new ArrayList();
        String str = storageId.securityOrigin;
        if (storageId.isLocalStorage) {
            for (Entry entry : this.mContext.getSharedPreferences(str, 0).getAll().entrySet()) {
                ArrayList arrayList2 = new ArrayList(2);
                arrayList2.add(entry.getKey());
                arrayList2.add(SharedPreferencesHelper.valueToString(entry.getValue()));
                arrayList.add(arrayList2);
            }
        }
        GetDOMStorageItemsResult getDOMStorageItemsResult = new GetDOMStorageItemsResult();
        getDOMStorageItemsResult.entries = arrayList;
        return getDOMStorageItemsResult;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:4|5|6|7|8|9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0042 */
    @ChromeDevtoolsMethod
    public void setDOMStorageItem(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) throws JSONException, JsonRpcException {
        StorageId storageId = (StorageId) this.mObjectMapper.convertValue(jSONObject.getJSONObject("storageId"), StorageId.class);
        String string = jSONObject.getString("key");
        String string2 = jSONObject.getString(BingRule.ACTION_PARAMETER_VALUE);
        if (storageId.isLocalStorage) {
            SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(storageId.securityOrigin, 0);
            Object obj = sharedPreferences.getAll().get(string);
            if (obj != null) {
                try {
                    Editor edit = sharedPreferences.edit();
                    assignByType(edit, string, SharedPreferencesHelper.valueFromString(string2, obj));
                    edit.apply();
                    throw new DOMStorageAssignmentException(String.format(Locale.US, "Type mismatch setting %s to %s (expected %s)", new Object[]{string, string2, obj.getClass().getSimpleName()}));
                } catch (DOMStorageAssignmentException e) {
                    CLog.writeToConsole(this.mDOMStoragePeerManager, MessageLevel.ERROR, MessageSource.STORAGE, e.getMessage());
                    if (sharedPreferences.contains(string)) {
                        this.mDOMStoragePeerManager.signalItemUpdated(storageId, string, string2, SharedPreferencesHelper.valueToString(obj));
                    } else {
                        this.mDOMStoragePeerManager.signalItemRemoved(storageId, string);
                    }
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported: cannot add new key ");
                sb.append(string);
                sb.append(" due to lack of type inference");
                throw new DOMStorageAssignmentException(sb.toString());
            }
        }
    }

    @ChromeDevtoolsMethod
    public void removeDOMStorageItem(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) throws JSONException {
        StorageId storageId = (StorageId) this.mObjectMapper.convertValue(jSONObject.getJSONObject("storageId"), StorageId.class);
        String string = jSONObject.getString("key");
        if (storageId.isLocalStorage) {
            this.mContext.getSharedPreferences(storageId.securityOrigin, 0).edit().remove(string).apply();
        }
    }

    private static void assignByType(Editor editor, String str, Object obj) throws IllegalArgumentException {
        if (obj instanceof Integer) {
            editor.putInt(str, ((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            editor.putLong(str, ((Long) obj).longValue());
        } else if (obj instanceof Float) {
            editor.putFloat(str, ((Float) obj).floatValue());
        } else if (obj instanceof Boolean) {
            editor.putBoolean(str, ((Boolean) obj).booleanValue());
        } else if (obj instanceof String) {
            editor.putString(str, (String) obj);
        } else if (obj instanceof Set) {
            putStringSet(editor, str, (Set) obj);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unsupported type=");
            sb.append(obj.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private static void putStringSet(Editor editor, String str, Set<String> set) {
        editor.putStringSet(str, set);
    }
}
