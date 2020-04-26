package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmModel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref.ObjectRef;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity;
import org.matrix.androidsdk.crypto.data.MXOlmSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$storeSession$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ String $deviceKey;
    final /* synthetic */ String $key;
    final /* synthetic */ MXOlmSession $session;
    final /* synthetic */ ObjectRef $sessionIdentifier;

    RealmCryptoStore$storeSession$1(String str, ObjectRef objectRef, String str2, MXOlmSession mXOlmSession) {
        this.$key = str;
        this.$sessionIdentifier = objectRef;
        this.$deviceKey = str2;
        this.$session = mXOlmSession;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        OlmSessionEntity olmSessionEntity = new OlmSessionEntity(null, null, null, null, 0, 31, null);
        olmSessionEntity.setPrimaryKey(this.$key);
        olmSessionEntity.setSessionId((String) this.$sessionIdentifier.element);
        olmSessionEntity.setDeviceKey(this.$deviceKey);
        olmSessionEntity.putOlmSession(this.$session.getOlmSession());
        olmSessionEntity.setLastReceivedMessageTs(this.$session.getLastReceivedMessageTs());
        realm.insertOrUpdate((RealmModel) olmSessionEntity);
    }
}
