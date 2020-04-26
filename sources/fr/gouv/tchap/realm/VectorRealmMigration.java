package fr.gouv.tchap.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.fr_gouv_tchap_model_MediaScanRealmProxy.ClassNameHelper;

public class VectorRealmMigration implements RealmMigration {
    public void migrate(DynamicRealm dynamicRealm, long j, long j2) {
        dynamicRealm.delete(ClassNameHelper.INTERNAL_CLASS_NAME);
    }
}
