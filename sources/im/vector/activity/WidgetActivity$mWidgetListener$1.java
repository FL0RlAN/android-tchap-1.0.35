package im.vector.activity;

import android.text.TextUtils;
import im.vector.widgets.Widget;
import im.vector.widgets.WidgetsManager.onWidgetUpdateListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "widget", "Lim/vector/widgets/Widget;", "kotlin.jvm.PlatformType", "onWidgetUpdate"}, k = 3, mv = {1, 1, 13})
/* compiled from: WidgetActivity.kt */
final class WidgetActivity$mWidgetListener$1 implements onWidgetUpdateListener {
    final /* synthetic */ WidgetActivity this$0;

    WidgetActivity$mWidgetListener$1(WidgetActivity widgetActivity) {
        this.this$0 = widgetActivity;
    }

    public final void onWidgetUpdate(Widget widget) {
        Intrinsics.checkExpressionValueIsNotNull(widget, "widget");
        CharSequence widgetId = widget.getWidgetId();
        Widget access$getMWidget$p = this.this$0.mWidget;
        if (access$getMWidget$p == null) {
            Intrinsics.throwNpe();
        }
        if (TextUtils.equals(widgetId, access$getMWidget$p.getWidgetId()) && !widget.isActive()) {
            this.this$0.finish();
        }
    }
}
