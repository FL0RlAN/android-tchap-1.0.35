package im.vector.activity;

import android.os.Handler;
import android.os.Looper;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShare;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"im/vector/activity/WidgetActivity$configureWebView$1$2", "Landroid/webkit/WebChromeClient;", "onPermissionRequest", "", "request", "Landroid/webkit/PermissionRequest;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: WidgetActivity.kt */
public final class WidgetActivity$configureWebView$1$2 extends WebChromeClient {
    WidgetActivity$configureWebView$1$2() {
    }

    public void onPermissionRequest(PermissionRequest permissionRequest) {
        Intrinsics.checkParameterIsNotNull(permissionRequest, RoomKeyShare.ACTION_SHARE_REQUEST);
        new Handler(Looper.getMainLooper()).post(new WidgetActivity$configureWebView$1$2$onPermissionRequest$1(permissionRequest));
    }
}
