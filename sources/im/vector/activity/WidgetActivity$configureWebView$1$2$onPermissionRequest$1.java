package im.vector.activity;

import android.webkit.PermissionRequest;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: WidgetActivity.kt */
final class WidgetActivity$configureWebView$1$2$onPermissionRequest$1 implements Runnable {
    final /* synthetic */ PermissionRequest $request;

    WidgetActivity$configureWebView$1$2$onPermissionRequest$1(PermissionRequest permissionRequest) {
        this.$request = permissionRequest;
    }

    public final void run() {
        PermissionRequest permissionRequest = this.$request;
        permissionRequest.grant(permissionRequest.getResources());
    }
}
