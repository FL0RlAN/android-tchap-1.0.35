package org.matrix.androidsdk.crypto.verification;

import android.os.Build.VERSION;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntityFields;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.CryptoRestClient;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationAccept;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationKey;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationMac;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;
import org.matrix.androidsdk.crypto.rest.model.crypto.SendToDeviceObject;
import org.matrix.androidsdk.crypto.verification.VerificationEmoji.EmojiRepresentation;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.olm.OlmSAS;
import org.matrix.olm.OlmUtility;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\b&\u0018\u0000 j2\u00020\u0001:\u0002jkB'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ \u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\u00032\u0006\u0010?\u001a\u00020@H\u0016J\u0018\u0010A\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010B\u001a\u00020\u0010H\u0016J\b\u0010C\u001a\u00020;H\u0004J\u000e\u0010D\u001a\u00020\u00032\u0006\u0010E\u001a\u00020#J\u0014\u0010F\u001a\b\u0012\u0004\u0012\u00020H0G2\u0006\u0010E\u001a\u00020#J\u0006\u0010I\u001a\u00020\u001cJ\u0010\u0010J\u001a\u0004\u0018\u00010\u00032\u0006\u0010K\u001a\u00020\u0003J\u0012\u0010L\u001a\u0004\u0018\u00010\u00032\u0006\u0010M\u001a\u00020\u0003H\u0004J\u001a\u0010N\u001a\u0004\u0018\u00010\u00032\u0006\u0010O\u001a\u00020\u00032\u0006\u0010P\u001a\u00020\u0003H\u0004J \u0010Q\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010R\u001a\u00020\u00032\u0006\u0010S\u001a\u00020TH&J\u0018\u0010U\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010S\u001a\u00020\u0016H&J\u0018\u0010V\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010W\u001a\u00020\nH&J\u0018\u0010X\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010(\u001a\u00020)H&J\b\u0010Y\u001a\u00020;H\u0002J@\u0010Z\u001a\u00020;2\u0006\u0010[\u001a\u00020\u00032\u0006\u0010\\\u001a\u00020]2\u0006\u0010<\u001a\u00020=2\u0006\u0010^\u001a\u00020/2\u0006\u0010_\u001a\u00020\u00102\u000e\u0010`\u001a\n\u0012\u0004\u0012\u00020;\u0018\u00010aH\u0004J<\u0010b\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\u0006\u0010c\u001a\u00020\u00032\u0006\u0010R\u001a\u00020\u00032\f\u0010d\u001a\b\u0012\u0004\u0012\u00020;0a2\f\u0010e\u001a\b\u0012\u0004\u0012\u00020;0aH\u0002J\u0006\u0010f\u001a\u00020\u0007J\u0006\u0010g\u001a\u00020\u0007J\u000e\u0010h\u001a\u00020;2\u0006\u0010<\u001a\u00020=J\u0010\u0010i\u001a\u00020;2\u0006\u0010<\u001a\u00020=H\u0004R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001c\u0010\"\u001a\u0004\u0018\u00010#X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u001c\u0010(\u001a\u0004\u0018\u00010)X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R+\u00100\u001a\u00020/2\u0006\u0010.\u001a\u00020/8F@FX\u0002¢\u0006\u0012\n\u0004\b5\u00106\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u001c\u00107\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010\u0018\"\u0004\b9\u0010\u001a¨\u0006l"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction;", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "transactionId", "", "otherUserId", "otherDevice", "isIncoming", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "accepted", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept;", "getAccepted", "()Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept;", "setAccepted", "(Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept;)V", "cancelledReason", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "getCancelledReason", "()Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "setCancelledReason", "(Lorg/matrix/androidsdk/crypto/verification/CancelCode;)V", "myMac", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac;", "getMyMac", "()Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac;", "setMyMac", "(Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac;)V", "olmSas", "Lorg/matrix/olm/OlmSAS;", "otherKey", "getOtherKey", "()Ljava/lang/String;", "setOtherKey", "(Ljava/lang/String;)V", "shortCodeBytes", "", "getShortCodeBytes", "()[B", "setShortCodeBytes", "([B)V", "startReq", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart;", "getStartReq", "()Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart;", "setStartReq", "(Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart;)V", "<set-?>", "Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;", "state", "getState", "()Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;", "setState", "(Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;)V", "state$delegate", "Lkotlin/properties/ReadWriteProperty;", "theirMac", "getTheirMac", "setTheirMac", "acceptToDeviceEvent", "", "session", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "senderId", "event", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/SendToDeviceObject;", "cancel", "code", "finalize", "getDecimalCodeRepresentation", "byteArray", "getEmojiCodeRepresentation", "", "Lorg/matrix/androidsdk/crypto/verification/VerificationEmoji$EmojiRepresentation;", "getSAS", "getShortCodeRepresentation", "shortAuthenticationStringMode", "hashUsingAgreedHashMethod", "toHash", "macUsingAgreedMethod", "message", "info", "onKeyVerificationKey", "userId", "vKey", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationKey;", "onKeyVerificationMac", "onVerificationAccept", "accept", "onVerificationStart", "releaseSAS", "sendToOther", "type", "keyToDevice", "", "nextState", "onErrorReason", "onDone", "Lkotlin/Function0;", "setDeviceVerified", "deviceId", "success", "error", "supportsDecimal", "supportsEmoji", "userHasVerifiedShortCode", "verifyMacs", "Companion", "SASVerificationTxState", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: SASVerificationTransaction.kt */
public abstract class SASVerificationTransaction extends VerificationTransaction {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.mutableProperty1(new MutablePropertyReference1Impl(Reflection.getOrCreateKotlinClass(SASVerificationTransaction.class), OutgoingRoomKeyRequestEntityFields.STATE, "getState()Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;"))};
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final List<String> KNOWN_AGREEMENT_PROTOCOLS = CollectionsKt.listOf("curve25519");
    /* access modifiers changed from: private */
    public static final List<String> KNOWN_HASHES = CollectionsKt.listOf("sha256");
    /* access modifiers changed from: private */
    public static final List<String> KNOWN_MACS = CollectionsKt.listOf(SAS_MAC_SHA256, SAS_MAC_SHA256_LONGKDF);
    /* access modifiers changed from: private */
    public static final List<String> KNOWN_SHORT_CODES;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = "javaClass";
    public static final String SAS_MAC_SHA256 = "hkdf-hmac-sha256";
    public static final String SAS_MAC_SHA256_LONGKDF = "hmac-sha256";
    private KeyVerificationAccept accepted;
    private CancelCode cancelledReason;
    private KeyVerificationMac myMac;
    private OlmSAS olmSas;
    private String otherKey;
    private byte[] shortCodeBytes;
    private KeyVerificationStart startReq;
    private final ReadWriteProperty state$delegate;
    private KeyVerificationMac theirMac;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0007R\u0011\u0010\u000e\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$Companion;", "", "()V", "KNOWN_AGREEMENT_PROTOCOLS", "", "", "getKNOWN_AGREEMENT_PROTOCOLS", "()Ljava/util/List;", "KNOWN_HASHES", "getKNOWN_HASHES", "KNOWN_MACS", "getKNOWN_MACS", "KNOWN_SHORT_CODES", "getKNOWN_SHORT_CODES", "LOG_TAG", "getLOG_TAG", "()Ljava/lang/String;", "SAS_MAC_SHA256", "SAS_MAC_SHA256_LONGKDF", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: SASVerificationTransaction.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getLOG_TAG() {
            return SASVerificationTransaction.LOG_TAG;
        }

        public final List<String> getKNOWN_AGREEMENT_PROTOCOLS() {
            return SASVerificationTransaction.KNOWN_AGREEMENT_PROTOCOLS;
        }

        public final List<String> getKNOWN_HASHES() {
            return SASVerificationTransaction.KNOWN_HASHES;
        }

        public final List<String> getKNOWN_MACS() {
            return SASVerificationTransaction.KNOWN_MACS;
        }

        public final List<String> getKNOWN_SHORT_CODES() {
            return SASVerificationTransaction.KNOWN_SHORT_CODES;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0014\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014¨\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction$SASVerificationTxState;", "", "(Ljava/lang/String;I)V", "None", "SendingStart", "Started", "OnStarted", "SendingAccept", "Accepted", "OnAccepted", "SendingKey", "KeySent", "OnKeyReceived", "ShortCodeReady", "ShortCodeAccepted", "SendingMac", "MacSent", "Verifying", "Verified", "Cancelled", "OnCancelled", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: SASVerificationTransaction.kt */
    public enum SASVerificationTxState {
        None,
        SendingStart,
        Started,
        OnStarted,
        SendingAccept,
        Accepted,
        OnAccepted,
        SendingKey,
        KeySent,
        OnKeyReceived,
        ShortCodeReady,
        ShortCodeAccepted,
        SendingMac,
        MacSent,
        Verifying,
        Verified,
        Cancelled,
        OnCancelled
    }

    public final SASVerificationTxState getState() {
        return (SASVerificationTxState) this.state$delegate.getValue(this, $$delegatedProperties[0]);
    }

    public abstract void onKeyVerificationKey(CryptoSession cryptoSession, String str, KeyVerificationKey keyVerificationKey);

    public abstract void onKeyVerificationMac(CryptoSession cryptoSession, KeyVerificationMac keyVerificationMac);

    public abstract void onVerificationAccept(CryptoSession cryptoSession, KeyVerificationAccept keyVerificationAccept);

    public abstract void onVerificationStart(CryptoSession cryptoSession, KeyVerificationStart keyVerificationStart);

    public final void setState(SASVerificationTxState sASVerificationTxState) {
        Intrinsics.checkParameterIsNotNull(sASVerificationTxState, "<set-?>");
        this.state$delegate.setValue(this, $$delegatedProperties[0], sASVerificationTxState);
    }

    public SASVerificationTransaction(String str, String str2, String str3, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "transactionId");
        Intrinsics.checkParameterIsNotNull(str2, "otherUserId");
        super(str, str2, str3, z);
        Delegates delegates = Delegates.INSTANCE;
        SASVerificationTxState sASVerificationTxState = SASVerificationTxState.None;
        this.state$delegate = new SASVerificationTransaction$$special$$inlined$observable$1(sASVerificationTxState, sASVerificationTxState, this);
    }

    static {
        List<String> list;
        int i = VERSION.SDK_INT;
        String str = KeyVerificationStart.SAS_MODE_DECIMAL;
        if (i >= 21) {
            list = CollectionsKt.listOf(KeyVerificationStart.SAS_MODE_EMOJI, str);
        } else {
            list = CollectionsKt.listOf(str);
        }
        KNOWN_SHORT_CODES = list;
    }

    public final CancelCode getCancelledReason() {
        return this.cancelledReason;
    }

    public final void setCancelledReason(CancelCode cancelCode) {
        this.cancelledReason = cancelCode;
    }

    public final KeyVerificationStart getStartReq() {
        return this.startReq;
    }

    public final void setStartReq(KeyVerificationStart keyVerificationStart) {
        this.startReq = keyVerificationStart;
    }

    public final KeyVerificationAccept getAccepted() {
        return this.accepted;
    }

    public final void setAccepted(KeyVerificationAccept keyVerificationAccept) {
        this.accepted = keyVerificationAccept;
    }

    public final String getOtherKey() {
        return this.otherKey;
    }

    public final void setOtherKey(String str) {
        this.otherKey = str;
    }

    public final byte[] getShortCodeBytes() {
        return this.shortCodeBytes;
    }

    public final void setShortCodeBytes(byte[] bArr) {
        this.shortCodeBytes = bArr;
    }

    public final KeyVerificationMac getMyMac() {
        return this.myMac;
    }

    public final void setMyMac(KeyVerificationMac keyVerificationMac) {
        this.myMac = keyVerificationMac;
    }

    public final KeyVerificationMac getTheirMac() {
        return this.theirMac;
    }

    public final void setTheirMac(KeyVerificationMac keyVerificationMac) {
        this.theirMac = keyVerificationMac;
    }

    public final OlmSAS getSAS() {
        if (this.olmSas == null) {
            this.olmSas = new OlmSAS();
        }
        OlmSAS olmSAS = this.olmSas;
        if (olmSAS == null) {
            Intrinsics.throwNpe();
        }
        return olmSAS;
    }

    /* access modifiers changed from: protected */
    public final void finalize() {
        releaseSAS();
    }

    /* access modifiers changed from: private */
    public final void releaseSAS() {
        OlmSAS olmSAS = this.olmSas;
        if (olmSAS != null) {
            olmSAS.releaseSas();
        }
        this.olmSas = null;
    }

    public final void userHasVerifiedShortCode(CryptoSession cryptoSession) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS short code verified by user for id:");
        sb.append(getTransactionId());
        Log.d(str, sb.toString());
        if (getState() != SASVerificationTxState.ShortCodeReady) {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## Accepted short code from invalid state ");
            sb2.append(getState());
            Log.e(str2, sb2.toString());
            cancel(cryptoSession, CancelCode.UnexpectedMessage);
            return;
        }
        setState(SASVerificationTxState.ShortCodeAccepted);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("MATRIX_KEY_VERIFICATION_MAC");
        sb3.append(cryptoSession.getMyUserId());
        sb3.append(cryptoSession.requireCrypto().getMyDevice().deviceId);
        sb3.append(getOtherUserId());
        sb3.append(getOtherDeviceId());
        sb3.append(getTransactionId());
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("ed25519:");
        sb5.append(cryptoSession.requireCrypto().getMyDevice().deviceId);
        String sb6 = sb5.toString();
        String fingerprint = cryptoSession.requireCrypto().getMyDevice().fingerprint();
        Intrinsics.checkExpressionValueIsNotNull(fingerprint, "session.requireCrypto().myDevice.fingerprint()");
        StringBuilder sb7 = new StringBuilder();
        sb7.append(sb4);
        sb7.append(sb6);
        String macUsingAgreedMethod = macUsingAgreedMethod(fingerprint, sb7.toString());
        StringBuilder sb8 = new StringBuilder();
        sb8.append(sb4);
        sb8.append("KEY_IDS");
        String macUsingAgreedMethod2 = macUsingAgreedMethod(sb6, sb8.toString());
        CharSequence charSequence = macUsingAgreedMethod;
        boolean z = false;
        if (!(charSequence == null || StringsKt.isBlank(charSequence))) {
            CharSequence charSequence2 = macUsingAgreedMethod2;
            if (charSequence2 == null || StringsKt.isBlank(charSequence2)) {
                z = true;
            }
            if (!z) {
                KeyVerificationMac create = KeyVerificationMac.Companion.create(getTransactionId(), MapsKt.mapOf(TuplesKt.to(sb6, macUsingAgreedMethod)), macUsingAgreedMethod2);
                this.myMac = create;
                setState(SASVerificationTxState.SendingMac);
                sendToOther("m.key.verification.mac", create, cryptoSession, SASVerificationTxState.MacSent, CancelCode.User, new SASVerificationTransaction$userHasVerifiedShortCode$1(this));
                if (this.theirMac != null) {
                    verifyMacs(cryptoSession);
                }
                return;
            }
        }
        String str3 = LOG_TAG;
        StringBuilder sb9 = new StringBuilder();
        sb9.append("## SAS verification [");
        sb9.append(getTransactionId());
        sb9.append("] failed to send KeyMac, empty key hashes.");
        Log.e(str3, sb9.toString());
        cancel(cryptoSession, CancelCode.UnexpectedMessage);
    }

    public void acceptToDeviceEvent(CryptoSession cryptoSession, String str, SendToDeviceObject sendToDeviceObject) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(str, "senderId");
        Intrinsics.checkParameterIsNotNull(sendToDeviceObject, NotificationCompat.CATEGORY_EVENT);
        if (sendToDeviceObject instanceof KeyVerificationStart) {
            onVerificationStart(cryptoSession, (KeyVerificationStart) sendToDeviceObject);
        } else if (sendToDeviceObject instanceof KeyVerificationAccept) {
            onVerificationAccept(cryptoSession, (KeyVerificationAccept) sendToDeviceObject);
        } else if (sendToDeviceObject instanceof KeyVerificationKey) {
            onKeyVerificationKey(cryptoSession, str, (KeyVerificationKey) sendToDeviceObject);
        } else if (sendToDeviceObject instanceof KeyVerificationMac) {
            onKeyVerificationMac(cryptoSession, (KeyVerificationMac) sendToDeviceObject);
        }
    }

    /* access modifiers changed from: protected */
    public final void verifyMacs(CryptoSession cryptoSession) {
        String str;
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verifying macs for id:");
        sb.append(getTransactionId());
        Log.d(str2, sb.toString());
        setState(SASVerificationTxState.Verifying);
        Map userDevices = cryptoSession.requireCrypto().getCryptoStore().getUserDevices(getOtherUserId());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("MATRIX_KEY_VERIFICATION_MAC");
        sb2.append(getOtherUserId());
        sb2.append(getOtherDeviceId());
        sb2.append(cryptoSession.getMyUserId());
        sb2.append(cryptoSession.requireCrypto().getMyDevice().deviceId);
        sb2.append(getTransactionId());
        String sb3 = sb2.toString();
        KeyVerificationMac keyVerificationMac = this.theirMac;
        if (keyVerificationMac == null) {
            Intrinsics.throwNpe();
        }
        Map<String, String> map = keyVerificationMac.mac;
        if (map == null) {
            Intrinsics.throwNpe();
        }
        String joinToString$default = CollectionsKt.joinToString$default(CollectionsKt.sorted(map.keySet()), ",", null, null, 0, null, null, 62, null);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(sb3);
        sb4.append("KEY_IDS");
        String macUsingAgreedMethod = macUsingAgreedMethod(joinToString$default, sb4.toString());
        KeyVerificationMac keyVerificationMac2 = this.theirMac;
        if (keyVerificationMac2 == null) {
            Intrinsics.throwNpe();
        }
        if (!Intrinsics.areEqual((Object) keyVerificationMac2.keys, (Object) macUsingAgreedMethod)) {
            cancel(cryptoSession, CancelCode.MismatchedKeys);
            return;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        KeyVerificationMac keyVerificationMac3 = this.theirMac;
        if (keyVerificationMac3 == null) {
            Intrinsics.throwNpe();
        }
        Map<String, String> map2 = keyVerificationMac3.mac;
        if (map2 == null) {
            Intrinsics.throwNpe();
        }
        for (String str3 : map2.keySet()) {
            String str4 = null;
            if (!StringsKt.startsWith$default(str3, "ed25519:", false, 2, null)) {
                str = str3;
            } else if (str3 != null) {
                str = str3.substring(8);
                Intrinsics.checkExpressionValueIsNotNull(str, "(this as java.lang.String).substring(startIndex)");
            } else {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            MXDeviceInfo mXDeviceInfo = (MXDeviceInfo) userDevices.get(str);
            String fingerprint = mXDeviceInfo != null ? mXDeviceInfo.fingerprint() : null;
            if (fingerprint == null) {
                String str5 = LOG_TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Verification: Could not find device ");
                sb5.append(str);
                sb5.append(" to verify");
                Log.e(str5, sb5.toString());
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(sb3);
                sb6.append(str3);
                String macUsingAgreedMethod2 = macUsingAgreedMethod(fingerprint, sb6.toString());
                KeyVerificationMac keyVerificationMac4 = this.theirMac;
                if (keyVerificationMac4 != null) {
                    Map<String, String> map3 = keyVerificationMac4.mac;
                    if (map3 != null) {
                        str4 = (String) map3.get(str3);
                    }
                }
                if (!Intrinsics.areEqual((Object) macUsingAgreedMethod2, (Object) str4)) {
                    cancel(cryptoSession, CancelCode.MismatchedKeys);
                    return;
                }
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            Log.e(LOG_TAG, "Verification: No devices verified");
            cancel(cryptoSession, CancelCode.MismatchedKeys);
            return;
        }
        for (String deviceVerified : arrayList) {
            setDeviceVerified(cryptoSession, deviceVerified, getOtherUserId(), new SASVerificationTransaction$verifyMacs$$inlined$forEach$lambda$1(this, cryptoSession), SASVerificationTransaction$verifyMacs$2$2.INSTANCE);
        }
    }

    private final void setDeviceVerified(CryptoSession cryptoSession, String str, String str2, Function0<Unit> function0, Function0<Unit> function02) {
        cryptoSession.requireCrypto().setDeviceVerification(1, str, str2, new SASVerificationTransaction$setDeviceVerified$1(this, function0, function02));
    }

    public void cancel(CryptoSession cryptoSession, CancelCode cancelCode) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(cancelCode, "code");
        this.cancelledReason = cancelCode;
        setState(SASVerificationTxState.Cancelled);
        org.matrix.androidsdk.crypto.verification.VerificationManager.Companion companion = VerificationManager.Companion;
        String transactionId = getTransactionId();
        String otherUserId = getOtherUserId();
        String otherDeviceId = getOtherDeviceId();
        if (otherDeviceId == null) {
            otherDeviceId = "";
        }
        companion.cancelTransaction(cryptoSession, transactionId, otherUserId, otherDeviceId, cancelCode);
    }

    /* access modifiers changed from: protected */
    public final void sendToOther(String str, Object obj, CryptoSession cryptoSession, SASVerificationTxState sASVerificationTxState, CancelCode cancelCode, Function0<Unit> function0) {
        String str2 = str;
        Object obj2 = obj;
        Intrinsics.checkParameterIsNotNull(str, PasswordLoginParams.IDENTIFIER_KEY_TYPE);
        Intrinsics.checkParameterIsNotNull(obj, "keyToDevice");
        CryptoSession cryptoSession2 = cryptoSession;
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        SASVerificationTxState sASVerificationTxState2 = sASVerificationTxState;
        Intrinsics.checkParameterIsNotNull(sASVerificationTxState2, "nextState");
        CancelCode cancelCode2 = cancelCode;
        Intrinsics.checkParameterIsNotNull(cancelCode2, "onErrorReason");
        MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
        mXUsersDevicesMap.setObject(obj, getOtherUserId(), getOtherDeviceId());
        CryptoRestClient cryptoRestClient = cryptoSession.requireCrypto().getCryptoRestClient();
        String transactionId = getTransactionId();
        SASVerificationTransaction$sendToOther$1 sASVerificationTransaction$sendToOther$1 = new SASVerificationTransaction$sendToOther$1(this, str, cryptoSession2, function0, sASVerificationTxState2, cancelCode2);
        cryptoRestClient.sendToDevice(str, mXUsersDevicesMap, transactionId, sASVerificationTransaction$sendToOther$1);
    }

    public final String getShortCodeRepresentation(String str) {
        Intrinsics.checkParameterIsNotNull(str, "shortAuthenticationStringMode");
        if (this.shortCodeBytes == null) {
            return null;
        }
        int hashCode = str.hashCode();
        if (hashCode != 96632902) {
            if (hashCode == 1542263633 && str.equals(KeyVerificationStart.SAS_MODE_DECIMAL)) {
                byte[] bArr = this.shortCodeBytes;
                if (bArr == null) {
                    Intrinsics.throwNpe();
                }
                if (bArr.length < 5) {
                    return null;
                }
                byte[] bArr2 = this.shortCodeBytes;
                if (bArr2 == null) {
                    Intrinsics.throwNpe();
                }
                return getDecimalCodeRepresentation(bArr2);
            }
        } else if (str.equals(KeyVerificationStart.SAS_MODE_EMOJI)) {
            byte[] bArr3 = this.shortCodeBytes;
            if (bArr3 == null) {
                Intrinsics.throwNpe();
            }
            if (bArr3.length < 6) {
                return null;
            }
            byte[] bArr4 = this.shortCodeBytes;
            if (bArr4 == null) {
                Intrinsics.throwNpe();
            }
            return CollectionsKt.joinToString$default(getEmojiCodeRepresentation(bArr4), " ", null, null, 0, null, SASVerificationTransaction$getShortCodeRepresentation$1.INSTANCE, 30, null);
        }
        return null;
    }

    public final boolean supportsEmoji() {
        KeyVerificationAccept keyVerificationAccept = this.accepted;
        if (keyVerificationAccept != null) {
            List<String> list = keyVerificationAccept.shortAuthenticationStrings;
            if (list != null && list.contains(KeyVerificationStart.SAS_MODE_EMOJI)) {
                return true;
            }
        }
        return false;
    }

    public final boolean supportsDecimal() {
        KeyVerificationAccept keyVerificationAccept = this.accepted;
        if (keyVerificationAccept != null) {
            List<String> list = keyVerificationAccept.shortAuthenticationStrings;
            if (list != null && list.contains(KeyVerificationStart.SAS_MODE_DECIMAL)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003f A[RETURN] */
    public final String hashUsingAgreedHashMethod(String str) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(str, "toHash");
        String lowerCase = "sha256".toLowerCase();
        String str2 = "(this as java.lang.String).toLowerCase()";
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, str2);
        KeyVerificationAccept keyVerificationAccept = this.accepted;
        if (keyVerificationAccept != null) {
            String str3 = keyVerificationAccept.hash;
            if (str3 != null) {
                if (str3 != null) {
                    obj = str3.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(obj, str2);
                    if (Intrinsics.areEqual((Object) lowerCase, obj)) {
                        return null;
                    }
                    OlmUtility olmUtility = new OlmUtility();
                    String sha256 = olmUtility.sha256(str);
                    olmUtility.releaseUtility();
                    return sha256;
                }
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
        }
        obj = null;
        if (Intrinsics.areEqual((Object) lowerCase, obj)) {
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0071 A[RETURN] */
    public final String macUsingAgreedMethod(String str, String str2) {
        Object obj;
        Object obj2;
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(str2, "info");
        String lowerCase = SAS_MAC_SHA256_LONGKDF.toLowerCase();
        String str3 = "(this as java.lang.String).toLowerCase()";
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, str3);
        KeyVerificationAccept keyVerificationAccept = this.accepted;
        String str4 = "null cannot be cast to non-null type java.lang.String";
        if (keyVerificationAccept != null) {
            String str5 = keyVerificationAccept.messageAuthenticationCode;
            if (str5 != null) {
                if (str5 != null) {
                    obj = str5.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(obj, str3);
                    if (!Intrinsics.areEqual((Object) lowerCase, obj)) {
                        return getSAS().calculateMacLongKdf(str, str2);
                    }
                    String lowerCase2 = SAS_MAC_SHA256.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(lowerCase2, str3);
                    KeyVerificationAccept keyVerificationAccept2 = this.accepted;
                    if (keyVerificationAccept2 != null) {
                        String str6 = keyVerificationAccept2.messageAuthenticationCode;
                        if (str6 != null) {
                            if (str6 != null) {
                                obj2 = str6.toLowerCase();
                                Intrinsics.checkExpressionValueIsNotNull(obj2, str3);
                                if (!Intrinsics.areEqual((Object) lowerCase2, obj2)) {
                                    return getSAS().calculateMac(str, str2);
                                }
                                return null;
                            }
                            throw new TypeCastException(str4);
                        }
                    }
                    obj2 = null;
                    if (!Intrinsics.areEqual((Object) lowerCase2, obj2)) {
                    }
                } else {
                    throw new TypeCastException(str4);
                }
            }
        }
        obj = null;
        if (!Intrinsics.areEqual((Object) lowerCase, obj)) {
        }
    }

    public final String getDecimalCodeRepresentation(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "byteArray");
        byte b = bArr[1] & UByte.MAX_VALUE;
        byte b2 = bArr[3] & UByte.MAX_VALUE;
        int i = (((bArr[0] & UByte.MAX_VALUE) << 5) | (b >> 3)) + 1000;
        int i2 = (((b & 7) << 10) | ((bArr[2] & UByte.MAX_VALUE) << 2) | (b2 >> 6)) + 1000;
        int i3 = (((bArr[4] & UByte.MAX_VALUE) >> 1) | ((b2 & 63) << 7)) + 1000;
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(' ');
        sb.append(i2);
        sb.append(' ');
        sb.append(i3);
        return sb.toString();
    }

    public final List<EmojiRepresentation> getEmojiCodeRepresentation(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "byteArray");
        byte b = bArr[0] & UByte.MAX_VALUE;
        byte b2 = bArr[1] & UByte.MAX_VALUE;
        byte b3 = bArr[2] & UByte.MAX_VALUE;
        byte b4 = bArr[3] & UByte.MAX_VALUE;
        byte b5 = bArr[4] & UByte.MAX_VALUE;
        return CollectionsKt.listOf(VerificationEmoji.INSTANCE.getEmojiForCode((b & 252) >> 2), VerificationEmoji.INSTANCE.getEmojiForCode(((b & 3) << 4) | ((b2 & 240) >> 4)), VerificationEmoji.INSTANCE.getEmojiForCode(((b2 & 15) << 2) | ((b3 & 192) >> 6)), VerificationEmoji.INSTANCE.getEmojiForCode(b3 & 63), VerificationEmoji.INSTANCE.getEmojiForCode((b4 & 252) >> 2), VerificationEmoji.INSTANCE.getEmojiForCode(((b4 & 3) << 4) | ((b5 & 240) >> 4)), VerificationEmoji.INSTANCE.getEmojiForCode((((bArr[5] & UByte.MAX_VALUE) & 192) >> 6) | ((b5 & 15) << 2)));
    }
}
