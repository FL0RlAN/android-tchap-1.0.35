package io.realm;

import android.util.JsonReader;
import android.util.JsonToken;
import fr.gouv.tchap.model.MediaScan;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.exceptions.RealmException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsObjectSchemaInfo.Builder;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmObjectProxy.CacheData;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.objectstore.OsObjectBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class fr_gouv_tchap_model_MediaScanRealmProxy extends MediaScan implements RealmObjectProxy, fr_gouv_tchap_model_MediaScanRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private MediaScanColumnInfo columnInfo;
    private ProxyState<MediaScan> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "MediaScan";
    }

    static final class MediaScanColumnInfo extends ColumnInfo {
        long antiVirusScanDateIndex;
        long antiVirusScanInfoIndex;
        long antiVirusScanStatusIndex;
        long maxColumnIndexValue;
        long urlIndex;

        MediaScanColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(4);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "url";
            this.urlIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = "antiVirusScanStatus";
            this.antiVirusScanStatusIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = "antiVirusScanInfo";
            this.antiVirusScanInfoIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            String str4 = "antiVirusScanDate";
            this.antiVirusScanDateIndex = addColumnDetails(str4, str4, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        MediaScanColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new MediaScanColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            MediaScanColumnInfo mediaScanColumnInfo = (MediaScanColumnInfo) columnInfo;
            MediaScanColumnInfo mediaScanColumnInfo2 = (MediaScanColumnInfo) columnInfo2;
            mediaScanColumnInfo2.urlIndex = mediaScanColumnInfo.urlIndex;
            mediaScanColumnInfo2.antiVirusScanStatusIndex = mediaScanColumnInfo.antiVirusScanStatusIndex;
            mediaScanColumnInfo2.antiVirusScanInfoIndex = mediaScanColumnInfo.antiVirusScanInfoIndex;
            mediaScanColumnInfo2.antiVirusScanDateIndex = mediaScanColumnInfo.antiVirusScanDateIndex;
            mediaScanColumnInfo2.maxColumnIndexValue = mediaScanColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    fr_gouv_tchap_model_MediaScanRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (MediaScanColumnInfo) realmObjectContext.getColumnInfo();
            this.proxyState = new ProxyState<>(this);
            this.proxyState.setRealm$realm(realmObjectContext.getRealm());
            this.proxyState.setRow$realm(realmObjectContext.getRow());
            this.proxyState.setAcceptDefaultValue$realm(realmObjectContext.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(realmObjectContext.getExcludeFields());
        }
    }

    public String realmGet$url() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.urlIndex);
    }

    public void realmSet$url(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            throw new RealmException("Primary key field 'url' cannot be changed after object was created.");
        }
    }

    public String realmGet$antiVirusScanStatus() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.antiVirusScanStatusIndex);
    }

    public void realmSet$antiVirusScanStatus(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.antiVirusScanStatusIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.antiVirusScanStatusIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.antiVirusScanStatusIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.antiVirusScanStatusIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$antiVirusScanInfo() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.antiVirusScanInfoIndex);
    }

    public void realmSet$antiVirusScanInfo(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.antiVirusScanInfoIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.antiVirusScanInfoIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.antiVirusScanInfoIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.antiVirusScanInfoIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public Date realmGet$antiVirusScanDate() {
        this.proxyState.getRealm$realm().checkIfValid();
        if (this.proxyState.getRow$realm().isNull(this.columnInfo.antiVirusScanDateIndex)) {
            return null;
        }
        return this.proxyState.getRow$realm().getDate(this.columnInfo.antiVirusScanDateIndex);
    }

    public void realmSet$antiVirusScanDate(Date date) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (date == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.antiVirusScanDateIndex);
            } else {
                this.proxyState.getRow$realm().setDate(this.columnInfo.antiVirusScanDateIndex, date);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (date == null) {
                row$realm.getTable().setNull(this.columnInfo.antiVirusScanDateIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setDate(this.columnInfo.antiVirusScanDateIndex, row$realm.getIndex(), date, true);
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 4, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("url", RealmFieldType.STRING, true, true, false);
        builder2.addPersistedProperty("antiVirusScanStatus", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("antiVirusScanInfo", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("antiVirusScanDate", RealmFieldType.DATE, false, false, false);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static MediaScanColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new MediaScanColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00d1  */
    public static MediaScan createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        fr_gouv_tchap_model_MediaScanRealmProxy fr_gouv_tchap_model_mediascanrealmproxy;
        String str;
        String str2;
        String str3;
        long j;
        List emptyList = Collections.emptyList();
        String str4 = "url";
        if (z) {
            Table table = realm.getTable(MediaScan.class);
            long j2 = ((MediaScanColumnInfo) realm.getSchema().getColumnInfo(MediaScan.class)).urlIndex;
            if (jSONObject.isNull(str4)) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, jSONObject.getString(str4));
            }
            if (j != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(j), realm.getSchema().getColumnInfo(MediaScan.class), false, Collections.emptyList());
                    fr_gouv_tchap_model_mediascanrealmproxy = new fr_gouv_tchap_model_MediaScanRealmProxy();
                    if (fr_gouv_tchap_model_mediascanrealmproxy == null) {
                        if (!jSONObject.has(str4)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'url'.");
                        } else if (jSONObject.isNull(str4)) {
                            fr_gouv_tchap_model_mediascanrealmproxy = (fr_gouv_tchap_model_MediaScanRealmProxy) realm.createObjectInternal(MediaScan.class, null, true, emptyList);
                        } else {
                            fr_gouv_tchap_model_mediascanrealmproxy = (fr_gouv_tchap_model_MediaScanRealmProxy) realm.createObjectInternal(MediaScan.class, jSONObject.getString(str4), true, emptyList);
                        }
                    }
                    fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = fr_gouv_tchap_model_mediascanrealmproxy;
                    str = "antiVirusScanStatus";
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanStatus(null);
                        } else {
                            fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanStatus(jSONObject.getString(str));
                        }
                    }
                    str2 = "antiVirusScanInfo";
                    if (jSONObject.has(str2)) {
                        if (jSONObject.isNull(str2)) {
                            fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanInfo(null);
                        } else {
                            fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanInfo(jSONObject.getString(str2));
                        }
                    }
                    str3 = "antiVirusScanDate";
                    if (jSONObject.has(str3)) {
                        if (jSONObject.isNull(str3)) {
                            fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanDate(null);
                        } else {
                            Object obj = jSONObject.get(str3);
                            if (obj instanceof String) {
                                fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanDate(JsonUtils.stringToDate((String) obj));
                            } else {
                                fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanDate(new Date(jSONObject.getLong(str3)));
                            }
                        }
                    }
                    return fr_gouv_tchap_model_mediascanrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        fr_gouv_tchap_model_mediascanrealmproxy = null;
        if (fr_gouv_tchap_model_mediascanrealmproxy == null) {
        }
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface2 = fr_gouv_tchap_model_mediascanrealmproxy;
        str = "antiVirusScanStatus";
        if (jSONObject.has(str)) {
        }
        str2 = "antiVirusScanInfo";
        if (jSONObject.has(str2)) {
        }
        str3 = "antiVirusScanDate";
        if (jSONObject.has(str3)) {
        }
        return fr_gouv_tchap_model_mediascanrealmproxy;
    }

    public static MediaScan createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        MediaScan mediaScan = new MediaScan();
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("url")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$url(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$url(null);
                }
                z = true;
            } else if (nextName.equals("antiVirusScanStatus")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanStatus(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanStatus(null);
                }
            } else if (nextName.equals("antiVirusScanInfo")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanInfo(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanInfo(null);
                }
            } else if (!nextName.equals("antiVirusScanDate")) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.skipValue();
                fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanDate(null);
            } else if (jsonReader.peek() == JsonToken.NUMBER) {
                long nextLong = jsonReader.nextLong();
                if (nextLong > -1) {
                    fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanDate(new Date(nextLong));
                }
            } else {
                fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanDate(JsonUtils.stringToDate(jsonReader.nextString()));
            }
        }
        jsonReader.endObject();
        if (z) {
            return (MediaScan) realm.copyToRealm(mediaScan, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'url'.");
    }

    private static fr_gouv_tchap_model_MediaScanRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(MediaScan.class), false, Collections.emptyList());
        fr_gouv_tchap_model_MediaScanRealmProxy fr_gouv_tchap_model_mediascanrealmproxy = new fr_gouv_tchap_model_MediaScanRealmProxy();
        realmObjectContext.clear();
        return fr_gouv_tchap_model_mediascanrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a1  */
    public static MediaScan copyOrUpdate(Realm realm, MediaScanColumnInfo mediaScanColumnInfo, MediaScan mediaScan, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        long j;
        if (mediaScan instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) mediaScan;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return mediaScan;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(mediaScan);
        if (realmObjectProxy2 != null) {
            return (MediaScan) realmObjectProxy2;
        }
        fr_gouv_tchap_model_MediaScanRealmProxy fr_gouv_tchap_model_mediascanrealmproxy = null;
        if (z) {
            Table table = realm.getTable(MediaScan.class);
            long j2 = mediaScanColumnInfo.urlIndex;
            String realmGet$url = mediaScan.realmGet$url();
            if (realmGet$url == null) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, realmGet$url);
            }
            if (j == -1) {
                z2 = false;
                return !z2 ? update(realm, mediaScanColumnInfo, fr_gouv_tchap_model_mediascanrealmproxy, mediaScan, map, set) : copy(realm, mediaScanColumnInfo, mediaScan, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(j), mediaScanColumnInfo, false, Collections.emptyList());
                fr_gouv_tchap_model_mediascanrealmproxy = new fr_gouv_tchap_model_MediaScanRealmProxy();
                map.put(mediaScan, fr_gouv_tchap_model_mediascanrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, mediaScanColumnInfo, fr_gouv_tchap_model_mediascanrealmproxy, mediaScan, map, set) : copy(realm, mediaScanColumnInfo, mediaScan, z, map, set);
    }

    public static MediaScan copy(Realm realm, MediaScanColumnInfo mediaScanColumnInfo, MediaScan mediaScan, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(mediaScan);
        if (realmObjectProxy != null) {
            return (MediaScan) realmObjectProxy;
        }
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(MediaScan.class), mediaScanColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(mediaScanColumnInfo.urlIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$url());
        osObjectBuilder.addString(mediaScanColumnInfo.antiVirusScanStatusIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanStatus());
        osObjectBuilder.addString(mediaScanColumnInfo.antiVirusScanInfoIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanInfo());
        osObjectBuilder.addDate(mediaScanColumnInfo.antiVirusScanDateIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanDate());
        fr_gouv_tchap_model_MediaScanRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(mediaScan, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, MediaScan mediaScan, Map<RealmModel, Long> map) {
        long j;
        long j2;
        MediaScan mediaScan2 = mediaScan;
        if (mediaScan2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) mediaScan2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(MediaScan.class);
        long nativePtr = table.getNativePtr();
        MediaScanColumnInfo mediaScanColumnInfo = (MediaScanColumnInfo) realm.getSchema().getColumnInfo(MediaScan.class);
        long j3 = mediaScanColumnInfo.urlIndex;
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan2;
        String realmGet$url = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$url();
        if (realmGet$url == null) {
            j = Table.nativeFindFirstNull(nativePtr, j3);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j3, realmGet$url);
        }
        if (j == -1) {
            j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$url);
        } else {
            Table.throwDuplicatePrimaryKeyException(realmGet$url);
            j2 = j;
        }
        map.put(mediaScan2, Long.valueOf(j2));
        String realmGet$antiVirusScanStatus = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanStatus();
        if (realmGet$antiVirusScanStatus != null) {
            Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanStatusIndex, j2, realmGet$antiVirusScanStatus, false);
        }
        String realmGet$antiVirusScanInfo = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanInfo();
        if (realmGet$antiVirusScanInfo != null) {
            Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanInfoIndex, j2, realmGet$antiVirusScanInfo, false);
        }
        Date realmGet$antiVirusScanDate = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanDate();
        if (realmGet$antiVirusScanDate != null) {
            Table.nativeSetTimestamp(nativePtr, mediaScanColumnInfo.antiVirusScanDateIndex, j2, realmGet$antiVirusScanDate.getTime(), false);
        }
        return j2;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        long j3;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(MediaScan.class);
        long nativePtr = table.getNativePtr();
        MediaScanColumnInfo mediaScanColumnInfo = (MediaScanColumnInfo) realm.getSchema().getColumnInfo(MediaScan.class);
        long j4 = mediaScanColumnInfo.urlIndex;
        while (it.hasNext()) {
            MediaScan mediaScan = (MediaScan) it.next();
            if (!map2.containsKey(mediaScan)) {
                if (mediaScan instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) mediaScan;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(mediaScan, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan;
                String realmGet$url = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$url();
                if (realmGet$url == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j4);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j4, realmGet$url);
                }
                if (j == -1) {
                    j2 = OsObject.createRowWithPrimaryKey(table, j4, realmGet$url);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$url);
                    j2 = j;
                }
                map2.put(mediaScan, Long.valueOf(j2));
                String realmGet$antiVirusScanStatus = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanStatus();
                if (realmGet$antiVirusScanStatus != null) {
                    j3 = j4;
                    Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanStatusIndex, j2, realmGet$antiVirusScanStatus, false);
                } else {
                    j3 = j4;
                }
                String realmGet$antiVirusScanInfo = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanInfo();
                if (realmGet$antiVirusScanInfo != null) {
                    Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanInfoIndex, j2, realmGet$antiVirusScanInfo, false);
                }
                Date realmGet$antiVirusScanDate = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanDate();
                if (realmGet$antiVirusScanDate != null) {
                    Table.nativeSetTimestamp(nativePtr, mediaScanColumnInfo.antiVirusScanDateIndex, j2, realmGet$antiVirusScanDate.getTime(), false);
                }
                j4 = j3;
            }
        }
    }

    public static long insertOrUpdate(Realm realm, MediaScan mediaScan, Map<RealmModel, Long> map) {
        long j;
        MediaScan mediaScan2 = mediaScan;
        if (mediaScan2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) mediaScan2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(MediaScan.class);
        long nativePtr = table.getNativePtr();
        MediaScanColumnInfo mediaScanColumnInfo = (MediaScanColumnInfo) realm.getSchema().getColumnInfo(MediaScan.class);
        long j2 = mediaScanColumnInfo.urlIndex;
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan2;
        String realmGet$url = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$url();
        if (realmGet$url == null) {
            j = Table.nativeFindFirstNull(nativePtr, j2);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j2, realmGet$url);
        }
        long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$url) : j;
        map.put(mediaScan2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$antiVirusScanStatus = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanStatus();
        if (realmGet$antiVirusScanStatus != null) {
            Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanStatusIndex, createRowWithPrimaryKey, realmGet$antiVirusScanStatus, false);
        } else {
            Table.nativeSetNull(nativePtr, mediaScanColumnInfo.antiVirusScanStatusIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$antiVirusScanInfo = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanInfo();
        if (realmGet$antiVirusScanInfo != null) {
            Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanInfoIndex, createRowWithPrimaryKey, realmGet$antiVirusScanInfo, false);
        } else {
            Table.nativeSetNull(nativePtr, mediaScanColumnInfo.antiVirusScanInfoIndex, createRowWithPrimaryKey, false);
        }
        Date realmGet$antiVirusScanDate = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanDate();
        if (realmGet$antiVirusScanDate != null) {
            Table.nativeSetTimestamp(nativePtr, mediaScanColumnInfo.antiVirusScanDateIndex, createRowWithPrimaryKey, realmGet$antiVirusScanDate.getTime(), false);
        } else {
            Table.nativeSetNull(nativePtr, mediaScanColumnInfo.antiVirusScanDateIndex, createRowWithPrimaryKey, false);
        }
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(MediaScan.class);
        long nativePtr = table.getNativePtr();
        MediaScanColumnInfo mediaScanColumnInfo = (MediaScanColumnInfo) realm.getSchema().getColumnInfo(MediaScan.class);
        long j3 = mediaScanColumnInfo.urlIndex;
        while (it.hasNext()) {
            MediaScan mediaScan = (MediaScan) it.next();
            if (!map2.containsKey(mediaScan)) {
                if (mediaScan instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) mediaScan;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(mediaScan, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan;
                String realmGet$url = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$url();
                if (realmGet$url == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j3);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j3, realmGet$url);
                }
                long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j3, realmGet$url) : j;
                map2.put(mediaScan, Long.valueOf(createRowWithPrimaryKey));
                String realmGet$antiVirusScanStatus = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanStatus();
                if (realmGet$antiVirusScanStatus != null) {
                    j2 = j3;
                    Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanStatusIndex, createRowWithPrimaryKey, realmGet$antiVirusScanStatus, false);
                } else {
                    j2 = j3;
                    Table.nativeSetNull(nativePtr, mediaScanColumnInfo.antiVirusScanStatusIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$antiVirusScanInfo = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanInfo();
                if (realmGet$antiVirusScanInfo != null) {
                    Table.nativeSetString(nativePtr, mediaScanColumnInfo.antiVirusScanInfoIndex, createRowWithPrimaryKey, realmGet$antiVirusScanInfo, false);
                } else {
                    Table.nativeSetNull(nativePtr, mediaScanColumnInfo.antiVirusScanInfoIndex, createRowWithPrimaryKey, false);
                }
                Date realmGet$antiVirusScanDate = fr_gouv_tchap_model_mediascanrealmproxyinterface.realmGet$antiVirusScanDate();
                if (realmGet$antiVirusScanDate != null) {
                    Table.nativeSetTimestamp(nativePtr, mediaScanColumnInfo.antiVirusScanDateIndex, createRowWithPrimaryKey, realmGet$antiVirusScanDate.getTime(), false);
                } else {
                    Table.nativeSetNull(nativePtr, mediaScanColumnInfo.antiVirusScanDateIndex, createRowWithPrimaryKey, false);
                }
                j3 = j2;
            }
        }
    }

    public static MediaScan createDetachedCopy(MediaScan mediaScan, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        MediaScan mediaScan2;
        if (i > i2 || mediaScan == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(mediaScan);
        if (cacheData == null) {
            mediaScan2 = new MediaScan();
            map.put(mediaScan, new CacheData(i, mediaScan2));
        } else if (i >= cacheData.minDepth) {
            return (MediaScan) cacheData.object;
        } else {
            MediaScan mediaScan3 = (MediaScan) cacheData.object;
            cacheData.minDepth = i;
            mediaScan2 = mediaScan3;
        }
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan2;
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface2 = mediaScan;
        fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$url(fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$url());
        fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanStatus(fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$antiVirusScanStatus());
        fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanInfo(fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$antiVirusScanInfo());
        fr_gouv_tchap_model_mediascanrealmproxyinterface.realmSet$antiVirusScanDate(fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$antiVirusScanDate());
        return mediaScan2;
    }

    static MediaScan update(Realm realm, MediaScanColumnInfo mediaScanColumnInfo, MediaScan mediaScan, MediaScan mediaScan2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface = mediaScan;
        fr_gouv_tchap_model_MediaScanRealmProxyInterface fr_gouv_tchap_model_mediascanrealmproxyinterface2 = mediaScan2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(MediaScan.class), mediaScanColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(mediaScanColumnInfo.urlIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$url());
        osObjectBuilder.addString(mediaScanColumnInfo.antiVirusScanStatusIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$antiVirusScanStatus());
        osObjectBuilder.addString(mediaScanColumnInfo.antiVirusScanInfoIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$antiVirusScanInfo());
        osObjectBuilder.addDate(mediaScanColumnInfo.antiVirusScanDateIndex, fr_gouv_tchap_model_mediascanrealmproxyinterface2.realmGet$antiVirusScanDate());
        osObjectBuilder.updateExistingObject();
        return mediaScan;
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.util.Date] */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("MediaScan = proxy[");
        sb.append("{url:");
        Object obj = "null";
        sb.append(realmGet$url() != null ? realmGet$url() : obj);
        String str = "}";
        sb.append(str);
        String str2 = ",";
        sb.append(str2);
        sb.append("{antiVirusScanStatus:");
        sb.append(realmGet$antiVirusScanStatus() != null ? realmGet$antiVirusScanStatus() : obj);
        sb.append(str);
        sb.append(str2);
        sb.append("{antiVirusScanInfo:");
        sb.append(realmGet$antiVirusScanInfo() != null ? realmGet$antiVirusScanInfo() : obj);
        sb.append(str);
        sb.append(str2);
        sb.append("{antiVirusScanDate:");
        if (realmGet$antiVirusScanDate() != null) {
            obj = realmGet$antiVirusScanDate();
        }
        sb.append(obj);
        sb.append(str);
        sb.append("]");
        return sb.toString();
    }

    public ProxyState<?> realmGet$proxyState() {
        return this.proxyState;
    }

    public int hashCode() {
        String path = this.proxyState.getRealm$realm().getPath();
        String name = this.proxyState.getRow$realm().getTable().getName();
        long index = this.proxyState.getRow$realm().getIndex();
        int i = 0;
        int hashCode = (527 + (path != null ? path.hashCode() : 0)) * 31;
        if (name != null) {
            i = name.hashCode();
        }
        return ((hashCode + i) * 31) + ((int) ((index >>> 32) ^ index));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        fr_gouv_tchap_model_MediaScanRealmProxy fr_gouv_tchap_model_mediascanrealmproxy = (fr_gouv_tchap_model_MediaScanRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = fr_gouv_tchap_model_mediascanrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = fr_gouv_tchap_model_mediascanrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == fr_gouv_tchap_model_mediascanrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
