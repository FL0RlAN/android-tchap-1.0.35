package org.matrix.androidsdk.crypto.verification;

import com.google.gson.Gson;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationAccept;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationKey;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationKey.Companion;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationMac;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u001bB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010\u0017\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u000e\u0010\u001a\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u001c"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest;", "Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction;", "transactionId", "", "otherUserId", "otherDeviceId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "uxState", "Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest$State;", "getUxState", "()Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest$State;", "onKeyVerificationKey", "", "session", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "userId", "vKey", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationKey;", "onKeyVerificationMac", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac;", "onVerificationAccept", "accept", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept;", "onVerificationStart", "startReq", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart;", "start", "State", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: OutgoingSASVerificationRequest.kt */
public final class OutgoingSASVerificationRequest extends SASVerificationTransaction {

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/OutgoingSASVerificationRequest$State;", "", "(Ljava/lang/String;I)V", "UNKNOWN", "WAIT_FOR_START", "WAIT_FOR_KEY_AGREEMENT", "SHOW_SAS", "WAIT_FOR_VERIFICATION", "VERIFIED", "CANCELLED_BY_ME", "CANCELLED_BY_OTHER", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: OutgoingSASVerificationRequest.kt */
    public enum State {
        UNKNOWN,
        WAIT_FOR_START,
        WAIT_FOR_KEY_AGREEMENT,
        SHOW_SAS,
        WAIT_FOR_VERIFICATION,
        VERIFIED,
        CANCELLED_BY_ME,
        CANCELLED_BY_OTHER
    }

    public OutgoingSASVerificationRequest(String str, String str2, String str3) {
        Intrinsics.checkParameterIsNotNull(str, "transactionId");
        Intrinsics.checkParameterIsNotNull(str2, "otherUserId");
        Intrinsics.checkParameterIsNotNull(str3, "otherDeviceId");
        super(str, str2, str3, false);
    }

    public final State getUxState() {
        switch (getState()) {
            case None:
                return State.WAIT_FOR_START;
            case SendingStart:
            case Started:
            case OnAccepted:
            case SendingKey:
            case KeySent:
            case OnKeyReceived:
                return State.WAIT_FOR_KEY_AGREEMENT;
            case ShortCodeReady:
                return State.SHOW_SAS;
            case ShortCodeAccepted:
            case SendingMac:
            case MacSent:
            case Verifying:
                return State.WAIT_FOR_VERIFICATION;
            case Verified:
                return State.VERIFIED;
            case OnCancelled:
                return State.CANCELLED_BY_ME;
            case Cancelled:
                return State.CANCELLED_BY_OTHER;
            default:
                return State.UNKNOWN;
        }
    }

