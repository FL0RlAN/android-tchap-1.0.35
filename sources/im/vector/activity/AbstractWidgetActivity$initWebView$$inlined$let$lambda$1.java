package im.vector.activity;

import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShare;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n¸\u0006\u0000"}, d2 = {"im/vector/activity/AbstractWidgetActivity$initWebView$1$1", "Landroid/webkit/WebChromeClient;", "onConsoleMessage", "", "consoleMessage", "Landroid/webkit/ConsoleMessage;", "onPermissionRequest", "", "request", "Landroid/webkit/PermissionRequest;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: AbstractWidgetActivity.kt */
public final class AbstractWidgetActivity$initWebView$$inlined$let$lambda$1 extends WebChromeClient {
    final /* synthetic */ AbstractWidgetActivity this$0;

    AbstractWidgetActivity$initWebView$$inlined$let$lambda$1(AbstractWidgetActivity abstractWidgetActivity) {
        this.this$0 = abstractWidgetActivity;
    }

    public void onPermissionRequest(final PermissionRequest permissionRequest) {
        Intrinsics.checkParameterIsNotNull(permissionRequest, RoomKeyShare.ACTION_SHARE_REQUEST);
        this.this$0.runOnUiThread(new Runnable() {
            public final void run() {
                PermissionRequest permissionRequest = permissionRequest;
                permissionRequest.grant(permissionRequest.getResources());
            }
        });
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Intrinsics.checkParameterIsNotNull(consoleMessage, "consoleMessage");
        String access$getLOG_TAG$cp = AbstractWidgetActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onConsoleMessage() : ");
        sb.append(consoleMessage.message());
        sb.append(" line ");
        sb.append(consoleMessage.lineNumber());
        sb.append(" source Id ");
        sb.append(consoleMessage.sourceId());
        Log.e(access$getLOG_TAG$cp, sb.toString());
        return super.onConsoleMessage(consoleMessage);
    }
}
