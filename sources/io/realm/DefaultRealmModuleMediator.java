package io.realm;

import android.util.JsonReader;
import fr.gouv.tchap.model.MediaScan;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.annotations.RealmModule;
import io.realm.fr_gouv_tchap_model_MediaScanRealmProxy.ClassNameHelper;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmObjectProxy.CacheData;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {
    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;

    public boolean transformerApplied() {
        return true;
    }

    DefaultRealmModuleMediator() {
    }

    static {
        HashSet hashSet = new HashSet(1);
        hashSet.add(MediaScan.class);
        MODEL_CLASSES = Collections.unmodifiableSet(hashSet);
    }

    public Map<Class<? extends RealmModel>, OsObjectSchemaInfo> getExpectedObjectSchemaInfoMap() {
        HashMap hashMap = new HashMap(1);
        hashMap.put(MediaScan.class, fr_gouv_tchap_model_MediaScanRealmProxy.getExpectedObjectSchemaInfo());
        return hashMap;
    }

    public ColumnInfo createColumnInfo(Class<? extends RealmModel> cls, OsSchemaInfo osSchemaInfo) {
        checkClass(cls);
        if (cls.equals(MediaScan.class)) {
            return fr_gouv_tchap_model_MediaScanRealmProxy.createColumnInfo(osSchemaInfo);
        }
        throw getMissingProxyClassException(cls);
    }

    public String getSimpleClassNameImpl(Class<? extends RealmModel> cls) {
        checkClass(cls);
        if (cls.equals(MediaScan.class)) {
            return ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        throw getMissingProxyClassException(cls);
    }

    public <E extends RealmModel> E newInstance(Class<E> cls, Object obj, Row row, ColumnInfo columnInfo, boolean z, List<String> list) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        try {
            realmObjectContext.set((BaseRealm) obj, row, columnInfo, z, list);
            checkClass(cls);
            if (cls.equals(MediaScan.class)) {
                return (RealmModel) cls.cast(new fr_gouv_tchap_model_MediaScanRealmProxy());
            }
            throw getMissingProxyClassException(cls);
        } finally {
            realmObjectContext.clear();
        }
    }

    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    public <E extends RealmModel> E copyOrUpdate(Realm realm, E e, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        Class superclass = e instanceof RealmObjectProxy ? e.getClass().getSuperclass() : e.getClass();
        if (superclass.equals(MediaScan.class)) {
            return (RealmModel) superclass.cast(fr_gouv_tchap_model_MediaScanRealmProxy.copyOrUpdate(realm, (MediaScanColumnInfo) realm.getSchema().getColumnInfo(MediaScan.class), (MediaScan) e, z, map, set));
        }
        throw getMissingProxyClassException(superclass);
    }

    public void insert(Realm realm, RealmModel realmModel, Map<RealmModel, Long> map) {
        Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
        if (superclass.equals(MediaScan.class)) {
            fr_gouv_tchap_model_MediaScanRealmProxy.insert(realm, (MediaScan) realmModel, map);
            return;
        }
        throw getMissingProxyClassException(superclass);
    }

    public void insert(Realm realm, Collection<? extends RealmModel> collection) {
        Iterator it = collection.iterator();
        HashMap hashMap = new HashMap(collection.size());
        if (it.hasNext()) {
            RealmModel realmModel = (RealmModel) it.next();
            Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
            if (superclass.equals(MediaScan.class)) {
                fr_gouv_tchap_model_MediaScanRealmProxy.insert(realm, (MediaScan) realmModel, (Map<RealmModel, Long>) hashMap);
                if (!it.hasNext()) {
                    return;
                }
                if (superclass.equals(MediaScan.class)) {
                    fr_gouv_tchap_model_MediaScanRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
                    return;
                }
                throw getMissingProxyClassException(superclass);
            }
            throw getMissingProxyClassException(superclass);
        }
    }

    public void insertOrUpdate(Realm realm, RealmModel realmModel, Map<RealmModel, Long> map) {
        Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
        if (superclass.equals(MediaScan.class)) {
            fr_gouv_tchap_model_MediaScanRealmProxy.insertOrUpdate(realm, (MediaScan) realmModel, map);
            return;
        }
        throw getMissingProxyClassException(superclass);
    }

    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> collection) {
        Iterator it = collection.iterator();
        HashMap hashMap = new HashMap(collection.size());
        if (it.hasNext()) {
            RealmModel realmModel = (RealmModel) it.next();
            Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
            if (superclass.equals(MediaScan.class)) {
                fr_gouv_tchap_model_MediaScanRealmProxy.insertOrUpdate(realm, (MediaScan) realmModel, (Map<RealmModel, Long>) hashMap);
                if (!it.hasNext()) {
                    return;
                }
                if (superclass.equals(MediaScan.class)) {
                    fr_gouv_tchap_model_MediaScanRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
                    return;
                }
                throw getMissingProxyClassException(superclass);
            }
            throw getMissingProxyClassException(superclass);
        }
    }

    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> cls, Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        checkClass(cls);
        if (cls.equals(MediaScan.class)) {
            return (RealmModel) cls.cast(fr_gouv_tchap_model_MediaScanRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        throw getMissingProxyClassException(cls);
    }

    public <E extends RealmModel> E createUsingJsonStream(Class<E> cls, Realm realm, JsonReader jsonReader) throws IOException {
        checkClass(cls);
        if (cls.equals(MediaScan.class)) {
            return (RealmModel) cls.cast(fr_gouv_tchap_model_MediaScanRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        throw getMissingProxyClassException(cls);
    }

    public <E extends RealmModel> E createDetachedCopy(E e, int i, Map<RealmModel, CacheData<RealmModel>> map) {
        Class superclass = e.getClass().getSuperclass();
        if (superclass.equals(MediaScan.class)) {
            return (RealmModel) superclass.cast(fr_gouv_tchap_model_MediaScanRealmProxy.createDetachedCopy((MediaScan) e, 0, i, map));
        }
        throw getMissingProxyClassException(superclass);
    }
}
