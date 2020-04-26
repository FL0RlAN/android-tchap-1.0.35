package org.piwik.sdk.extra;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.piwik.sdk.QueryParams;
import org.piwik.sdk.TrackMe;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.DownloadTracker.Extra;
import org.piwik.sdk.extra.DownloadTracker.Extra.None;
import org.piwik.sdk.tools.ActivityHelper;
import org.piwik.sdk.tools.CurrencyFormatter;
import timber.log.Timber;

public class TrackHelper {
    private static final String LOGGER_TAG = "PIWIK:TrackHelper";
    protected final TrackMe mBaseTrackMe;

    public static class AppTracking {
        private final Application mApplication;
        /* access modifiers changed from: private */
        public final TrackHelper mBaseBuilder;

        public AppTracking(TrackHelper trackHelper, Application application) {
            this.mBaseBuilder = trackHelper;
            this.mApplication = application;
        }

        public ActivityLifecycleCallbacks with(final Tracker tracker) {
            AnonymousClass1 r0 = new ActivityLifecycleCallbacks() {
                public void onActivityCreated(Activity activity, Bundle bundle) {
                }

                public void onActivityDestroyed(Activity activity) {
                }

                public void onActivityPaused(Activity activity) {
                }

                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                }

                public void onActivityStarted(Activity activity) {
                }

                public void onActivityResumed(Activity activity) {
                    AppTracking.this.mBaseBuilder.screen(activity).with(tracker);
                }

                public void onActivityStopped(Activity activity) {
                    if (activity != null && activity.isTaskRoot()) {
                        tracker.dispatch();
                    }
                }
            };
            this.mApplication.registerActivityLifecycleCallbacks(r0);
            return r0;
        }
    }

    static abstract class BaseEvent {
        private final TrackHelper mBaseBuilder;

        public abstract TrackMe build();

        BaseEvent(TrackHelper trackHelper) {
            this.mBaseBuilder = trackHelper;
        }

        /* access modifiers changed from: 0000 */
        public TrackMe getBaseTrackMe() {
            return this.mBaseBuilder.mBaseTrackMe;
        }

        public void with(PiwikApplication piwikApplication) {
            with(piwikApplication.getTracker());
        }

        public void with(Tracker tracker) {
            TrackMe build = build();
            if (build != null) {
                tracker.track(build);
            }
        }
    }

    public static class CartUpdate extends BaseEvent {
        private EcommerceItems mEcommerceItems;
        private final int mGrandTotal;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        CartUpdate(TrackHelper trackHelper, int i) {
            super(trackHelper);
            this.mGrandTotal = i;
        }

        public CartUpdate items(EcommerceItems ecommerceItems) {
            this.mEcommerceItems = ecommerceItems;
            return this;
        }

        public TrackMe build() {
            if (this.mEcommerceItems == null) {
                this.mEcommerceItems = new EcommerceItems();
            }
            return new TrackMe(getBaseTrackMe()).set(QueryParams.GOAL_ID, 0).set(QueryParams.REVENUE, CurrencyFormatter.priceString(Integer.valueOf(this.mGrandTotal))).set(QueryParams.ECOMMERCE_ITEMS, this.mEcommerceItems.toJson());
        }
    }

    public static class ContentImpression extends BaseEvent {
        private final String mContentName;
        private String mContentPiece;
        private String mContentTarget;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        ContentImpression(TrackHelper trackHelper, String str) {
            super(trackHelper);
            this.mContentName = str;
        }

        public ContentImpression piece(String str) {
            this.mContentPiece = str;
            return this;
        }

        public ContentImpression target(String str) {
            this.mContentTarget = str;
            return this;
        }

        public TrackMe build() {
            String str = this.mContentName;
            if (str == null || str.length() == 0) {
                return null;
            }
            return new TrackMe(getBaseTrackMe()).set(QueryParams.CONTENT_NAME, this.mContentName).set(QueryParams.CONTENT_PIECE, this.mContentPiece).set(QueryParams.CONTENT_TARGET, this.mContentTarget);
        }
    }

    public static class ContentInteraction extends BaseEvent {
        private final String mContentName;
        private String mContentPiece;
        private String mContentTarget;
        private final String mInteraction;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        ContentInteraction(TrackHelper trackHelper, String str, String str2) {
            super(trackHelper);
            this.mContentName = str;
            this.mInteraction = str2;
        }

        public ContentInteraction piece(String str) {
            this.mContentPiece = str;
            return this;
        }

        public ContentInteraction target(String str) {
            this.mContentTarget = str;
            return this;
        }

        public TrackMe build() {
            String str = this.mContentName;
            if (!(str == null || str.length() == 0)) {
                String str2 = this.mInteraction;
                if (!(str2 == null || str2.length() == 0)) {
                    return new TrackMe(getBaseTrackMe()).set(QueryParams.CONTENT_NAME, this.mContentName).set(QueryParams.CONTENT_PIECE, this.mContentPiece).set(QueryParams.CONTENT_TARGET, this.mContentTarget).set(QueryParams.CONTENT_INTERACTION, this.mInteraction);
                }
            }
            return null;
        }
    }

    public static class Dimension extends TrackHelper {
        Dimension(TrackMe trackMe) {
            super(trackMe);
        }

        public Dimension dimension(int i, String str) {
            CustomDimension.setDimension(this.mBaseTrackMe, i, str);
            return this;
        }
    }

    public static class Download {
        private final TrackHelper mBaseBuilder;
        private DownloadTracker mDownloadTracker;
        private Extra mExtra = new None();
        private boolean mForced = false;
        private String mVersion;

        Download(DownloadTracker downloadTracker, TrackHelper trackHelper) {
            this.mDownloadTracker = downloadTracker;
            this.mBaseBuilder = trackHelper;
        }

        public Download identifier(Extra extra) {
            this.mExtra = extra;
            return this;
        }

        public Download force() {
            this.mForced = true;
            return this;
        }

        public Download version(String str) {
            this.mVersion = str;
            return this;
        }

        public void with(Tracker tracker) {
            if (this.mDownloadTracker == null) {
                this.mDownloadTracker = new DownloadTracker(tracker);
            }
            String str = this.mVersion;
            if (str != null) {
                this.mDownloadTracker.setVersion(str);
            }
            if (this.mForced) {
                this.mDownloadTracker.trackNewAppDownload(this.mBaseBuilder.mBaseTrackMe, this.mExtra);
            } else {
                this.mDownloadTracker.trackOnce(this.mBaseBuilder.mBaseTrackMe, this.mExtra);
            }
        }
    }

    public static class EventBuilder extends BaseEvent {
        private final String mAction;
        private final String mCategory;
        private String mName;
        private String mPath;
        private Float mValue;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        EventBuilder(TrackHelper trackHelper, String str, String str2) {
            super(trackHelper);
            this.mCategory = str;
            this.mAction = str2;
        }

        public EventBuilder path(String str) {
            this.mPath = str;
            return this;
        }

        public EventBuilder name(String str) {
            this.mName = str;
            return this;
        }

        public EventBuilder value(Float f) {
            this.mValue = f;
            return this;
        }

        public TrackMe build() {
            TrackMe trackMe = new TrackMe(getBaseTrackMe()).set(QueryParams.URL_PATH, this.mPath).set(QueryParams.EVENT_CATEGORY, this.mCategory).set(QueryParams.EVENT_ACTION, this.mAction).set(QueryParams.EVENT_NAME, this.mName);
            if (this.mValue != null) {
                trackMe.set(QueryParams.EVENT_VALUE, this.mValue.floatValue());
            }
            return trackMe;
        }
    }

    public static class Exception extends BaseEvent {
        private String mDescription;
        private boolean mIsFatal;
        private final Throwable mThrowable;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        Exception(TrackHelper trackHelper, Throwable th) {
            super(trackHelper);
            this.mThrowable = th;
        }

        public Exception description(String str) {
            this.mDescription = str;
            return this;
        }

        public Exception fatal(boolean z) {
            this.mIsFatal = z;
            return this;
        }

        public TrackMe build() {
            String str;
            String str2 = "/";
            try {
                StackTraceElement stackTraceElement = this.mThrowable.getStackTrace()[0];
                StringBuilder sb = new StringBuilder();
                sb.append(stackTraceElement.getClassName());
                sb.append(str2);
                sb.append(stackTraceElement.getMethodName());
                sb.append(":");
                sb.append(stackTraceElement.getLineNumber());
                str = sb.toString();
            } catch (Exception e) {
                Timber.tag(TrackHelper.LOGGER_TAG).w(e, "Couldn't get stack info", new Object[0]);
                str = this.mThrowable.getClass().getName();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("exception/");
            sb2.append(this.mIsFatal ? "fatal/" : "");
            sb2.append(str);
            sb2.append(str2);
            sb2.append(this.mDescription);
            return new TrackMe(getBaseTrackMe()).set(QueryParams.ACTION_NAME, sb2.toString()).set(QueryParams.EVENT_CATEGORY, "Exception").set(QueryParams.EVENT_ACTION, str).set(QueryParams.EVENT_NAME, this.mDescription).set(QueryParams.EVENT_VALUE, this.mIsFatal ? 1 : 0);
        }
    }

    public static class Goal extends BaseEvent {
        private final int mIdGoal;
        private Float mRevenue;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        Goal(TrackHelper trackHelper, int i) {
            super(trackHelper);
            this.mIdGoal = i;
        }

        public Goal revenue(Float f) {
            this.mRevenue = f;
            return this;
        }

        public TrackMe build() {
            if (this.mIdGoal < 0) {
                return null;
            }
            TrackMe trackMe = new TrackMe(getBaseTrackMe()).set(QueryParams.GOAL_ID, this.mIdGoal);
            if (this.mRevenue != null) {
                trackMe.set(QueryParams.REVENUE, this.mRevenue.floatValue());
            }
            return trackMe;
        }
    }

    public static class Order extends BaseEvent {
        private Integer mDiscount;
        private EcommerceItems mEcommerceItems;
        private final int mGrandTotal;
        private final String mOrderId;
        private Integer mShipping;
        private Integer mSubTotal;
        private Integer mTax;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        Order(TrackHelper trackHelper, String str, int i) {
            super(trackHelper);
            this.mOrderId = str;
            this.mGrandTotal = i;
        }

        public Order subTotal(Integer num) {
            this.mSubTotal = num;
            return this;
        }

        public Order tax(Integer num) {
            this.mTax = num;
            return this;
        }

        public Order shipping(Integer num) {
            this.mShipping = num;
            return this;
        }

        public Order discount(Integer num) {
            this.mDiscount = num;
            return this;
        }

        public Order items(EcommerceItems ecommerceItems) {
            this.mEcommerceItems = ecommerceItems;
            return this;
        }

        public TrackMe build() {
            if (this.mEcommerceItems == null) {
                this.mEcommerceItems = new EcommerceItems();
            }
            return new TrackMe(getBaseTrackMe()).set(QueryParams.GOAL_ID, 0).set(QueryParams.ORDER_ID, this.mOrderId).set(QueryParams.REVENUE, CurrencyFormatter.priceString(Integer.valueOf(this.mGrandTotal))).set(QueryParams.ECOMMERCE_ITEMS, this.mEcommerceItems.toJson()).set(QueryParams.SUBTOTAL, CurrencyFormatter.priceString(this.mSubTotal)).set(QueryParams.TAX, CurrencyFormatter.priceString(this.mTax)).set(QueryParams.SHIPPING, CurrencyFormatter.priceString(this.mShipping)).set(QueryParams.DISCOUNT, CurrencyFormatter.priceString(this.mDiscount));
        }
    }

    public static class Outlink extends BaseEvent {
        private final URL mURL;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        Outlink(TrackHelper trackHelper, URL url) {
            super(trackHelper);
            this.mURL = url;
        }

        public TrackMe build() {
            if (this.mURL.getProtocol().equals("http") || this.mURL.getProtocol().equals("https") || this.mURL.getProtocol().equals("ftp")) {
                return new TrackMe(getBaseTrackMe()).set(QueryParams.LINK, this.mURL.toExternalForm()).set(QueryParams.URL_PATH, this.mURL.toExternalForm());
            }
            return null;
        }
    }

    public static class Screen extends BaseEvent {
        private final Map<Integer, String> mCustomDimensions = new HashMap();
        private final CustomVariables mCustomVariables = new CustomVariables();
        private final String mPath;
        private String mTitle;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        Screen(TrackHelper trackHelper, String str) {
            super(trackHelper);
            this.mPath = str;
        }

        public Screen title(String str) {
            this.mTitle = str;
            return this;
        }

        public Screen dimension(int i, String str) {
            this.mCustomDimensions.put(Integer.valueOf(i), str);
            return this;
        }

        @Deprecated
        public Screen variable(int i, String str, String str2) {
            this.mCustomVariables.put(i, str, str2);
            return this;
        }

        public TrackMe build() {
            if (this.mPath == null) {
                return null;
            }
            TrackMe trackMe = new TrackMe(getBaseTrackMe()).set(QueryParams.URL_PATH, this.mPath).set(QueryParams.ACTION_NAME, this.mTitle);
            if (this.mCustomVariables.size() > 0) {
                trackMe.set(QueryParams.SCREEN_SCOPE_CUSTOM_VARIABLES, this.mCustomVariables.toString());
            }
            for (Entry entry : this.mCustomDimensions.entrySet()) {
                CustomDimension.setDimension(trackMe, ((Integer) entry.getKey()).intValue(), (String) entry.getValue());
            }
            return trackMe;
        }
    }

    public static class Search extends BaseEvent {
        private String mCategory;
        private Integer mCount;
        private final String mKeyword;

        public /* bridge */ /* synthetic */ void with(Tracker tracker) {
            super.with(tracker);
        }

        public /* bridge */ /* synthetic */ void with(PiwikApplication piwikApplication) {
            super.with(piwikApplication);
        }

        Search(TrackHelper trackHelper, String str) {
            super(trackHelper);
            this.mKeyword = str;
        }

        public Search category(String str) {
            this.mCategory = str;
            return this;
        }

        public Search count(Integer num) {
            this.mCount = num;
            return this;
        }

        public TrackMe build() {
            TrackMe trackMe = new TrackMe(getBaseTrackMe()).set(QueryParams.SEARCH_KEYWORD, this.mKeyword).set(QueryParams.SEARCH_CATEGORY, this.mCategory);
            if (this.mCount != null) {
                trackMe.set(QueryParams.SEARCH_NUMBER_OF_HITS, this.mCount.intValue());
            }
            return trackMe;
        }
    }

    public static class UncaughtExceptions {
        private final TrackHelper mBaseBuilder;

        UncaughtExceptions(TrackHelper trackHelper) {
            this.mBaseBuilder = trackHelper;
        }

        public UncaughtExceptionHandler with(Tracker tracker) {
            if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof PiwikExceptionHandler)) {
                PiwikExceptionHandler piwikExceptionHandler = new PiwikExceptionHandler(tracker, this.mBaseBuilder.mBaseTrackMe);
                Thread.setDefaultUncaughtExceptionHandler(piwikExceptionHandler);
                return piwikExceptionHandler;
            }
            throw new RuntimeException("Trying to wrap an existing PiwikExceptionHandler.");
        }
    }

    public static class VisitVariables extends TrackHelper {
        public VisitVariables(TrackHelper trackHelper, CustomVariables customVariables) {
            super(trackHelper.mBaseTrackMe);
            CustomVariables customVariables2 = new CustomVariables(this.mBaseTrackMe.get(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES));
            customVariables2.putAll(customVariables);
            this.mBaseTrackMe.set(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES, customVariables2.toString());
        }

        public VisitVariables visitVariables(int i, String str, String str2) {
            CustomVariables customVariables = new CustomVariables(this.mBaseTrackMe.get(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES));
            customVariables.put(i, str, str2);
            this.mBaseTrackMe.set(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES, customVariables.toString());
            return this;
        }
    }

    private TrackHelper() {
        this(null);
    }

    private TrackHelper(TrackMe trackMe) {
        if (trackMe == null) {
            trackMe = new TrackMe();
        }
        this.mBaseTrackMe = trackMe;
    }

    public static TrackHelper track() {
        return new TrackHelper();
    }

    public static TrackHelper track(TrackMe trackMe) {
        return new TrackHelper(trackMe);
    }

    public Screen screen(String str) {
        return new Screen(this, str);
    }

    public Screen screen(Activity activity) {
        String breadcrumbs = ActivityHelper.getBreadcrumbs(activity);
        return new Screen(this, ActivityHelper.breadcrumbsToPath(breadcrumbs)).title(breadcrumbs);
    }

    public EventBuilder event(String str, String str2) {
        return new EventBuilder(this, str, str2);
    }

    public Goal goal(int i) {
        return new Goal(this, i);
    }

    public Outlink outlink(URL url) {
        return new Outlink(this, url);
    }

    public Search search(String str) {
        return new Search(this, str);
    }

    public Download download(DownloadTracker downloadTracker) {
        return new Download(downloadTracker, this);
    }

    public Download download() {
        return new Download(null, this);
    }

    public ContentImpression impression(String str) {
        return new ContentImpression(this, str);
    }

    public ContentInteraction interaction(String str, String str2) {
        return new ContentInteraction(this, str, str2);
    }

    public CartUpdate cartUpdate(int i) {
        return new CartUpdate(this, i);
    }

    public Order order(String str, int i) {
        return new Order(this, str, i);
    }

    public Exception exception(Throwable th) {
        return new Exception(this, th);
    }

    public UncaughtExceptions uncaughtExceptions() {
        return new UncaughtExceptions(this);
    }

    public AppTracking screens(Application application) {
        return new AppTracking(this, application);
    }

    public Dimension dimension(int i, String str) {
        return new Dimension(this.mBaseTrackMe).dimension(i, str);
    }

    @Deprecated
    public VisitVariables visitVariables(int i, String str, String str2) {
        CustomVariables customVariables = new CustomVariables();
        customVariables.put(i, str, str2);
        return visitVariables(customVariables);
    }

    @Deprecated
    public VisitVariables visitVariables(CustomVariables customVariables) {
        return new VisitVariables(this, customVariables);
    }
}
