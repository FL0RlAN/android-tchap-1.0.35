package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmModel;
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
import org.matrix.olm.OlmInboundGroupSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$storeInboundGroupSessions$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ List $sessions;
    final /* synthetic */ RealmCryptoStore this$0;

    RealmCryptoStore$storeInboundGroupSessions$1(RealmCryptoStore realmCryptoStore, List list) {
        this.this$0 = realmCryptoStore;
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
            String str = null;
            try {
                str = mXOlmInboundGroupSession2.mSession.sessionIdentifier();
            } catch (OlmException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## storeInboundGroupSession() : sessionIdentifier failed ");
                sb.append(e.getMessage());
                Log.e("RealmCryptoStore", sb.toString(), e);
            }
            if (str != null) {
                String createPrimaryKey = OlmInboundGroupSessionEntityKt.createPrimaryKey(OlmInboundGroupSessionEntity.Companion, str, mXOlmInboundGroupSession2.mSenderKey);
                if (!Intrinsics.areEqual((Object) (MXOlmInboundGroupSession2) this.this$0.inboundGroupSessionToRelease.get(createPrimaryKey), (Object) mXOlmInboundGroupSession2)) {
                    MXOlmInboundGroupSession2 mXOlmInboundGroupSession22 = (MXOlmInboundGroupSession2) this.this$0.inboundGroupSessionToRelease.get(createPrimaryKey);
                    if (mXOlmInboundGroupSession22 != null) {
                        OlmInboundGroupSession olmInboundGroupSession = mXOlmInboundGroupSession22.mSession;
                        if (olmInboundGroupSession != null) {
                            olmInboundGroupSession.releaseSession();
                        }
                    }
                }
                this.this$0.inboundGroupSessionToRelease.put(createPrimaryKey, mXOlmInboundGroupSession2);
                OlmInboundGroupSessionEntity olmInboundGroupSessionEntity = new OlmInboundGroupSessionEntity(null, null, null, null, false, 31, null);
                olmInboundGroupSessionEntity.setPrimaryKey(createPrimaryKey);
                olmInboundGroupSessionEntity.setSessionId(str);
                olmInboundGroupSessionEntity.setSenderKey(mXOlmInboundGroupSession2.mSenderKey);
                olmInboundGroupSessionEntity.putInboundGroupSession(mXOlmInboundGroupSession2);
                realm.insertOrUpdate((RealmModel) olmInboundGroupSessionEntity);
            }
        }
    }
}
