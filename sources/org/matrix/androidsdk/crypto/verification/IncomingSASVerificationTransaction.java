package org.matrix.androidsdk.crypto.verification;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.MXCrypto;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationAccept;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationKey;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationKey.Companion;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationMac;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u001bB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J \u0010\u0010\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0015H\u0016J\u0018\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0018\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u000e\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rR\u0011\u0010\u0006\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u001c"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/IncomingSASVerificationTransaction;", "Lorg/matrix/androidsdk/crypto/verification/SASVerificationTransaction;", "transactionId", "", "otherUserID", "(Ljava/lang/String;Ljava/lang/String;)V", "uxState", "Lorg/matrix/androidsdk/crypto/verification/IncomingSASVerificationTransaction$State;", "getUxState", "()Lorg/matrix/androidsdk/crypto/verification/IncomingSASVerificationTransaction$State;", "doAccept", "", "session", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "accept", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationAccept;", "onKeyVerificationKey", "userId", "vKey", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationKey;", "onKeyVerificationMac", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationMac;", "onVerificationAccept", "onVerificationStart", "startReq", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart;", "performAccept", "State", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: IncomingSASVerificationTransaction.kt */
public final class IncomingSASVerificationTransaction extends SASVerificationTransaction {

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/IncomingSASVerificationTransaction$State;", "", "(Ljava/lang/String;I)V", "UNKNOWN", "SHOW_ACCEPT", "WAIT_FOR_KEY_AGREEMENT", "SHOW_SAS", "WAIT_FOR_VERIFICATION", "VERIFIED", "CANCELLED_BY_ME", "CANCELLED_BY_OTHER", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: IncomingSASVerificationTransaction.kt */
    public enum State {
        UNKNOWN,
        SHOW_ACCEPT,
        WAIT_FOR_KEY_AGREEMENT,
        SHOW_SAS,
        WAIT_FOR_VERIFICATION,
        VERIFIED,
        CANCELLED_BY_ME,
        CANCELLED_BY_OTHER
    }

