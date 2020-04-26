package im.vector.analytics;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H&J\u001c\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\tH&J \u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\tH&¨\u0006\u0010"}, d2 = {"Lim/vector/analytics/Analytics;", "", "forceDispatch", "", "trackEvent", "event", "Lim/vector/analytics/TrackingEvent;", "trackScreen", "screen", "", "title", "visitVariable", "index", "", "name", "value", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: Analytics.kt */
public interface Analytics {

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    /* compiled from: Analytics.kt */
    public static final class DefaultImpls {
        public static /* synthetic */ void trackScreen$default(Analytics analytics, String str, String str2, int i, Object obj) {
            if (obj == null) {
                if ((i & 2) != 0) {
                    str2 = null;
                }
                analytics.trackScreen(str, str2);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: trackScreen");
        }
    }

    void forceDispatch();

    void trackEvent(TrackingEvent trackingEvent);

    void trackScreen(String str, String str2);

    void visitVariable(int i, String str, String str2);
}