    public void onVerificationStart(CryptoSession cryptoSession, KeyVerificationStart keyVerificationStart) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(keyVerificationStart, "startReq");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## onVerificationStart - unexpected id:");
        sb.append(getTransactionId());
        Log.e(log_tag, sb.toString());
        cancel(cryptoSession, CancelCode.UnexpectedMessage);
    }

    public final void start(CryptoSession cryptoSession) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        if (getState() == SASVerificationTxState.None) {
            KeyVerificationStart keyVerificationStart = new KeyVerificationStart();
            MXDeviceInfo myDevice = cryptoSession.requireCrypto().getMyDevice();
            keyVerificationStart.fromDevice = myDevice != null ? myDevice.deviceId : null;
            keyVerificationStart.method = KeyVerificationStart.VERIF_METHOD_SAS;
            keyVerificationStart.transactionID = getTransactionId();
            keyVerificationStart.keyAgreementProtocols = SASVerificationTransaction.Companion.getKNOWN_AGREEMENT_PROTOCOLS();
            keyVerificationStart.hashes = SASVerificationTransaction.Companion.getKNOWN_HASHES();
            keyVerificationStart.messageAuthenticationCodes = SASVerificationTransaction.Companion.getKNOWN_MACS();
            keyVerificationStart.shortAuthenticationStrings = SASVerificationTransaction.Companion.getKNOWN_SHORT_CODES();
            setStartReq(keyVerificationStart);
            new MXUsersDevicesMap().setObject(keyVerificationStart, getOtherUserId(), getOtherDeviceId());
            setState(SASVerificationTxState.SendingStart);
            sendToOther("m.key.verification.start", keyVerificationStart, cryptoSession, SASVerificationTxState.Started, CancelCode.User, null);
            return;
        }
        Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## start verification from invalid state");
        throw new IllegalStateException("Interactive Key verification already started");
    }

    public void onVerificationAccept(CryptoSession cryptoSession, KeyVerificationAccept keyVerificationAccept) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(keyVerificationAccept, "accept");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## onVerificationAccept id:");
        sb.append(getTransactionId());
        Log.d(log_tag, sb.toString());
        if (getState() != SASVerificationTxState.Started) {
            String log_tag2 = SASVerificationTransaction.Companion.getLOG_TAG();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## received accept request from invalid state ");
            sb2.append(getState());
            Log.e(log_tag2, sb2.toString());
            cancel(cryptoSession, CancelCode.UnexpectedMessage);
            return;
        }
        if (CollectionsKt.contains(SASVerificationTransaction.Companion.getKNOWN_AGREEMENT_PROTOCOLS(), keyVerificationAccept.keyAgreementProtocol) && CollectionsKt.contains(SASVerificationTransaction.Companion.getKNOWN_HASHES(), keyVerificationAccept.hash) && CollectionsKt.contains(SASVerificationTransaction.Companion.getKNOWN_MACS(), keyVerificationAccept.messageAuthenticationCode)) {
            List<String> list = keyVerificationAccept.shortAuthenticationStrings;
            if (list == null) {
                Intrinsics.throwNpe();
            }
            if (!CollectionsKt.intersect(list, SASVerificationTransaction.Companion.getKNOWN_SHORT_CODES()).isEmpty()) {
                setAccepted(keyVerificationAccept);
                setState(SASVerificationTxState.OnAccepted);
                String publicKey = getSAS().getPublicKey();
                Companion companion = KeyVerificationKey.Companion;
                String transactionId = getTransactionId();
                Intrinsics.checkExpressionValueIsNotNull(publicKey, "pubKey");
                KeyVerificationKey create = companion.create(transactionId, publicKey);
                setState(SASVerificationTxState.SendingKey);
                sendToOther("m.key.verification.key", create, cryptoSession, SASVerificationTxState.KeySent, CancelCode.User, new OutgoingSASVerificationRequest$onVerificationAccept$1(this));
                return;
            }
        }
        Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## received accept request from invalid state");
        cancel(cryptoSession, CancelCode.UnknownMethod);
    }

    public void onKeyVerificationKey(CryptoSession cryptoSession, String str, KeyVerificationKey keyVerificationKey) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(str, "userId");
        Intrinsics.checkParameterIsNotNull(keyVerificationKey, "vKey");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## onKeyVerificationKey id:");
        sb.append(getTransactionId());
        Log.d(log_tag, sb.toString());
        if (getState() == SASVerificationTxState.SendingKey || getState() == SASVerificationTxState.KeySent) {
            setOtherKey(keyVerificationKey.key);
            String str2 = keyVerificationKey.key;
            Gson basicGson = JsonUtility.getBasicGson();
            KeyVerificationStart startReq = getStartReq();
            if (startReq == null) {
                Intrinsics.throwNpe();
            }
            String hashUsingAgreedHashMethod = hashUsingAgreedHashMethod(Intrinsics.stringPlus(str2, JsonUtility.canonicalize(basicGson.toJsonTree(startReq)).toString()));
            if (hashUsingAgreedHashMethod == null) {
                hashUsingAgreedHashMethod = "";
            }
            KeyVerificationAccept accepted = getAccepted();
            if (accepted == null) {
                Intrinsics.throwNpe();
            }
            if (StringsKt.equals$default(accepted.commitment, hashUsingAgreedHashMethod, false, 2, null)) {
                getSAS().setTheirPublicKey(getOtherKey());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("MATRIX_KEY_VERIFICATION_SAS");
                sb2.append(cryptoSession.getMyUserId());
                sb2.append(cryptoSession.requireCrypto().getMyDevice().deviceId);
                sb2.append(getOtherUserId());
                sb2.append(getOtherDeviceId());
                sb2.append(getTransactionId());
                setShortCodeBytes(getSAS().generateShortCode(sb2.toString(), 6));
                setState(SASVerificationTxState.ShortCodeReady);
            } else {
                cancel(cryptoSession, CancelCode.MismatchedCommitment);
            }
            return;
        }
        String log_tag2 = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("## received key from invalid state ");
        sb3.append(getState());
        Log.e(log_tag2, sb3.toString());
        cancel(cryptoSession, CancelCode.UnexpectedMessage);
    }

    public void onKeyVerificationMac(CryptoSession cryptoSession, KeyVerificationMac keyVerificationMac) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(keyVerificationMac, "vKey");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## onKeyVerificationMac id:");
        sb.append(getTransactionId());
        Log.d(log_tag, sb.toString());
        if (getState() == SASVerificationTxState.OnKeyReceived || getState() == SASVerificationTxState.ShortCodeReady || getState() == SASVerificationTxState.ShortCodeAccepted || getState() == SASVerificationTxState.SendingMac || getState() == SASVerificationTxState.MacSent) {
            setTheirMac(keyVerificationMac);
            if (getMyMac() != null) {
                verifyMacs(cryptoSession);
            }
            return;
        }
        String log_tag2 = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## received key from invalid state ");
        sb2.append(getState());
        Log.e(log_tag2, sb2.toString());
        cancel(cryptoSession, CancelCode.UnexpectedMessage);
    }
}
