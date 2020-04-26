package org.matrix.androidsdk.crypto.rest.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\f\u001a\u00020\rR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart;", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/SendToDeviceObject;", "()V", "fromDevice", "", "hashes", "", "keyAgreementProtocols", "messageAuthenticationCodes", "method", "shortAuthenticationStrings", "transactionID", "isValid", "", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeyVerificationStart.kt */
public final class KeyVerificationStart implements SendToDeviceObject {
    public static final Companion Companion = new Companion(null);
    public static final String SAS_MODE_DECIMAL = "decimal";
    public static final String SAS_MODE_EMOJI = "emoji";
    public static final String VERIF_METHOD_SAS = "m.sas.v1";
    @SerializedName("from_device")
    public String fromDevice;
    public List<String> hashes;
    @SerializedName("key_agreement_protocols")
    public List<String> keyAgreementProtocols;
    @SerializedName("message_authentication_codes")
    public List<String> messageAuthenticationCodes;
    public String method;
    @SerializedName("short_authentication_string")
    public List<String> shortAuthenticationStrings;
    @SerializedName("transaction_id")
    public String transactionID;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart$Companion;", "", "()V", "SAS_MODE_DECIMAL", "", "SAS_MODE_EMOJI", "VERIF_METHOD_SAS", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: KeyVerificationStart.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0089, code lost:
        if (r0.contains(org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SAS_MAC_SHA256_LONGKDF) == false) goto L_0x00ab;
     */
    public final boolean isValid() {
        CharSequence charSequence = this.transactionID;
        if (!(charSequence == null || StringsKt.isBlank(charSequence))) {
            CharSequence charSequence2 = this.fromDevice;
            if (!(charSequence2 == null || StringsKt.isBlank(charSequence2)) && !(!Intrinsics.areEqual((Object) this.method, (Object) VERIF_METHOD_SAS))) {
                Collection collection = this.keyAgreementProtocols;
                if (!(collection == null || collection.isEmpty())) {
                    Collection collection2 = this.hashes;
                    if (!(collection2 == null || collection2.isEmpty())) {
                        List<String> list = this.hashes;
                        if (list == null || list.contains("sha256")) {
                            Collection collection3 = this.messageAuthenticationCodes;
                            if (!(collection3 == null || collection3.isEmpty())) {
                                List<String> list2 = this.messageAuthenticationCodes;
                                if (list2 != null && !list2.contains(SASVerificationTransaction.SAS_MAC_SHA256)) {
                                    List<String> list3 = this.messageAuthenticationCodes;
                                    if (list3 != null) {
                                    }
                                }
                                Collection collection4 = this.shortAuthenticationStrings;
                                if (!(collection4 == null || collection4.isEmpty())) {
                                    List<String> list4 = this.shortAuthenticationStrings;
                                    if (list4 == null || list4.contains(SAS_MODE_DECIMAL)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## received invalid verification request");
        return false;
    }
}
