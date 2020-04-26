package im.vector.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.binaryfork.spanny.Spanny;
import fr.gouv.tchap.a.R;
import im.vector.features.hhs.ResourceLimitErrorFormatter;
import im.vector.features.hhs.ResourceLimitErrorFormatter.Mode;
import im.vector.features.hhs.ResourceLimitErrorFormatter.Mode.Hard;
import im.vector.features.hhs.ResourceLimitErrorFormatter.Mode.Soft;
import im.vector.listeners.IMessagesAdapterActionsListener;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.MatrixURLSpan;
import java.util.HashMap;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.RoomTombstoneContent;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 32\u00020\u0001:\u000523456B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u001f\u001a\u00020 H\u0002J\u000e\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020\u001cJ\b\u0010#\u001a\u00020 H\u0002J\b\u0010$\u001a\u00020 H\u0002J\b\u0010%\u001a\u00020 H\u0002J\u0010\u0010&\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020'H\u0002J\u0010\u0010(\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020)H\u0002J\u0010\u0010*\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020+H\u0002J\u0010\u0010,\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020-H\u0002J\b\u0010.\u001a\u00020 H\u0002J\u0010\u0010/\u001a\u00020 2\u0006\u0010\u001b\u001a\u000200H\u0002J\b\u00101\u001a\u00020 H\u0002R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001e\u0010\u0015\u001a\u00020\u00168\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u00067"}, d2 = {"Lim/vector/view/NotificationAreaView;", "Landroid/widget/RelativeLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "delegate", "Lim/vector/view/NotificationAreaView$Delegate;", "getDelegate", "()Lim/vector/view/NotificationAreaView$Delegate;", "setDelegate", "(Lim/vector/view/NotificationAreaView$Delegate;)V", "imageView", "Landroid/widget/ImageView;", "getImageView", "()Landroid/widget/ImageView;", "setImageView", "(Landroid/widget/ImageView;)V", "messageView", "Landroid/widget/TextView;", "getMessageView", "()Landroid/widget/TextView;", "setMessageView", "(Landroid/widget/TextView;)V", "state", "Lim/vector/view/NotificationAreaView$State;", "visibilityForEmptyContent", "visibilityForMessages", "cleanUp", "", "render", "newState", "renderConnectionError", "renderDefault", "renderHidden", "renderResourceLimitExceededError", "Lim/vector/view/NotificationAreaView$State$ResourceLimitExceededError;", "renderScrollToBottom", "Lim/vector/view/NotificationAreaView$State$ScrollToBottom;", "renderTombstone", "Lim/vector/view/NotificationAreaView$State$Tombstone;", "renderTyping", "Lim/vector/view/NotificationAreaView$State$Typing;", "renderUnreadPreview", "renderUnsent", "Lim/vector/view/NotificationAreaView$State$UnsentEvents;", "setupView", "CancelAllClickableSpan", "Companion", "Delegate", "ResendAllClickableSpan", "State", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: NotificationAreaView.kt */
public final class NotificationAreaView extends RelativeLayout {
    public static final Companion Companion = new Companion(null);
    private static final String SHOW_INFO_AREA_KEY = "SETTINGS_SHOW_INFO_AREA_KEY";
    private static final String SHOW_INFO_AREA_VALUE_ALWAYS = "always";
    private static final String SHOW_INFO_AREA_VALUE_MESSAGES_AND_ERRORS = "messages_and_errors";
    private static final String SHOW_INFO_AREA_VALUE_ONLY_ERRORS = "only_errors";
    private HashMap _$_findViewCache;
    private Delegate delegate;
    @BindView(2131296956)
    public ImageView imageView;
    @BindView(2131296957)
    public TextView messageView;
    /* access modifiers changed from: private */
    public State state;
    private final int visibilityForEmptyContent;
    private final int visibilityForMessages;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, d2 = {"Lim/vector/view/NotificationAreaView$CancelAllClickableSpan;", "Landroid/text/style/ClickableSpan;", "(Lim/vector/view/NotificationAreaView;)V", "onClick", "", "widget", "Landroid/view/View;", "updateDrawState", "ds", "Landroid/text/TextPaint;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: NotificationAreaView.kt */
    private final class CancelAllClickableSpan extends ClickableSpan {
        public CancelAllClickableSpan() {
        }

        public void onClick(View view) {
            Intrinsics.checkParameterIsNotNull(view, "widget");
            Delegate delegate = NotificationAreaView.this.getDelegate();
            if (delegate != null) {
                delegate.deleteUnsentEvents();
            }
            NotificationAreaView notificationAreaView = NotificationAreaView.this;
            notificationAreaView.render(notificationAreaView.state);
        }

        public void updateDrawState(TextPaint textPaint) {
            Intrinsics.checkParameterIsNotNull(textPaint, "ds");
            super.updateDrawState(textPaint);
            textPaint.setColor(ContextCompat.getColor(NotificationAreaView.this.getContext(), R.color.vector_fuchsia_color));
            textPaint.bgColor = 0;
            textPaint.setUnderlineText(true);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lim/vector/view/NotificationAreaView$Companion;", "", "()V", "SHOW_INFO_AREA_KEY", "", "SHOW_INFO_AREA_VALUE_ALWAYS", "SHOW_INFO_AREA_VALUE_MESSAGES_AND_ERRORS", "SHOW_INFO_AREA_VALUE_ONLY_ERRORS", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: NotificationAreaView.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\b\u0010\u0005\u001a\u00020\u0003H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\u0003H&¨\u0006\t"}, d2 = {"Lim/vector/view/NotificationAreaView$Delegate;", "", "closeScreen", "", "deleteUnsentEvents", "jumpToBottom", "providesMessagesActionListener", "Lim/vector/listeners/IMessagesAdapterActionsListener;", "resendUnsentEvents", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: NotificationAreaView.kt */
    public interface Delegate {
        void closeScreen();

        void deleteUnsentEvents();

        void jumpToBottom();

        IMessagesAdapterActionsListener providesMessagesActionListener();

        void resendUnsentEvents();
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, d2 = {"Lim/vector/view/NotificationAreaView$ResendAllClickableSpan;", "Landroid/text/style/ClickableSpan;", "(Lim/vector/view/NotificationAreaView;)V", "onClick", "", "widget", "Landroid/view/View;", "updateDrawState", "ds", "Landroid/text/TextPaint;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: NotificationAreaView.kt */
    private final class ResendAllClickableSpan extends ClickableSpan {
        public ResendAllClickableSpan() {
        }

        public void onClick(View view) {
            Intrinsics.checkParameterIsNotNull(view, "widget");
            Delegate delegate = NotificationAreaView.this.getDelegate();
            if (delegate != null) {
                delegate.resendUnsentEvents();
            }
            NotificationAreaView notificationAreaView = NotificationAreaView.this;
            notificationAreaView.render(notificationAreaView.state);
        }

        public void updateDrawState(TextPaint textPaint) {
            Intrinsics.checkParameterIsNotNull(textPaint, "ds");
            super.updateDrawState(textPaint);
            textPaint.setColor(ContextCompat.getColor(NotificationAreaView.this.getContext(), R.color.vector_fuchsia_color));
            textPaint.bgColor = 0;
            textPaint.setUnderlineText(true);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\n\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\fB\u0007\b\u0002¢\u0006\u0002\u0010\u0002\u0001\n\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016¨\u0006\u0017"}, d2 = {"Lim/vector/view/NotificationAreaView$State;", "", "()V", "ConnectionError", "Default", "Hidden", "Initial", "ResourceLimitExceededError", "ScrollToBottom", "Tombstone", "Typing", "UnreadPreview", "UnsentEvents", "Lim/vector/view/NotificationAreaView$State$Initial;", "Lim/vector/view/NotificationAreaView$State$Default;", "Lim/vector/view/NotificationAreaView$State$Hidden;", "Lim/vector/view/NotificationAreaView$State$ResourceLimitExceededError;", "Lim/vector/view/NotificationAreaView$State$ConnectionError;", "Lim/vector/view/NotificationAreaView$State$Tombstone;", "Lim/vector/view/NotificationAreaView$State$Typing;", "Lim/vector/view/NotificationAreaView$State$UnreadPreview;", "Lim/vector/view/NotificationAreaView$State$ScrollToBottom;", "Lim/vector/view/NotificationAreaView$State$UnsentEvents;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: NotificationAreaView.kt */
    public static abstract class State {

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/view/NotificationAreaView$State$ConnectionError;", "Lim/vector/view/NotificationAreaView$State;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class ConnectionError extends State {
            public static final ConnectionError INSTANCE = new ConnectionError();

            private ConnectionError() {
                super(null);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/view/NotificationAreaView$State$Default;", "Lim/vector/view/NotificationAreaView$State;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class Default extends State {
            public static final Default INSTANCE = new Default();

            private Default() {
                super(null);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/view/NotificationAreaView$State$Hidden;", "Lim/vector/view/NotificationAreaView$State;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class Hidden extends State {
            public static final Hidden INSTANCE = new Hidden();

            private Hidden() {
                super(null);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/view/NotificationAreaView$State$Initial;", "Lim/vector/view/NotificationAreaView$State;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class Initial extends State {
            public static final Initial INSTANCE = new Initial();

            private Initial() {
                super(null);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\r\u001a\u00020\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\u0014"}, d2 = {"Lim/vector/view/NotificationAreaView$State$ResourceLimitExceededError;", "Lim/vector/view/NotificationAreaView$State;", "isSoft", "", "matrixError", "Lorg/matrix/androidsdk/core/model/MatrixError;", "(ZLorg/matrix/androidsdk/core/model/MatrixError;)V", "()Z", "getMatrixError", "()Lorg/matrix/androidsdk/core/model/MatrixError;", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class ResourceLimitExceededError extends State {
            private final boolean isSoft;
            private final MatrixError matrixError;

            public static /* synthetic */ ResourceLimitExceededError copy$default(ResourceLimitExceededError resourceLimitExceededError, boolean z, MatrixError matrixError2, int i, Object obj) {
                if ((i & 1) != 0) {
                    z = resourceLimitExceededError.isSoft;
                }
                if ((i & 2) != 0) {
                    matrixError2 = resourceLimitExceededError.matrixError;
                }
                return resourceLimitExceededError.copy(z, matrixError2);
            }

            public final boolean component1() {
                return this.isSoft;
            }

            public final MatrixError component2() {
                return this.matrixError;
            }

            public final ResourceLimitExceededError copy(boolean z, MatrixError matrixError2) {
                Intrinsics.checkParameterIsNotNull(matrixError2, "matrixError");
                return new ResourceLimitExceededError(z, matrixError2);
            }

            public boolean equals(Object obj) {
                if (this != obj) {
                    if (obj instanceof ResourceLimitExceededError) {
                        ResourceLimitExceededError resourceLimitExceededError = (ResourceLimitExceededError) obj;
                        if (!(this.isSoft == resourceLimitExceededError.isSoft) || !Intrinsics.areEqual((Object) this.matrixError, (Object) resourceLimitExceededError.matrixError)) {
                            return false;
                        }
                    }
                    return false;
                }
                return true;
            }

            public int hashCode() {
                boolean z = this.isSoft;
                if (z) {
                    z = true;
                }
                int i = (z ? 1 : 0) * true;
                MatrixError matrixError2 = this.matrixError;
                return i + (matrixError2 != null ? matrixError2.hashCode() : 0);
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("ResourceLimitExceededError(isSoft=");
                sb.append(this.isSoft);
                sb.append(", matrixError=");
                sb.append(this.matrixError);
                sb.append(")");
                return sb.toString();
            }

            public ResourceLimitExceededError(boolean z, MatrixError matrixError2) {
                Intrinsics.checkParameterIsNotNull(matrixError2, "matrixError");
                super(null);
                this.isSoft = z;
                this.matrixError = matrixError2;
            }

            public final MatrixError getMatrixError() {
                return this.matrixError;
            }

            public final boolean isSoft() {
                return this.isSoft;
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u001f\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0005HÖ\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, d2 = {"Lim/vector/view/NotificationAreaView$State$ScrollToBottom;", "Lim/vector/view/NotificationAreaView$State;", "unreadCount", "", "message", "", "(ILjava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "getUnreadCount", "()I", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class ScrollToBottom extends State {
            private final String message;
            private final int unreadCount;

            public static /* synthetic */ ScrollToBottom copy$default(ScrollToBottom scrollToBottom, int i, String str, int i2, Object obj) {
                if ((i2 & 1) != 0) {
                    i = scrollToBottom.unreadCount;
                }
                if ((i2 & 2) != 0) {
                    str = scrollToBottom.message;
                }
                return scrollToBottom.copy(i, str);
            }

            public final int component1() {
                return this.unreadCount;
            }

            public final String component2() {
                return this.message;
            }

            public final ScrollToBottom copy(int i, String str) {
                return new ScrollToBottom(i, str);
            }

            public boolean equals(Object obj) {
                if (this != obj) {
                    if (obj instanceof ScrollToBottom) {
                        ScrollToBottom scrollToBottom = (ScrollToBottom) obj;
                        if (!(this.unreadCount == scrollToBottom.unreadCount) || !Intrinsics.areEqual((Object) this.message, (Object) scrollToBottom.message)) {
                            return false;
                        }
                    }
                    return false;
                }
                return true;
            }

            public int hashCode() {
                int i = this.unreadCount * 31;
                String str = this.message;
                return i + (str != null ? str.hashCode() : 0);
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("ScrollToBottom(unreadCount=");
                sb.append(this.unreadCount);
                sb.append(", message=");
                sb.append(this.message);
                sb.append(")");
                return sb.toString();
            }

            public ScrollToBottom(int i, String str) {
                super(null);
                this.unreadCount = i;
                this.message = str;
            }

            public /* synthetic */ ScrollToBottom(int i, String str, int i2, DefaultConstructorMarker defaultConstructorMarker) {
                if ((i2 & 2) != 0) {
                    str = null;
                }
                this(i, str);
            }

            public final String getMessage() {
                return this.message;
            }

            public final int getUnreadCount() {
                return this.unreadCount;
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lim/vector/view/NotificationAreaView$State$Tombstone;", "Lim/vector/view/NotificationAreaView$State;", "tombstoneContent", "Lorg/matrix/androidsdk/rest/model/RoomTombstoneContent;", "(Lorg/matrix/androidsdk/rest/model/RoomTombstoneContent;)V", "getTombstoneContent", "()Lorg/matrix/androidsdk/rest/model/RoomTombstoneContent;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class Tombstone extends State {
            private final RoomTombstoneContent tombstoneContent;

            public static /* synthetic */ Tombstone copy$default(Tombstone tombstone, RoomTombstoneContent roomTombstoneContent, int i, Object obj) {
                if ((i & 1) != 0) {
                    roomTombstoneContent = tombstone.tombstoneContent;
                }
                return tombstone.copy(roomTombstoneContent);
            }

            public final RoomTombstoneContent component1() {
                return this.tombstoneContent;
            }

            public final Tombstone copy(RoomTombstoneContent roomTombstoneContent) {
                Intrinsics.checkParameterIsNotNull(roomTombstoneContent, "tombstoneContent");
                return new Tombstone(roomTombstoneContent);
            }

            /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
                if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1.tombstoneContent, (java.lang.Object) ((im.vector.view.NotificationAreaView.State.Tombstone) r2).tombstoneContent) != false) goto L_0x0015;
             */
            public boolean equals(Object obj) {
                if (this != obj) {
                    if (obj instanceof Tombstone) {
                    }
                    return false;
                }
                return true;
            }

            public int hashCode() {
                RoomTombstoneContent roomTombstoneContent = this.tombstoneContent;
                if (roomTombstoneContent != null) {
                    return roomTombstoneContent.hashCode();
                }
                return 0;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("Tombstone(tombstoneContent=");
                sb.append(this.tombstoneContent);
                sb.append(")");
                return sb.toString();
            }

            public Tombstone(RoomTombstoneContent roomTombstoneContent) {
                Intrinsics.checkParameterIsNotNull(roomTombstoneContent, "tombstoneContent");
                super(null);
                this.tombstoneContent = roomTombstoneContent;
            }

            public final RoomTombstoneContent getTombstoneContent() {
                return this.tombstoneContent;
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, d2 = {"Lim/vector/view/NotificationAreaView$State$Typing;", "Lim/vector/view/NotificationAreaView$State;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class Typing extends State {
            private final String message;

            public static /* synthetic */ Typing copy$default(Typing typing, String str, int i, Object obj) {
                if ((i & 1) != 0) {
                    str = typing.message;
                }
                return typing.copy(str);
            }

            public final String component1() {
                return this.message;
            }

            public final Typing copy(String str) {
                Intrinsics.checkParameterIsNotNull(str, "message");
                return new Typing(str);
            }

            /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
                if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1.message, (java.lang.Object) ((im.vector.view.NotificationAreaView.State.Typing) r2).message) != false) goto L_0x0015;
             */
            public boolean equals(Object obj) {
                if (this != obj) {
                    if (obj instanceof Typing) {
                    }
                    return false;
                }
                return true;
            }

            public int hashCode() {
                String str = this.message;
                if (str != null) {
                    return str.hashCode();
                }
                return 0;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("Typing(message=");
                sb.append(this.message);
                sb.append(")");
                return sb.toString();
            }

            public Typing(String str) {
                Intrinsics.checkParameterIsNotNull(str, "message");
                super(null);
                this.message = str;
            }

            public final String getMessage() {
                return this.message;
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lim/vector/view/NotificationAreaView$State$UnreadPreview;", "Lim/vector/view/NotificationAreaView$State;", "()V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class UnreadPreview extends State {
            public static final UnreadPreview INSTANCE = new UnreadPreview();

            private UnreadPreview() {
                super(null);
            }
        }

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u000eHÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0013"}, d2 = {"Lim/vector/view/NotificationAreaView$State$UnsentEvents;", "Lim/vector/view/NotificationAreaView$State;", "hasUndeliverableEvents", "", "hasUnknownDeviceEvents", "(ZZ)V", "getHasUndeliverableEvents", "()Z", "getHasUnknownDeviceEvents", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
        /* compiled from: NotificationAreaView.kt */
        public static final class UnsentEvents extends State {
            private final boolean hasUndeliverableEvents;
            private final boolean hasUnknownDeviceEvents;

            public static /* synthetic */ UnsentEvents copy$default(UnsentEvents unsentEvents, boolean z, boolean z2, int i, Object obj) {
                if ((i & 1) != 0) {
                    z = unsentEvents.hasUndeliverableEvents;
                }
                if ((i & 2) != 0) {
                    z2 = unsentEvents.hasUnknownDeviceEvents;
                }
                return unsentEvents.copy(z, z2);
            }

            public final boolean component1() {
                return this.hasUndeliverableEvents;
            }

            public final boolean component2() {
                return this.hasUnknownDeviceEvents;
            }

            public final UnsentEvents copy(boolean z, boolean z2) {
                return new UnsentEvents(z, z2);
            }

            public boolean equals(Object obj) {
                if (this != obj) {
                    if (obj instanceof UnsentEvents) {
                        UnsentEvents unsentEvents = (UnsentEvents) obj;
                        if (this.hasUndeliverableEvents == unsentEvents.hasUndeliverableEvents) {
                            if (this.hasUnknownDeviceEvents == unsentEvents.hasUnknownDeviceEvents) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
                return true;
            }

            public int hashCode() {
                boolean z = this.hasUndeliverableEvents;
                int i = 1;
                if (z) {
                    z = true;
                }
                int i2 = (z ? 1 : 0) * true;
                boolean z2 = this.hasUnknownDeviceEvents;
                if (!z2) {
                    i = z2;
                }
                return i2 + i;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("UnsentEvents(hasUndeliverableEvents=");
                sb.append(this.hasUndeliverableEvents);
                sb.append(", hasUnknownDeviceEvents=");
                sb.append(this.hasUnknownDeviceEvents);
                sb.append(")");
                return sb.toString();
            }

            public UnsentEvents(boolean z, boolean z2) {
                super(null);
                this.hasUndeliverableEvents = z;
                this.hasUnknownDeviceEvents = z2;
            }

            public final boolean getHasUndeliverableEvents() {
                return this.hasUndeliverableEvents;
            }

            public final boolean getHasUnknownDeviceEvents() {
                return this.hasUnknownDeviceEvents;
            }
        }

        private State() {
        }

        public /* synthetic */ State(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public NotificationAreaView(Context context) {
        this(context, null, 0, 6, null);
    }

    public NotificationAreaView(Context context, AttributeSet attributeSet) {
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

    public /* synthetic */ NotificationAreaView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 2) != 0) {
            attributeSet = null;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        this(context, attributeSet, i);
    }

    public NotificationAreaView(Context context, AttributeSet attributeSet, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, attributeSet, i);
        this.state = Initial.INSTANCE;
        setupView();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String str = SHOW_INFO_AREA_VALUE_ALWAYS;
        String string = defaultSharedPreferences.getString(SHOW_INFO_AREA_KEY, str);
        if (string != null) {
            int hashCode = string.hashCode();
            if (hashCode != -1725804634) {
                if (hashCode == -1414557169 && string.equals(str)) {
                    this.visibilityForEmptyContent = 0;
                    this.visibilityForMessages = 0;
                    return;
                }
            } else if (string.equals(SHOW_INFO_AREA_VALUE_MESSAGES_AND_ERRORS)) {
                this.visibilityForEmptyContent = 8;
                this.visibilityForMessages = 0;
                return;
            }
        }
        this.visibilityForEmptyContent = 8;
        this.visibilityForMessages = 8;
    }

    public final ImageView getImageView() {
        ImageView imageView2 = this.imageView;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("imageView");
        }
        return imageView2;
    }

    public final void setImageView(ImageView imageView2) {
        Intrinsics.checkParameterIsNotNull(imageView2, "<set-?>");
        this.imageView = imageView2;
    }

    public final TextView getMessageView() {
        TextView textView = this.messageView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("messageView");
        }
        return textView;
    }

    public final void setMessageView(TextView textView) {
        Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
        this.messageView = textView;
    }

    public final Delegate getDelegate() {
        return this.delegate;
    }

    public final void setDelegate(Delegate delegate2) {
        this.delegate = delegate2;
    }

    public final void render(State state2) {
        Intrinsics.checkParameterIsNotNull(state2, "newState");
        String str = "NotificationAreaView";
        if (Intrinsics.areEqual((Object) state2, (Object) this.state)) {
            Log.d(str, "State unchanged");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Rendering ");
        sb.append(state2);
        Log.d(str, sb.toString());
        cleanUp();
        this.state = state2;
        if (state2 instanceof Default) {
            renderDefault();
        } else if (state2 instanceof Hidden) {
            renderHidden();
        } else if (state2 instanceof Tombstone) {
            renderTombstone((Tombstone) state2);
        } else if (state2 instanceof ResourceLimitExceededError) {
            renderResourceLimitExceededError((ResourceLimitExceededError) state2);
        } else if (state2 instanceof ConnectionError) {
            renderConnectionError();
        } else if (state2 instanceof Typing) {
            renderTyping((Typing) state2);
        } else if (state2 instanceof UnreadPreview) {
            renderUnreadPreview();
        } else if (state2 instanceof ScrollToBottom) {
            renderScrollToBottom((ScrollToBottom) state2);
        } else if (state2 instanceof UnsentEvents) {
            renderUnsent((UnsentEvents) state2);
        }
    }

    private final void setupView() {
        RelativeLayout.inflate(getContext(), R.layout.view_notification_area, this);
        ButterKnife.bind((View) this);
    }

    private final void cleanUp() {
        TextView textView = this.messageView;
        String str = "messageView";
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView.setOnClickListener(null);
        ImageView imageView2 = this.imageView;
        String str2 = "imageView";
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str2);
        }
        imageView2.setOnClickListener(null);
        setBackgroundColor(0);
        TextView textView2 = this.messageView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView2.setText(null);
        ImageView imageView3 = this.imageView;
        if (imageView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str2);
        }
        imageView3.setImageResource(0);
    }

    private final void renderTombstone(Tombstone tombstone) {
        setVisibility(0);
        ImageView imageView2 = this.imageView;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("imageView");
        }
        imageView2.setImageResource(R.drawable.error);
        String createPermalink = PermalinkUtils.createPermalink(tombstone.getTombstoneContent().replacementRoom);
        Pattern pattern = MXPatterns.PATTERN_CONTAIN_APP_LINK_PERMALINK_ROOM_ID;
        Delegate delegate2 = this.delegate;
        MatrixURLSpan matrixURLSpan = new MatrixURLSpan(createPermalink, pattern, delegate2 != null ? delegate2.providesMessagesActionListener() : null);
        ThemeUtils themeUtils = ThemeUtils.INSTANCE;
        Context context = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "context");
        int color = themeUtils.getColor(context, R.attr.vctr_message_text_color);
        Spanny append = new Spanny(getResources().getString(R.string.room_tombstone_versioned_description), new StyleSpan(1), new ForegroundColorSpan(color)).append((CharSequence) "\n").append(getResources().getString(R.string.room_tombstone_continuation_link), matrixURLSpan, new ForegroundColorSpan(color));
        TextView textView = this.messageView;
        String str = "messageView";
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView2 = this.messageView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView2.setText(append);
    }

    private final void renderResourceLimitExceededError(ResourceLimitExceededError resourceLimitExceededError) {
        Mode mode;
        int i;
        setVisibility(0);
        Context context = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "context");
        ResourceLimitErrorFormatter resourceLimitErrorFormatter = new ResourceLimitErrorFormatter(context);
        if (resourceLimitExceededError.isSoft()) {
            i = R.color.soft_resource_limit_exceeded;
            mode = Soft.INSTANCE;
        } else {
            i = R.color.hard_resource_limit_exceeded;
            mode = Hard.INSTANCE;
        }
        CharSequence format$default = ResourceLimitErrorFormatter.format$default(resourceLimitErrorFormatter, resourceLimitExceededError.getMatrixError(), mode, null, true, 4, null);
        TextView textView = this.messageView;
        String str = "messageView";
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView.setTextColor(-1);
        TextView textView2 = this.messageView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView2.setText(format$default);
        TextView textView3 = this.messageView;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView4 = this.messageView;
        if (textView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView4.setLinkTextColor(-1);
        setBackgroundColor(ContextCompat.getColor(getContext(), i));
    }

    private final void renderConnectionError() {
        setVisibility(0);
        ImageView imageView2 = this.imageView;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("imageView");
        }
        imageView2.setImageResource(R.drawable.error);
        TextView textView = this.messageView;
        String str = "messageView";
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.vector_fuchsia_color));
        TextView textView2 = this.messageView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView2.setText(new SpannableString(getResources().getString(R.string.room_offline_notification)));
    }

    private final void renderTyping(Typing typing) {
        setVisibility(this.visibilityForMessages);
        ImageView imageView2 = this.imageView;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("imageView");
        }
        imageView2.setImageResource(R.drawable.vector_typing);
        TextView textView = this.messageView;
        String str = "messageView";
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView.setText(new SpannableString(typing.getMessage()));
        TextView textView2 = this.messageView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        ThemeUtils themeUtils = ThemeUtils.INSTANCE;
        Context context = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "context");
        textView2.setTextColor(themeUtils.getColor(context, R.attr.vctr_room_notification_text_color));
    }

    private final void renderUnreadPreview() {
        setVisibility(this.visibilityForMessages);
        ImageView imageView2 = this.imageView;
        String str = "imageView";
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        imageView2.setImageResource(R.drawable.scrolldown);
        TextView textView = this.messageView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("messageView");
        }
        ThemeUtils themeUtils = ThemeUtils.INSTANCE;
        Context context = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "context");
        textView.setTextColor(themeUtils.getColor(context, R.attr.vctr_room_notification_text_color));
        ImageView imageView3 = this.imageView;
        if (imageView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        imageView3.setOnClickListener(new NotificationAreaView$renderUnreadPreview$1(this));
    }

    private final void renderScrollToBottom(ScrollToBottom scrollToBottom) {
        setVisibility(this.visibilityForMessages);
        String str = "imageView";
        String str2 = "messageView";
        if (scrollToBottom.getUnreadCount() > 0) {
            ImageView imageView2 = this.imageView;
            if (imageView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            imageView2.setImageResource(R.drawable.newmessages);
            TextView textView = this.messageView;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str2);
            }
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.vector_fuchsia_color));
            TextView textView2 = this.messageView;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str2);
            }
            textView2.setText(new SpannableString(getResources().getQuantityString(R.plurals.room_new_messages_notification, scrollToBottom.getUnreadCount(), new Object[]{Integer.valueOf(scrollToBottom.getUnreadCount())})));
        } else {
            ImageView imageView3 = this.imageView;
            if (imageView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            imageView3.setImageResource(R.drawable.scrolldown);
            TextView textView3 = this.messageView;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str2);
            }
            ThemeUtils themeUtils = ThemeUtils.INSTANCE;
            Context context = getContext();
            Intrinsics.checkExpressionValueIsNotNull(context, "context");
            textView3.setTextColor(themeUtils.getColor(context, R.attr.vctr_room_notification_text_color));
            if (!TextUtils.isEmpty(scrollToBottom.getMessage())) {
                TextView textView4 = this.messageView;
                if (textView4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str2);
                }
                textView4.setText(new SpannableString(scrollToBottom.getMessage()));
            }
        }
        TextView textView5 = this.messageView;
        if (textView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str2);
        }
        textView5.setOnClickListener(new NotificationAreaView$renderScrollToBottom$1(this));
        ImageView imageView4 = this.imageView;
        if (imageView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        imageView4.setOnClickListener(new NotificationAreaView$renderScrollToBottom$2(this));
    }

    private final void renderUnsent(UnsentEvents unsentEvents) {
        setVisibility(0);
        ImageView imageView2 = this.imageView;
        if (imageView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("imageView");
        }
        imageView2.setImageResource(R.drawable.error);
        String string = getResources().getString(R.string.room_prompt_cancel);
        String string2 = getResources().getString(R.string.room_prompt_resend);
        String string3 = getContext().getString(unsentEvents.getHasUnknownDeviceEvents() ? R.string.room_unknown_devices_messages_notification : R.string.room_unsent_messages_notification, new Object[]{string2, string});
        Intrinsics.checkExpressionValueIsNotNull(string3, "message");
        CharSequence charSequence = string3;
        Intrinsics.checkExpressionValueIsNotNull(string, "cancelAll");
        int indexOf$default = StringsKt.indexOf$default(charSequence, string, 0, false, 6, (Object) null);
        Intrinsics.checkExpressionValueIsNotNull(string2, "resendAll");
        int indexOf$default2 = StringsKt.indexOf$default(charSequence, string2, 0, false, 6, (Object) null);
        SpannableString spannableString = new SpannableString(charSequence);
        if (indexOf$default >= 0) {
            spannableString.setSpan(new CancelAllClickableSpan(), indexOf$default, string.length() + indexOf$default, 0);
        }
        if (indexOf$default2 >= 0) {
            spannableString.setSpan(new ResendAllClickableSpan(), indexOf$default2, string2.length() + indexOf$default2, 0);
        }
        TextView textView = this.messageView;
        String str = "messageView";
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView2 = this.messageView;
        if (textView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView2.setTextColor(ContextCompat.getColor(getContext(), R.color.vector_fuchsia_color));
        TextView textView3 = this.messageView;
        if (textView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        textView3.setText(spannableString);
    }

    private final void renderDefault() {
        setVisibility(this.visibilityForEmptyContent);
    }

    private final void renderHidden() {
        setVisibility(8);
    }
}
