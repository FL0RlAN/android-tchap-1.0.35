package org.matrix.androidsdk.crypto.verification;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;
import org.matrix.androidsdk.crypto.verification.VerificationManager.Companion;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Lorg/matrix/androidsdk/crypto/data/MXUsersDevicesMap;", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
final class VerificationManager$onStartRequestReceived$1 extends Lambda implements Function1<MXUsersDevicesMap<MXDeviceInfo>, Unit> {
    final /* synthetic */ CryptoEvent $event;
    final /* synthetic */ String $otherUserId;
    final /* synthetic */ KeyVerificationStart $startReq;
    final /* synthetic */ VerificationManager this$0;

    VerificationManager$onStartRequestReceived$1(VerificationManager verificationManager, KeyVerificationStart keyVerificationStart, String str, CryptoEvent cryptoEvent) {
        this.this$0 = verificationManager;
        this.$startReq = keyVerificationStart;
        this.$otherUserId = str;
        this.$event = cryptoEvent;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((MXUsersDevicesMap) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
        Intrinsics.checkParameterIsNotNull(mXUsersDevicesMap, "it");
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS onStartRequestReceived ");
        String str = this.$startReq.transactionID;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        sb.append(str);
        Log.d(log_tag, sb.toString());
        String str2 = this.$startReq.transactionID;
        if (str2 == null) {
            Intrinsics.throwNpe();
        }
        VerificationTransaction existingTransaction = this.this$0.getExistingTransaction(this.$otherUserId, str2);
        Collection<VerificationTransaction> access$getExistingTransactionsForUser = this.this$0.getExistingTransactionsForUser(this.$otherUserId);
        if (existingTransaction != null) {
            String log_tag2 = SASVerificationTransaction.Companion.getLOG_TAG();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## SAS onStartRequestReceived - Request exist with same if ");
            String str3 = this.$startReq.transactionID;
            if (str3 == null) {
                Intrinsics.throwNpe();
            }
            sb2.append(str3);
            Log.d(log_tag2, sb2.toString());
            existingTransaction.cancel(this.this$0.getSession(), CancelCode.UnexpectedMessage);
            Companion companion = VerificationManager.Companion;
            CryptoSession session = this.this$0.getSession();
            String str4 = this.$otherUserId;
            String str5 = this.$startReq.fromDevice;
            if (str5 == null) {
                Intrinsics.throwNpe();
            }
            companion.cancelTransaction(session, str2, str4, str5, CancelCode.UnexpectedMessage);
        } else if (access$getExistingTransactionsForUser == null || access$getExistingTransactionsForUser.isEmpty()) {
            if (Intrinsics.areEqual((Object) KeyVerificationStart.VERIF_METHOD_SAS, (Object) this.$startReq.method)) {
                String log_tag3 = SASVerificationTransaction.Companion.getLOG_TAG();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## SAS onStartRequestReceived - request accepted ");
                String str6 = this.$startReq.transactionID;
                if (str6 == null) {
                    Intrinsics.throwNpe();
                }
                sb3.append(str6);
                Log.d(log_tag3, sb3.toString());
                String str7 = this.$startReq.transactionID;
                if (str7 == null) {
                    Intrinsics.throwNpe();
                }
                IncomingSASVerificationTransaction incomingSASVerificationTransaction = new IncomingSASVerificationTransaction(str7, this.$otherUserId);
                this.this$0.addTransaction(incomingSASVerificationTransaction);
                CryptoSession session2 = this.this$0.getSession();
                String str8 = this.$otherUserId;
                KeyVerificationStart keyVerificationStart = this.$startReq;
                Intrinsics.checkExpressionValueIsNotNull(keyVerificationStart, "startReq");
                incomingSASVerificationTransaction.acceptToDeviceEvent(session2, str8, keyVerificationStart);
                return;
            }
            String log_tag4 = SASVerificationTransaction.Companion.getLOG_TAG();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("## SAS onStartRequestReceived - unknown method ");
            sb4.append(this.$startReq.method);
            Log.e(log_tag4, sb4.toString());
            Companion companion2 = VerificationManager.Companion;
            CryptoSession session3 = this.this$0.getSession();
            String str9 = this.$otherUserId;
            String str10 = this.$startReq.fromDevice;
            if (str10 == null) {
                str10 = this.$event.getSenderKey();
            }
            companion2.cancelTransaction(session3, str2, str9, str10, CancelCode.UnknownMethod);
        } else {
            String log_tag5 = SASVerificationTransaction.Companion.getLOG_TAG();
            StringBuilder sb5 = new StringBuilder();
            sb5.append("## SAS onStartRequestReceived - There is already a transaction with this user ");
            String str11 = this.$startReq.transactionID;
            if (str11 == null) {
                Intrinsics.throwNpe();
            }
            sb5.append(str11);
            Log.d(log_tag5, sb5.toString());
            for (VerificationTransaction cancel : access$getExistingTransactionsForUser) {
                cancel.cancel(this.this$0.getSession(), CancelCode.UnexpectedMessage);
            }
            Companion companion3 = VerificationManager.Companion;
            CryptoSession session4 = this.this$0.getSession();
            String str12 = this.$otherUserId;
            String str13 = this.$startReq.fromDevice;
            if (str13 == null) {
                Intrinsics.throwNpe();
            }
            companion3.cancelTransaction(session4, str2, str12, str13, CancelCode.UnexpectedMessage);
        }
    }
}