    public IncomingSASVerificationTransaction(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "transactionId");
        Intrinsics.checkParameterIsNotNull(str2, "otherUserID");
        super(str, str2, null, true);
    }

    public final State getUxState() {
        switch (getState()) {
            case OnStarted:
                return State.SHOW_ACCEPT;
            case SendingAccept:
            case Accepted:
            case OnKeyReceived:
            case SendingKey:
            case KeySent:
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
            case Cancelled:
                return State.CANCELLED_BY_ME;
            case OnCancelled:
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
        sb.append("## SAS received verification request from state ");
        sb.append(getState());
        Log.d(log_tag, sb.toString());
        if (getState() == SASVerificationTxState.None) {
            setStartReq(keyVerificationStart);
            setState(SASVerificationTxState.OnStarted);
            setOtherDeviceId(keyVerificationStart.fromDevice);
            return;
        }
        Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## received verification request from invalid state");
        throw new IllegalStateException("Interactive Key verification already started");
    }

    public final void performAccept(CryptoSession cryptoSession) {
        String str;
        String str2;
        String str3;
        boolean z;
        boolean z2;
        Object obj;
        Object obj2;
        Object obj3;
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        if (getState() != SASVerificationTxState.OnStarted) {
            String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
            StringBuilder sb = new StringBuilder();
            sb.append("## Cannot perform accept from state ");
            sb.append(getState());
            Log.e(log_tag, sb.toString());
            return;
        }
        KeyVerificationStart startReq = getStartReq();
        if (startReq == null) {
            Intrinsics.throwNpe();
        }
        List<String> list = startReq.keyAgreementProtocols;
        List list2 = null;
        if (list != null) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj3 = null;
                    break;
                }
                obj3 = it.next();
                if (SASVerificationTransaction.Companion.getKNOWN_AGREEMENT_PROTOCOLS().contains((String) obj3)) {
                    break;
                }
            }
            str = (String) obj3;
        } else {
            str = null;
        }
        KeyVerificationStart startReq2 = getStartReq();
        if (startReq2 == null) {
            Intrinsics.throwNpe();
        }
        List<String> list3 = startReq2.hashes;
        if (list3 != null) {
            Iterator it2 = list3.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    obj2 = null;
                    break;
                }
                obj2 = it2.next();
                if (SASVerificationTransaction.Companion.getKNOWN_HASHES().contains((String) obj2)) {
                    break;
                }
            }
            str2 = (String) obj2;
        } else {
            str2 = null;
        }
        KeyVerificationStart startReq3 = getStartReq();
        if (startReq3 == null) {
            Intrinsics.throwNpe();
        }
        List<String> list4 = startReq3.messageAuthenticationCodes;
        if (list4 != null) {
            Iterator it3 = list4.iterator();
            while (true) {
                if (!it3.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it3.next();
                if (SASVerificationTransaction.Companion.getKNOWN_MACS().contains((String) obj)) {
                    break;
                }
            }
            str3 = (String) obj;
        } else {
            str3 = null;
        }
        KeyVerificationStart startReq4 = getStartReq();
        if (startReq4 == null) {
            Intrinsics.throwNpe();
        }
        List<String> list5 = startReq4.shortAuthenticationStrings;
        if (list5 != null) {
            Iterable iterable = list5;
            Collection arrayList = new ArrayList();
            for (Object next : iterable) {
                if (SASVerificationTransaction.Companion.getKNOWN_SHORT_CODES().contains((String) next)) {
                    arrayList.add(next);
                }
            }
            list2 = (List) arrayList;
        }
        List list6 = list2;
        boolean z3 = false;
        Iterable listOf = CollectionsKt.listOf(str, str2, str3);
        if (!(listOf instanceof Collection) || !((Collection) listOf).isEmpty()) {
            Iterator it4 = listOf.iterator();
            while (true) {
                if (!it4.hasNext()) {
                    break;
                }
                CharSequence charSequence = (String) it4.next();
                if (charSequence == null || StringsKt.isBlank(charSequence)) {
                    z2 = true;
                    continue;
                } else {
                    z2 = false;
                    continue;
                }
                if (z2) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (!z) {
            Collection collection = list6;
            if (collection == null || collection.isEmpty()) {
                z3 = true;
            }
            if (!z3) {
                MXCrypto requireCrypto = cryptoSession.requireCrypto();
                String otherUserId = getOtherUserId();
                String otherDeviceId = getOtherDeviceId();
                IncomingSASVerificationTransaction$performAccept$2 incomingSASVerificationTransaction$performAccept$2 = new IncomingSASVerificationTransaction$performAccept$2(this, cryptoSession, str, str2, str3, list6);
                requireCrypto.getDeviceInfo(otherUserId, otherDeviceId, incomingSASVerificationTransaction$performAccept$2);
                return;
            }
        }
        Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## Failed to find agreement ");
        cancel(cryptoSession, CancelCode.UnknownMethod);
    }

    /* access modifiers changed from: private */
    public final void doAccept(CryptoSession cryptoSession, KeyVerificationAccept keyVerificationAccept) {
        setAccepted(keyVerificationAccept);
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS accept request id:");
        sb.append(getTransactionId());
        Log.d(log_tag, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getSAS().getPublicKey());
        Gson basicGson = JsonUtility.getBasicGson();
        KeyVerificationStart startReq = getStartReq();
        if (startReq == null) {
            Intrinsics.throwNpe();
        }
        sb2.append(JsonUtility.canonicalize(basicGson.toJsonTree(startReq)).toString());
        String hashUsingAgreedHashMethod = hashUsingAgreedHashMethod(sb2.toString());
        if (hashUsingAgreedHashMethod == null) {
            hashUsingAgreedHashMethod = "";
        }
        keyVerificationAccept.commitment = hashUsingAgreedHashMethod;
        setState(SASVerificationTxState.SendingAccept);
        sendToOther("m.key.verification.accept", keyVerificationAccept, cryptoSession, SASVerificationTxState.Accepted, CancelCode.User, new IncomingSASVerificationTransaction$doAccept$1(this));
    }

    public void onVerificationAccept(CryptoSession cryptoSession, KeyVerificationAccept keyVerificationAccept) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(keyVerificationAccept, "accept");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS invalid message for incoming request id:");
        sb.append(getTransactionId());
        Log.d(log_tag, sb.toString());
        cancel(cryptoSession, CancelCode.UnexpectedMessage);
    }

    public void onKeyVerificationKey(CryptoSession cryptoSession, String str, KeyVerificationKey keyVerificationKey) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(str, "userId");
        Intrinsics.checkParameterIsNotNull(keyVerificationKey, "vKey");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS received key for request id:");
        sb.append(getTransactionId());
        Log.d(log_tag, sb.toString());
        if (getState() == SASVerificationTxState.SendingAccept || getState() == SASVerificationTxState.Accepted) {
            setOtherKey(keyVerificationKey.key);
            String publicKey = getSAS().getPublicKey();
            Companion companion = KeyVerificationKey.Companion;
            String transactionId = getTransactionId();
            Intrinsics.checkExpressionValueIsNotNull(publicKey, "pubKey");
            KeyVerificationKey create = companion.create(transactionId, publicKey);
            setState(SASVerificationTxState.SendingKey);
            sendToOther("m.key.verification.key", create, cryptoSession, SASVerificationTxState.KeySent, CancelCode.User, new IncomingSASVerificationTransaction$onKeyVerificationKey$1(this));
            getSAS().setTheirPublicKey(getOtherKey());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("MATRIX_KEY_VERIFICATION_SAS");
            sb2.append(getOtherUserId());
            sb2.append(getOtherDeviceId());
            sb2.append(cryptoSession.getMyUserId());
            sb2.append(cryptoSession.requireCrypto().getMyDevice().deviceId);
            sb2.append(getTransactionId());
            setShortCodeBytes(getSAS().generateShortCode(sb2.toString(), 6));
            String log_tag2 = SASVerificationTransaction.Companion.getLOG_TAG();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("************  BOB CODE ");
            byte[] shortCodeBytes = getShortCodeBytes();
            if (shortCodeBytes == null) {
                Intrinsics.throwNpe();
            }
            sb3.append(getDecimalCodeRepresentation(shortCodeBytes));
            Log.e(log_tag2, sb3.toString());
            String log_tag3 = SASVerificationTransaction.Companion.getLOG_TAG();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("************  BOB EMOJI CODE ");
            sb4.append(getShortCodeRepresentation(KeyVerificationStart.SAS_MODE_EMOJI));
            Log.e(log_tag3, sb4.toString());
            setState(SASVerificationTxState.ShortCodeReady);
            return;
        }
        String log_tag4 = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("## received key from invalid state ");
        sb5.append(getState());
        Log.e(log_tag4, sb5.toString());
        cancel(cryptoSession, CancelCode.UnexpectedMessage);
    }

    public void onKeyVerificationMac(CryptoSession cryptoSession, KeyVerificationMac keyVerificationMac) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        Intrinsics.checkParameterIsNotNull(keyVerificationMac, "vKey");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS received mac for request id:");
        sb.append(getTransactionId());
        Log.d(log_tag, sb.toString());
        if (getState() == SASVerificationTxState.SendingKey || getState() == SASVerificationTxState.KeySent || getState() == SASVerificationTxState.ShortCodeReady || getState() == SASVerificationTxState.ShortCodeAccepted || getState() == SASVerificationTxState.SendingMac || getState() == SASVerificationTxState.MacSent) {
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
