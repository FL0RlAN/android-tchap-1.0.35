package im.vector.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import im.vector.widgets.Widget;
import im.vector.widgets.WidgetsManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "<anonymous parameter 1>", "", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: WidgetActivity.kt */
final class WidgetActivity$onCloseClick$1 implements OnClickListener {
    final /* synthetic */ WidgetActivity this$0;

    WidgetActivity$onCloseClick$1(WidgetActivity widgetActivity) {
        this.this$0 = widgetActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.this$0.showWaitingView();
        WidgetsManager sharedInstance = WidgetsManager.getSharedInstance();
        MXSession access$getMSession$p = this.this$0.mSession;
        Room access$getMRoom$p = this.this$0.mRoom;
        Widget access$getMWidget$p = this.this$0.mWidget;
        if (access$getMWidget$p == null) {
            Intrinsics.throwNpe();
        }
        sharedInstance.closeWidget(access$getMSession$p, access$getMRoom$p, access$getMWidget$p.getWidgetId(), new ApiCallback<Void>(this) {
            final /* synthetic */ WidgetActivity$onCloseClick$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onSuccess(Void voidR) {
                this.this$0.this$0.hideWaitingView();
                this.this$0.this$0.finish();
            }

            private final void onError(String str) {
                this.this$0.this$0.hideWaitingView();
                Toast makeText = Toast.makeText(this.this$0.this$0, str, 0);
                makeText.show();
                Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            }

            public void onNetworkError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                String localizedMessage = exc.getLocalizedMessage();
                Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
                onError(localizedMessage);
            }

            public void onMatrixError(MatrixError matrixError) {
                Intrinsics.checkParameterIsNotNull(matrixError, "e");
                String localizedMessage = matrixError.getLocalizedMessage();
                Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
                onError(localizedMessage);
            }

            public void onUnexpectedError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                String localizedMessage = exc.getLocalizedMessage();
                Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
                onError(localizedMessage);
            }
        });
    }
}
