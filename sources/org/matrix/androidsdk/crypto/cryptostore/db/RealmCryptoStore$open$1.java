package org.matrix.androidsdk.crypto.cryptostore.db;

import android.text.TextUtils;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmQuery;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref.BooleanRef;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntity;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "realm", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$open$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ RealmCryptoStore this$0;

    RealmCryptoStore$open$1(RealmCryptoStore realmCryptoStore) {
        this.this$0 = realmCryptoStore;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        RealmQuery where = realm.where(CryptoMetadataEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) where.findFirst();
        final BooleanRef booleanRef = new BooleanRef();
        booleanRef.element = false;
        if (cryptoMetadataEntity != null && (!TextUtils.equals(cryptoMetadataEntity.getUserId(), RealmCryptoStore.access$getCredentials$p(this.this$0).getUserId()) || (RealmCryptoStore.access$getCredentials$p(this.this$0).getDeviceId() != null && !TextUtils.equals(RealmCryptoStore.access$getCredentials$p(this.this$0).getDeviceId(), cryptoMetadataEntity.getDeviceId())))) {
            Log.w("RealmCryptoStore", "## open() : Credentials do not match, close this store and delete data");
            booleanRef.element = true;
            cryptoMetadataEntity = null;
        }
        if (cryptoMetadataEntity == null) {
            realm.executeTransaction(new Transaction(this) {
                final /* synthetic */ RealmCryptoStore$open$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void execute(Realm realm) {
                    if (booleanRef.element) {
                        realm.deleteAll();
                    }
                    ((CryptoMetadataEntity) realm.createObject(CryptoMetadataEntity.class, RealmCryptoStore.access$getCredentials$p(this.this$0.this$0).getUserId())).setDeviceId(RealmCryptoStore.access$getCredentials$p(this.this$0.this$0).getDeviceId());
                }
            });
        }
    }
}
