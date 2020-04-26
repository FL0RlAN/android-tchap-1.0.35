package im.vector.analytics.e2e;

import androidx.core.app.NotificationCompat;
import im.vector.analytics.Analytics;
import im.vector.analytics.TrackingEvent.DecryptionFailure;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.GroupingKt;
import kotlin.concurrent.TimersKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomMember;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\u000eH\u0002J\u0006\u0010\u000f\u001a\u00020\u000eJ\u001c\u0010\u0010\u001a\u00020\u000e2\b\u0010\u0011\u001a\u0004\u0018\u00010\t2\b\u0010\u0012\u001a\u0004\u0018\u00010\tH\u0016J\u001e\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\fX\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lim/vector/analytics/e2e/DecryptionFailureTracker;", "Lorg/matrix/androidsdk/listeners/MXEventListener;", "analytics", "Lim/vector/analytics/Analytics;", "(Lim/vector/analytics/Analytics;)V", "checkFailuresTimer", "Ljava/util/Timer;", "reportedFailures", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lim/vector/analytics/e2e/DecryptionFailure;", "trackedEvents", "Ljava/util/HashSet;", "checkFailures", "", "dispatch", "onEventDecrypted", "roomId", "eventId", "reportUnableToDecryptError", "event", "Lorg/matrix/androidsdk/rest/model/Event;", "roomState", "Lorg/matrix/androidsdk/data/RoomState;", "userId", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DecryptionFailureTracker.kt */
public final class DecryptionFailureTracker extends MXEventListener {
    private static final long CHECK_PERIOD = 5000;
    public static final Companion Companion = new Companion(null);
    private static final long GRACE_PERIOD = 60000;
    private final Analytics analytics;
    private final Timer checkFailuresTimer;
    private final ConcurrentHashMap<String, DecryptionFailure> reportedFailures = new ConcurrentHashMap<>();
    private final HashSet<String> trackedEvents = new HashSet<>();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lim/vector/analytics/e2e/DecryptionFailureTracker$Companion;", "", "()V", "CHECK_PERIOD", "", "GRACE_PERIOD", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DecryptionFailureTracker.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public DecryptionFailureTracker(Analytics analytics2) {
        Intrinsics.checkParameterIsNotNull(analytics2, "analytics");
        this.analytics = analytics2;
        Timer timer = TimersKt.timer(null, false);
        timer.schedule(new DecryptionFailureTracker$$special$$inlined$timer$1(this), 0, CHECK_PERIOD);
        this.checkFailuresTimer = timer;
    }

    public final void reportUnableToDecryptError(Event event, RoomState roomState, String str) {
        DecryptionFailureReason decryptionFailureReason;
        Intrinsics.checkParameterIsNotNull(event, NotificationCompat.CATEGORY_EVENT);
        Intrinsics.checkParameterIsNotNull(roomState, "roomState");
        Intrinsics.checkParameterIsNotNull(str, "userId");
        if (this.reportedFailures.get(event.eventId) == null && !this.trackedEvents.contains(event.eventId)) {
            RoomMember member = roomState.getMember(str);
            if (member != null && !(!Intrinsics.areEqual((Object) member.membership, (Object) "join"))) {
                String str2 = event.getCryptoError().errcode;
                if (str2 != null) {
                    switch (str2.hashCode()) {
                        case -1815105677:
                            if (str2.equals(MXCryptoError.OLM_ERROR_CODE)) {
                                decryptionFailureReason = DecryptionFailureReason.OLM_INDEX_ERROR;
                                break;
                            }
                        case -1377433611:
                            if (str2.equals(MXCryptoError.UNABLE_TO_DECRYPT_ERROR_CODE)) {
                                decryptionFailureReason = DecryptionFailureReason.UNEXPECTED;
                                break;
                            }
                        case -25310625:
                            if (str2.equals(MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_ERROR_CODE)) {
                                decryptionFailureReason = DecryptionFailureReason.OLM_KEYS_NOT_SENT;
                                break;
                            }
                        case 1490927539:
                            if (str2.equals(MXCryptoError.ENCRYPTING_NOT_ENABLED_ERROR_CODE)) {
                                decryptionFailureReason = DecryptionFailureReason.UNEXPECTED;
                                break;
                            }
                    }
                }
                decryptionFailureReason = DecryptionFailureReason.UNSPECIFIED;
                String str3 = event.eventId;
                String str4 = "event.eventId";
                Intrinsics.checkExpressionValueIsNotNull(str3, str4);
                DecryptionFailure decryptionFailure = new DecryptionFailure(decryptionFailureReason, str3);
                Map map = this.reportedFailures;
                String str5 = event.eventId;
                Intrinsics.checkExpressionValueIsNotNull(str5, str4);
                map.put(str5, decryptionFailure);
            }
        }
    }

    public final void dispatch() {
        checkFailures();
    }

    public void onEventDecrypted(String str, String str2) {
        Map map = this.reportedFailures;
        if (map != null) {
            TypeIntrinsics.asMutableMap(map).remove(str2);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
    }

    /* access modifiers changed from: private */
    public final void checkFailures() {
        long time = new Date().getTime();
        ArrayList arrayList = new ArrayList();
        for (DecryptionFailure decryptionFailure : this.reportedFailures.values()) {
            if (decryptionFailure.getTimestamp() < time - 60000) {
                arrayList.add(decryptionFailure);
                this.reportedFailures.remove(decryptionFailure.getFailedEventId());
                this.trackedEvents.add(decryptionFailure.getFailedEventId());
            }
        }
        for (Entry entry : GroupingKt.eachCount(new DecryptionFailureTracker$checkFailures$$inlined$groupingBy$1(arrayList)).entrySet()) {
            this.analytics.trackEvent(new DecryptionFailure((DecryptionFailureReason) entry.getKey(), ((Number) entry.getValue()).intValue()));
        }
    }
}
