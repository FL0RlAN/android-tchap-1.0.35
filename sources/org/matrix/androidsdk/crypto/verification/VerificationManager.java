package org.matrix.androidsdk.crypto.verification;

import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.MXDeviceList;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationAccept;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationCancel;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationKey;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationMac;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;
import org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 92\u00020\u0001:\u00029:B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0007J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\rH\u0002J \u0010\u0016\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\fJ\u0018\u0010\u001a\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\fJH\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0018\u0010\u001f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0!\u0012\u0004\u0012\u00020\u00120 2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00120$H\u0002J\u0018\u0010%\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\fH\u0002J\u0010\u0010&\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\rH\u0002J\u0010\u0010'\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\rH\u0002J\u0018\u0010(\u001a\u0004\u0018\u00010\r2\u0006\u0010)\u001a\u00020\f2\u0006\u0010*\u001a\u00020\fJ\u0018\u0010+\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010,2\u0006\u0010)\u001a\u00020\fH\u0002J\u0016\u0010-\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\fJ\u0010\u0010.\u001a\u00020\u00122\u0006\u0010/\u001a\u000200H\u0002J\u0010\u00101\u001a\u00020\u00122\u0006\u0010/\u001a\u000200H\u0002J\u0010\u00102\u001a\u00020\u00122\u0006\u0010/\u001a\u000200H\u0002J\u0010\u00103\u001a\u00020\u00122\u0006\u0010/\u001a\u000200H\u0002J\u0010\u00104\u001a\u00020\u00122\u0006\u0010/\u001a\u000200H\u0002J\u000e\u00105\u001a\u00020\u00122\u0006\u0010/\u001a\u000200J\u000e\u00106\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0007J\u0018\u00107\u001a\u00020\u00122\u0006\u0010)\u001a\u00020\f2\u0006\u0010*\u001a\u00020\fH\u0002J\u0010\u00108\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\rH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tRb\u0010\n\u001aV\u0012\u0004\u0012\u00020\f\u0012 \u0012\u001e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bj\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r`\u000e0\u000bj*\u0012\u0004\u0012\u00020\f\u0012 \u0012\u001e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bj\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r`\u000e`\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000¨\u0006;"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationManager;", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction$Listener;", "session", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "(Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;)V", "listeners", "Ljava/util/ArrayList;", "Lorg/matrix/androidsdk/crypto/verification/VerificationManager$VerificationManagerListener;", "getSession", "()Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "txMap", "Ljava/util/HashMap;", "", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "Lkotlin/collections/HashMap;", "uiHandler", "Landroid/os/Handler;", "addListener", "", "listener", "addTransaction", "tx", "beginKeyVerification", "method", "userId", "deviceID", "beginKeyVerificationSAS", "checkKeysAreDownloaded", "otherUserId", "startReq", "Lorg/matrix/androidsdk/crypto/rest/model/crypto/KeyVerificationStart;", "success", "Lkotlin/Function1;", "Lorg/matrix/androidsdk/crypto/data/MXUsersDevicesMap;", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "error", "Lkotlin/Function0;", "createUniqueIDForTransaction", "dispatchTxAdded", "dispatchTxUpdated", "getExistingTransaction", "otherUser", "tid", "getExistingTransactionsForUser", "", "markedLocallyAsManuallyVerified", "onAcceptReceived", "event", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoEvent;", "onCancelReceived", "onKeyReceived", "onMacReceived", "onStartRequestReceived", "onToDeviceEvent", "removeListener", "removeTransaction", "transactionUpdated", "Companion", "VerificationManagerListener", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
public final class VerificationManager implements Listener {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final String LOG_TAG;
    /* access modifiers changed from: private */
    public ArrayList<VerificationManagerListener> listeners = new ArrayList<>();
    private final CryptoSession session;
    private final HashMap<String, HashMap<String, VerificationTransaction>> txMap = new HashMap<>();
    /* access modifiers changed from: private */
    public final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J.\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationManager$Companion;", "", "()V", "LOG_TAG", "", "getLOG_TAG", "()Ljava/lang/String;", "cancelTransaction", "", "session", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoSession;", "transactionId", "userId", "userDevice", "code", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VerificationManager.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getLOG_TAG() {
            return VerificationManager.LOG_TAG;
        }

