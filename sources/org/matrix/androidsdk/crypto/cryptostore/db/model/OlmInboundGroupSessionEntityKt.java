package org.matrix.androidsdk.crypto.cryptostore.db.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity.Companion;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u00012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001¨\u0006\u0005"}, d2 = {"createPrimaryKey", "", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OlmInboundGroupSessionEntity$Companion;", "sessionId", "senderKey", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: OlmInboundGroupSessionEntity.kt */
public final class OlmInboundGroupSessionEntityKt {
    public static final String createPrimaryKey(Companion companion, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(companion, "receiver$0");
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append('|');
        sb.append(str2);
        return sb.toString();
    }
}
