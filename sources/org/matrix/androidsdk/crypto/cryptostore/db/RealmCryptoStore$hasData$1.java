package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmQuery;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntity;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$hasData$1 extends Lambda implements Function1<Realm, Boolean> {
    public static final RealmCryptoStore$hasData$1 INSTANCE = new RealmCryptoStore$hasData$1();

    RealmCryptoStore$hasData$1() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return Boolean.valueOf(invoke((Realm) obj));
    }

    public final boolean invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        if (!realm.isEmpty()) {
            RealmQuery where = realm.where(CryptoMetadataEntity.class);
            Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
            if (where.count() > 0) {
                return true;
            }
        }
        return false;
    }
}
