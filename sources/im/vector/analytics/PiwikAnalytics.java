package im.vector.analytics;

import android.content.Context;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;
import org.piwik.sdk.Piwik;
import org.piwik.sdk.QueryParams;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.TrackerConfig;
import org.piwik.sdk.extra.CustomVariables;
import org.piwik.sdk.extra.TrackHelper;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u001a\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0016J \u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u000eH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lim/vector/analytics/PiwikAnalytics;", "Lim/vector/analytics/Analytics;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "tracker", "Lorg/piwik/sdk/Tracker;", "forceDispatch", "", "trackEvent", "event", "Lim/vector/analytics/TrackingEvent;", "trackScreen", "screen", "", "title", "visitVariable", "index", "", "name", "value", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: PiwikAnalytics.kt */
public final class PiwikAnalytics implements Analytics {
    private final Tracker tracker;

    public PiwikAnalytics(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Tracker newTracker = Piwik.getInstance(context).newTracker(new TrackerConfig("https://piwik.riot.im/", 1, " AndroidPiwikTracker"));
        Intrinsics.checkExpressionValueIsNotNull(newTracker, "Piwik.getInstance(context).newTracker(config)");
        this.tracker = newTracker;
    }

    public void trackScreen(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "screen");
        TrackHelper.track().screen(str).title(str2).with(this.tracker);
    }

    public void trackEvent(TrackingEvent trackingEvent) {
        Intrinsics.checkParameterIsNotNull(trackingEvent, NotificationCompat.CATEGORY_EVENT);
        TrackHelper.track().event(trackingEvent.getCategory().getValue(), trackingEvent.getAction().getValue()).name(trackingEvent.getTitle()).value(trackingEvent.getValue()).with(this.tracker);
    }

    public void visitVariable(int i, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.NAME);
        Intrinsics.checkParameterIsNotNull(str2, BingRule.ACTION_PARAMETER_VALUE);
        CustomVariables customVariables = new CustomVariables(this.tracker.getDefaultTrackMe().get(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES));
        customVariables.put(i, str, str2);
        this.tracker.getDefaultTrackMe().set(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES, customVariables.toString());
    }

    public void forceDispatch() {
        this.tracker.dispatch();
    }
}
