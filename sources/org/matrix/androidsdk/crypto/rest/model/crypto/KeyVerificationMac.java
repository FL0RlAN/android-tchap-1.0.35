package org.matrix.androidsdk.crypto.rest.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\b\u001a\u00020\tR\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R \u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac;", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/SendToDeviceObject;", "()V", "keys", "", "mac", "", "transactionID", "isValid", "", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeyVerificationMac.kt */
public final class KeyVerificationMac implements SendToDeviceObject {
    public static final Companion Companion = new Companion(null);
    public String keys;
    public Map<String, String> mac;
    @SerializedName("transaction_id")
    public String transactionID;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J*\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\b2\u0006\u0010\t\u001a\u00020\u0006¨\u0006\n"}, d2 = {"Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac$Companion;", "", "()V", "create", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac;", "tid", "", "mac", "", "keys", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: KeyVerificationMac.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final KeyVerificationMac create(String str, Map<String, String> map, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "tid");
            Intrinsics.checkParameterIsNotNull(map, "mac");
            Intrinsics.checkParameterIsNotNull(str2, "keys");
            KeyVerificationMac keyVerificationMac = new KeyVerificationMac();
            keyVerificationMac.transactionID = str;
            keyVerificationMac.mac = map;
            keyVerificationMac.keys = str2;
            return keyVerificationMac;
        }
    }

    public final boolean isValid() {
        CharSequence charSequence = this.transactionID;
        if (!(charSequence == null || StringsKt.isBlank(charSequence))) {
            CharSequence charSequence2 = this.keys;
            if (!(charSequence2 == null || StringsKt.isBlank(charSequence2))) {
                Map<String, String> map = this.mac;
                return !(map == null || map.isEmpty());
            }
        }
    }
}