        public final void cancelTransaction(CryptoSession cryptoSession, String str, String str2, String str3, CancelCode cancelCode) {
            Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
            Intrinsics.checkParameterIsNotNull(str, "transactionId");
            Intrinsics.checkParameterIsNotNull(str2, "userId");
            Intrinsics.checkParameterIsNotNull(str3, "userDevice");
            Intrinsics.checkParameterIsNotNull(cancelCode, "code");
            KeyVerificationCancel create = KeyVerificationCancel.Companion.create(str, cancelCode);
            MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
            mXUsersDevicesMap.setObject(create, str2, str3);
            cryptoSession.requireCrypto().getCryptoRestClient().sendToDevice("m.key.verification.cancel", mXUsersDevicesMap, str, new VerificationManager$Companion$cancelTransaction$1(str, cancelCode));
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH&¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/crypto/verification/VerificationManager$VerificationManagerListener;", "", "markedAsManuallyVerified", "", "userId", "", "deviceId", "transactionCreated", "tx", "Lorg/matrix/androidsdk/crypto/verification/VerificationTransaction;", "transactionUpdated", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VerificationManager.kt */
    public interface VerificationManagerListener {
        void markedAsManuallyVerified(String str, String str2);

        void transactionCreated(VerificationTransaction verificationTransaction);

        void transactionUpdated(VerificationTransaction verificationTransaction);
    }

    public VerificationManager(CryptoSession cryptoSession) {
        Intrinsics.checkParameterIsNotNull(cryptoSession, "session");
        this.session = cryptoSession;
    }

    public final CryptoSession getSession() {
        return this.session;
    }

    public final void onToDeviceEvent(CryptoEvent cryptoEvent) {
        Intrinsics.checkParameterIsNotNull(cryptoEvent, NotificationCompat.CATEGORY_EVENT);
        this.session.requireCrypto().getDecryptingThreadHandler().post(new VerificationManager$onToDeviceEvent$1(this, cryptoEvent));
    }

    public final void addListener(VerificationManagerListener verificationManagerListener) {
        Intrinsics.checkParameterIsNotNull(verificationManagerListener, CastExtraArgs.LISTENER);
        this.uiHandler.post(new VerificationManager$addListener$1(this, verificationManagerListener));
    }

    public final void removeListener(VerificationManagerListener verificationManagerListener) {
        Intrinsics.checkParameterIsNotNull(verificationManagerListener, CastExtraArgs.LISTENER);
        this.uiHandler.post(new VerificationManager$removeListener$1(this, verificationManagerListener));
    }

    private final void dispatchTxAdded(VerificationTransaction verificationTransaction) {
        this.uiHandler.post(new VerificationManager$dispatchTxAdded$1(this, verificationTransaction));
    }

    private final void dispatchTxUpdated(VerificationTransaction verificationTransaction) {
        this.uiHandler.post(new VerificationManager$dispatchTxUpdated$1(this, verificationTransaction));
    }

    public final void markedLocallyAsManuallyVerified(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "userId");
        Intrinsics.checkParameterIsNotNull(str2, "deviceID");
        this.session.requireCrypto().setDeviceVerification(1, str2, str, new VerificationManager$markedLocallyAsManuallyVerified$1(this, str, str2));
    }

    /* access modifiers changed from: private */
    public final void onStartRequestReceived(CryptoEvent cryptoEvent) {
        String str;
        KeyVerificationStart keyVerificationStart = (KeyVerificationStart) JsonUtility.getBasicGson().fromJson(cryptoEvent.getContent(), KeyVerificationStart.class);
        String sender = cryptoEvent.getSender();
        if (!keyVerificationStart.isValid()) {
            Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## received invalid verification request");
            if (keyVerificationStart.transactionID != null) {
                Companion companion = Companion;
                CryptoSession cryptoSession = this.session;
                String str2 = keyVerificationStart.transactionID;
                if (str2 == null) {
                    Intrinsics.throwNpe();
                }
                if (keyVerificationStart != null) {
                    String str3 = keyVerificationStart.fromDevice;
                    if (str3 != null) {
                        str = str3;
                        companion.cancelTransaction(cryptoSession, str2, sender, str, CancelCode.UnknownMethod);
                    }
                }
                str = cryptoEvent.getSenderKey();
                companion.cancelTransaction(cryptoSession, str2, sender, str, CancelCode.UnknownMethod);
            }
            return;
        }
        CryptoSession cryptoSession2 = this.session;
        Intrinsics.checkExpressionValueIsNotNull(keyVerificationStart, "startReq");
        checkKeysAreDownloaded(cryptoSession2, sender, keyVerificationStart, new VerificationManager$onStartRequestReceived$1(this, keyVerificationStart, sender, cryptoEvent), new VerificationManager$onStartRequestReceived$2(this, keyVerificationStart, sender));
    }

    private final void checkKeysAreDownloaded(CryptoSession cryptoSession, String str, KeyVerificationStart keyVerificationStart, Function1<? super MXUsersDevicesMap<MXDeviceInfo>, Unit> function1, Function0<Unit> function0) {
        MXDeviceList deviceList = cryptoSession.requireCrypto().getDeviceList();
        List listOf = CollectionsKt.listOf(str);
        VerificationManager$checkKeysAreDownloaded$1 verificationManager$checkKeysAreDownloaded$1 = new VerificationManager$checkKeysAreDownloaded$1(cryptoSession, function0, str, keyVerificationStart, function1);
        deviceList.downloadKeys(listOf, true, verificationManager$checkKeysAreDownloaded$1);
    }

