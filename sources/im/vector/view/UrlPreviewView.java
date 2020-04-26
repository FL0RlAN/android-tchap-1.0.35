package im.vector.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.gouv.tchap.a.R;
import im.vector.VectorApp;
import java.util.HashMap;
import java.util.HashSet;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.URLPreview;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 %2\u00020\u0001:\u0001%B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\r\u0010\u001c\u001a\u00020\u001dH\u0001¢\u0006\u0002\b\u001eJ(\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010$\u001a\u00020\u001bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u00020\n8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\f\"\u0004\b\u0019\u0010\u000eR\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lim/vector/view/UrlPreviewView;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "mDescriptionTextView", "Landroid/widget/TextView;", "getMDescriptionTextView", "()Landroid/widget/TextView;", "setMDescriptionTextView", "(Landroid/widget/TextView;)V", "mImageView", "Landroid/widget/ImageView;", "getMImageView", "()Landroid/widget/ImageView;", "setMImageView", "(Landroid/widget/ImageView;)V", "mIsDismissed", "", "mTitleTextView", "getMTitleTextView", "setMTitleTextView", "mUID", "", "closeUrlPreview", "", "closeUrlPreview$vector_appAgentWithoutvoipWithpinningMatrixorg", "setUrlPreview", "session", "Lorg/matrix/androidsdk/MXSession;", "preview", "Lorg/matrix/androidsdk/rest/model/URLPreview;", "uid", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: UrlPreviewView.kt */
public final class UrlPreviewView extends FrameLayout {
    public static final Companion Companion = new Companion(null);
    private static final String DISMISSED_URL_PREVIEWS_PREF_KEY = "DISMISSED_URL_PREVIEWS_PREF_KEY";
    private static final String LOG_TAG = UrlPreviewView.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final Lazy sDismissedUrlsPreviews$delegate = LazyKt.lazy(UrlPreviewView$Companion$sDismissedUrlsPreviews$2.INSTANCE);
    private HashMap _$_findViewCache;
    @BindView(2131297152)
    public TextView mDescriptionTextView;
    @BindView(2131297154)
    public ImageView mImageView;
    private boolean mIsDismissed;
    @BindView(2131297156)
    public TextView mTitleTextView;
    private String mUID;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R)\u0010\u0007\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00040\u00040\b8BX\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n¨\u0006\u0010"}, d2 = {"Lim/vector/view/UrlPreviewView$Companion;", "", "()V", "DISMISSED_URL_PREVIEWS_PREF_KEY", "", "LOG_TAG", "kotlin.jvm.PlatformType", "sDismissedUrlsPreviews", "Ljava/util/HashSet;", "getSDismissedUrlsPreviews", "()Ljava/util/HashSet;", "sDismissedUrlsPreviews$delegate", "Lkotlin/Lazy;", "didUrlPreviewDismiss", "", "uid", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: UrlPreviewView.kt */
    public static final class Companion {
        static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(Companion.class), "sDismissedUrlsPreviews", "getSDismissedUrlsPreviews()Ljava/util/HashSet;"))};

        /* access modifiers changed from: private */
        public final HashSet<String> getSDismissedUrlsPreviews() {
            Lazy access$getSDismissedUrlsPreviews$cp = UrlPreviewView.sDismissedUrlsPreviews$delegate;
            Companion companion = UrlPreviewView.Companion;
            KProperty kProperty = $$delegatedProperties[0];
            return (HashSet) access$getSDismissedUrlsPreviews$cp.getValue();
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean didUrlPreviewDismiss(String str) {
            Intrinsics.checkParameterIsNotNull(str, "uid");
            return getSDismissedUrlsPreviews().contains(str);
        }
    }

    public UrlPreviewView(Context context) {
        this(context, null, 0, 6, null);
    }

    public UrlPreviewView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public /* synthetic */ UrlPreviewView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 2) != 0) {
            attributeSet = null;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        this(context, attributeSet, i);
    }

    public UrlPreviewView(Context context, AttributeSet attributeSet, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, attributeSet, i);
        View.inflate(context, R.layout.url_preview_view, this);
        ButterKnife.bind((View) this);
    }

    public final ImageView getMImageView() {
        ImageView imageView = this.mImageView;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mImageView");
        }
        return imageView;
    }

    public final void setMImageView(ImageView imageView) {
        Intrinsics.checkParameterIsNotNull(imageView, "<set-?>");
        this.mImageView = imageView;
    }

    public final TextView getMTitleTextView() {
        TextView textView = this.mTitleTextView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mTitleTextView");
        }
        return textView;
    }

    public final void setMTitleTextView(TextView textView) {
        Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
        this.mTitleTextView = textView;
    }

    public final TextView getMDescriptionTextView() {
        TextView textView = this.mDescriptionTextView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mDescriptionTextView");
        }
        return textView;
    }

    public final void setMDescriptionTextView(TextView textView) {
        Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
        this.mDescriptionTextView = textView;
    }

    public final void setUrlPreview(Context context, MXSession mXSession, URLPreview uRLPreview, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(mXSession, "session");
        Intrinsics.checkParameterIsNotNull(str, "uid");
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setUrlPreview ");
        sb.append(this);
        Log.d(str2, sb.toString());
        if (uRLPreview == null || this.mIsDismissed || Companion.didUrlPreviewDismiss(str) || !mXSession.isURLPreviewEnabled()) {
            setVisibility(8);
            return;
        }
        setVisibility(0);
        MXMediaCache mediaCache = mXSession.getMediaCache();
        HomeServerConnectionConfig homeServerConfig = mXSession.getHomeServerConfig();
        ImageView imageView = this.mImageView;
        String str3 = "mImageView";
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str3);
        }
        mediaCache.loadAvatarThumbnail(homeServerConfig, imageView, uRLPreview.getThumbnailURL(), context.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size));
        TextView textView = this.mTitleTextView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mTitleTextView");
        }
        if (uRLPreview.getRequestedURL() != null && uRLPreview.getTitle() != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("<a href=\"");
            sb2.append(uRLPreview.getRequestedURL());
            sb2.append("\">");
            sb2.append(uRLPreview.getTitle());
            sb2.append("</a>");
            textView.setText(Html.fromHtml(sb2.toString()));
        } else if (uRLPreview.getTitle() != null) {
            textView.setText(uRLPreview.getTitle());
        } else {
            textView.setText(uRLPreview.getRequestedURL());
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView2 = this.mDescriptionTextView;
        String str4 = "mDescriptionTextView";
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str4);
        }
        if (TextUtils.isEmpty(uRLPreview.getDescription())) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            textView2.setText(uRLPreview.getDescription());
        }
        this.mUID = str;
        if (uRLPreview.getRequestedURL() == null) {
            TextView textView3 = this.mDescriptionTextView;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str4);
            }
            textView3.setClickable(false);
            ImageView imageView2 = this.mImageView;
            if (imageView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str3);
            }
            imageView2.setClickable(false);
        } else if (uRLPreview.getRequestedURL() != null) {
            TextView textView4 = this.mDescriptionTextView;
            if (textView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str4);
            }
            textView4.setOnClickListener(new UrlPreviewView$setUrlPreview$3(context, uRLPreview));
            ImageView imageView3 = this.mImageView;
            if (imageView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str3);
            }
            imageView3.setOnClickListener(new UrlPreviewView$setUrlPreview$4(context, uRLPreview));
        }
    }

    @OnClick({2131297153})
    public final void closeUrlPreview$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        this.mIsDismissed = true;
        setVisibility(8);
        Companion.getSDismissedUrlsPreviews().add(this.mUID);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(VectorApp.getInstance());
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…(VectorApp.getInstance())");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        edit.putStringSet(DISMISSED_URL_PREVIEWS_PREF_KEY, Companion.getSDismissedUrlsPreviews());
        edit.apply();
    }
}
