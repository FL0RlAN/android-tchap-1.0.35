package im.vector.analytics;

import im.vector.analytics.TrackingEvent.IncrementalSync;
import im.vector.analytics.TrackingEvent.InitialSync;
import im.vector.analytics.TrackingEvent.Rooms;
import im.vector.analytics.TrackingEvent.StorePreload;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.data.metrics.MetricsListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lim/vector/analytics/MetricsListenerProxy;", "Lorg/matrix/androidsdk/data/metrics/MetricsListener;", "analytics", "Lim/vector/analytics/Analytics;", "(Lim/vector/analytics/Analytics;)V", "getAnalytics", "()Lim/vector/analytics/Analytics;", "firstSyncDispatched", "Ljava/util/concurrent/atomic/AtomicBoolean;", "incrementalSyncDispatched", "roomsLoadedDispatched", "storePreloadDispatched", "onIncrementalSyncFinished", "", "duration", "", "onInitialSyncFinished", "onRoomsLoaded", "nbOfRooms", "", "onStorePreloaded", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: MetricsListenerProxy.kt */
public final class MetricsListenerProxy implements MetricsListener {
    private final Analytics analytics;
    private final AtomicBoolean firstSyncDispatched = new AtomicBoolean();
    private final AtomicBoolean incrementalSyncDispatched = new AtomicBoolean();
    private final AtomicBoolean roomsLoadedDispatched = new AtomicBoolean();
    private final AtomicBoolean storePreloadDispatched = new AtomicBoolean();

    public MetricsListenerProxy(Analytics analytics2) {
        Intrinsics.checkParameterIsNotNull(analytics2, "analytics");
        this.analytics = analytics2;
    }

    public final Analytics getAnalytics() {
        return this.analytics;
    }

    public void onInitialSyncFinished(long j) {
        if (!this.firstSyncDispatched.getAndSet(true)) {
            this.analytics.trackEvent(new InitialSync(j));
        }
    }

    public void onIncrementalSyncFinished(long j) {
        if (!this.incrementalSyncDispatched.getAndSet(true)) {
            this.analytics.trackEvent(new IncrementalSync(j));
        }
    }

    public void onStorePreloaded(long j) {
        if (!this.storePreloadDispatched.getAndSet(true)) {
            this.analytics.trackEvent(new StorePreload(j));
        }
    }

    public void onRoomsLoaded(int i) {
        if (!this.roomsLoadedDispatched.getAndSet(true)) {
            this.analytics.trackEvent(new Rooms(i));
        }
    }
}