    /* access modifiers changed from: private */
    public final void onCancelReceived(CryptoEvent cryptoEvent) {
        Log.d(LOG_TAG, "## SAS onCancelReceived");
        KeyVerificationCancel keyVerificationCancel = (KeyVerificationCancel) JsonUtility.getBasicGson().fromJson(cryptoEvent.getContent(), KeyVerificationCancel.class);
        if (!keyVerificationCancel.isValid()) {
            Log.e(LOG_TAG, "## Received invalid accept request");
            return;
        }
        String sender = cryptoEvent.getSender();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS onCancelReceived otherUser:");
        sb.append(sender);
        sb.append(" reason:");
        sb.append(keyVerificationCancel.reason);
        Log.d(str, sb.toString());
        String str2 = keyVerificationCancel.transactionID;
        if (str2 == null) {
            Intrinsics.throwNpe();
        }
        VerificationTransaction existingTransaction = getExistingTransaction(sender, str2);
        if (existingTransaction == null) {
            Log.e(LOG_TAG, "## Received invalid cancel request");
            return;
        }
        if (existingTransaction instanceof SASVerificationTransaction) {
            SASVerificationTransaction sASVerificationTransaction = (SASVerificationTransaction) existingTransaction;
            sASVerificationTransaction.setCancelledReason(CancelCodeKt.safeValueOf(keyVerificationCancel.code));
            sASVerificationTransaction.setState(SASVerificationTxState.OnCancelled);
        }
    }

    /* access modifiers changed from: private */
    public final void onAcceptReceived(CryptoEvent cryptoEvent) {
        KeyVerificationAccept keyVerificationAccept = (KeyVerificationAccept) JsonUtility.getBasicGson().fromJson(cryptoEvent.getContent(), KeyVerificationAccept.class);
        String str = "## Received invalid accept request";
        if (!keyVerificationAccept.isValid()) {
            Log.e(LOG_TAG, str);
            return;
        }
        String sender = cryptoEvent.getSender();
        String str2 = keyVerificationAccept.transactionID;
        if (str2 == null) {
            Intrinsics.throwNpe();
        }
        VerificationTransaction existingTransaction = getExistingTransaction(sender, str2);
        if (existingTransaction == null) {
            Log.e(LOG_TAG, str);
            return;
        }
        if (existingTransaction instanceof SASVerificationTransaction) {
            CryptoSession cryptoSession = this.session;
            Intrinsics.checkExpressionValueIsNotNull(keyVerificationAccept, "acceptReq");
            existingTransaction.acceptToDeviceEvent(cryptoSession, sender, keyVerificationAccept);
        }
    }

    /* access modifiers changed from: private */
    public final void onKeyReceived(CryptoEvent cryptoEvent) {
        KeyVerificationKey keyVerificationKey = (KeyVerificationKey) JsonUtility.getBasicGson().fromJson(cryptoEvent.getContent(), KeyVerificationKey.class);
        if (!keyVerificationKey.isValid()) {
            Log.e(LOG_TAG, "## Received invalid key request");
            return;
        }
        String sender = cryptoEvent.getSender();
        String str = keyVerificationKey.transactionID;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        VerificationTransaction existingTransaction = getExistingTransaction(sender, str);
        if (existingTransaction == null) {
            Log.e(LOG_TAG, "## Received invalid accept request");
            return;
        }
        if (existingTransaction instanceof SASVerificationTransaction) {
            CryptoSession cryptoSession = this.session;
            Intrinsics.checkExpressionValueIsNotNull(keyVerificationKey, "keyReq");
            existingTransaction.acceptToDeviceEvent(cryptoSession, sender, keyVerificationKey);
        }
    }

    /* access modifiers changed from: private */
    public final void onMacReceived(CryptoEvent cryptoEvent) {
        KeyVerificationMac keyVerificationMac = (KeyVerificationMac) JsonUtility.getBasicGson().fromJson(cryptoEvent.getContent(), KeyVerificationMac.class);
        if (!keyVerificationMac.isValid()) {
            Log.e(LOG_TAG, "## Received invalid key request");
            return;
        }
        String sender = cryptoEvent.getSender();
        String str = keyVerificationMac.transactionID;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        VerificationTransaction existingTransaction = getExistingTransaction(sender, str);
        if (existingTransaction == null) {
            Log.e(LOG_TAG, "## Received invalid accept request");
            return;
        }
        if (existingTransaction instanceof SASVerificationTransaction) {
            CryptoSession cryptoSession = this.session;
            Intrinsics.checkExpressionValueIsNotNull(keyVerificationMac, "macReq");
            existingTransaction.acceptToDeviceEvent(cryptoSession, sender, keyVerificationMac);
        }
    }

