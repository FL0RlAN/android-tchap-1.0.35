package org.matrix.androidsdk.crypto.rest.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u000b\u001a\u00020\fR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\t8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept;", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/SendToDeviceObject;", "()V", "commitment", "", "hash", "keyAgreementProtocol", "messageAuthenticationCode", "shortAuthenticationStrings", "", "transactionID", "isValid", "", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeyVerificationAccept.kt */
public final class KeyVerificationAccept implements SendToDeviceObject {
    public static final Companion Companion = new Companion(null);
    public String commitment;
    public String hash;
    @SerializedName("key_agreement_protocol")
    public String keyAgreementProtocol;
    @SerializedName("message_authentication_code")
    public String messageAuthenticationCode;
    @SerializedName("short_authentication_string")
    public List<String> shortAuthenticationStrings;
    @SerializedName("transaction_id")
    public String transactionID;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J<\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\f¨\u0006\r"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept$Companion;", "", "()V", "create", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept;", "tid", "", "keyAgreementProtocol", "hash", "commitment", "messageAuthenticationCode", "shortAuthenticationStrings", "", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: KeyVerificationAccept.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final KeyVerificationAccept create(String str, String str2, String str3, String str4, String str5, List<String> list) {
            Intrinsics.checkParameterIsNotNull(str, "tid");
            Intrinsics.checkParameterIsNotNull(str2, "keyAgreementProtocol");
            Intrinsics.checkParameterIsNotNull(str3, "hash");
            Intrinsics.checkParameterIsNotNull(str4, "commitment");
            Intrinsics.checkParameterIsNotNull(str5, "messageAuthenticationCode");
            Intrinsics.checkParameterIsNotNull(list, "shortAuthenticationStrings");
            KeyVerificationAccept keyVerificationAccept = new KeyVerificationAccept();
            keyVerificationAccept.transactionID = str;
            keyVerificationAccept.keyAgreementProtocol = str2;
            keyVerificationAccept.hash = str3;
            keyVerificationAccept.commitment = str4;
            keyVerificationAccept.messageAuthenticationCode = str5;
            keyVerificationAccept.shortAuthenticationStrings = list;
            return keyVerificationAccept;
        }
    }

    public final boolean isValid() {
        CharSequence charSequence = this.transactionID;
        if (!(charSequence == null || StringsKt.isBlank(charSequence))) {
            CharSequence charSequence2 = this.keyAgreementProtocol;
            if (!(charSequence2 == null || StringsKt.isBlank(charSequence2))) {
                CharSequence charSequence3 = this.hash;
                if (!(charSequence3 == null || StringsKt.isBlank(charSequence3))) {
                    CharSequence charSequence4 = this.commitment;
                    if (!(charSequence4 == null || StringsKt.isBlank(charSequence4))) {
                        CharSequence charSequence5 = this.messageAuthenticationCode;
                        if (!(charSequence5 == null || StringsKt.isBlank(charSequence5))) {
                            Collection collection = this.shortAuthenticationStrings;
                            if (!(collection == null || collection.isEmpty())) {
                                return true;
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
