package org.matrix.androidsdk.crypto.verification;

import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.SendToDeviceObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001:\u0001%B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ \u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u001eH&J\u000e\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\fJ\u0018\u0010!\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\"\u001a\u00020#H&J\u000e\u0010$\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\tR*\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\f0\u000bj\b\u0012\u0004\u0012\u00020\f`\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013¨\u0006&"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "", "transactionId", "", "otherUserId", "otherDeviceId", "isIncoming", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "()Z", "listeners", "Ljava/util/ArrayList;", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction$Listener;", "Lkotlin/collections/ArrayList;", "getListeners", "()Ljava/util/ArrayList;", "setListeners", "(Ljava/util/ArrayList;)V", "getOtherDeviceId", "()Ljava/lang/String;", "setOtherDeviceId", "(Ljava/lang/String;)V", "getOtherUserId", "getTransactionId", "acceptToDeviceEvent", "", "session", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "senderId", "event", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/SendToDeviceObject;", "addListener", "listener", "cancel", "code", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "removeListener", "Listener", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: VerificationTransaction.kt */
public abstract class VerificationTransaction {
    private final boolean isIncoming;
    private ArrayList<Listener> listeners;
    private String otherDeviceId;
    private final String otherUserId;
    private final String transactionId;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction$Listener;", "", "transactionUpdated", "", "tx", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VerificationTransaction.kt */
    public interface Listener {
        void transactionUpdated(VerificationTransaction verificationTransaction);
    }

    public abstract void acceptToDeviceEvent(CryptoSession cryptoSession, String str, SendToDeviceObject sendToDeviceObject);

    public abstract void cancel(CryptoSession cryptoSession, CancelCode cancelCode);

    public VerificationTransaction(String str, String str2, String str3, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "transactionId");
        Intrinsics.checkParameterIsNotNull(str2, "otherUserId");
        this.transactionId = str;
        this.otherUserId = str2;
        this.otherDeviceId = str3;
        this.isIncoming = z;
        this.listeners = new ArrayList<>();
    }

    public final String getTransactionId() {
        return this.transactionId;
    }

    public final String getOtherUserId() {
        return this.otherUserId;
    }

    public /* synthetic */ VerificationTransaction(String str, String str2, String str3, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 4) != 0) {
            str3 = null;
        }
        this(str, str2, str3, z);
    }

    public final String getOtherDeviceId() {
        return this.otherDeviceId;
    }

    public final void setOtherDeviceId(String str) {
        this.otherDeviceId = str;
    }

    public final boolean isIncoming() {
        return this.isIncoming;
    }

    /* access modifiers changed from: protected */
    public final ArrayList<Listener> getListeners() {
        return this.listeners;
    }

    /* access modifiers changed from: protected */
    public final void setListeners(ArrayList<Listener> arrayList) {
        Intrinsics.checkParameterIsNotNull(arrayList, "<set-?>");
        this.listeners = arrayList;
    }

    public final void addListener(Listener listener) {
        Intrinsics.checkParameterIsNotNull(listener, CastExtraArgs.LISTENER);
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public final void removeListener(Listener listener) {
        Intrinsics.checkParameterIsNotNull(listener, CastExtraArgs.LISTENER);
        this.listeners.remove(listener);
    }
}
