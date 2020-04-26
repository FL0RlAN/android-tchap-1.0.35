package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmQuery;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntityKt;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.olm.OlmException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$markBackupDoneForInboundGroupSessions$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ List $sessions;

    RealmCryptoStore$markBackupDoneForInboundGroupSessions$1(List list) {
        this.$sessions = list;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        for (MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 : this.$sessions) {
            try {
                String createPrimaryKey = OlmInboundGroupSessionEntityKt.createPrimaryKey(OlmInboundGroupSessionEntity.Companion, mXOlmInboundGroupSession2.mSession.sessionIdentifier(), mXOlmInboundGroupSession2.mSenderKey);
                RealmQuery where = realm.where(OlmInboundGroupSessionEntity.class);
                Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
                OlmInboundGroupSessionEntity olmInboundGroupSessionEntity = (OlmInboundGroupSessionEntity) where.equalTo("primaryKey", createPrimaryKey).findFirst();
                if (olmInboundGroupSessionEntity != null) {
                    olmInboundGroupSessionEntity.setBackedUp(true);
                }
            } catch (OlmException e) {
                Log.e("RealmCryptoStore", "OlmException", e);
            }
        }
    }
}