    public final VerificationTransaction getExistingTransaction(String str, String str2) {
        VerificationTransaction verificationTransaction;
        Intrinsics.checkParameterIsNotNull(str, "otherUser");
        Intrinsics.checkParameterIsNotNull(str2, "tid");
        synchronized (this.txMap) {
            HashMap hashMap = (HashMap) this.txMap.get(str);
            verificationTransaction = hashMap != null ? (VerificationTransaction) hashMap.get(str2) : null;
        }
        return verificationTransaction;
    }

    /* access modifiers changed from: private */
    public final Collection<VerificationTransaction> getExistingTransactionsForUser(String str) {
        Collection<VerificationTransaction> values;
        synchronized (this.txMap) {
            HashMap hashMap = (HashMap) this.txMap.get(str);
            values = hashMap != null ? hashMap.values() : null;
        }
        return values;
    }

    private final void removeTransaction(String str, String str2) {
        synchronized (this.txMap) {
            HashMap hashMap = (HashMap) this.txMap.get(str);
            if (hashMap != null) {
                VerificationTransaction verificationTransaction = (VerificationTransaction) hashMap.remove(str2);
                if (verificationTransaction != null) {
                    verificationTransaction.removeListener(this);
                    Unit unit = Unit.INSTANCE;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final void addTransaction(VerificationTransaction verificationTransaction) {
        String otherUserId = verificationTransaction.getOtherUserId();
        synchronized (this.txMap) {
            if (this.txMap.get(otherUserId) == null) {
                this.txMap.put(otherUserId, new HashMap());
            }
            HashMap hashMap = (HashMap) this.txMap.get(otherUserId);
            if (hashMap != null) {
                hashMap.put(verificationTransaction.getTransactionId(), verificationTransaction);
            }
            dispatchTxAdded(verificationTransaction);
            verificationTransaction.addListener(this);
            Unit unit = Unit.INSTANCE;
        }
    }

    public final String beginKeyVerificationSAS(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "userId");
        Intrinsics.checkParameterIsNotNull(str2, "deviceID");
        return beginKeyVerification(KeyVerificationStart.VERIF_METHOD_SAS, str, str2);
    }

    public final String beginKeyVerification(String str, String str2, String str3) {
        Intrinsics.checkParameterIsNotNull(str, "method");
        Intrinsics.checkParameterIsNotNull(str2, "userId");
        Intrinsics.checkParameterIsNotNull(str3, "deviceID");
        String createUniqueIDForTransaction = createUniqueIDForTransaction(str2, str3);
        if (Intrinsics.areEqual((Object) KeyVerificationStart.VERIF_METHOD_SAS, (Object) str)) {
            OutgoingSASVerificationRequest outgoingSASVerificationRequest = new OutgoingSASVerificationRequest(createUniqueIDForTransaction, str2, str3);
            addTransaction(outgoingSASVerificationRequest);
            this.session.requireCrypto().getDecryptingThreadHandler().post(new VerificationManager$beginKeyVerification$1(this, outgoingSASVerificationRequest));
            return createUniqueIDForTransaction;
        }
        throw new IllegalArgumentException("Unknown verification method");
    }

    private final String createUniqueIDForTransaction(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.session.getMyUserId());
        String str3 = "|";
        stringBuffer.append(str3);
        stringBuffer.append(this.session.requireCrypto().getMyDevice().deviceId);
        stringBuffer.append(str3);
        stringBuffer.append(str);
        stringBuffer.append(str3);
        stringBuffer.append(str2);
        stringBuffer.append(str3);
        stringBuffer.append(UUID.randomUUID().toString());
        String stringBuffer2 = stringBuffer.toString();
        Intrinsics.checkExpressionValueIsNotNull(stringBuffer2, "buff.toString()");
        return stringBuffer2;
    }

    public void transactionUpdated(VerificationTransaction verificationTransaction) {
        Intrinsics.checkParameterIsNotNull(verificationTransaction, "tx");
        dispatchTxUpdated(verificationTransaction);
        if (verificationTransaction instanceof SASVerificationTransaction) {
            SASVerificationTransaction sASVerificationTransaction = (SASVerificationTransaction) verificationTransaction;
            if (sASVerificationTransaction.getState() == SASVerificationTxState.Cancelled || sASVerificationTransaction.getState() == SASVerificationTxState.OnCancelled || sASVerificationTransaction.getState() == SASVerificationTxState.Verified) {
                removeTransaction(verificationTransaction.getOtherUserId(), verificationTransaction.getTransactionId());
            }
        }
    }

    static {
        String name = VerificationManager.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "VerificationManager::class.java.name");
        LOG_TAG = name;
    }
}
