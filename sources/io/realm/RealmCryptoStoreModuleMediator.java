package io.realm;

import android.util.JsonReader;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.annotations.RealmModule;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmObjectProxy.CacheData;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.ClassNameHelper;
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
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.IncomingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;

@RealmModule
class RealmCryptoStoreModuleMediator extends RealmProxyMediator {
    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;

    public boolean transformerApplied() {
        return true;
    }

    RealmCryptoStoreModuleMediator() {
    }

    static {
        HashSet hashSet = new HashSet(9);
        hashSet.add(CryptoRoomEntity.class);
        hashSet.add(OlmInboundGroupSessionEntity.class);
        hashSet.add(OutgoingRoomKeyRequestEntity.class);
        hashSet.add(KeysBackupDataEntity.class);
        hashSet.add(UserEntity.class);
        hashSet.add(DeviceInfoEntity.class);
        hashSet.add(CryptoMetadataEntity.class);
        hashSet.add(IncomingRoomKeyRequestEntity.class);
        hashSet.add(OlmSessionEntity.class);
        MODEL_CLASSES = Collections.unmodifiableSet(hashSet);
    }

    public Map<Class<? extends RealmModel>, OsObjectSchemaInfo> getExpectedObjectSchemaInfoMap() {
        HashMap hashMap = new HashMap(9);
        hashMap.put(CryptoRoomEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(OlmInboundGroupSessionEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(OutgoingRoomKeyRequestEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(KeysBackupDataEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(UserEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(DeviceInfoEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(CryptoMetadataEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(IncomingRoomKeyRequestEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.getExpectedObjectSchemaInfo());
        hashMap.put(OlmSessionEntity.class, org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.getExpectedObjectSchemaInfo());
        return hashMap;
    }

    public ColumnInfo createColumnInfo(Class<? extends RealmModel> cls, OsSchemaInfo osSchemaInfo) {
        checkClass(cls);
        if (cls.equals(CryptoRoomEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(OlmInboundGroupSessionEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(OutgoingRoomKeyRequestEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(KeysBackupDataEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(UserEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(DeviceInfoEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(CryptoMetadataEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(IncomingRoomKeyRequestEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        if (cls.equals(OlmSessionEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.createColumnInfo(osSchemaInfo);
        }
        throw getMissingProxyClassException(cls);
    }

    public String getSimpleClassNameImpl(Class<? extends RealmModel> cls) {
        checkClass(cls);
        if (cls.equals(CryptoRoomEntity.class)) {
            return ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(OlmInboundGroupSessionEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(OutgoingRoomKeyRequestEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(KeysBackupDataEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(UserEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(DeviceInfoEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(CryptoMetadataEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(IncomingRoomKeyRequestEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        if (cls.equals(OlmSessionEntity.class)) {
            return org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME;
        }
        throw getMissingProxyClassException(cls);
    }

    public <E extends RealmModel> E newInstance(Class<E> cls, Object obj, Row row, ColumnInfo columnInfo, boolean z, List<String> list) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        try {
            realmObjectContext.set((BaseRealm) obj, row, columnInfo, z, list);
            checkClass(cls);
            if (cls.equals(CryptoRoomEntity.class)) {
                return (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy());
            }
            if (cls.equals(OlmInboundGroupSessionEntity.class)) {
                E e = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy());
                realmObjectContext.clear();
                return e;
            } else if (cls.equals(OutgoingRoomKeyRequestEntity.class)) {
                E e2 = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy());
                realmObjectContext.clear();
                return e2;
            } else if (cls.equals(KeysBackupDataEntity.class)) {
                E e3 = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy());
                realmObjectContext.clear();
                return e3;
            } else if (cls.equals(UserEntity.class)) {
                E e4 = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy());
                realmObjectContext.clear();
                return e4;
            } else if (cls.equals(DeviceInfoEntity.class)) {
                E e5 = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy());
                realmObjectContext.clear();
                return e5;
            } else if (cls.equals(CryptoMetadataEntity.class)) {
                E e6 = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy());
                realmObjectContext.clear();
                return e6;
            } else if (cls.equals(IncomingRoomKeyRequestEntity.class)) {
                E e7 = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy());
                realmObjectContext.clear();
                return e7;
            } else if (cls.equals(OlmSessionEntity.class)) {
                E e8 = (RealmModel) cls.cast(new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy());
                realmObjectContext.clear();
                return e8;
            } else {
                throw getMissingProxyClassException(cls);
            }
        } finally {
            realmObjectContext.clear();
        }
    }

    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    public <E extends RealmModel> E copyOrUpdate(Realm realm, E e, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        Class superclass = e instanceof RealmObjectProxy ? e.getClass().getSuperclass() : e.getClass();
        if (superclass.equals(CryptoRoomEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.copyOrUpdate(realm, (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoRoomEntity.class), (CryptoRoomEntity) e, z, map, set));
        } else if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.copyOrUpdate(realm, (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class), (OlmInboundGroupSessionEntity) e, z, map, set));
        } else if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.copyOrUpdate(realm, (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class), (OutgoingRoomKeyRequestEntity) e, z, map, set));
        } else if (superclass.equals(KeysBackupDataEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.copyOrUpdate(realm, (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(KeysBackupDataEntity.class), (KeysBackupDataEntity) e, z, map, set));
        } else if (superclass.equals(UserEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.copyOrUpdate(realm, (UserEntityColumnInfo) realm.getSchema().getColumnInfo(UserEntity.class), (UserEntity) e, z, map, set));
        } else if (superclass.equals(DeviceInfoEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.copyOrUpdate(realm, (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class), (DeviceInfoEntity) e, z, map, set));
        } else if (superclass.equals(CryptoMetadataEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.copyOrUpdate(realm, (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoMetadataEntity.class), (CryptoMetadataEntity) e, z, map, set));
        } else if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.copyOrUpdate(realm, (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(IncomingRoomKeyRequestEntity.class), (IncomingRoomKeyRequestEntity) e, z, map, set));
        } else if (superclass.equals(OlmSessionEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.copyOrUpdate(realm, (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmSessionEntity.class), (OlmSessionEntity) e, z, map, set));
        } else {
            throw getMissingProxyClassException(superclass);
        }
    }

    public void insert(Realm realm, RealmModel realmModel, Map<RealmModel, Long> map) {
        Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
        if (superclass.equals(CryptoRoomEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.insert(realm, (CryptoRoomEntity) realmModel, map);
        } else if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insert(realm, (OlmInboundGroupSessionEntity) realmModel, map);
        } else if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insert(realm, (OutgoingRoomKeyRequestEntity) realmModel, map);
        } else if (superclass.equals(KeysBackupDataEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insert(realm, (KeysBackupDataEntity) realmModel, map);
        } else if (superclass.equals(UserEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.insert(realm, (UserEntity) realmModel, map);
        } else if (superclass.equals(DeviceInfoEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, (DeviceInfoEntity) realmModel, map);
        } else if (superclass.equals(CryptoMetadataEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insert(realm, (CryptoMetadataEntity) realmModel, map);
        } else if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insert(realm, (IncomingRoomKeyRequestEntity) realmModel, map);
        } else if (superclass.equals(OlmSessionEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.insert(realm, (OlmSessionEntity) realmModel, map);
        } else {
            throw getMissingProxyClassException(superclass);
        }
    }

    public void insert(Realm realm, Collection<? extends RealmModel> collection) {
        Iterator it = collection.iterator();
        HashMap hashMap = new HashMap(collection.size());
        if (it.hasNext()) {
            RealmModel realmModel = (RealmModel) it.next();
            Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
            if (superclass.equals(CryptoRoomEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.insert(realm, (CryptoRoomEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insert(realm, (OlmInboundGroupSessionEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insert(realm, (OutgoingRoomKeyRequestEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(KeysBackupDataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insert(realm, (KeysBackupDataEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(UserEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.insert(realm, (UserEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(DeviceInfoEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, (DeviceInfoEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(CryptoMetadataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insert(realm, (CryptoMetadataEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insert(realm, (IncomingRoomKeyRequestEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.insert(realm, (OlmSessionEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else {
                throw getMissingProxyClassException(superclass);
            }
            if (!it.hasNext()) {
                return;
            }
            if (superclass.equals(CryptoRoomEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(KeysBackupDataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(UserEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(DeviceInfoEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(CryptoMetadataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.insert(realm, it, (Map<RealmModel, Long>) hashMap);
            } else {
                throw getMissingProxyClassException(superclass);
            }
        }
    }

    public void insertOrUpdate(Realm realm, RealmModel realmModel, Map<RealmModel, Long> map) {
        Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
        if (superclass.equals(CryptoRoomEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.insertOrUpdate(realm, (CryptoRoomEntity) realmModel, map);
        } else if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insertOrUpdate(realm, (OlmInboundGroupSessionEntity) realmModel, map);
        } else if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (OutgoingRoomKeyRequestEntity) realmModel, map);
        } else if (superclass.equals(KeysBackupDataEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insertOrUpdate(realm, (KeysBackupDataEntity) realmModel, map);
        } else if (superclass.equals(UserEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.insertOrUpdate(realm, (UserEntity) realmModel, map);
        } else if (superclass.equals(DeviceInfoEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, (DeviceInfoEntity) realmModel, map);
        } else if (superclass.equals(CryptoMetadataEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insertOrUpdate(realm, (CryptoMetadataEntity) realmModel, map);
        } else if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (IncomingRoomKeyRequestEntity) realmModel, map);
        } else if (superclass.equals(OlmSessionEntity.class)) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.insertOrUpdate(realm, (OlmSessionEntity) realmModel, map);
        } else {
            throw getMissingProxyClassException(superclass);
        }
    }

    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> collection) {
        Iterator it = collection.iterator();
        HashMap hashMap = new HashMap(collection.size());
        if (it.hasNext()) {
            RealmModel realmModel = (RealmModel) it.next();
            Class superclass = realmModel instanceof RealmObjectProxy ? realmModel.getClass().getSuperclass() : realmModel.getClass();
            if (superclass.equals(CryptoRoomEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.insertOrUpdate(realm, (CryptoRoomEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insertOrUpdate(realm, (OlmInboundGroupSessionEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (OutgoingRoomKeyRequestEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(KeysBackupDataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insertOrUpdate(realm, (KeysBackupDataEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(UserEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.insertOrUpdate(realm, (UserEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(DeviceInfoEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, (DeviceInfoEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(CryptoMetadataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insertOrUpdate(realm, (CryptoMetadataEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, (IncomingRoomKeyRequestEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.insertOrUpdate(realm, (OlmSessionEntity) realmModel, (Map<RealmModel, Long>) hashMap);
            } else {
                throw getMissingProxyClassException(superclass);
            }
            if (!it.hasNext()) {
                return;
            }
            if (superclass.equals(CryptoRoomEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(KeysBackupDataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(UserEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(DeviceInfoEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(CryptoMetadataEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else if (superclass.equals(OlmSessionEntity.class)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.insertOrUpdate(realm, it, (Map<RealmModel, Long>) hashMap);
            } else {
                throw getMissingProxyClassException(superclass);
            }
        }
    }

    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> cls, Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        checkClass(cls);
        if (cls.equals(CryptoRoomEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(OlmInboundGroupSessionEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(OutgoingRoomKeyRequestEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(KeysBackupDataEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(UserEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(DeviceInfoEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(CryptoMetadataEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(IncomingRoomKeyRequestEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        if (cls.equals(OlmSessionEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONObject, z));
        }
        throw getMissingProxyClassException(cls);
    }

    public <E extends RealmModel> E createUsingJsonStream(Class<E> cls, Realm realm, JsonReader jsonReader) throws IOException {
        checkClass(cls);
        if (cls.equals(CryptoRoomEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(OlmInboundGroupSessionEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(OutgoingRoomKeyRequestEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(KeysBackupDataEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(UserEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(DeviceInfoEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(CryptoMetadataEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(IncomingRoomKeyRequestEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        if (cls.equals(OlmSessionEntity.class)) {
            return (RealmModel) cls.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
        }
        throw getMissingProxyClassException(cls);
    }

    public <E extends RealmModel> E createDetachedCopy(E e, int i, Map<RealmModel, CacheData<RealmModel>> map) {
        Class superclass = e.getClass().getSuperclass();
        if (superclass.equals(CryptoRoomEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy.createDetachedCopy((CryptoRoomEntity) e, 0, i, map));
        }
        if (superclass.equals(OlmInboundGroupSessionEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy.createDetachedCopy((OlmInboundGroupSessionEntity) e, 0, i, map));
        }
        if (superclass.equals(OutgoingRoomKeyRequestEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.createDetachedCopy((OutgoingRoomKeyRequestEntity) e, 0, i, map));
        }
        if (superclass.equals(KeysBackupDataEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.createDetachedCopy((KeysBackupDataEntity) e, 0, i, map));
        }
        if (superclass.equals(UserEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.createDetachedCopy((UserEntity) e, 0, i, map));
        }
        if (superclass.equals(DeviceInfoEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.createDetachedCopy((DeviceInfoEntity) e, 0, i, map));
        }
        if (superclass.equals(CryptoMetadataEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy.createDetachedCopy((CryptoMetadataEntity) e, 0, i, map));
        }
        if (superclass.equals(IncomingRoomKeyRequestEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.createDetachedCopy((IncomingRoomKeyRequestEntity) e, 0, i, map));
        }
        if (superclass.equals(OlmSessionEntity.class)) {
            return (RealmModel) superclass.cast(org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.createDetachedCopy((OlmSessionEntity) e, 0, i, map));
        }
        throw getMissingProxyClassException(superclass);
    }
}
