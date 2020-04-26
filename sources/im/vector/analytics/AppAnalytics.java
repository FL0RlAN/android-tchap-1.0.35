package im.vector.analytics;

import android.content.Context;
import androidx.core.app.NotificationCompat;
import im.vector.util.PreferencesManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u0005\"\u00020\u0001¢\u0006\u0002\u0010\u0006J\b\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u001a\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000fH\u0016J \u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u000fH\u0016R\u0018\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u0005X\u0004¢\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lim/vector/analytics/AppAnalytics;", "Lim/vector/analytics/Analytics;", "context", "Landroid/content/Context;", "analytics", "", "(Landroid/content/Context;[Lim/vector/analytics/Analytics;)V", "[Lim/vector/analytics/Analytics;", "forceDispatch", "", "trackEvent", "event", "Lim/vector/analytics/TrackingEvent;", "trackScreen", "screen", "", "title", "visitVariable", "index", "", "name", "value", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: AppAnalytics.kt */
public final class AppAnalytics implements Analytics {
    private final Analytics[] analytics;
    private final Context context;

    public AppAnalytics(Context context2, Analytics... analyticsArr) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        Intrinsics.checkParameterIsNotNull(analyticsArr, "analytics");
        this.context = context2;
        this.analytics = analyticsArr;
    }

    public void trackScreen(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "screen");
        if (PreferencesManager.useAnalytics(this.context)) {
            for (Analytics trackScreen : this.analytics) {
                trackScreen.trackScreen(str, str2);
            }
            return;
        }
        Log.d("Analytics - screen", str);
    }

    public void visitVariable(int i, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.NAME);
        Intrinsics.checkParameterIsNotNull(str2, BingRule.ACTION_PARAMETER_VALUE);
        if (PreferencesManager.useAnalytics(this.context)) {
            for (Analytics visitVariable : this.analytics) {
                visitVariable.visitVariable(i, str, str2);
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Variable ");
        sb.append(str);
        sb.append(" at index ");
        sb.append(i);
        sb.append(": ");
        sb.append(str2);
        Log.d("Analytics - visit variable", sb.toString());
    }

    public void trackEvent(TrackingEvent trackingEvent) {
        Intrinsics.checkParameterIsNotNull(trackingEvent, NotificationCompat.CATEGORY_EVENT);
        if (PreferencesManager.useAnalytics(this.context)) {
            for (Analytics trackEvent : this.analytics) {
                trackEvent.trackEvent(trackingEvent);
            }
            return;
        }
        Log.d("Analytics - event", trackingEvent.toString());
    }

    public void forceDispatch() {
        for (Analytics forceDispatch : this.analytics) {
            forceDispatch.forceDispatch();
        }
    }
}
