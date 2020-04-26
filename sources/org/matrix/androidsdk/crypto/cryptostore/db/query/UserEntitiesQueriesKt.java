package org.matrix.androidsdk.crypto.cryptostore.db.query;

import io.realm.Realm;
import io.realm.RealmQuery;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity.Companion;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0000\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0000¨\u0006\t"}, d2 = {"delete", "", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/UserEntity$Companion;", "realm", "Lio/realm/Realm;", "userId", "", "getOrCreate", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/UserEntity;", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: UserEntitiesQueries.kt */
public final class UserEntitiesQueriesKt {
    public static final UserEntity getOrCreate(Companion companion, Realm realm, String str) {
        Intrinsics.checkParameterIsNotNull(companion, "receiver$0");
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        String str2 = "userId";
        Intrinsics.checkParameterIsNotNull(str, str2);
        RealmQuery where = realm.where(UserEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        UserEntity userEntity = (UserEntity) where.equalTo(str2, str).findFirst();
        if (userEntity != null) {
            return userEntity;
        }
        UserEntity userEntity2 = (UserEntity) realm.createObject(UserEntity.class, str);
        Intrinsics.checkExpressionValueIsNotNull(userEntity2, "let {\n                re…va, userId)\n            }");
        return userEntity2;
    }

    public static final void delete(Companion companion, Realm realm, String str) {
        Intrinsics.checkParameterIsNotNull(companion, "receiver$0");
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        String str2 = "userId";
        Intrinsics.checkParameterIsNotNull(str, str2);
        RealmQuery where = realm.where(UserEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        UserEntity userEntity = (UserEntity) where.equalTo(str2, str).findFirst();
        if (userEntity != null) {
            userEntity.deleteFromRealm();
        }
    }
}
