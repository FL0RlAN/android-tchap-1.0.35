package org.matrix.androidsdk.crypto.verification;

import android.util.Base64;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationAccept;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationAccept.Companion;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\u0006"}, d2 = {"org/matrix/androidsdk/crypto/verification/IncomingSASVerificationTransaction$performAccept$2", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "onSuccess", "", "info", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: IncomingSASVerificationTransaction.kt */
public final class IncomingSASVerificationTransaction$performAccept$2 extends SimpleApiCallback<MXDeviceInfo> {
    final /* synthetic */ String $agreedHash;
    final /* synthetic */ String $agreedMac;
    final /* synthetic */ String $agreedProtocol;
    final /* synthetic */ List $agreedShortCode;
    final /* synthetic */ CryptoSession $session;
    final /* synthetic */ IncomingSASVerificationTransaction this$0;

    IncomingSASVerificationTransaction$performAccept$2(IncomingSASVerificationTransaction incomingSASVerificationTransaction, CryptoSession cryptoSession, String str, String str2, String str3, List list) {
        this.this$0 = incomingSASVerificationTransaction;
        this.$session = cryptoSession;
        this.$agreedProtocol = str;
        this.$agreedHash = str2;
        this.$agreedMac = str3;
        this.$agreedShortCode = list;
    }

    public void onSuccess(MXDeviceInfo mXDeviceInfo) {
        if ((mXDeviceInfo != null ? mXDeviceInfo.fingerprint() : null) == null) {
            Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## Failed to find device key ");
            this.$session.requireCrypto().getDecryptingThreadHandler().post(new IncomingSASVerificationTransaction$performAccept$2$onSuccess$1(this));
            return;
        }
        Companion companion = KeyVerificationAccept.Companion;
        String transactionId = this.this$0.getTransactionId();
        String str = this.$agreedProtocol;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        String str2 = this.$agreedHash;
        if (str2 == null) {
            Intrinsics.throwNpe();
        }
        String str3 = this.$agreedMac;
        if (str3 == null) {
            Intrinsics.throwNpe();
        }
        List list = this.$agreedShortCode;
        byte[] bytes = "temporary commitment".getBytes(Charsets.UTF_8);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        String encodeToString = Base64.encodeToString(bytes, 0);
        Intrinsics.checkExpressionValueIsNotNull(encodeToString, "Base64.encodeToString(\"t…eArray(), Base64.DEFAULT)");
        this.$session.requireCrypto().getDecryptingThreadHandler().post(new IncomingSASVerificationTransaction$performAccept$2$onSuccess$2(this, companion.create(transactionId, str, str2, encodeToString, str3, list)));
    }
}
